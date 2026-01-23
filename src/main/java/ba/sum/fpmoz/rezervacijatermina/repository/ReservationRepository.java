package ba.sum.fpmoz.rezervacijatermina.repository;

import ba.sum.fpmoz.rezervacijatermina.models.Reservation;
import ba.sum.fpmoz.rezervacijatermina.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);

}
