package morozov.ru.services.dbservices;

import morozov.ru.models.Role;
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
public class RoleServiceImplTest {

    @Autowired
    private RoleServiceImpl roleService;

    @Test
    public void whenGetAllRoles() {
        //т.к. при старте приложения в БД создаются три записи ролей- ожидется 3 а не 0.
        assertEquals(3, roleService.getAll().size());
    }

    @Test
    public void whenSaveRole() {
        Role role = new Role();
        role.setName("Test Role");
        roleService.saveRole(role);
        assertEquals(4, roleService.getAll().size());
    }

    @Test
    public void whenDeleteRole() {
        Role role = new Role();
        role.setName("Test Role");
        roleService.saveRole(role);
        roleService.deleteRole(role.getName());
        assertEquals(3, roleService.getAll().size());
    }
}