package org.example.repo2cloud.dto.responseDTO;

import org.example.repo2cloud.entity.models.Role;

public record UserProfileResponse(Long id,String username, String name, String email, Role role, Float credit) {
}