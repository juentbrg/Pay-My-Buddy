package com.julien.paymybuddy.controller;

import com.julien.paymybuddy.entity.UserEntity;
import com.julien.paymybuddy.pojo.LoginRequest;
import com.julien.paymybuddy.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    private static AutoCloseable mocks;

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpSession httpSession;

    @BeforeEach
    protected void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterAll
    public static void close() throws Exception {
        if (null != mocks){
            mocks.close();
        }
    }

    @Test
    public void testLoginSuccess() {
        UserEntity user = new UserEntity();
        user.setUserId(1);
        user.setEmail("john.doe@gmail.com");
        user.setUsername("john");
        user.setPassword("$2y$10$JJ2Pmluh0vOtzWEbJYLtTeut12Z7XIqS4Md.6DbO9ZVV9yP54pHPS");

        LoginRequest loginRequest = new LoginRequest("john.doe@gmail.com", "password");

        when(userRepository.findByEmail("john.doe@gmail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("$2y$10$JJ2Pmluh0vOtzWEbJYLtTeut12Z7XIqS4Md.6DbO9ZVV9yP54pHPS", "password")).thenReturn(true);
        when(httpServletRequest.getSession(true)).thenReturn(httpSession);

        ResponseEntity<?> result = authController.login(loginRequest, httpServletRequest);

        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void testLoginInvalidPassword() {
        UserEntity user = new UserEntity();
        user.setUserId(1);
        user.setEmail("john.doe@gmail.com");
        user.setUsername("john");
        user.setPassword("$2y$10$JJ2Pmluh0vOtzWEbJYLtTeut12Z7XIqS4Md.6DbO9ZVV9yP54pHPS");

        LoginRequest loginRequest = new LoginRequest("john.doe@gmail.com", "notOkPassword");

        when(userRepository.findByEmail("john.doe@gmail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("$2y$10$JJ2Pmluh0vOtzWEbJYLtTeut12Z7XIqS4Md.6DbO9ZVV9yP54pHPS", "notOkPassword")).thenReturn(false);

        ResponseEntity<?> result = authController.login(loginRequest, httpServletRequest);

        assertEquals(401, result.getStatusCode().value());
    }

    @Test
    public void testLoginUserNotFound() {
        LoginRequest loginRequest = new LoginRequest("john.doe@gmail.com", "notOkPassword");

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        ResponseEntity<?> result = authController.login(loginRequest, httpServletRequest);

        assertEquals(401, result.getStatusCode().value());

        verify(httpServletRequest, never()).getSession(anyBoolean());
    }

    @Test
    public void testLogoutSuccess() {
        when(httpServletRequest.getSession(true)).thenReturn(httpSession);

        ResponseEntity<?> result = authController.logout(httpServletRequest);

        assertEquals(200, result.getStatusCode().value());
        verify(httpSession).removeAttribute("user");
        verify(httpSession).invalidate();
    }

    @Test
    public void testLogoutNoSession() {
        when(httpServletRequest.getSession(true)).thenReturn(null);

        ResponseEntity<?> result = authController.logout(httpServletRequest);

        assertEquals(404, result.getStatusCode().value());
    }

    @Test
    public void testCheckSessionActive() {
        UserEntity user = new UserEntity();
        user.setUserId(1);
        user.setEmail("john.doe@gmail.com");
        user.setUsername("john");

        when(httpSession.getAttribute("user")).thenReturn(user);

        ResponseEntity<?> result = authController.checkSession(httpSession);

        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void testCheckSessionInactive() {
        when(httpSession.getAttribute("user")).thenReturn(null);

        ResponseEntity<?> result = authController.checkSession(httpSession);

        assertEquals(401, result.getStatusCode().value());
    }
}
