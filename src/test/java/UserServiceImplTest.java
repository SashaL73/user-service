import aston.intensiv.userservice.dao.UserDao;
import aston.intensiv.userservice.model.User;
import aston.intensiv.userservice.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    private UserDao userDao;
    private UserServiceImpl userService;

    @BeforeEach
    void set() {
        userDao = Mockito.mock(UserDao.class);
        userService = new UserServiceImpl(userDao);
    }

    @Test
    void testSaveUser() {
        User user = new User();
        user.setName("Test");
        user.setEmail("test@test.com");
        user.setAge(20);
        user.setCreatedAt(LocalDateTime.now());

        when(userDao.save(user)).thenReturn(user);

        User savedUser = userService.saveUser(user);
        assertEquals("Test", savedUser.getName());
    }

    @Test
    void testSaveUserWithEmptyName() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setAge(20);
        user.setCreatedAt(LocalDateTime.now());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(user);
        });

        assertEquals("Имя не должно быть пустым", exception.getMessage());

    }

    @Test
    void testSaveUserWithEmptyEmail() {
        User user = new User();
        user.setName("Test");
        user.setAge(20);
        user.setCreatedAt(LocalDateTime.now());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(user);
        });

        assertEquals("Email не должен быть пустым", exception.getMessage());

    }

    @Test
    void testSaveUserWithEmptyAge() {
        User user = new User();
        user.setName("Test");
        user.setEmail("test@test.com");
        user.setCreatedAt(LocalDateTime.now());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(user);
        });

        assertEquals("Возраст должен быть больше или равен 0", exception.getMessage());

    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setId(1L);
        user.setName("Test");
        user.setEmail("test@test.com");
        user.setAge(20);
        user.setCreatedAt(LocalDateTime.now());

        when(userDao.save(user)).thenReturn(user);
        when(userDao.findById(1L)).thenReturn(user);

        User savedUser = userService.saveUser(user);

        User findUser = userService.getUserById(1L);

        assertEquals(savedUser.getName(), findUser.getName());
    }

    @Test
    void findAllUsers() {
        User user = new User();
        user.setId(1L);
        user.setName("Test");
        user.setEmail("test@test.com");
        user.setAge(20);
        user.setCreatedAt(LocalDateTime.now());

        User user1 = new User();
        user1.setId(2L);
        user1.setName("Test1");
        user1.setEmail("test1@test.com");
        user1.setAge(21);
        user1.setCreatedAt(LocalDateTime.now());

        when(userDao.findAll()).thenReturn(List.of(user,user1));

        List<User> users = userService.findAllUsers();

        assertEquals(2, users.size());

    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setName("Test");
        user.setEmail("test@test.com");
        user.setAge(20);
        user.setCreatedAt(LocalDateTime.now());

        when(userDao.update(user)).thenReturn(user);

        User updatedUser = userService.updateUser(user);

        assertEquals(user.getName(), updatedUser.getName());
    }

    @Test
    void testDeleteUser() {
        Long id = 1L;
        userService.deleteUser(id);
    }
}
