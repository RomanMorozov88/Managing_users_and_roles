package morozov.ru.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import morozov.ru.models.createupdateanswers.NegativeAnswer;
import morozov.ru.models.users.User;
import morozov.ru.models.users.UserWithRoles;
import morozov.ru.services.dbservices.UserService;
import morozov.ru.services.validateservice.ValidateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

/**
 * Тестируется только контроллер и его ответы.
 * UserService и Validate заглушены.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(UserControl.class)
public class UserControlTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private ValidateService validateService;

    @Test
    public void whenGetAllUsers() throws Exception {
        User user = new User();
        user.setName("Test Name");
        user.setLogin("Test Login");
        user.setPassword("Test Password");
        Mockito.when(userService.getAll())
                .thenReturn(new ArrayList<User>(Arrays.asList(user)));
        mockMvc.perform(get("/muar/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(user.getName()))
                .andExpect(jsonPath("$[0].login").value(user.getLogin()))
                .andExpect(jsonPath("$[0].password").value(user.getPassword()));
    }

    @Test
    public void whenGetUserByLogin() throws Exception {
        UserWithRoles user = new UserWithRoles();
        user.setName("Test Name");
        user.setLogin("Test Login");
        user.setPassword("Test Password");
        Mockito.when(userService.getByLogin(ArgumentMatchers.any(String.class)))
                .thenReturn(user);
        mockMvc.perform(get("/muar/users/Test login")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.login").value(user.getLogin()))
                .andExpect(jsonPath("$.password").value(user.getPassword()))
                .andExpect(jsonPath("$.roles", hasSize(0)));
    }

    @Test
    public void whenGetNullUserByLogin() throws Exception {
        Mockito.when(userService.getByLogin(ArgumentMatchers.any(String.class)))
                .thenReturn(null);
        mockMvc.perform(get("/muar/users/login")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(redirectedUrl("/muar/users/notfound"));
    }

    @Test
    public void whenSaveUser() throws Exception {
        UserWithRoles user = new UserWithRoles();
        user.setName("Test Name");
        user.setLogin("Test Login");
        user.setPassword("Test Password");
        Mockito.when(userService.saveUser(ArgumentMatchers.any(UserWithRoles.class)))
                .thenReturn(true);
        Mockito.when(validateService.validateFields(ArgumentMatchers.any(UserWithRoles.class)))
                .thenReturn(new NegativeAnswer(true));
        mockMvc.perform(post("/muar/users")
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(redirectedUrl("/muar/users/success"))
                .andExpect(jsonPath("$.success").value("true"));
    }

    @Test
    public void whenUpdateUser() throws Exception {
        UserWithRoles user = new UserWithRoles();
        user.setName("Test Name");
        user.setLogin("Test Login");
        user.setPassword("Test Password");
        Mockito.when(userService.updateUser(ArgumentMatchers.any(UserWithRoles.class)))
                .thenReturn(true);
        Mockito.when(validateService.validateFields(ArgumentMatchers.any(UserWithRoles.class)))
                .thenReturn(new NegativeAnswer(true));
        mockMvc.perform(put("/muar/users")
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(redirectedUrl("/muar/users/success"))
                .andExpect(jsonPath("$.success").value("true"));
    }

    @Test
    public void whenSaveWrongUser() throws Exception {
        UserWithRoles user = new UserWithRoles();
        user.setName("");
        user.setPassword("password");
        NegativeAnswer answer = new NegativeAnswer(false);
        answer.setErrorMessage("Some error 01");
        answer.setErrorMessage("Some error 02");
        Mockito.when(validateService.validateFields(ArgumentMatchers.any(UserWithRoles.class)))
                .thenReturn(answer);
        mockMvc.perform(post("/muar/users")
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.errors[0]").value("Some error 01"))
                .andExpect(jsonPath("$.errors[1]").value("Some error 02"));
    }

    @Test
    public void whenUpdateWrongUser() throws Exception {
        UserWithRoles user = new UserWithRoles();
        user.setName("");
        user.setPassword("password");
        NegativeAnswer answer = new NegativeAnswer(false);
        answer.setErrorMessage("Some error 01");
        answer.setErrorMessage("Some error 02");
        Mockito.when(validateService.validateFields(ArgumentMatchers.any(UserWithRoles.class)))
                .thenReturn(answer);
        mockMvc.perform(put("/muar/users")
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.errors[0]").value("Some error 01"))
                .andExpect(jsonPath("$.errors[1]").value("Some error 02"));
    }

}