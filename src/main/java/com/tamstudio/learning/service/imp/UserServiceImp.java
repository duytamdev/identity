package com.tamstudio.learning.service.imp;

import com.tamstudio.learning.dto.request.UserCreateRequest;
import com.tamstudio.learning.dto.request.UserUpdateRequest;
import com.tamstudio.learning.dto.response.ApiResponse;
import com.tamstudio.learning.entity.Role;
import com.tamstudio.learning.entity.User;
import com.tamstudio.learning.exception.AppException;
import com.tamstudio.learning.exception.ErrorCode;
import com.tamstudio.learning.repository.RoleRepository;
import com.tamstudio.learning.repository.UserRepository;
import com.tamstudio.learning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @Override
    public ApiResponse<User> createRequest(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXITS);
        }


        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setDateOfBirth(request.getDateOfBirth());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findById("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        roles.add(userRole);
          newUser.setRoles(roles);
        ApiResponse<User> response = new ApiResponse<>();
        response.setMessage("Created user success");
        response.setData(userRepository.save(newUser));
        return response;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
      var auth =  SecurityContextHolder.getContext().getAuthentication();
      auth.getAuthorities().forEach(System.out::println);
        return userRepository.findAll();
    }

    @Override
    @PostAuthorize("returnObject.username == authentication.name || hasRole('ADMIN')")
    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public ApiResponse<User> updateUser(String id, UserUpdateRequest request) {
        User user = getUserById(id);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDateOfBirth(request.getDateOfBirth());

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        ApiResponse<User> response = new ApiResponse<>();
        response.setMessage("Updated user success");
        response.setData(userRepository.save(user));
        response.setCode(200);
        return response;
    }

    @Override
    public ApiResponse<User> deleteUser(String id) {
        userRepository.deleteById(id);
        ApiResponse<User> response = new ApiResponse<>();
        response.setMessage("Deleted user success");
        response.setCode(200);
        return response;
    }

    @Override
    public ApiResponse<User> getCurrentUser() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(name);
        if (user.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        ApiResponse<User> response = new ApiResponse<>();
        response.setMessage("Get current user success");
        response.setData(user.get());
        return response;
    }

}
