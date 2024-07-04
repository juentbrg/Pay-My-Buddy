package com.julien.paymybuddy.service;

import com.julien.paymybuddy.dto.SecuredGetUserDTO;
import com.julien.paymybuddy.dto.SecuredPostUserDTO;
import com.julien.paymybuddy.entity.UserEntity;
import com.julien.paymybuddy.exception.UserAlreadyExistsException;
import com.julien.paymybuddy.exception.UserNotFoundException;
import com.julien.paymybuddy.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<SecuredGetUserDTO> findAllUsers() {
        List<UserEntity> userList = userRepository.findAll();

        return userList.stream()
                .map(SecuredGetUserDTO::new)
                .toList();
    }
    
    @Transactional(readOnly = true)
    public SecuredGetUserDTO findUserById(Long id) {
        Optional<UserEntity> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            return new SecuredGetUserDTO(user);
        }
        throw new UserNotFoundException();
    }

    @Transactional
    public SecuredGetUserDTO createUser(SecuredPostUserDTO securedPostUserDTO) {
        Optional<UserEntity> emailExists = userRepository.findByEmail(securedPostUserDTO.getEmail());
        if (emailExists.isPresent()) {
            throw new UserAlreadyExistsException("Email already taken.");
        }

        Optional<UserEntity> usernameExists = userRepository.findByUsername(securedPostUserDTO.getUsername());
        if (usernameExists.isPresent()) {
            throw new UserAlreadyExistsException("Username already taken.");
        }

        UserEntity user = new UserEntity();
        String hashedPassword = passwordEncoder.encode(securedPostUserDTO.getPassword());
        user.setUsername(securedPostUserDTO.getUsername());
        user.setEmail(securedPostUserDTO.getEmail());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return new SecuredGetUserDTO(user);
    }

    @Transactional
    public SecuredGetUserDTO updateUser(long id, SecuredPostUserDTO securedPostUserDTO){
        Optional<UserEntity> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            if (StringUtils.isNotBlank(securedPostUserDTO.getUsername())) {
                Optional<UserEntity> usernameExists = userRepository.findByUsername(securedPostUserDTO.getUsername());
                if (usernameExists.isPresent()) {
                    throw new UserAlreadyExistsException("Username already taken.");
                }
                user.setUsername(securedPostUserDTO.getUsername());
            }
            if(StringUtils.isNotBlank(securedPostUserDTO.getEmail())) {
                Optional<UserEntity> emailExists = userRepository.findByEmail(securedPostUserDTO.getEmail());
                if (emailExists.isPresent()) {
                    throw new UserAlreadyExistsException("Email already taken.");
                }
                user.setEmail(securedPostUserDTO.getEmail());
            }
            if(StringUtils.isNotBlank(securedPostUserDTO.getPassword())) {
                String hashedPassword = passwordEncoder.encode(securedPostUserDTO.getPassword());
                user.setPassword(hashedPassword);
            }
            userRepository.save(user);
            return new SecuredGetUserDTO(user);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Transactional
    public boolean deleteUser(long id){
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        throw new UserNotFoundException();
    }
}
