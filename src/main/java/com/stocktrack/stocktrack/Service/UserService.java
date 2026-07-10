package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.DTO.UserResponseDTO;
import com.stocktrack.stocktrack.Mapper.UserMapper;
import com.stocktrack.stocktrack.Model.User;
import com.stocktrack.stocktrack.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " does not exist."));
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(Long id) {
        return UserMapper.mapToResponseDTO(getUserEntityById(id));
    }

    public UserResponseDTO addUser(User user) {
        return UserMapper.mapToResponseDTO(userRepository.save(user));
    }

    public UserResponseDTO updateUserById(Long id, User updatedUser) {
        User existingUser = getUserEntityById(id);

        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setRole(updatedUser.getRole());
        //add update passwd later

        return UserMapper.mapToResponseDTO(userRepository.save(existingUser));
    }

    public void deleteUserById(Long id) {
        User user = getUserEntityById(id);
        userRepository.delete(user);
    }
}