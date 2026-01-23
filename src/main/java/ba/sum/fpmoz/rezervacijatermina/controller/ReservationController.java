package ba.sum.fpmoz.rezervacijatermina.controller;

import ba.sum.fpmoz.rezervacijatermina.dto.ReservationDTO;
import ba.sum.fpmoz.rezervacijatermina.models.Reservation;
import ba.sum.fpmoz.rezervacijatermina.services.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservations")
@Tag(name = "Reservation Controller", description = "Upravljanje rezervacijama u čitaonici")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    // Dohvati sve rezervacije (USER i ADMIN)
    @Operation(summary = "Dohvati sve rezervacije")
    @ApiResponse(responseCode = "200", description = "Lista rezervacija")
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    // Dohvati rezervaciju po ID-u (USER i ADMIN)
    @Operation(summary = "Dohvati rezervaciju po ID-u")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rezervacija pronađena"),
            @ApiResponse(responseCode = "404", description = "Rezervacija nije pronađena")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationService.getReservationById(id);
        return reservation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Kreiraj novu rezervaciju (USER)
    @Operation(summary = "Kreiraj novu rezervaciju")
    @ApiResponse(responseCode = "200", description = "Rezervacija kreirana")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationDTO dto) {
        Reservation reservation = reservationService.createReservation(dto.getRoomId(), dto.getDate());
        return ResponseEntity.ok(reservation);
    }

    // Otkaži rezervaciju (USER)
    @Operation(summary = "Otkaži rezervaciju")
    @ApiResponse(responseCode = "200", description = "Rezervacija otkazana")
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.cancelReservation(id));
    }
}
