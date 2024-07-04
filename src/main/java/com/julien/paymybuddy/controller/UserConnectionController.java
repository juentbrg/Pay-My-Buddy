package com.julien.paymybuddy.controller;

import com.julien.paymybuddy.dto.UserConnectionDTO;
import com.julien.paymybuddy.entity.UserEntity;
import com.julien.paymybuddy.exception.ConnectionAlreadyExistException;
import com.julien.paymybuddy.exception.UserNotFoundException;
import com.julien.paymybuddy.service.UserConnectionService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(UserConnectionController.class);

    @GetMapping("/get-connection")
    public ResponseEntity<List<UserConnectionDTO>> getCurrentUserConnections(HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("user");

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            List<UserConnectionDTO> connectionsForUser = userConnectionService.getConnectionsForUser(currentUser.getUserId());

            if (null != connectionsForUser) {
                logger.debug("Connections for user {} found", currentUser.getUserId());
                return ResponseEntity.ok(connectionsForUser);
            } else {
                logger.debug("Connections for user {} found but user don't have connections", currentUser.getUserId());
                return ResponseEntity.noContent().build();
            }
        } catch (UserNotFoundException e) {
            logger.error("User not found", e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Unexpected error", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/get-connection-name")
    public ResponseEntity<List<String>> getCurrentUserConnectionUsername(HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("user");

        if (currentUser == null) {
            logger.error("User not found on session");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            List<String> connectionsNamesForUser = userConnectionService.getConnectionsNamesForUser(currentUser.getUserId());
            logger.debug("Connections usernames for user {} found", currentUser.getUserId());
            return ResponseEntity.ok(connectionsNamesForUser);
        } catch (Exception e) {
            logger.error("Unexpected error", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/add-connection")
    public ResponseEntity<UserConnectionDTO> addUserConnection(@RequestBody String userToConnectMail, HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("user");

        if (currentUser == null) {
            logger.error("User not found on session");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            UserConnectionDTO userConnectionDTO = userConnectionService.addConnection(currentUser.getUserId(), userToConnectMail);
            logger.debug("Connection {} added", currentUser.getUserId());
            return ResponseEntity.ok(userConnectionDTO);
        } catch (ConnectionAlreadyExistException e) {
            logger.error("Connection {} already exists", currentUser.getUserId());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }catch (UserNotFoundException e) {
            logger.error("User not found", e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Unexpected error", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
