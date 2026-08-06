package org.example.repo2cloud.service;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.repo2cloud.dto.requestDTO.DeploymentRequest;
import org.example.repo2cloud.entity.Deployment;
import org.example.repo2cloud.entity.models.Status;
import org.example.repo2cloud.repository.DeploymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class DeploymentService {
    private final DeploymentRepository deploymentRepository;

    public Status requestDeployment(DeploymentRequest request){
        Deployment deployment = new Deployment();
        deployment.setUserId(request.getUserId());
        deployment.setUrl(request.getUrl());
        deployment.setStatus(request.getStatus());
        deployment.setRequestedAt(LocalDateTime.now());
        deploymentRepository.save(deployment);
        return deployment.getStatus();
    }

}
