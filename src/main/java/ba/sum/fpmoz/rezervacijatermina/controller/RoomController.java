package ba.sum.fpmoz.rezervacijatermina.controller;


import ba.sum.fpmoz.rezervacijatermina.models.Room;
import ba.sum.fpmoz.rezervacijatermina.repository.RoomRepository;
import ba.sum.fpmoz.rezervacijatermina.services.RoomService;
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
@RequestMapping("/rooms")
@Tag(name = "Room Controller", description = "Operacije nad sobama u čitaonici")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Operation(summary = "Dohvati sve sobe")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista svih soba")
    })
    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @Operation(summary = "Dohvati sobu po ID-u")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Soba pronađena"),
            @ApiResponse(responseCode = "404", description = "Soba nije pronađena")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Room>> getRoomById(@PathVariable Long id) {
        Optional<Room> room = roomService.getRoomById(id);
        if (room.isPresent()) {
            return ResponseEntity.ok(room);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @Operation(summary = "Kreiraj novu sobu")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Soba kreirana"),
            @ApiResponse(responseCode = "403", description = "Samo admin može kreirati sobu")
    })

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        Room savedRoom = roomService.createRoom(room);
        return ResponseEntity.ok(savedRoom);
    }



    @Operation(summary = "Ažuriraj postojeću sobu")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Soba ažurirana"),
            @ApiResponse(responseCode = "404", description = "Soba nije pronađena"),
            @ApiResponse(responseCode = "403", description = "Samo admin može ažurirati sobu")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room room) {
        Room updatedRoom = roomService.updateRoom(id, room);
        if (updatedRoom != null) {
            return ResponseEntity.ok(updatedRoom);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @Operation(summary = "Obriši sobu")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Soba obrisana"),
            @ApiResponse(responseCode = "404", description = "Soba nije pronađena"),
            @ApiResponse(responseCode = "403", description = "Samo admin može obrisati sobu")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        boolean deleted = roomService.deleteRoom(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }
}
