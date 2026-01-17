package ba.sum.fpmoz.rezervacijatermina.repository;

import ba.sum.fpmoz.rezervacijatermina.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}