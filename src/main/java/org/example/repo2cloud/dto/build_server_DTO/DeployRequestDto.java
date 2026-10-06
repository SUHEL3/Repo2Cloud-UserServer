package org.example.repo2cloud.dto.build_server_DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeployRequestDto {

    @NotNull(message = "deploymentId is required")
    private Long deploymentId;

    @NotNull(message = "ownerUserId is required")
    private Long ownerUserId;

    @NotBlank(message = "repositoryUrl is required")
    @Pattern(
            regexp = "^(https?://)(www\\.)?github\\.com/[\\w.-]+/[\\w.-]+(\\.git)?/?$",
            message = "repositoryUrl must be a valid GitHub HTTPS URL"
    )
    private String repositoryUrl;

    @Builder.Default
    private String branch = "main";
}
