package morozov.ru.repositories;

import morozov.ru.models.users.User;
import morozov.ru.models.users.UserWithRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepo extends JpaRepository<UserWithRoles, String> {

    UserWithRoles findByLogin(String login);

    void deleteByLogin(String login);

    /**
     * Что бы вытащить из БД всех пользователей без ролей-
     * проворачиваем такой-вот финт.
     * @return
     */
    @Query("SELECT new morozov.ru.models.users.User(uwr.name, uwr.login, uwr.password) FROM UserWithRoles uwr")
    List<User> getAllUsers();
}
