package com.tamstudio.learning.mapper;

import com.tamstudio.learning.dto.request.UserCreateRequest;
import com.tamstudio.learning.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreateRequest userCreateRequest);
}
