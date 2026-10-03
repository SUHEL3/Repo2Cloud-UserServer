package org.example.repo2cloud.mapper;

import org.example.repo2cloud.dto.requestDTO.RegisterUserRequest;
import org.example.repo2cloud.dto.requestDTO.UserProfileUpdateRequest;
import org.example.repo2cloud.dto.responseDTO.UserProfileResponse;
import org.example.repo2cloud.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import java.lang.annotation.Target;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegisterUserRequest request);
    UserProfileResponse toUserProfile(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserProfileUpdateRequest request, @MappingTarget User user);
}
