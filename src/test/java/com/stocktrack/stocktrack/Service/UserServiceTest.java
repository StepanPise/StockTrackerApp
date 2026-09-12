package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.DTO.Request.UserRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.UserResponseDTO;
import com.stocktrack.stocktrack.Entity.User;
import com.stocktrack.stocktrack.Exception.ResourceNotFoundException;
import com.stocktrack.stocktrack.Mapper.UserMapper;
import com.stocktrack.stocktrack.Repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("getUserById: Should return UserResponseDTO when user exists")
    void getUserById_UserExists_ReturnsUserResponseDTO() {
        // ARRANGE
        Long userId = 1L;

        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail("test@example.com");

        UserResponseDTO expectedDto = new UserResponseDTO();
        expectedDto.setId(userId);
        expectedDto.setEmail("test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userMapper.mapToResponseDTO(mockUser)).thenReturn(expectedDto);

        // ACT
        UserResponseDTO actualResult = userService.getUserById(userId);

        // ASSERT
        assertNotNull(actualResult);
        assertEquals(expectedDto.getId(), actualResult.getId());
        assertEquals(expectedDto.getEmail(), actualResult.getEmail());

        verify(userRepository, times(1)).findById(userId);
    }


    @Test
    @DisplayName("getUserById: Should throw ResourceNotFoundException when user does not exist")
    void getUserById_UserDoesNotExist_ThrowsResourceNotFoundException() {
        Long nonExistentId = 999999L;
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserById(nonExistentId)
        );

        assertEquals("User with ID " + nonExistentId + " does not exist.", exception.getMessage());

        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("updateUserById: Should throw IllegalArgumentException when email is already taken")
    void updateUserById_EmailAlreadyTaken_ThrowsIllegalArgumentException() {
        Long userId = 1L;

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("current@example.com");

        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setEmail("taken@example.com");
        requestDTO.setPassword("newPassword123");

        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setEmail("taken@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("taken@example.com")).thenReturn(Optional.of(anotherUser));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUserById(userId, requestDTO)
        );

        assertEquals("Email 'taken@example.com' is already taken.", exception.getMessage());

        verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("updateUserById: Should update and return UserResponseDTO when data is valid")
    void updateUserById_ValidData_ReturnsUpdatedUserResponseDTO() {
        Long userId = 1L;

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("current@example.com");

        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setEmail("new@example.com");
        requestDTO.setPassword("rawPassword");

        UserResponseDTO expectedDto = new UserResponseDTO();
        expectedDto.setId(userId);
        expectedDto.setEmail("new@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("rawPassword")).thenReturn("hashedPassword");
        when(userMapper.mapToResponseDTO(existingUser)).thenReturn(expectedDto);

        UserResponseDTO actualResult = userService.updateUserById(userId, requestDTO);

        assertNotNull(actualResult);
        assertEquals(expectedDto.getId(), actualResult.getId());
        assertEquals(expectedDto.getEmail(), actualResult.getEmail());
        assertEquals("hashedPassword", existingUser.getPasswordHash());

        verify(passwordEncoder, times(1)).encode("rawPassword");
        verify(userMapper, times(1)).mapToResponseDTO(existingUser);
    }

    @Test
    @DisplayName("deleteUserById: Should delete user when user exists")
    void deleteUserById_UserExists_DeletesUser() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        userService.deleteUserById(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(existingUser);
    }
}