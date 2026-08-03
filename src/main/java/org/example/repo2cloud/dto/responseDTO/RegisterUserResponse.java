package org.example.repo2cloud.dto.responseDTO;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.example.repo2cloud.entity.models.Role;

public class RegisterUserResponse {
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Float credits;
}
