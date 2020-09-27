package morozov.ru.repositories;

import morozov.ru.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Integer> {

    void deleteByName(String name);

}
