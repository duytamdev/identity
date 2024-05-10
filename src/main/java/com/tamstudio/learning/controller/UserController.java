package com.tamstudio.learning.controller;

import com.tamstudio.learning.dto.request.UserCreateRequest;
import com.tamstudio.learning.dto.request.UserUpdateRequest;
import com.tamstudio.learning.dto.response.ApiResponse;
import com.tamstudio.learning.dto.response.UserResponse;
import com.tamstudio.learning.entity.User;
import com.tamstudio.learning.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ApiResponse<User> createUser(@RequestBody @Valid UserCreateRequest request) {
        return userService.createRequest(request);
    }

    @GetMapping
    public List<User> getAllUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getAuthorities().forEach(System.out::println);
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setDateOfBirth(user.getDateOfBirth());
         response.setRoles(user.getRoles());
        return response;
    }

    @GetMapping("/me")
    public ApiResponse<User> getMyInfo() {
        return userService.getCurrentUser();
    }

    @PutMapping("/{id}")
    public ApiResponse<User> updateUser(@PathVariable String id, @RequestBody @Valid UserUpdateRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<User> deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }
}
