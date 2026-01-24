package ba.sum.fpmoz.rezervacijatermina.controller;

import ba.sum.fpmoz.rezervacijatermina.dto.ChangePassword;
import ba.sum.fpmoz.rezervacijatermina.models.User;
import ba.sum.fpmoz.rezervacijatermina.repository.UserRepository;
import ba.sum.fpmoz.rezervacijatermina.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
@Tag(
        name = "User Controller",
        description = "Operacije nad korisnicima."
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    //Dohvaca trenutno prijavljenog korisnika
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
            String email = userDetails.getUsername();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(401).build();
        }
    }
    @Operation(
            summary = "Promjena lozinke",
            description = "Korisnik može promijeniti svoju lozinku tako da unese staru i novu lozinku."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lozinka je uspješno promijenjena"),
            @ApiResponse(responseCode = "400", description = "Stara lozinka nije ispravna"),
            @ApiResponse(responseCode = "401", description = "Korisnik nije prijavljen")
    })


    @PutMapping("/change-password")
    //Promjena lozinke za usera
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePassword request,
            Authentication authentication
    ) {
        String email = authentication.getName(); // dolazi iz JWT-a

        userService.changePassword(
                email,
                request.getOldPassword(),
                request.getNewPassword()
        );

        return ResponseEntity.ok("Lozinka uspješno promijenjena");
    }

}
