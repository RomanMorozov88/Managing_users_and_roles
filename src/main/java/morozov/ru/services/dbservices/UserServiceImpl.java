package morozov.ru.services.dbservices;

import morozov.ru.models.users.User;
import morozov.ru.models.users.UserWithRoles;
import morozov.ru.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(isolation = Isolation.READ_COMMITTED)
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public boolean saveUser(UserWithRoles user) {
        boolean result = false;
        if (this.userRepo.findByLogin(user.getLogin()) == null) {
            this.userRepo.save(user);
            result = true;
        }
        return result;
    }

    @Override
    public UserWithRoles getByLogin(String login) {
        return userRepo.findByLogin(login);
    }

    @Override
    public List<User> getAll() {
        return userRepo.getAllUsers();
    }

    @Override
    public void deleteUser(String login) {
        userRepo.deleteByLogin(login);
    }

    /**
     * Предполагаем, что все проверки на null
     * уже были сделаны.
     *
     * @param user
     * @return
     */
    @Override
    public boolean updateUser(UserWithRoles user) {
        boolean result = false;
        UserWithRoles buffer = userRepo.findByLogin(user.getLogin());
        if (buffer != null) {
            if (user.getRoles() != null && user.getRoles().size() > 0) {
                userRepo.save(user);
            } else {
                user.setRoles(buffer.getRoles());
                userRepo.save(user);
            }
            result = true;
        }
        return result;
    }
}
