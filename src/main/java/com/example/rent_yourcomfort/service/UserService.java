package com.example.rent_yourcomfort.service;

import com.example.rent_yourcomfort.model.User;
import java.util.List;

public interface UserService {

    User createUser(User user);

    User getUserById(Long id);

    List<User> listUsers();

    void deleteUser(Long id);

    User updateUser(Long id, User user);

    User updateWalletToUser(User user);

}
