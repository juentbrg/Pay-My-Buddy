package com.julien.paymybuddy.controller;

import com.julien.paymybuddy.dto.UserConnectionDTO;
import com.julien.paymybuddy.entity.UserEntity;
import com.julien.paymybuddy.exception.ConnectionAlreadyExistException;
import com.julien.paymybuddy.exception.UserNotFoundException;
import com.julien.paymybuddy.service.UserConnectionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/connection")
public class UserConnectionController {

    private final UserConnectionService userConnectionService;

    public UserConnectionController(UserConnectionService userConnectionService) {
        this.userConnectionService = userConnectionService;
    }

    @GetMapping("/get-connection")
    public ResponseEntity<List<UserConnectionDTO>> getCurrentUserConnections(HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("user");

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            List<UserConnectionDTO> connectionsForUser = userConnectionService.getConnectionsForUser(currentUser.getUserId());

            if (null != connectionsForUser) {
                return ResponseEntity.ok(connectionsForUser);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/get-connection-name")
    public ResponseEntity<List<String>> getCurrentUserConnectionUsername(HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("user");

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            List<String> connectionsNamesForUser = userConnectionService.getConnectionsNamesForUser(currentUser.getUserId());
            return ResponseEntity.ok(connectionsNamesForUser);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/add-connection")
    public ResponseEntity<UserConnectionDTO> addUserConnection(@RequestBody String userToConnectMail, HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("user");

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            UserConnectionDTO userConnectionDTO = userConnectionService.addConnection(currentUser.getUserId(), userToConnectMail);
            return ResponseEntity.ok(userConnectionDTO);
        } catch (ConnectionAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
