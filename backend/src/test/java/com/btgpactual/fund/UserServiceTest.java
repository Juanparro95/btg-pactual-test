package com.btgpactual.fund;

import com.btgpactual.fund.model.User;
import com.btgpactual.fund.repository.IUserRepository;
import com.btgpactual.fund.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setId("1");
        mockUser.setName("John Doe");
        mockUser.setEmail("example@user.com");
        mockUser.setTotalTransactions(new BigDecimal("100000"));
    }

    // Test: Crear usuario
    @Test
    void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User createdUser = userService.createUser(mockUser);

        assertNotNull(createdUser);
        assertEquals("John Doe", createdUser.getName());
        verify(userRepository, times(1)).save(mockUser);
    }

    // Test: Encontrar usuario por ID
    @Test
    void testFindUserById() {
        when(userRepository.findById("1")).thenReturn(Optional.of(mockUser));

        Optional<User> userOptional = userService.findUserById("1");

        assertTrue(userOptional.isPresent());
        assertEquals("John Doe", userOptional.get().getName());
    }

    // Test: Encontrar usuario por email
    @Test
    void testFindUserByEmail() {
        when(userRepository.findByEmail("example@user.com")).thenReturn(mockUser);

        User user = userService.findUserByEmail("example@user.com");

        assertNotNull(user);
        assertEquals("example@user.com", user.getEmail());
    }

    // Test: Actualizar usuario
    @Test
    void testUpdateUser() {
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        mockUser.setName("Jane Doe");
        User updatedUser = userService.updateUser(mockUser);

        assertEquals("Jane Doe", updatedUser.getName());
        verify(userRepository, times(1)).save(mockUser);
    }

    // Test: Eliminar usuario
    @Test
    void testDeleteUser() {
        userService.deleteUser("1");

        verify(userRepository, times(1)).deleteById("1");
    }
}
