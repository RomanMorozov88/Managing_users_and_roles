package morozov.ru.services.dbservices;

import morozov.ru.models.users.User;
import morozov.ru.models.users.UserWithRoles;

import java.util.List;

public interface UserService {

    boolean saveUser(UserWithRoles user);

    UserWithRoles getByLogin(String login);

    List<User> getAll();

    void deleteUser(String login);

    boolean updateUser(UserWithRoles user);

}
