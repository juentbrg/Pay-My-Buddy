package com.julien.paymybuddy.controller;

import com.julien.paymybuddy.dto.UserConnectionDTO;
import com.julien.paymybuddy.entity.UserConnectionEntity;
import com.julien.paymybuddy.entity.UserEntity;
import com.julien.paymybuddy.exception.ConnectionAlreadyExistException;
import com.julien.paymybuddy.exception.UserNotFoundException;
import com.julien.paymybuddy.service.UserConnectionService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserConnectionControllerTest {

    private static AutoCloseable mocks;

    @InjectMocks
    private UserConnectionController userConnectionController;

    @Mock
    private UserConnectionService userConnectionService;

    @Mock
    private HttpSession session;

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
    public void getCurrentUserConnectionOkTest() {
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

        when(session.getAttribute("user")).thenReturn(userJohn);
        when(userConnectionService.getConnectionsForUser(userJohn.getUserId())).thenReturn(List.of(new UserConnectionDTO(userConnection)));

        ResponseEntity<List<UserConnectionDTO>> result = userConnectionController.getCurrentUserConnections(session);

        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void getCurrentUserConnectionUnauthorizedTest() {
        when(session.getAttribute("user")).thenReturn(null);

        ResponseEntity<List<UserConnectionDTO>> result = userConnectionController.getCurrentUserConnections(session);

        assertEquals(result.getStatusCode().value(), 401);
    }

    @Test
    public void getCurrentUserConnectionNotContentTest() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        when(session.getAttribute("user")).thenReturn(userJohn);
        when(userConnectionService.getConnectionsForUser(userJohn.getUserId())).thenReturn(null);

        ResponseEntity<List<UserConnectionDTO>> result = userConnectionController.getCurrentUserConnections(session);

        assertEquals(result.getStatusCode().value(), 204);
    }

    @Test
    public void getCurrentUserConnectionNotFoundTest() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        when(session.getAttribute("user")).thenReturn(userJohn);
        when(userConnectionService.getConnectionsForUser(userJohn.getUserId())).thenThrow(UserNotFoundException.class);

        ResponseEntity<List<UserConnectionDTO>> result = userConnectionController.getCurrentUserConnections(session);

        assertEquals(result.getStatusCode().value(), 404);
    }

    @Test
    public void getCurrentUserConnectionUnknownErrorTest() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        when(session.getAttribute("user")).thenReturn(userJohn);
        when(userConnectionService.getConnectionsForUser(userJohn.getUserId())).thenThrow(RuntimeException.class);

        ResponseEntity<List<UserConnectionDTO>> result = userConnectionController.getCurrentUserConnections(session);

        assertEquals(result.getStatusCode().value(), 500);
    }

    @Test
    public void getCurrentUserConnectionUsernameOkTest() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        List<String> contactUsernameList = new ArrayList<>();
        contactUsernameList.add("Jane");
        contactUsernameList.add("Pedro");

        when(session.getAttribute("user")).thenReturn(userJohn);
        when(userConnectionService.getConnectionsNamesForUser(userJohn.getUserId())).thenReturn(contactUsernameList);

        ResponseEntity<List<String>> result = userConnectionController.getCurrentUserConnectionUsername(session);

        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void getCurrentUserConnectionUsernameUnauthorizedTest() {
        when(session.getAttribute("user")).thenReturn(null);

        ResponseEntity<List<String>> result = userConnectionController.getCurrentUserConnectionUsername(session);

        assertEquals(result.getStatusCode().value(), 401);
    }

    @Test
    public void getCurrentUserConnectionUsernameUnknownErrorTest() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        when(session.getAttribute("user")).thenReturn(userJohn);
        when(userConnectionService.getConnectionsNamesForUser(userJohn.getUserId())).thenThrow(RuntimeException.class);

        ResponseEntity<List<String>> result = userConnectionController.getCurrentUserConnectionUsername(session);

        assertEquals(result.getStatusCode().value(), 500);
    }

    @Test
    public void addUserConnectionOkTest() {
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

        when(session.getAttribute("user")).thenReturn(userJohn);
        when(userConnectionService.addConnection(userJohn.getUserId(), userJane.getEmail())).thenReturn(new UserConnectionDTO(userConnection));

        ResponseEntity<UserConnectionDTO> result = userConnectionController.addUserConnection("jane.doe@mail.com", session);

        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void addUserConnectionUnauthorizedTest() {
        when(session.getAttribute("user")).thenReturn(null);

        ResponseEntity<UserConnectionDTO> result = userConnectionController.addUserConnection("jane.doe@mail.com", session);

        assertEquals(result.getStatusCode().value(), 401);
    }

    @Test
    public void addUserConnectionWithConnectionAlreadyExistTest() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        when(session.getAttribute("user")).thenReturn(userJohn);
        when(userConnectionService.addConnection(userJohn.getUserId(), "jane.doe@mail.com")).thenThrow(ConnectionAlreadyExistException.class);

        ResponseEntity<UserConnectionDTO> result = userConnectionController.addUserConnection("jane.doe@mail.com", session);

        assertEquals(result.getStatusCode().value(), 409);
    }

    @Test
    public void addUserConnectionUserNotFoundTest() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        when(session.getAttribute("user")).thenReturn(userJohn);
        when(userConnectionService.addConnection(userJohn.getUserId(), "jane.doe@mail.com")).thenThrow(UserNotFoundException.class);

        ResponseEntity<UserConnectionDTO> result = userConnectionController.addUserConnection("jane.doe@mail.com", session);

        assertEquals(result.getStatusCode().value(), 404);
    }

    @Test
    public void addUserConnectionUnknownErrorTest() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        when(session.getAttribute("user")).thenReturn(userJohn);
        when(userConnectionService.addConnection(userJohn.getUserId(), "jane.doe@mail.com")).thenThrow(RuntimeException.class);

        ResponseEntity<UserConnectionDTO> result = userConnectionController.addUserConnection("jane.doe@mail.com", session);

        assertEquals(result.getStatusCode().value(), 500);
    }
}
