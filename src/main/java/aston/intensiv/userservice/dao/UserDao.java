package aston.intensiv.userservice.dao;

import aston.intensiv.userservice.model.User;

import java.util.List;

public interface UserDao {
    User findById(Long id);

    User save(User user);

    void delete(Long id);

    User update(User user);

    List<User> findAll();
}
