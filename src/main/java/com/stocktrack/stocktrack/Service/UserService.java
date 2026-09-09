package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.DTO.Request.UserRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.UserResponseDTO;
import com.stocktrack.stocktrack.Exception.ResourceNotFoundException;
import com.stocktrack.stocktrack.Mapper.UserMapper;
import com.stocktrack.stocktrack.Entity.User;
import com.stocktrack.stocktrack.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    //With this private method I can throw exceptions only in one place
    private User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " does not exist."));
    }

    @Transactional(readOnly = true) //optimalization
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        return userMapper.mapToResponseDTO(getUserEntityById(id));
    }

    @Transactional
    public UserResponseDTO addUser(UserRequestDTO userRequestDTO) {
        User user = userMapper.mapToEntity(userRequestDTO);
        return userMapper.mapToResponseDTO(userRepository.save(user));
    }

    @Transactional
    public UserResponseDTO updateUserById(Long id, UserRequestDTO requestDTO) {
        User existingUser = getUserEntityById(id);

        existingUser.setEmail(requestDTO.getEmail());
        existingUser.setPasswordHash(requestDTO.getPassword());// passwd to hash?

        return userMapper.mapToResponseDTO(userRepository.save(existingUser));
    }

    @Transactional
    public void deleteUserById(Long id) {
        User user = getUserEntityById(id);
        userRepository.delete(user);
    }
}