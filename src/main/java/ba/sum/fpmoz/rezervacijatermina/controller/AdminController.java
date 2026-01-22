package ba.sum.fpmoz.rezervacijatermina.controller;

import ba.sum.fpmoz.rezervacijatermina.models.User;
import ba.sum.fpmoz.rezervacijatermina.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Controller", description = "Operacije koje može raditi samo admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Operation(
            summary = "Dohvati korisnika po ID-u",
            description = "Samo ADMIN korisnik može dohvatiti korisnika po ID-u"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Korisnik pronađen"),
            @ApiResponse(responseCode = "404", description = "Korisnik nije pronađen"),
            @ApiResponse(responseCode = "403", description = "Zabranjen pristup")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).build());
    }

    @Operation(
            summary = "Dohvati sve korisnike",
            description = "Samo ADMIN korisnik može dohvatiti listu svih korisnika"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista korisnika"),
            @ApiResponse(responseCode = "403", description = "Zabranjen pristup")
    })
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}
