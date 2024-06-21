package com.julien.paymybuddy.controller;

import com.julien.paymybuddy.dto.SecuredGetUserDTO;
import com.julien.paymybuddy.dto.SecuredPostUserDTO;
import com.julien.paymybuddy.exception.UserAlreadyExistsException;
import com.julien.paymybuddy.exception.UserNotFoundException;
import com.julien.paymybuddy.service.UserService;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<SecuredGetUserDTO>> getAllUsers() {
        try {
            List<SecuredGetUserDTO> userList = userService.findAllUsers();
            if (userList.isEmpty()) {
                logger.error("No users found.");
                return ResponseEntity.noContent().build();
            }
            logger.info("Successfully retrieved all users.");
            return ResponseEntity.ok(userList);
        } catch (Exception e){
            logger.error("Error retrieving users.", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecuredGetUserDTO> getUserById(@PathVariable("id") long id) {
        try {
            SecuredGetUserDTO user = userService.findUserById(id);
            logger.info("Successfully retrieved user with id {}", id);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            logger.error("No user found with id {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Retrieve user error: unknown error.");
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<SecuredGetUserDTO> register(@Valid @RequestBody SecuredPostUserDTO securedPostUserDTO) {
        try {
            SecuredGetUserDTO user = userService.createUser(securedPostUserDTO);
            logger.info("Successfully created new user.");
            return ResponseEntity.ok(user);
        } catch (UserAlreadyExistsException e) {
            logger.error("Error creating new user.", e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            logger.error("Sign up error: unknown error");
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SecuredGetUserDTO> updateUser(@PathVariable long id, @Valid @RequestBody SecuredPostUserDTO securedPostUserDTO) {
        try {
            SecuredGetUserDTO securedGetUserDTO = userService.updateUser(id, securedPostUserDTO);
            logger.info("Successfully updated user with id {}", id);
            return ResponseEntity.ok(securedGetUserDTO);
        } catch (UserNotFoundException e) {
            logger.error("Update error: no user found.");
            return ResponseEntity.notFound().build();
        } catch (UserAlreadyExistsException e) {
            logger.error("Update user error: user already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (RuntimeException e){
            logger.error("Update user error: unknown error", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<JSONObject> deleteUser(@PathVariable long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(new JSONObject().put("message", "User successfully deleted."));
        } catch (UserNotFoundException e) {
            logger.info("User not found.");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.info("unknown error on deleting user");
            return ResponseEntity.internalServerError().build();
        }
    }
}
