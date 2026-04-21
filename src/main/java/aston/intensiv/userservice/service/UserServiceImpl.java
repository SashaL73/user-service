package aston.intensiv.userservice.service;

import aston.intensiv.userservice.dao.UserDao;
import aston.intensiv.userservice.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public User getUserById(Long id) {
        log.info("Получение пользователя c id: {}", id);
        return userDao.findById(id);
    }

    @Override
    public List<User> findAllUsers() {
        return userDao.findAll().stream()
                .sorted(Comparator.comparing(User::getId))
                .toList();
    }

    @Override
    public User saveUser(User user) {
        log.info("Создание пользователя: {}, {}, {}", user.getName(), user.getEmail(), user.getAge());
        validateUser(user);
        User savedUser = userDao.save(user);
        log.info("Пользователь создан: {}", savedUser);
        return savedUser;
    }

    @Override
    public User updateUser(User user) {
        log.info("Обновление пользователя id={}", user.getId());
        validateUser(user);
        User updatedUser = userDao.update(user);
        log.info("Пользователь обновлён: {}", updatedUser);
        return updatedUser;
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Удаление пользователя c id: {}", id);
        userDao.delete(id);
    }

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new IllegalArgumentException("Имя не должно быть пустым");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email не должен быть пустым");
        }
        if (user.getAge() == null || user.getAge() < 0) {
            throw new IllegalArgumentException("Возраст должен быть больше или равен 0");
        }
    }
}
