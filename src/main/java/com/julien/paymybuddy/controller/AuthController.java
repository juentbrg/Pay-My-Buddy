package com.julien.paymybuddy.controller;

import com.julien.paymybuddy.entity.UserEntity;
import com.julien.paymybuddy.pojo.LoginRequest;
import com.julien.paymybuddy.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(loginRequest.getEmail());
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                logger.info("User " + user.getEmail() + " logged in. Session ID: " + session.getId());
                return ResponseEntity.ok().body(new JSONObject().put("message", "User successfully logged in"));
            } else {
                return ResponseEntity.status(401).body(new JSONObject().put("message", "Invalid password."));
            }
        } else {
            return ResponseEntity.status(401).body(new JSONObject().put("message", "Unrecognized email."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("user");
            logger.info("Invalidating session ID: " + session.getId());
            session.invalidate();
            return ResponseEntity.ok().body(new JSONObject().put("message", "Logout successful"));
        } else {
            logger.error("No session to invalidate.");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/session")
    public ResponseEntity<?> checkSession(HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (null != user) {
            logger.info("User " + user.getEmail() + " logged in. Session ID: " + session.getId());
            return ResponseEntity.ok().body(new JSONObject().put("message", "Session active for user: " + user.getUsername()));
        } else {
            logger.info("No user logged in.");
            return ResponseEntity.status(401).body("No active session");
        }
    }
}
