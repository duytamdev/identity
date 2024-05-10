package com.tamstudio.learning.controller;

import com.tamstudio.learning.dto.request.PermissionRequest;
import com.tamstudio.learning.dto.response.ApiResponse;
import com.tamstudio.learning.dto.response.PermissionResponse;
import com.tamstudio.learning.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequestMapping("/permissions")
public class PermissionController {

    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
        PermissionResponse permissionResponse = permissionService.create(request);
        ApiResponse<PermissionResponse> response = new ApiResponse<>();
        response.setData(permissionResponse);
        response.setCode(200);
        response.setMessage("Create permission successfully");
        return response;
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAllPermission() {
        ApiResponse<List<PermissionResponse>> response = new ApiResponse<>();
        response.setData(permissionService.getAll());
        response.setCode(200);
        response.setMessage("Get all permission successfully");
        return response;
    }

    @DeleteMapping("/{name}")
    ApiResponse<String> deletePermission(@PathVariable String name) {
        permissionService.delete(name);
        ApiResponse<String> response = new ApiResponse<>();
        response.setData("Delete permission successfully");
        response.setCode(200);
        response.setMessage("Delete permission successfully");
        return response;
    }
}
