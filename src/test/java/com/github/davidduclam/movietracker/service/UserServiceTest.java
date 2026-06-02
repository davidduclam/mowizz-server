package com.github.davidduclam.movietracker.service;

import com.github.davidduclam.movietracker.model.User;
import com.github.davidduclam.movietracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testSaveUser() {
        User u = new User();
        when(userRepository.save(u)).thenReturn(u);

        User result = userService.saveUser(u);

        assertEquals(u, result);
    }

    @Test
    void testGetAllUsers() {
        User u = new User();
        u.setUsername("Test");
        when(userRepository.findAll()).thenReturn(List.of(u));

        List<User> result = userService.getAllUsers();

        assertEquals(List.of(u), result);
    }

    @Test
    void testGetAllUsers_EmptyList() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<User> result = userService.getAllUsers();

        assertEquals(List.of(), result);
    }

    @Test
    void testGetUserById() {
        User u = new User();
        u.setUsername("Test");
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));

        Optional<User> result = userService.getUserById(1L);

        assertEquals(Optional.of(u), result);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(1L);

        assertEquals(Optional.empty(), result);
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
}
