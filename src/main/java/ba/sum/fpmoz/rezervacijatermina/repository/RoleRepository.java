package ba.sum.fpmoz.rezervacijatermina.repository;

import ba.sum.fpmoz.rezervacijatermina.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(String name);

}
