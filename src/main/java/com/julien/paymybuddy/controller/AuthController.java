package com.julien.paymybuddy.controller;

import com.julien.paymybuddy.entity.UserEntity;
import com.julien.paymybuddy.pojo.LoginRequest;
import com.julien.paymybuddy.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(loginRequest.getEmail());
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                return ResponseEntity.ok().body(new JSONObject().put("message", "User successfully logged in"));
            } else {
                return ResponseEntity.status(401).body("Invalid username or password");
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok().body(new JSONObject().put("message", "Logout successful"));
    }

    @GetMapping("/session")
    public ResponseEntity<?> checkSession(HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (null != user) {
            return ResponseEntity.ok().body(new JSONObject().put("message", "Session active for user: " + user.getUsername()));
        } else {
            return ResponseEntity.status(401).body("No active session");
        }
    }
}
