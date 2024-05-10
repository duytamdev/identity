package com.tamstudio.learning.service.imp;

import com.tamstudio.learning.dto.request.RoleRequest;
import com.tamstudio.learning.dto.response.RoleResponse;
import com.tamstudio.learning.entity.Role;
import com.tamstudio.learning.exception.AppException;
import com.tamstudio.learning.exception.ErrorCode;
import com.tamstudio.learning.repository.PermissionRepository;
import com.tamstudio.learning.repository.RoleRepository;
import com.tamstudio.learning.service.RoleService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class RoleServiceImp implements RoleService {

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final PermissionRepository permissionRepository;

    public RoleServiceImp(RoleRepository roleRepository,PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }


    @Override
    public RoleResponse create(RoleRequest roleRequest) {
        Role role = new Role();
        return getRoleResponse(roleRequest, role);
    }

    @Override
    public List<RoleResponse> getAll() {
       List<Role> roles = roleRepository.findAll();
       return roles.stream().map(role -> {
           RoleResponse roleResponse = new RoleResponse();
           roleResponse.setName(role.getName());
           roleResponse.setDescription(role.getDescription());
           roleResponse.setPermissions(role.getPermissions());
           return roleResponse;
       }).toList();
    }

    @Override
    public RoleResponse getRoleById(String id) {
        Role role = roleRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setName(role.getName());
        roleResponse.setDescription(role.getDescription());
        roleResponse.setPermissions(role.getPermissions());
        return roleResponse;
    }

    @Override
    public RoleResponse update(String id, RoleRequest roleRequest) {
        Role role = roleRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        return getRoleResponse(roleRequest, role);
    }

    @NotNull
    private RoleResponse getRoleResponse(RoleRequest roleRequest, Role role) {
        System.out.println(roleRequest.toString());
        var permissions = permissionRepository.findAllById(roleRequest.getPermissions());

        role.setName(roleRequest.getName());
        role.setDescription(roleRequest.getDescription());
        role.setPermissions(new HashSet<>(permissions));
        roleRepository.save(role);

        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setName(role.getName());
        roleResponse.setDescription(role.getDescription());
        roleResponse.setPermissions(role.getPermissions());
        return roleResponse;
    }

    @Override
    public void delete(String id) {
        Role role = roleRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        roleRepository.delete(role);
    }
}
