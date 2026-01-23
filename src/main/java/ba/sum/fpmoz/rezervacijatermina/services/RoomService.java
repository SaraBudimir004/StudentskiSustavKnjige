package ba.sum.fpmoz.rezervacijatermina.services;

import ba.sum.fpmoz.rezervacijatermina.models.Room;
import ba.sum.fpmoz.rezervacijatermina.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    // Dohvati sve sobe
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Dohvati sobu po ID-u
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    // Kreiraj novu sobu
    public Room createRoom(Room room) {
        // Validacija
        if(room.getName() == null || room.getName().isEmpty()) {
            throw new RuntimeException("Ime sobe ne smije biti prazno");
        }
        if(room.getCapacity() == null || room.getCapacity() <= 0) {
            throw new RuntimeException("Kapacitet sobe mora biti veći od 0");
        }
        return roomRepository.save(room);
    }

    // Ažuriraj sobu
    public Room updateRoom(Long id, Room updatedRoom) {
        return roomRepository.findById(id).map(room -> {
            room.setName(updatedRoom.getName());
            room.setCapacity(updatedRoom.getCapacity());
            room.setLocation(updatedRoom.getLocation()); // dodano ažuriranje lokacije
            return roomRepository.save(room);
        }).orElseThrow(() -> new RuntimeException("Soba nije pronađena"));
    }

    // Obriši sobu
    public boolean deleteRoom(Long id) {
        if(roomRepository.existsById(id)) {
            roomRepository.deleteById(id);
            return true; // obrisano
        }
        return false; // soba ne postoji
    }
}
