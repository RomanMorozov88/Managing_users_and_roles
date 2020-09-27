package morozov.ru.services.dbservices;

import morozov.ru.models.Role;

import java.util.List;

public interface RoleService {

    Role saveRole(Role role);

    List<Role> getAll();

    void deleteRole(String name);
}
