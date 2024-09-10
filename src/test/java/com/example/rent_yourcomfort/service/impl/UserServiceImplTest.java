package com.example.rent_yourcomfort.service.impl;

import com.example.rent_yourcomfort.model.Role;
import com.example.rent_yourcomfort.model.User;
import com.example.rent_yourcomfort.model.Wallet;
import com.example.rent_yourcomfort.repository.UserRepository;
import com.example.rent_yourcomfort.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private WalletRepository walletRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateUser_UserAlreadyExists() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> {
            userService.createUser(user);
        }, "This user already exists!");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUser_NewUser() {

        User user = new User();
        user.setEmail("newuser@example.com");
        user.setFirstname("Tom");
        user.setLastname("Test");
        user.setPhone("0666219062");

        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setFrozenBalance(BigDecimal.ZERO);
        wallet.setCurrency("USD");
        wallet.setUser(user);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        when(userRepository.save(any(User.class))).thenReturn(user);

        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser.getWallet());
        assertEquals(BigDecimal.ZERO, createdUser.getWallet().getBalance());
        assertEquals(BigDecimal.ZERO, createdUser.getWallet().getFrozenBalance());

        verify(walletRepository).save(any(Wallet.class));
        verify(userRepository).save(any(User.class));
    }


    @Test
    void testGetUserById_UserExists() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        // Mock behavior for userRepository to return the user
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(userId);

        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());

        // Verify that the userRepository's findById method was called
        verify(userRepository).findById(userId);
    }

    @Test
    void testGetUserById_UserNotFound() {

        Long userId = 2L;

        // Mock behavior for userRepository to return an empty Optional
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.getUserById(userId));
        assertEquals("Housing not found with id " + userId, exception.getMessage());

        // Verify that the userRepository's findById method was called
        verify(userRepository).findById(userId);
    }

    @Test
    void testListUsers_UsersExist() {

        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        List<User> users = List.of(user1, user2);

        // Mock behavior for userRepository to return a list of users
        when(userRepository.findAll()).thenReturn(users);

        List<User> userList = userService.listUsers();

        assertNotNull(userList);
        assertEquals(2, userList.size());
        assertTrue(userList.contains(user1));
        assertTrue(userList.contains(user2));

        // Verify that the userRepository's findAll method was called
        verify(userRepository).findAll();
    }

    @Test
    void testListUsers_NoUsers() {

        List<User> users = Collections.emptyList();

        // Mock behavior for userRepository to return an empty list
        when(userRepository.findAll()).thenReturn(users);

        List<User> userList = userService.listUsers();

        assertNotNull(userList);
        assertTrue(userList.isEmpty());

        verify(userRepository).findAll();
    }

    @Test
    void testDeleteUser_UserExists() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        // Mock behavior for userRepository to return the user
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        // Verify that the delete method was called
        verify(userRepository).delete(user);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        // Given
        Long userId = 2L;

        // Mock behavior for userRepository to return an empty Optional
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(userId));
        assertEquals("Housing not found with id " + userId, exception.getMessage());

        // Verify that the delete method was not called
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void testUpdateUser_UserExists() {

        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setFirstname("OldFirstName");
        existingUser.setLastname("OldLastName");
        existingUser.setEmail("oldemail@example.com");
        existingUser.setPhone("1234567890");
        existingUser.setPassword("oldPassword");
        existingUser.setRole(Collections.singleton(Role.USER));
        existingUser.setBookings(new ArrayList<>());

        User updatedUser = new User();
        updatedUser.setFirstname("NewFirstName");
        updatedUser.setLastname("NewLastName");
        updatedUser.setEmail("newemail@example.com");
        updatedUser.setPhone("0987654321");
        updatedUser.setPassword("newPassword");
        updatedUser.setRole(Collections.singleton(Role.ADMIN));
        updatedUser.setBookings(new ArrayList<>());

        // Mock behavior for userRepository to return the existing user
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // Mock behavior for userRepository to save the updated user
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User result = userService.updateUser(userId, updatedUser);

        assertNotNull(result);
        assertEquals("NewFirstName", result.getFirstname());
        assertEquals("NewLastName", result.getLastname());
        assertEquals("newemail@example.com", result.getEmail());
        assertEquals("0987654321", result.getPhone());
        assertEquals("newPassword", result.getPassword());
        assertEquals(Collections.singleton(Role.ADMIN), result.getRole());
        assertEquals(new ArrayList<>(), result.getBookings());

        // Verify that the userRepository's save method was called
        verify(userRepository).save(existingUser);
    }

    @Test
    void testUpdateUser_UserNotFound() {

        Long userId = 2L;
        User userToUpdate = new User();

        // Mock behavior for userRepository to return an empty Optional
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.updateUser(userId, userToUpdate));
        assertEquals("Housing not found with id " + userId, exception.getMessage());

        // Verify that the save method was not called
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void testUpdateWalletToUser_UserExists() {
        // Given
        String email = "user@example.com";
        Wallet newWallet = new Wallet();
        newWallet.setBalance(BigDecimal.valueOf(100.00));
        newWallet.setFrozenBalance(BigDecimal.valueOf(10.00));
        newWallet.setCurrency("USD");

        User existingUser = new User();
        existingUser.setEmail(email);
        existingUser.setWallet(new Wallet()); // Existing wallet

        User updatedUser = new User();
        updatedUser.setEmail(email);
        updatedUser.setWallet(newWallet);

        // Mock behavior for userRepository to return the existing user
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        // Mock behavior for userRepository to save the updated user
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateWalletToUser(updatedUser);

        assertNotNull(result.getWallet());
        assertEquals(newWallet.getBalance(), result.getWallet().getBalance());
        assertEquals(newWallet.getFrozenBalance(), result.getWallet().getFrozenBalance());
        assertEquals(newWallet.getCurrency(), result.getWallet().getCurrency());

        // Verify that the userRepository's save method was called with an appropriate User object
        verify(userRepository).save(argThat(user ->
                user.getEmail().equals(email) &&
                        user.getWallet().getBalance().equals(newWallet.getBalance()) &&
                        user.getWallet().getFrozenBalance().equals(newWallet.getFrozenBalance()) &&
                        user.getWallet().getCurrency().equals(newWallet.getCurrency())
        ));
    }

    @Test
    void testUpdateWalletToUser_UserNotFound() {

        String email = "user@example.com";
        Wallet newWallet = new Wallet();
        newWallet.setBalance(BigDecimal.valueOf(100.00));
        newWallet.setFrozenBalance(BigDecimal.valueOf(10.00));
        newWallet.setCurrency("USD");

        User userWithNewWallet = new User();
        userWithNewWallet.setEmail(email);
        userWithNewWallet.setWallet(newWallet);

        // Mock behavior for userRepository to return an empty Optional
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.updateWalletToUser(userWithNewWallet));

        // Verify that the save method was not called
        verify(userRepository, never()).save(any(User.class));
    }

}
