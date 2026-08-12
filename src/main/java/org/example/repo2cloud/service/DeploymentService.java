package org.example.repo2cloud.service;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.repo2cloud.dto.requestDTO.DeploymentRequest;
import org.example.repo2cloud.entity.Deployment;
import org.example.repo2cloud.entity.models.Role;
import org.example.repo2cloud.entity.models.Status;
import org.example.repo2cloud.repository.DeploymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
        deployment.setStatus(Status.BUILDING);
        deployment.setRequestedAt(LocalDateTime.now());
        deploymentRepository.save(deployment);
        return deployment.getStatus();
    }

    public List<Deployment> getAllDeployment(Long userId){
            return deploymentRepository.getDeploymentsByUserId(userId).orElseThrow(
                    ()-> new RuntimeException("Deployment not found.")
            );
    }

    public String deleteDeployment(Long id){
        Deployment deployment = deploymentRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Deployment not found")
        );
        deploymentRepository.delete(deployment);
        return "Deployment deleted successfully.";
    }

    public Status restart(Long deploymentId){
        Deployment deployment = deploymentRepository.findById(deploymentId).orElseThrow(
                ()-> new RuntimeException("Deployment not found")
        );
        deployment.setStatus(Status.BUILDING);
        deploymentRepository.save(deployment);
        return deployment.getStatus();
    }

    public Status getDeploymentStatus(Long deploymentId){
        return deploymentRepository.getStatus(deploymentId).orElseThrow(
                ()-> new RuntimeException("Deployment not found")
        );
    }
}
