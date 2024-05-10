package com.tamstudio.learning.service;

import com.tamstudio.learning.dto.request.RoleRequest;
import com.tamstudio.learning.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {

    RoleResponse create(RoleRequest roleRequest);
    List<RoleResponse> getAll();
    RoleResponse getRoleById(String id);
    RoleResponse update(String id, RoleRequest roleRequest);
    void delete(String id);
}
