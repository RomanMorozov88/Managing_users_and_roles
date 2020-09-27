package morozov.ru.controllers;

import morozov.ru.models.Role;
import morozov.ru.services.dbservices.RoleService;
import morozov.ru.services.validateservice.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления ролями.
 * Обрабатывает запросы по адресу /muar/roles
 */
@RestController
public class RoleControl {

    private RoleService roleService;
    private ValidateService validateService;

    @Autowired
    public RoleControl(RoleService roleService, ValidateService validateService) {
        this.roleService = roleService;
        this.validateService = validateService;
    }

    /**
     * Список всех ролей.
     *
     * @return
     */
    @GetMapping("/muar/roles")
    public List<Role> getRoles() {
        return roleService.getAll();
    }

    /**
     * Добавление новой роли.
     * Если новая роль добавляется в БД-
     * то обновляется и список ролей, хранящийся в validateService.
     *
     * @param role
     * @return
     */
    @PostMapping("/muar/roles")
    public Role saveRole(@RequestBody Role role) {
        Role result = null;
        if (!this.validateService.checkRoles(role)) {
            result = roleService.saveRole(role);
            this.validateService.setExistingRoles(roleService.getAll());
        }
        return result;
    }

    /**
     * Удаление роли.
     *
     * @param role
     */
    @DeleteMapping("/muar/roles/{role}")
    public void deleteRole(@PathVariable String role) {
        roleService.deleteRole(role);
    }
}
