package org.example.repo2cloud.dto.build_server_DTO;

import com.repo2cloud.buildserver.entity.LogLevel;
import com.repo2cloud.buildserver.entity.LogSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeploymentLogDto {

    private Long deploymentId;
    private LogLevel logLevel;
    private LogSource source;
    private String message;
    private LocalDateTime timestamp;
}
