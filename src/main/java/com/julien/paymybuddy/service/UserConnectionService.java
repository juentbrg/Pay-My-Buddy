package com.julien.paymybuddy.service;

import com.julien.paymybuddy.dto.UserConnectionDTO;
import com.julien.paymybuddy.entity.UserConnectionEntity;
import com.julien.paymybuddy.entity.UserEntity;
import com.julien.paymybuddy.exception.ConnectionAlreadyExistException;
import com.julien.paymybuddy.exception.UserNotFoundException;
import com.julien.paymybuddy.repository.UserConnectionRepository;
import com.julien.paymybuddy.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserConnectionService {

    private final UserConnectionRepository userConnectionRepository;
    private final UserRepository userRepository;

    public UserConnectionService(UserConnectionRepository userConnectionRepository, UserRepository userRepository) {
        this.userConnectionRepository = userConnectionRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserConnectionDTO> getConnectionsForUser(long userId) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            UserEntity userEntity = userOpt.get();
            List<UserConnectionEntity> connections = userConnectionRepository.findByUser1WithUsers(userEntity);

            if (connections.isEmpty()) {
                return null;
            }

            return connections.stream()
                    .map(UserConnectionDTO::new)
                    .collect(Collectors.toList());
        } else {
            throw new UserNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public List<String> getConnectionsNamesForUser(long userId) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            UserEntity userEntity = userOpt.get();
            List<String> connectionUsernameList = userConnectionRepository.findUserNamesOfConnections(userEntity);

            if (connectionUsernameList.isEmpty()) {
                return null;
            }

            return connectionUsernameList;

        } else {
            throw new UserNotFoundException();
        }
    }

    @Transactional
    public UserConnectionDTO addConnection(long userId, String userToConnectEmail) {
        Optional<UserEntity> user1Opt = userRepository.findById(userId);
        Optional<UserEntity> user2Opt = userRepository.findByEmail(userToConnectEmail);

        if (user1Opt.isPresent() && user2Opt.isPresent()) {
            UserEntity user1 = user1Opt.get();
            UserEntity user2 = user2Opt.get();

            if (userConnectionRepository.findByUser1AndUser2(user1, user2).isPresent()) {
                throw new ConnectionAlreadyExistException();
            }

            UserConnectionEntity userConnectionEntity = new UserConnectionEntity();
            userConnectionEntity.setUser1(user1);
            userConnectionEntity.setUser2(user2);

            userConnectionRepository.save(userConnectionEntity);

            return new UserConnectionDTO(userConnectionEntity);
        } else {
            throw new UserNotFoundException();
        }
    }
}
