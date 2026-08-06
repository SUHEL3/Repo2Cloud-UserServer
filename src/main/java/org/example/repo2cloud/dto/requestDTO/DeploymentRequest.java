package org.example.repo2cloud.dto.requestDTO;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.repo2cloud.entity.models.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeploymentRequest {
    private Long userId;
    private String url;
    @Enumerated(EnumType.STRING)
    private Status status;
}
