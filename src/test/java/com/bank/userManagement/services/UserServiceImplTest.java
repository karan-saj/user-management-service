package com.bank.userManagement.services;

import com.bank.userManagement.dto.UserDTO;
import com.bank.userManagement.entity.UserEntity;
import com.bank.userManagement.repository.UserRepository;
import com.bank.userManagement.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ValidationUtil validation;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDTO userDTO;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDTO = new UserDTO(1L, "John", "Doe", "1990-01-01");
        userEntity = new UserEntity(1L, "John", "Doe", LocalDate.parse("1990-01-01"));
    }

    @Test
    void fetchUserDetails_success() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        UserDTO result = userService.fetchUserDetails(userId);

        assertNotNull(result);
        assertEquals(userDTO.getFirstName(), result.getFirstName());
        assertEquals(userDTO.getLastName(), result.getLastName());
        assertEquals(userDTO.getDateOfBirth(), result.getDateOfBirth());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void createUser_success() {
        doNothing().when(validation).validateUserDetails(userDTO);

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDTO result = userService.createUser(userDTO);

        assertNotNull(result);
        assertEquals(userDTO.getFirstName(), result.getFirstName());
        assertEquals(userDTO.getLastName(), result.getLastName());
        assertEquals(userDTO.getDateOfBirth(), result.getDateOfBirth());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void createUser_validationFailed() {
        doThrow(new RuntimeException("Validation failed")).when(validation).validateUserDetails(userDTO);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.createUser(userDTO));
        assertEquals("Internal error occurred while creating the user", exception.getMessage());
    }
}
