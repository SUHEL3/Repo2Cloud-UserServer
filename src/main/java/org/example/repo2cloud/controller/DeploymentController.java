package org.example.repo2cloud.controller;

import lombok.RequiredArgsConstructor;
import org.example.repo2cloud.dto.requestDTO.DeploymentRequest;
import org.example.repo2cloud.entity.Deployment;
import org.example.repo2cloud.entity.models.Status;
import org.example.repo2cloud.service.DeploymentService;
import org.example.repo2cloud.wrapper.ApiResponse;
import org.example.repo2cloud.wrapper.ApiRoute;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiRoute.DEPLOYMENT)
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

    @GetMapping("/get/{userId}")
    public ResponseEntity<ApiResponse<List<Deployment>>> getDeployments(
            @PathVariable Long userId
    ){
        return ResponseEntity.ok(
                new ApiResponse<>("Deployments for user with Id:"+userId,
                        deploymentService.getAllDeployment(userId))
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> delete(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(
                new ApiResponse<>("Deployment delete response",
                        deploymentService.deleteDeployment(id))
        );
    }
 }
