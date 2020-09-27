package morozov.ru.services.validateservice;

import morozov.ru.models.Role;
import morozov.ru.models.createupdateanswers.NegativeAnswer;
import morozov.ru.models.users.UserWithRoles;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Проверка входящих данных.
 */
@Component
public class ValidateService {

    private static final String PASSWORD_PATTERN = "(.*[0-9]+.*[A-Z]+.*)|(.*[A-Z]+.*[0-9]+.*)";
    private static final String EMPTY_NAME = "Empty name";
    private static final String EMPTY_LOGIN = "Empty login";
    private static final String EMPTY_PASSWORD = "Empty password";
    private static final String WRONG_PASSWORD = "Wrong password format";
    private static final String WRONG_ROLES = "Wrong role or roles";

    private List<Role> existingRoles = new ArrayList<>();

    public ValidateService() {
    }

    public List<Role> getExistingRoles() {
        return existingRoles;
    }

    public void setExistingRoles(List<Role> existingRoles) {
        this.existingRoles = existingRoles;
    }

    /**
     * Основная проверка всех полей.
     *
     * @param user
     * @return
     */
    public NegativeAnswer validateFields(UserWithRoles user) {
        NegativeAnswer result = new NegativeAnswer();
        boolean flag = true;
        if (user.getName() == null || user.getName().equals("")) {
            result.setErrorMessage(EMPTY_NAME);
            flag = false;
        }
        if (user.getLogin() == null || user.getLogin().equals("")) {
            result.setErrorMessage(EMPTY_LOGIN);
            flag = false;
        }
        if (user.getPassword() == null || user.getPassword().equals("")) {
            result.setErrorMessage(EMPTY_PASSWORD);
            flag = false;
        } else if (!checkPassword(user.getPassword())) {
            result.setErrorMessage(WRONG_PASSWORD);
            flag = false;
        }
        if (
                user.getRoles() != null
                        && user.getRoles().size() > 0
                        && !checkRoles(user.getRoles())
        ) {
            result.setErrorMessage(WRONG_ROLES);
            flag = false;
        }
        result.setSuccess(flag);
        return result;
    }

    /**
     * Проверяет строку на соответствие шаблону.
     *
     * @param password
     * @return
     */
    private boolean checkPassword(String password) {
        boolean result = false;
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        String buffer = null;
        while (matcher.find()) {
            buffer = matcher.group();
        }
        if (buffer != null) {
            result = true;
        }
        return result;
    }

    /**
     * Проверка корректности ролей.
     * Подразумевается, что на входе будет только id роли.
     *
     * @param roles
     * @return
     */
    private boolean checkRoles(Set<Role> roles) {
        return roles.stream().allMatch(x -> {
            boolean result = false;
            for (Role r : this.existingRoles) {
                result = r.getId() == x.getId();
                if (result) {
                    break;
                }
            }
            return result;
        });
    }

    public boolean checkRoles(Role role) {
        boolean result = false;
        for (Role r : existingRoles) {
            result = role.getName().equals(r.getName());
            if (result) {
                break;
            }
        }
        return result;
    }

}