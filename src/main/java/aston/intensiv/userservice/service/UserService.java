package aston.intensiv.userservice.service;

import aston.intensiv.userservice.model.User;

import java.util.List;

public interface UserService {

    User getUserById(Long id);

    List<User> findAllUsers();

    User saveUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);
}
