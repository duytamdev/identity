package com.tamstudio.learning.dto.response;

import com.tamstudio.learning.entity.Permission;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
public class RoleResponse {
    String name;
    String description;
    Set<Permission> permissions;
}
