import aston.intensiv.userservice.config.TestHibernateConfig;
import aston.intensiv.userservice.dao.UserDao;
import aston.intensiv.userservice.dao.UserDaoImpl;
import aston.intensiv.userservice.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
public class UserDaoImplTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.1")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("12345");

    private static SessionFactory sessionFactory;
    private UserDao userDao;

    @BeforeAll
    static void beforeAll() {
        sessionFactory = TestHibernateConfig.buildSessionFactory(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        );
    }

    @BeforeEach
    void setUserDao() {
        userDao = new UserDaoImpl(sessionFactory);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            tx.commit();
        }
    }

    @AfterAll
    static void closeSession() {
        sessionFactory.close();
    }

    @Test
    @Order(1)
    void testSaveUserGetName() {
        User user = new User();
        user.setName("Test");
        user.setEmail("test@test.com");
        user.setAge(20);

        User saved = userDao.save(user);

        assertNotNull(saved.getId());
        assertEquals("Test", saved.getName());
    }

    @Test
    @Order(2)
    void testFindUserById() {
        User user = new User();
        user.setName("Test1");
        user.setEmail("test@test.com");
        user.setAge(20);

        User saved = userDao.save(user);

        assertEquals("Test1",userDao.findById(saved.getId()).getName());
    }

    @Test
    @Order(3)
    void testFindAllUsers() {
        User user = new User();
        user.setName("Test");
        user.setEmail("test@test.com");
        user.setAge(20);

        User user1 = new User();
        user1.setName("Test1");
        user1.setEmail("test1@test.com");
        user1.setAge(20);

        userDao.save(user);
        userDao.save(user1);

        List<User> userList = userDao.findAll();

        assertEquals(2,userList.size());
    }

    @Test
    @Order(4)
    void testUpdateUser() {
        User user = new User();
        user.setName("Test");
        user.setEmail("test@test.com");
        user.setAge(20);

        User savedUser = userDao.save(user);

        User userForUpdate = userDao.findById(savedUser.getId());

        assertEquals("Test", userForUpdate.getName());
        assertEquals("test@test.com", userForUpdate.getEmail());
        assertEquals(20, userForUpdate.getAge());

        userForUpdate.setName("Updated");
        userForUpdate.setEmail("updated@updated.com");
        userForUpdate.setAge(22);

        User updatedUser = userDao.update(userForUpdate);

        assertEquals("Updated", updatedUser.getName());
        assertEquals("updated@updated.com", updatedUser.getEmail());
        assertEquals(22, updatedUser.getAge());
    }

    @Test
    @Order(5)
    void testDeleteUser() {
        User user = new User();
        user.setName("Test");
        user.setEmail("test@test.com");
        user.setAge(20);

        User user1 = new User();
        user1.setName("Test1");
        user1.setEmail("test1@test.com");
        user1.setAge(20);

        User savedUser = userDao.save(user);
        userDao.save(user1);

        List<User> userList = userDao.findAll();
        assertEquals(2,userList.size());

        userDao.delete(savedUser.getId());

        List<User> userListAfterDelete = userDao.findAll();
        assertEquals(1,userListAfterDelete.size());

        userListAfterDelete.stream()
                .peek(u -> assertEquals("Test1", u.getName()));

    }
}
