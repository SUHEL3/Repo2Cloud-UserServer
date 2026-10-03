package org.example.repo2cloud.dto.requestDTO;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.example.repo2cloud.entity.models.Role;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserProfileUpdateRequest {
    private String name = null;
    private String email = null;
    private String username = null;
}
