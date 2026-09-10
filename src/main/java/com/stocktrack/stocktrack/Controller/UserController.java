package com.stocktrack.stocktrack.Controller;

import com.stocktrack.stocktrack.DTO.Request.UserRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.UserResponseDTO;
import com.stocktrack.stocktrack.Entity.User;
import com.stocktrack.stocktrack.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // ----------------- USER METHODS -----------------
    @GetMapping("/me")
    public UserResponseDTO getMyProfile(@AuthenticationPrincipal User currentUser) {
        return userService.getCurrentUserProfile(currentUser);
    }

    @PutMapping("/me")
    public UserResponseDTO updateMyProfile(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody UserRequestDTO userRequestDTO) {
        return userService.updateCurrentUserProfile(currentUser, userRequestDTO);
    }

    // ----------------- ADMIN METHODS -----------------
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public UserResponseDTO addUser(@Valid @RequestBody UserRequestDTO userRequestDTO){
//        return userService.addUser(userRequestDTO);
//    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO updateUserById(@PathVariable Long id, @Valid @RequestBody UserRequestDTO userRequestDTO){
        return userService.updateUserById(id, userRequestDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long id){
        userService.deleteUserById(id);
    }

}
