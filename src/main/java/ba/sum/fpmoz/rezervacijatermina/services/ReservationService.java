package ba.sum.fpmoz.rezervacijatermina.services;

import ba.sum.fpmoz.rezervacijatermina.models.Reservation;
import ba.sum.fpmoz.rezervacijatermina.models.ReservationStatus;
import ba.sum.fpmoz.rezervacijatermina.models.Room;
import ba.sum.fpmoz.rezervacijatermina.models.User;
import ba.sum.fpmoz.rezervacijatermina.repository.ReservationRepository;
import ba.sum.fpmoz.rezervacijatermina.repository.RoomRepository;
import ba.sum.fpmoz.rezervacijatermina.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    // Kreiranje nove rezervacije
    public Reservation createReservation(Long roomId, String date) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Soba nije pronađena"));

        LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);

        // Provjera da datum nije u prošlosti
        if(parsedDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("Ne možete rezervirati za prošli datum");
        }
        // Definiramo cijeli dan
        LocalDateTime startOfDay = parsedDate.atTime(10, 0);
        LocalDateTime endOfDay = parsedDate.atTime(20, 0);

        // Provjera kapaciteta
        int currentReservations = reservationRepository
                .countByRoomAndStartTimeBetweenAndStatus(room, startOfDay, endOfDay, ReservationStatus.ACTIVE);

        if (currentReservations >= room.getCapacity()) {
            throw new RuntimeException("Soba je popunjena za taj dan");
        }

        // Kreiranje rezervacije
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setStartTime(startOfDay);
        reservation.setEndTime(endOfDay);
        reservation.setStatus(ReservationStatus.ACTIVE);

        return reservationRepository.save(reservation);
    }

    // Dohvati sve rezervacije
    public List<Reservation> getAllReservations() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));

        // Ako je ADMIN, vidi sve rezervacije
        boolean isAdmin = user.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN"));
        if (isAdmin) {
            return reservationRepository.findAll();
        } else {
            return reservationRepository.findByUser(user); // USER vidi samo svoje
        }
    }

    // Dohvati rezervaciju po ID-u
    public Optional<Reservation> getReservationById(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rezervacija nije pronađena"));

        // USER može vidjeti samo svoje rezervacije
        boolean isAdmin = user.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN"));
        if (!isAdmin && !reservation.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Nemate pravo pristupa ovoj rezervaciji");
        }

        return Optional.of(reservation);
    }

    // Otkaži rezervaciju
    public Reservation cancelReservation(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rezervacija nije pronađena"));

        boolean isAdmin = user.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN"));
        if (!isAdmin && !reservation.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Nemate pravo otkazati ovu rezervaciju");
        }

        reservation.setStatus(ReservationStatus.CANCELED);
        return reservationRepository.save(reservation);
    }
}
