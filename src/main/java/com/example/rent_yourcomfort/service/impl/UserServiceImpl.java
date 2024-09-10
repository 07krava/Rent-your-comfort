package com.example.rent_yourcomfort.service.impl;

import com.example.rent_yourcomfort.service.UserService;
import com.example.rent_yourcomfort.model.Role;
import com.example.rent_yourcomfort.model.User;
import com.example.rent_yourcomfort.model.Wallet;
import com.example.rent_yourcomfort.repository.UserRepository;
import com.example.rent_yourcomfort.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private WalletRepository walletRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    public User createUser(User user) {

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new RuntimeException("This user already exists!");
        }

        Wallet walletEntity = user.getWallet();
        if (walletEntity == null) {
            // Если кошелька нет, создаем новый
            walletEntity = new Wallet();
            walletEntity.setUser(user);
            user.setWallet(walletEntity);
        }

        user.getWallet().setBalance(walletEntity.getBalance() != null ? walletEntity.getBalance() : BigDecimal.ZERO);
        walletEntity.setCurrency(walletEntity.getCurrency());
        walletEntity.setFrozenBalance(walletEntity.getFrozenBalance()!= null ? walletEntity.getFrozenBalance() : BigDecimal.ZERO);        walletEntity.setUser(user);

        user.setWallet(walletEntity);
        user.setRole(Collections.singleton(Role.USER));
        walletRepository.save(walletEntity);

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Housing not found with id " + id));
        return user;
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Housing not found with id " + id));
        userRepository.delete(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        User userEntity = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Housing not found with id " + id));
        userEntity.setFirstname(user.getFirstname());
        userEntity.setLastname(user.getLastname());
        userEntity.setEmail(user.getEmail());
        userEntity.setPhone(user.getPhone());
        userEntity.setPassword(user.getPassword());
        userEntity.setRole(user.getRole());
        userEntity.setBookings(user.getBookings());
        userEntity.setBookings(user.getBookings());

        return userRepository.save(userEntity);
    }

    public User updateWalletToUser(User user){
        User user1 = userRepository.findByEmail(user.getEmail()).orElseThrow();
        user1.setWallet(user.getWallet());
        return userRepository.save(user1);
    }
}
