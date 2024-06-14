package com.julien.paymybuddy.service;

import com.julien.paymybuddy.dto.SecuredGetUserDTO;
import com.julien.paymybuddy.dto.SecuredPostUserDTO;
import com.julien.paymybuddy.entity.UserEntity;
import com.julien.paymybuddy.exception.UserAlreadyExistsException;
import com.julien.paymybuddy.exception.UserNotFoundException;
import com.julien.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private static AutoCloseable mocks;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

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
    public void findAllUsersOkTest() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1);
        userEntity.setUsername("username");
        userEntity.setEmail("john.doe@gmail.com");
        userEntity.setPassword("password");
        List<UserEntity> userList = List.of(userEntity);

        when(userRepository.findAll()).thenReturn(userList);

        List<SecuredGetUserDTO> result = userService.findAllUsers();

        assertNotNull(result);
        assertEquals(result.getFirst().getUsername(), userEntity.getUsername());
    }

    @Test
    public void findAllUsersReturnEmptyListTest() {
        List<UserEntity> userList = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(userList);

        List<SecuredGetUserDTO> result = userService.findAllUsers();

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void findUserByIdOkTest() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1);
        userEntity.setUsername("username");
        userEntity.setEmail("john.doe@gmail.com");
        userEntity.setPassword("password");

        when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));

        SecuredGetUserDTO result = userService.findUserById(1L);

        assertNotNull(result);
        assertEquals(result.getUsername(), userEntity.getUsername());
    }

    @Test
    public void findUserByIdNotFoundUserTest() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            SecuredGetUserDTO result = userService.findUserById(1L);
            assertNull(result);
        });

    }

    @Test
    public void createUserOkTest() {
        SecuredPostUserDTO securedPostUserDTO = new SecuredPostUserDTO();
        securedPostUserDTO.setUsername("username");
        securedPostUserDTO.setEmail("john.doe@gmail.com");
        securedPostUserDTO.setPassword("password");

        SecuredGetUserDTO user = userService.createUser(securedPostUserDTO);

        assertNotNull(user);
        assertEquals(user.getUsername(), securedPostUserDTO.getUsername());
    }

    @Test
    public void createUserEmailAlreadyTakenTest() {
        SecuredPostUserDTO securedPostUserDTO = new SecuredPostUserDTO();
        securedPostUserDTO.setUsername("username");
        securedPostUserDTO.setEmail("john.doe@gmail.com");
        securedPostUserDTO.setPassword("password");

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1);
        userEntity.setUsername("test");
        userEntity.setEmail("john.doe@gmail.com");
        userEntity.setPassword("password");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));

        assertThrows(UserAlreadyExistsException.class, () -> {
            SecuredGetUserDTO user = userService.createUser(securedPostUserDTO);
            assertNull(user);
        });
    }

    @Test
    public void createUserUsernameAlreadyTakenTest() {
        SecuredPostUserDTO securedPostUserDTO = new SecuredPostUserDTO();
        securedPostUserDTO.setUsername("username");
        securedPostUserDTO.setEmail("john.doe@gmail.com");
        securedPostUserDTO.setPassword("password");

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1);
        userEntity.setUsername("username");
        userEntity.setEmail("test@gmail.com");
        userEntity.setPassword("password");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));

        assertThrows(UserAlreadyExistsException.class, () -> {
            SecuredGetUserDTO user = userService.createUser(securedPostUserDTO);
            assertNull(user);
        });
    }

    @Test
    public void updateUserOkTest() {
        SecuredPostUserDTO securedPostUserDTO = new SecuredPostUserDTO();
        securedPostUserDTO.setEmail("john.doe@gmail.com");

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1);
        userEntity.setUsername("username");
        userEntity.setEmail("test@gmail.com");
        userEntity.setPassword("password");

        when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));

        SecuredGetUserDTO result = userService.updateUser(1, securedPostUserDTO);
        assertNotNull(userEntity);
        assertEquals(result.getEmail(), securedPostUserDTO.getEmail());
    }

    @Test
    public void updateUserEmailAlreadyTakenTest() {
        SecuredPostUserDTO securedPostUserDTO = new SecuredPostUserDTO();
        securedPostUserDTO.setEmail("john.doe@gmail.com");

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setUserId(1);
        userEntity1.setUsername("username");
        userEntity1.setEmail("test@gmail.com");
        userEntity1.setPassword("password");

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setUserId(2);
        userEntity2.setUsername("testname");
        userEntity2.setEmail("john.doe@gmail.com");
        userEntity2.setPassword("password");

        when(userRepository.findById(any())).thenReturn(Optional.of(userEntity1));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity2));

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.updateUser(1, securedPostUserDTO);
        });
    }

    @Test
    public void updateUserUsernameAlreadyTakenTest() {
        SecuredPostUserDTO securedPostUserDTO = new SecuredPostUserDTO();
        securedPostUserDTO.setUsername("testName");

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setUserId(1);
        userEntity1.setUsername("username");
        userEntity1.setEmail("test@gmail.com");
        userEntity1.setPassword("password");

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setUserId(2);
        userEntity2.setUsername("testname");
        userEntity2.setEmail("john.doe@gmail.com");
        userEntity2.setPassword("password");

        when(userRepository.findById(any())).thenReturn(Optional.of(userEntity1));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity2));

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.updateUser(1, securedPostUserDTO);
        });
    }

    @Test
    public void updateUserWithUserNotFoundTest() {
        SecuredPostUserDTO securedPostUserDTO = new SecuredPostUserDTO();
        securedPostUserDTO.setEmail("john.doe@gmail.com");

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(1, securedPostUserDTO);
        });
    }

    @Test
    public void deleteUserOkTest() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1);
        userEntity.setUsername("username");
        userEntity.setEmail("test@gmail.com");
        userEntity.setPassword("password");

        when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));

        boolean result = userService.deleteUser(1);

        assertTrue(result);
    }

    @Test
    public void deleteUserWithUserNotFoundTest() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(1);
        });
    }
}
