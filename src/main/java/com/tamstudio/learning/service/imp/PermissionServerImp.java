package com.tamstudio.learning.service.imp;

import com.tamstudio.learning.dto.request.PermissionRequest;
import com.tamstudio.learning.dto.response.PermissionResponse;
import com.tamstudio.learning.entity.Permission;
import com.tamstudio.learning.repository.PermissionRepository;
import com.tamstudio.learning.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServerImp implements PermissionService {

    PermissionRepository permissionRepository;

    public PermissionServerImp(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public PermissionResponse create(PermissionRequest request) {
        Permission permission = new Permission();
        permission.setName(request.getName());
        permission.setDescription(request.getDescription());
        permissionRepository.save(permission);
        PermissionResponse permissionResponse = new PermissionResponse();
        permissionResponse.setName(permission.getName());
        permissionResponse.setDescription(permission.getDescription());
        return permissionResponse;
    }

    @Override
    public List<PermissionResponse> getAll() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream().map(permission -> {
            PermissionResponse permissionResponse = new PermissionResponse();
            permissionResponse.setName(permission.getName());
            permissionResponse.setDescription(permission.getDescription());
            return permissionResponse;
        }).toList();
    }

    @Override
    public void delete(String name) {
        permissionRepository.deleteById(name);
    }
}
