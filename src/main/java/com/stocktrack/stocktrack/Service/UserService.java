package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.Model.User;
import com.stocktrack.stocktrack.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " does not exist."));
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User updateUserById(Long id, User updatedUser) {
        User existingUser = getUserById(id);

        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setRole(updatedUser.getRole());
        //add update passwd later

        return userRepository.save(existingUser);
    }

    public void deleteUserById(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}