package ba.sum.fpmoz.rezervacijatermina.services;

import ba.sum.fpmoz.rezervacijatermina.models.Role;
import ba.sum.fpmoz.rezervacijatermina.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Kreira novu ulogu (ADMIN, USER)
    public Role createRole(String roleName) {
        if(roleRepository.findByName(roleName).isPresent()) {
            throw new RuntimeException("Uloga već postoji!");
        }

        Role role = new Role();
        role.setName(roleName.toUpperCase());
        return roleRepository.save(role);
    }

    // Dohvati role po imenu
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Uloga ne postoji"));
    }
}


