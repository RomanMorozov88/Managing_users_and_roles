package morozov.ru;

import morozov.ru.models.Role;
import morozov.ru.models.users.UserWithRoles;
import morozov.ru.services.dbservices.RoleService;
import morozov.ru.services.dbservices.UserService;
import morozov.ru.services.validateservice.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

/**
 * Записывает некоторые Role и UserWithRoles в БД при старте приложения.
 * Так же заполняет список ролей в validateService.
 */
@Component
public class DataInit {

    private RoleService roleService;
    private UserService userService;
    private ValidateService validateService;

    @Autowired
    public DataInit(
            RoleService roleService,
            UserService userService,
            ValidateService validateService
    ) {
        this.roleService = roleService;
        this.userService = userService;
        this.validateService = validateService;
    }

    @PostConstruct
    @Transactional
    public void setDataInDB() {
        roleService.saveRole(new Role("admin"));
        roleService.saveRole(new Role("user"));
        roleService.saveRole(new Role("guest"));

        UserWithRoles user = new UserWithRoles();
        user.setName("Vasya");
        user.setLogin("vasya");
        user.setPassword("4Evasya");
        Role role = new Role();
        role.setId(2); //Мы УВЕРЕНЫ, что у нас есть роль с id = 2.
        user.setRole(role);
        userService.saveUser(user);
        //т.к. ролей не очень много и не планируется частое их добавление\удаление
        //то в validateService будет хранится обычный лист ролей,
        //что бы не ходить в БД каждый раз при проверке ролей во время
        //Добавления\редактирования пользователей.
        validateService.setExistingRoles(roleService.getAll());
    }
}
