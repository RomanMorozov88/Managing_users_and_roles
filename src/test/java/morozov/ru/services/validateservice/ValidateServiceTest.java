package morozov.ru.services.validateservice;

import morozov.ru.models.Role;
import morozov.ru.models.createupdateanswers.NegativeAnswer;
import morozov.ru.models.users.UserWithRoles;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class ValidateServiceTest {

    private ValidateService validateService;

    @Before
    public void dataInit() {
        validateService = new ValidateService();
        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setId(1);
        role.setName("test 01");
        roles.add(role);
        role = new Role();
        role.setId(2);
        role.setName("test 02");
        roles.add(role);
        validateService.setExistingRoles(roles);
    }

    @Test
    public void whenCheckRightUser() {
        UserWithRoles user = new UserWithRoles();
        user.setName("name");
        user.setLogin("login");
        user.setPassword("1Qpassword");
        Role role = new Role();
        role.setId(1);
        user.setRole(role);
        NegativeAnswer expected = new NegativeAnswer(true);
        NegativeAnswer result = validateService.validateFields(user);
        assertEquals(expected, result);
    }

    @Test
    public void whenCheckWrongUser() {
        UserWithRoles user = new UserWithRoles();
        user.setName("");
        user.setPassword("password");
        Role role = new Role();
        role.setId(400);
        user.setRole(role);
        NegativeAnswer expected = new NegativeAnswer(false);
        expected.setErrorMessage("Empty name");
        expected.setErrorMessage("Empty login");
        expected.setErrorMessage("Wrong password format");
        expected.setErrorMessage("Wrong role or roles");
        NegativeAnswer result = validateService.validateFields(user);
        assertEquals(expected, result);
    }

    @Test
    public void whenCheckSingleWrongRole() {
        Role role = new Role();
        role.setName("Some inexist role name");
        boolean expected = false;
        boolean result = validateService.checkRoles(role);
        assertEquals(expected, result);
    }

    @Test
    public void whenCheckSingleCorrectRole() {
        Role role = new Role();
        role.setName("test 01");
        boolean expected = true;
        boolean result = validateService.checkRoles(role);
        assertEquals(expected, result);
    }
}