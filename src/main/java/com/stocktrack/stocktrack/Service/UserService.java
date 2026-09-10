package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.DTO.Request.UserRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.UserResponseDTO;
import com.stocktrack.stocktrack.Exception.ResourceNotFoundException;
import com.stocktrack.stocktrack.Mapper.UserMapper;
import com.stocktrack.stocktrack.Entity.User;
import com.stocktrack.stocktrack.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    // ----------------- PRIVATE HELPER METHODS -----------------

    private User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " does not exist."));
    }

    private void validateEmailUniqueness(String newEmail, String currentEmail) {
        if (newEmail.equalsIgnoreCase(currentEmail)) {
            return;
        }
        if (userRepository.findByEmail(newEmail).isPresent()) {
            throw new IllegalArgumentException("Email '" + newEmail + "' is already taken.");
        }
    }


    // ----------------- USER METHODS -----------------
    public UserResponseDTO getCurrentUserProfile(User currentUser) {
        return userMapper.mapToResponseDTO(currentUser);
    }

    @Transactional
    public UserResponseDTO updateCurrentUserProfile(User currentUser, UserRequestDTO requestDTO) {
        User user = getUserEntityById(currentUser.getId());

        validateEmailUniqueness(requestDTO.getEmail(), user.getEmail());

        user.setEmail(requestDTO.getEmail());
        user.setPasswordHash(passwordEncoder.encode(requestDTO.getPassword()));
        return userMapper.mapToResponseDTO(user); // Hibernates DIRTY CHECKING
    }


    // ----------------- ADMIN METHODS -----------------

    @Transactional(readOnly = true) //optimalization
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::mapToResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        return userMapper.mapToResponseDTO(getUserEntityById(id));
    }

    @Transactional
    public UserResponseDTO updateUserById(Long id, UserRequestDTO requestDTO) {
        User existingUser = getUserEntityById(id);

        validateEmailUniqueness(requestDTO.getEmail(), existingUser.getEmail());

        existingUser.setEmail(requestDTO.getEmail());
        existingUser.setPasswordHash(passwordEncoder.encode(requestDTO.getPassword()));

        return userMapper.mapToResponseDTO(existingUser);
    }

    @Transactional
    public void deleteUserById(Long id) {
        User user = getUserEntityById(id);
        userRepository.delete(user);
    }
}