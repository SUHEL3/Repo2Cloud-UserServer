package org.example.repo2cloud.dto.build_server_DTO;

import com.repo2cloud.buildserver.entity.BuilderType;
import com.repo2cloud.buildserver.entity.DeploymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeploymentResponseDto {

    private Long deploymentId;
    private Long ownerUserId;
    private String repositoryUrl;
    private String branch;
    private String imageName;
    private String containerId;
    private String containerName;
    private Integer allocatedPort;
    private DeploymentStatus status;
    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Build metadata — populated after a successful build
    private BuilderType builderType;
    private String detectedLanguage;
    private String detectedFramework;
    private Instant buildStartedAt;
    private Instant buildCompletedAt;
    private Long buildDurationMs;
}
