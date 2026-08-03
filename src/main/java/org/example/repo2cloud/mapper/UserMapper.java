package org.example.repo2cloud.mapper;

import org.example.repo2cloud.dto.requestDTO.RegisterUserRequest;
import org.example.repo2cloud.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegisterUserRequest request);
}
