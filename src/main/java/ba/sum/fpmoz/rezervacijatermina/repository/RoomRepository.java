package ba.sum.fpmoz.rezervacijatermina.repository;

import ba.sum.fpmoz.rezervacijatermina.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

}