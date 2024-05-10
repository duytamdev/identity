package com.tamstudio.learning.controller;

import com.tamstudio.learning.dto.request.RoleRequest;
import com.tamstudio.learning.dto.response.ApiResponse;
import com.tamstudio.learning.dto.response.RoleResponse;
import com.tamstudio.learning.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class RoleController {

    RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request){
        RoleResponse roleResponse = roleService.create(request);
        ApiResponse<RoleResponse> response = new ApiResponse<>();
        response.setData(roleResponse);
        response.setCode(200);
        response.setMessage("Create role successfully");
        return response;
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRole(){
        ApiResponse<List<RoleResponse>> response = new ApiResponse<>();
        response.setData(roleService.getAll());
        response.setCode(200);
        response.setMessage("Get all role successfully");
        return response;
    }

    @DeleteMapping("/{name}")
    public ApiResponse<String> deleteRole(@PathVariable String name){
        roleService.delete(name);
        ApiResponse<String> response = new ApiResponse<>();
        response.setData("Delete role successfully");
        response.setCode(200);
        response.setMessage("Delete role successfully");
        return response;
    }
    @PutMapping
    public ApiResponse<RoleResponse> updateRole( @RequestBody RoleRequest request){
        RoleResponse roleResponse = roleService.update(request.getName(), request);
        ApiResponse<RoleResponse> response = new ApiResponse<>();
        response.setData(roleResponse);
        response.setCode(200);
        response.setMessage("Update role successfully");
        return response;
    }
}
