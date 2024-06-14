package com.julien.paymybuddy.controller;

import com.julien.paymybuddy.dto.SecuredGetUserDTO;
import com.julien.paymybuddy.dto.SecuredPostUserDTO;
import com.julien.paymybuddy.entity.UserEntity;
import com.julien.paymybuddy.exception.UserAlreadyExistsException;
import com.julien.paymybuddy.exception.UserNotFoundException;
import com.julien.paymybuddy.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private static AutoCloseable mocks;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp(){
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterAll
    public static void close() throws Exception {
        if (null != mocks){
            mocks.close();
        }
    }

    @Test
    public void getAllUsersOkTest() {
        SecuredGetUserDTO user = new SecuredGetUserDTO();
        user.setUsername("test");
        user.setEmail("test@test.com");
        List<SecuredGetUserDTO> userList = List.of(user);

        when(userService.findAllUsers()).thenReturn(userList);

        ResponseEntity<List<SecuredGetUserDTO>> result = userController.getAllUsers();

        assertNotNull(result.getBody());
        assertEquals(result.getBody().getFirst().getUsername(), user.getUsername());
    }

    @Test
    public void getAllUsersWithNoUsersTest() {
        when(userService.findAllUsers()).thenReturn(Collections.emptyList());

        ResponseEntity<List<SecuredGetUserDTO>> result = userController.getAllUsers();

        assertNull(result.getBody());
        assertEquals(result.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void getAllUserWithUnknownError() {
        when(userService.findAllUsers()).thenThrow(new RuntimeException());

        ResponseEntity<List<SecuredGetUserDTO>> result = userController.getAllUsers();

        assertNull(result.getBody());
        assertEquals(result.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void getUserByIdOkTest() {
        SecuredGetUserDTO user = new SecuredGetUserDTO();
        user.setUsername("test");
        user.setEmail("test@test.com");

        when(userService.findUserById(any())).thenReturn(user);

        ResponseEntity<SecuredGetUserDTO> result = userController.getUserById(1);
        assertNotNull(result.getBody());
        assertEquals(result.getBody().getUsername(), user.getUsername());
    }

    @Test
    public void getUserByIdWithNotFoundUserTest() {
        when(userService.findUserById(any())).thenThrow(new UserNotFoundException());

        ResponseEntity<SecuredGetUserDTO> result = userController.getUserById(1);

        assertNull(result.getBody());
        assertEquals(result.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void getUserByIdWithUnknownErrorTest() {
        when(userService.findUserById(any())).thenThrow(new RuntimeException());

        ResponseEntity<SecuredGetUserDTO> result = userController.getUserById(1);

        assertNull(result.getBody());
        assertEquals(result.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void signUpUserOkTest() {
        UserEntity user = new UserEntity();
        user.setUserId(1);
        user.setUsername("test");
        user.setEmail("test@test.com");
        user.setPassword("password");

        SecuredPostUserDTO securedPostUserDTO = new SecuredPostUserDTO(user);
        SecuredGetUserDTO securedGetUserDTO = new SecuredGetUserDTO(user);

        when(userService.createUser(securedPostUserDTO)).thenReturn(securedGetUserDTO);

        ResponseEntity<SecuredGetUserDTO> result = userController.signUp(securedPostUserDTO);

        assertNotNull(result.getBody());
        assertEquals(user.getUsername(), result.getBody().getUsername());
    }

    @Test
    public void signUpUserWithUserAlreadyExistTest() {
        SecuredPostUserDTO user = new SecuredPostUserDTO();
        user.setUsername("test");
        user.setEmail("test@test.com");
        user.setPassword("password");

        when(userService.createUser(user)).thenThrow(new UserAlreadyExistsException("Email already taken."));

        ResponseEntity<SecuredGetUserDTO> result = userController.signUp(user);

        assertNull(result.getBody());
        assertEquals(result.getStatusCode(), HttpStatus.CONFLICT);
    }

    @Test
    public void signUpUserWithUnknownErrorTest() {
        SecuredPostUserDTO user = new SecuredPostUserDTO();
        user.setUsername("test");
        user.setEmail("test@test.com");
        user.setPassword("password");

        when(userService.createUser(user)).thenThrow(new RuntimeException());

        ResponseEntity<SecuredGetUserDTO> result = userController.signUp(user);

        assertNull(result.getBody());
        assertEquals(result.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void updateUserOkTest() {
        SecuredPostUserDTO securedPostUserDTO = new SecuredPostUserDTO();
        securedPostUserDTO.setEmail("john.doe@gmail.com");

        SecuredGetUserDTO securedGetUserDTO = new SecuredGetUserDTO();
        securedGetUserDTO.setUsername("username");
        securedGetUserDTO.setEmail("john.doe@gmail.com");

        when(userService.updateUser(1, securedPostUserDTO)).thenReturn(securedGetUserDTO);

        ResponseEntity<SecuredGetUserDTO> result = userController.updateUser(1, securedPostUserDTO);

        assertNotNull(result.getBody());
        assertEquals(result.getBody().getEmail(), securedPostUserDTO.getEmail());
    }

    @Test
    public void updateUserWithNotFoundUserTest() {
        SecuredPostUserDTO securedPostUserDTO = new SecuredPostUserDTO();
        securedPostUserDTO.setEmail("john.doe@gmail.com");

        when(userService.updateUser(1, securedPostUserDTO)).thenThrow(new UserNotFoundException());

        ResponseEntity<SecuredGetUserDTO> result = userController.updateUser(1, securedPostUserDTO);

        assertNull(result.getBody());
        assertEquals(result.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateUserWithUserAlreadyExistTest() {
        SecuredPostUserDTO securedPostUserDTO = new SecuredPostUserDTO();
        securedPostUserDTO.setEmail("john.doe@gmail.com");

        when(userService.updateUser(1, securedPostUserDTO)).thenThrow(new UserAlreadyExistsException("Email already taken."));

        ResponseEntity<SecuredGetUserDTO> result = userController.updateUser(1, securedPostUserDTO);

        assertNull(result.getBody());
        assertEquals(result.getStatusCode(), HttpStatus.CONFLICT);
    }

    @Test
    public void updateUserWithUnknownErrorTest() {
        SecuredPostUserDTO securedPostUserDTO = new SecuredPostUserDTO();
        securedPostUserDTO.setEmail("john.doe@gmail.com");

        when(userService.updateUser(1, securedPostUserDTO)).thenThrow(new RuntimeException());

        ResponseEntity<SecuredGetUserDTO> result = userController.updateUser(1, securedPostUserDTO);

        assertNull(result.getBody());
        assertEquals(result.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void deleteUserOkTest() throws JSONException {
        when(userService.deleteUser(1)).thenReturn(true);

        ResponseEntity<JSONObject> result = userController.deleteUser(1);

        assertNotNull(result.getBody());
        assertEquals(result.getBody().getString("message"), "User successfully deleted.");
    }

    @Test
    public void deleteUserWithNotFoundUserTest() {
        when(userService.deleteUser(1)).thenThrow(new UserNotFoundException());

        ResponseEntity<JSONObject> result = userController.deleteUser(1);

        assertNull(result.getBody());
        assertEquals(result.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteUserWithUnknownErrorTest() {
        when(userService.deleteUser(1)).thenThrow(new RuntimeException());

        ResponseEntity<JSONObject> result = userController.deleteUser(1);

        assertNull(result.getBody());
        assertEquals(result.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
