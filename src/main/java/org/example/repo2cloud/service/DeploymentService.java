package org.example.repo2cloud.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.repo2cloud.client.BuildServerClient;
import org.example.repo2cloud.dto.build_server_DTO.DeployRequestDto;
import org.example.repo2cloud.dto.build_server_DTO.DeploymentDto;
import org.example.repo2cloud.dto.build_server_DTO.DeploymentLogDto;
import org.example.repo2cloud.dto.build_server_DTO.DeploymentResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service orchestrating deployment operations between the User Server
 * and the Build Server via {@link BuildServerClient}.
 *
 * <p>Flow: Controller → DeploymentService → BuildServerClient → RestClient → Build Server</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeploymentService {

    private final BuildServerClient buildServerClient;

    public DeploymentResponseDto deploy(DeployRequestDto request) {
        log.info("Triggering deployment on Build Server for repository: {}, branch: {}, deploymentId: {}",
                request.getRepositoryUrl(), request.getBranch(), request.getDeploymentId());
        return buildServerClient.deploy(request);
    }

    public DeploymentResponseDto getDeployment(Long id) {
        log.info("Fetching deployment status for deploymentId: {}", id);
        return buildServerClient.getDeployment(id);
    }


    public List<DeploymentResponseDto> getAllDeployments() {
        log.info("Fetching all deployments from Build Server");
        return buildServerClient.getAllDeployments();
    }

    public List<DeploymentLogDto> getLogs(Long id) {
        log.info("Fetching logs for deploymentId: {}", id);
        return buildServerClient.getLogs(id);
    }

    public DeploymentResponseDto stop(Long id) {
        log.info("Requesting stop for deploymentId: {}", id);
        return buildServerClient.stop(id);
    }


    public void delete(Long id) {
        log.info("Requesting deletion of deploymentId: {}", id);
        buildServerClient.delete(id);
    }


    public DeploymentResponseDto start(Long id) {
        log.info("Requesting start for deploymentId: {}", id);
        return buildServerClient.start(id);
    }

    public List<DeploymentDto> getDeploymentsByUser(Long userId) {
        log.info("Fetching deployments for userId: {}", userId);
        return buildServerClient.getDeploymentsByUser(userId);
    }
}
