package com.tamstudio.learning.service;

import com.tamstudio.learning.dto.request.PermissionRequest;
import com.tamstudio.learning.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {

    PermissionResponse create(PermissionRequest request);

    List<PermissionResponse> getAll();

    void delete(String name);
}
