package morozov.ru.services.dbservices;

import morozov.ru.models.users.UserWithRoles;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestEntityManager
@ComponentScan("morozov.ru")
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    public void whenSaveAndGetUser() {
        UserWithRoles user = new UserWithRoles();
        user.setName("name");
        user.setLogin("login");
        user.setPassword("password");
        userService.saveUser(user);
        UserWithRoles result = userService.getByLogin(user.getLogin());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getLogin(), result.getLogin());
        assertEquals(user.getPassword(), result.getPassword());
    }

    @Test
    public void whenSaveAnDeleteUser() {
        UserWithRoles user = new UserWithRoles();
        user.setName("name");
        user.setLogin("login");
        user.setPassword("password");
        userService.saveUser(user);
        userService.deleteUser("login");
        //т.к. при старте приложения в БД создаётся одна запись поьзователя- ожидется 1 а не 0.
        assertEquals(1, userService.getAll().size());
    }

}