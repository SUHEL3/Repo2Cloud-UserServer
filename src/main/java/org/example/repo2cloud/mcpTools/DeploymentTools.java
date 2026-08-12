package org.example.repo2cloud.mcpTools;

import lombok.RequiredArgsConstructor;
import org.example.repo2cloud.entity.Deployment;
import org.example.repo2cloud.entity.models.Status;
import org.example.repo2cloud.service.DeploymentService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeploymentTools {

    private final DeploymentService deploymentService;

    @Tool(description = "Get deployment status using deployment id")
    public Status getDeploymentStatus(Long deploymentId){
        return deploymentService.getDeploymentStatus(deploymentId);
    }

    @Tool(description = "Get all deployments data of user using user id")
    public List<Deployment> getUsersAllDeployments(Long userId){
        return deploymentService.getAllDeployment(userId);
    }
}
