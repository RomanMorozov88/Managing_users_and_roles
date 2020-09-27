package morozov.ru.controllers;

import morozov.ru.models.createupdateanswers.Answer;
import morozov.ru.models.createupdateanswers.NegativeAnswer;
import morozov.ru.models.users.User;
import morozov.ru.models.users.UserWithRoles;
import morozov.ru.services.dbservices.UserService;
import morozov.ru.services.validateservice.ValidateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Основной контроллер.
 * Обрабатывает запросы по адресу /muar/users:
 *
 * GET /muar/users
 * GET /muar/users/{login}
 * POST /muar/users
 * PUT /muar/users
 * DELETE /muar/users/{login}
 *
 * GET /muar/users/success
 * GET /muar/users/notfound
 *
 */
@RestController
public class UserControl {

    private static final Logger LOG = LogManager.getLogger(UserControl.class);
    private static final String LOGIN_IS = "Login is already exist";
    private static final String LOGIN_IS_NOT = "Login is not exist";
    private static final String NOT_FOUND = "Not found";

    private UserService userService;
    private ValidateService validateService;

    @Autowired
    public UserControl(UserService userService, ValidateService validateService) {
        this.userService = userService;
        this.validateService = validateService;
    }

    /**
     * Возвращает список всех пользователей
     * без указания их ролей.
     *
     * @return
     */
    @GetMapping("/muar/users")
    public List<User> getUsers() {
        return userService.getAll();
    }

    /**
     * Возвращает пользователя с указанием его ролей.
     * Если пользователя не существует-
     * редиректит на метод getNegativeAnswer()
     *
     * @param login
     * @return
     */
    @GetMapping("/muar/users/{login}")
    public UserWithRoles getUserByLogin(@PathVariable String login, HttpServletResponse response) {
        UserWithRoles result = userService.getByLogin(login);
        if (result == null) {
            this.redirectMsg.accept(response, "/muar/users/notfound");
        }
        return result;
    }

    /**
     * Добавление нового пользователя.
     *
     * @param user
     * @param response
     * @return
     */
    @PostMapping("/muar/users")
    public NegativeAnswer saveUser(@RequestBody UserWithRoles user, HttpServletResponse response) {
        NegativeAnswer nAnswer = this.validateService.validateFields(user);
        if (nAnswer.getSuccess()) {
            if (userService.saveUser(user)) {
                this.redirectMsg.accept(response, "/muar/users/success");
            } else {
                nAnswer.setSuccess(false);
                nAnswer.setErrorMessage(LOGIN_IS);
            }
        }
        return nAnswer;
    }

    /**
     * Редактирование пользователя.
     *
     * @param user
     * @param response
     * @return
     */
    @PutMapping("/muar/users")
    public NegativeAnswer updateUser(@RequestBody UserWithRoles user, HttpServletResponse response) {
        NegativeAnswer nAnswer = this.validateService.validateFields(user);
        if (nAnswer.getSuccess()) {
            if (userService.updateUser(user)) {
                this.redirectMsg.accept(response, "/muar/users/success");
            } else {
                nAnswer.setSuccess(false);
                nAnswer.setErrorMessage(LOGIN_IS_NOT);
            }
        }
        return nAnswer;
    }

    /**
     * Удаление пользователя по логину.
     *
     * @param login
     */
    @DeleteMapping("/muar/users/{login}")
    public void deleteUser(@PathVariable String login) {
        userService.deleteUser(login);
    }

    /**
     * Если добавление\редактирование прошли успешно-
     * перенаправляет из соответствующих методов сюда.
     *
     * @return
     */
    @GetMapping("/muar/users/success")
    public Answer getPositiveAnswer() {
        return new Answer(true);
    }

    /**
     * Если что-либо не найдено.
     * @return
     */
    @GetMapping("/muar/users/notfound")
    public NegativeAnswer getNegativeAnswer() {
        NegativeAnswer answer = new NegativeAnswer(false);
        answer.setErrorMessage(NOT_FOUND);
        return answer;
    }

    /**
     * Редиректим по мере необходимости.
     */
    private BiConsumer<HttpServletResponse, String> redirectMsg = (response, uri) -> {
        try {
            response.sendRedirect(uri);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("Error in UserControl:", e);
        }
    };
}
