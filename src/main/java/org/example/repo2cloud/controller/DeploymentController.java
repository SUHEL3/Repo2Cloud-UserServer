package org.example.repo2cloud.controller;

import lombok.RequiredArgsConstructor;
import org.example.repo2cloud.dto.requestDTO.DeploymentRequest;
import org.example.repo2cloud.entity.models.Status;
import org.example.repo2cloud.service.DeploymentService;
import org.example.repo2cloud.wrapper.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deployments")
@RequiredArgsConstructor
public class DeploymentController {

    private final DeploymentService deploymentService;

    @PostMapping("/request")
    public ResponseEntity<ApiResponse<Status>> requestDeployment(
            @RequestBody DeploymentRequest request) {
        return ResponseEntity.ok(
                new ApiResponse<>("Deployment status",
                        deploymentService.requestDeployment(request))
        );
    }
}
