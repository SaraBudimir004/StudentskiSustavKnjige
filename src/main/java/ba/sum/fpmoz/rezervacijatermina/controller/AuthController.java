package ba.sum.fpmoz.rezervacijatermina.controller;

import ba.sum.fpmoz.rezervacijatermina.models.Role;
import ba.sum.fpmoz.rezervacijatermina.models.User;
import ba.sum.fpmoz.rezervacijatermina.repository.RoleRepository;
import ba.sum.fpmoz.rezervacijatermina.repository.UserRepository;
import ba.sum.fpmoz.rezervacijatermina.security.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Registracija, prijava i JWT tokeni")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    @Operation(
            summary = "Registracija novog korisnika",
            description = "Kreira novog korisnika sa defaultnom USER rolom. Lozinka se automatski hashira."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Korisnik uspješno registriran",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "409", description = "Email već postoji"),
            @ApiResponse(responseCode = "500", description = "Greška na serveru")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(409).body("Korisnik s tim emailom već postoji.");
        }
        //Haskiranje lozinke
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //Dohvacanje user uloge
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Greška: Uloga USER nije pronađena u bazi!"));

        user.getRoles().add(role);// korisnik dobiva user ulogu  i puni set<Role>

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @Operation(
            summary = "Prijava korisnika",
            description = "Provjerava email i lozinku te vraća JWT access i refresh token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uspješna prijava"),
            @ApiResponse(responseCode = "401", description = "Neispravni podaci")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    examples = @ExampleObject(
                            value = "{\"email\": \"sarabudimir@gmail.com\", \"password\": \"lozinka123\"}"
                    )
            ))
            @RequestBody Map<String, String> userData) {

        String email = userData.get("email");
        String password = userData.get("password");

        // Provjera korisnika po emaili ako ne postojionda je null
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body("Neispravni email ili lozinka.");
        }

        // Dohvati uloge korisnika jel user ili admin
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        // Generiranje tokena
        String accessToken = jwtUtil.generateToken(user.getEmail(), roles);// kraci rok trajanja
        String refreshToken = jwtUtil.generateRefreshToken(); // dobivanje novog access tokena, duzi je

        // Spremi refresh token i vezan je uz korisnika
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        ));
    }

    @Operation(
            summary = "Osvježavanje access tokena",
            description = "Na temelju važećeg refresh tokena generira novi JWT access token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token osvježen"),
            @ApiResponse(responseCode = "401", description = "Nevažeći refresh token")
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        // trazi korisnika sa refresh tokenom i sprema ga u db
        User user = userRepository.findByRefreshToken(refreshToken).orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).body("Nevažeći refresh token.");
        }
        //uzima user ili admin i stavlja ulogu u jwt
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        //noci access token, moze dalje koristiti aplikaiju
        String newAccessToken = jwtUtil.generateToken(user.getEmail(), roles);

        return ResponseEntity.ok(Map.of(
                "accessToken", newAccessToken
        ));
    }
}