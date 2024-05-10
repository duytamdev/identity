package com.tamstudio.learning.service;

import com.tamstudio.learning.dto.request.UserCreateRequest;
import com.tamstudio.learning.dto.request.UserUpdateRequest;
import com.tamstudio.learning.dto.response.ApiResponse;
import com.tamstudio.learning.entity.User;

import java.util.List;

public interface UserService {

    ApiResponse<User> createRequest(UserCreateRequest request);

    List<User> getAllUsers();

    User getUserById(String id);

    ApiResponse<User> updateUser(String id, UserUpdateRequest request);

    ApiResponse<User> deleteUser(String id);

    ApiResponse<User> getCurrentUser();

}
