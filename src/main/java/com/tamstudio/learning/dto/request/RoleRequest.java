package com.tamstudio.learning.dto.request;

import com.tamstudio.learning.entity.Permission;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
public class RoleRequest {
    String name;
    String description;
    Set<String> permissions;
}
