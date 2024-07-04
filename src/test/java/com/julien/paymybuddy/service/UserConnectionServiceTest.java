package com.julien.paymybuddy.service;

import com.julien.paymybuddy.dto.UserConnectionDTO;
import com.julien.paymybuddy.entity.UserConnectionEntity;
import com.julien.paymybuddy.entity.UserEntity;
import com.julien.paymybuddy.exception.ConnectionAlreadyExistException;
import com.julien.paymybuddy.exception.UserNotFoundException;
import com.julien.paymybuddy.repository.UserConnectionRepository;
import com.julien.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserConnectionServiceTest {

    private static AutoCloseable mocks;

    @InjectMocks
    private UserConnectionService userConnectionService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserConnectionRepository userConnectionRepository;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterAll
    public static void tearDown() throws Exception {
        if (null != mocks) {
            mocks.close();
        }
    }

    @Test
    public void getConnectionsForUserOkTest() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        UserEntity userJane = new UserEntity();
        userJane.setUserId(2);
        userJane.setUsername("Jane");
        userJane.setPassword("password");
        userJane.setEmail("jane.doe@mail.com");

        UserConnectionEntity userConnection = new UserConnectionEntity();
        userConnection.setConnectionId(1);
        userConnection.setUser1(userJohn);
        userConnection.setUser2(userJane);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userJohn));
        when(userConnectionRepository.findByUser1WithUsers(userJohn)).thenReturn(List.of(userConnection));

        List<UserConnectionDTO> result = userConnectionService.getConnectionsForUser(1);

        UserConnectionDTO expectedDTO = new UserConnectionDTO(userConnection);

        assertEquals(1, result.size());
        assertEquals(expectedDTO, result.getFirst());
    }

    @Test
    public void getConnectionsForUserNotFoundUserTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(UserNotFoundException.class, () -> {
            userConnectionService.getConnectionsForUser(1);
        });

        verify(userConnectionRepository, never()).findByUser1WithUsers(any());
    }

    @Test
    public void getConnectionsForUserNotFoundUserConnectionTest() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(userJohn));
        when(userConnectionRepository.findByUser1WithUsers(userJohn)).thenReturn(Collections.emptyList());

        List<UserConnectionDTO> result = userConnectionService.getConnectionsForUser(1);

        assertNull(result);
    }

    @Test
    public void getConnectionsNamesForUsersOkTest() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        List<String> connectionsNamesList = List.of("Jane", "Josh");

        when(userRepository.findById(1L)).thenReturn(Optional.of(userJohn));
        when(userConnectionRepository.findUserNamesOfConnections(userJohn)).thenReturn(connectionsNamesList);

        List<String> result = userConnectionService.getConnectionsNamesForUser(1);

        assertEquals(connectionsNamesList, result);
    }

    @Test
    public void getConnectionsNamesForUsersNotFoundUserTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userConnectionService.getConnectionsNamesForUser(1);
        });

        verify(userConnectionRepository, never()).findUserNamesOfConnections(any());
    }

    @Test
    public void getConnectionsNamesForUsersNotConnectionsNamesTest() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(userJohn));
        when(userConnectionRepository.findUserNamesOfConnections(userJohn)).thenReturn(Collections.emptyList());

        List<String> result = userConnectionService.getConnectionsNamesForUser(1);

        assertNull(result);
    }

    @Test
    public void addConnectionOkTest() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        UserEntity userJane = new UserEntity();
        userJane.setUserId(2);
        userJane.setUsername("Jane");
        userJane.setPassword("password");
        userJane.setEmail("jane.doe@mail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(userJohn));
        when(userRepository.findByEmail("jane.doe@mail.com")).thenReturn(Optional.of(userJane));

        UserConnectionDTO userConnectionDTO = userConnectionService.addConnection(1L, "jane.doe@mail.com");

        assertNotNull(userConnectionDTO);
        assertEquals(userConnectionDTO.getUser1().getUsername(), userJohn.getUsername());
    }

    @Test
    public void addConnectionNotFoundUser1Test() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userConnectionService.addConnection(1L, "jane.doe@mail.com");
        });
    }

    @Test
    public void addConnectionNotFoundUser2Test() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(userJohn));
        when(userRepository.findByEmail("jane.doe@mail.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userConnectionService.addConnection(1L, "jane.doe@mail.com");
        });
    }

    @Test
    public void addConnectionWithConnectionAlreadyExistsTest() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        UserEntity userJane = new UserEntity();
        userJane.setUserId(2);
        userJane.setUsername("Jane");
        userJane.setPassword("password");
        userJane.setEmail("jane.doe@mail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(userJohn));
        when(userRepository.findByEmail("jane.doe@mail.com")).thenReturn(Optional.of(userJane));
        when(userConnectionRepository.findByUser1AndUser2(userJohn, userJane)).thenReturn(Optional.of(new UserConnectionEntity()));

        assertThrows(ConnectionAlreadyExistException.class, () -> {
            userConnectionService.addConnection(1L, "jane.doe@mail.com");
        });
    }
}
