package com.ecomm_alten.back.service;

import com.ecomm_alten.back.dto.SignupRequest;
import com.ecomm_alten.back.exception.UserNotFoundException;
import com.ecomm_alten.back.exception.UserAlreadyExistsException;
import com.ecomm_alten.back.model.Role;
import com.ecomm_alten.back.model.User;
import com.ecomm_alten.back.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith( MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder encoder;
    @InjectMocks
    UserService userService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(
                userService,
                "adminEmail",
                "admin@admin.com"
        );
    }


    @Test
    void register_AdminEmail_AssignsAdminRole() {
        var dto = new SignupRequest(
                "admin",
                "admin",
                "adminpassword",
                "admin@admin.com"
        );

        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(encoder.encode(dto.password())).thenReturn("encodedAdminPassword");

        userService.register(dto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("admin@admin.com", savedUser.getEmail());
        assertTrue(savedUser.getRoles().contains(Role.ADMIN));
    }
    @Test
    void register_Success() {
        var dto = new SignupRequest(
                "souka",
                "soukaa",
                "encoded",
                "souka.gua@gmail.com"
        );
        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(encoder.encode(dto.password())).thenReturn("encoded");
        userService.register(dto);
        ArgumentCaptor<User> userCap = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCap.capture());
        User saved = userCap.getValue();
        assertEquals("souka", saved.getUsername());
        assertEquals("soukaa", saved.getFirstname());
        assertEquals("souka.gua@gmail.com", saved.getEmail());
        assertEquals("encoded", saved.getPassword());
    }

    @Test
    void register_EmailExists_Throws() {
        var dto = new SignupRequest("souka","soukaa","secret","souka.gua@gmail.com");
        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () ->
                userService.register(dto)
        );
        verify(userRepository, never()).save(any());
    }

    @Test
    void loadUserByUsername_NotFound() {
        when(userRepository.findByEmail("x@x.com")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () ->
                userService.loadUserByUsername("x@x.com")
        );
    }
}
