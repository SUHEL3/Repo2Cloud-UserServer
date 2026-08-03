package org.example.repo2cloud.dto.requestDTO;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import org.example.repo2cloud.entity.models.Role;

@Getter
@Setter
public class RegisterUserRequest {
    private String name;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Float credits;
}
