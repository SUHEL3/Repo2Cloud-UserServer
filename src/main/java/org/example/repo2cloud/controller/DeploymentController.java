package org.example.repo2cloud.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.repo2cloud.dto.build_server_DTO.DeployRequestDto;
import org.example.repo2cloud.dto.build_server_DTO.DeploymentDto;
import org.example.repo2cloud.dto.build_server_DTO.DeploymentLogDto;
import org.example.repo2cloud.dto.build_server_DTO.DeploymentResponseDto;
import org.example.repo2cloud.service.DeploymentService;
import org.example.repo2cloud.wrapper.ApiResponse;
import org.example.repo2cloud.wrapper.ApiRoute;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller exposing deployment operations to the User Server clients.
 * Delegates all operations to {@link DeploymentService}, which in turn
 * talks to the Build Server via {@link org.example.repo2cloud.client.BuildServerClient}.
 */
@RestController
@RequestMapping(ApiRoute.DEPLOYMENT)
@RequiredArgsConstructor
public class DeploymentController {

    private final DeploymentService deploymentService;


    @PostMapping({"/deploy", "/request"})
    public ResponseEntity<ApiResponse<DeploymentResponseDto>> deploy(
            @Valid @RequestBody DeployRequestDto request
    ) {
        DeploymentResponseDto response = deploymentService.deploy(request);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new ApiResponse<>("Deployment accepted and started", response));
    }

    // Gets status and metadata for a specific deployment.
    @GetMapping({"/{id}", "/getstatus/{id}"})
    public ResponseEntity<ApiResponse<DeploymentResponseDto>> getDeployment(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>("Deployment status", deploymentService.getDeployment(id))
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DeploymentResponseDto>>> getAllDeployments() {
        return ResponseEntity.ok(
                new ApiResponse<>("All deployments", deploymentService.getAllDeployments())
        );
    }

   // Gets logs for a specific deployment.
    @GetMapping("/{id}/logs")
    public ResponseEntity<ApiResponse<List<DeploymentLogDto>>> getLogs(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>("Deployment logs", deploymentService.getLogs(id))
        );
    }

    // Stops a running deployment container.

    @PostMapping("/{id}/stop")
    public ResponseEntity<ApiResponse<DeploymentResponseDto>> stop(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>("Deployment stopped", deploymentService.stop(id))
        );
    }

    @DeleteMapping({"/{id}", "/delete/{id}"})
    public ResponseEntity<ApiResponse<String>> delete(
            @PathVariable("id") Long id
    ) {
        deploymentService.delete(id);
        return ResponseEntity.ok(
                new ApiResponse<>("Deployment deleted successfully", "Deleted deployment with ID: " + id)
        );
    }

    // Starts a previously stopped deployment.

    @PostMapping({"/{id}/start", "/restart/{id}"})
    public ResponseEntity<ApiResponse<DeploymentResponseDto>> start(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>("Deployment started", deploymentService.start(id))
        );
    }

    // Gets all deployments for a given user.
    @GetMapping({"/user/{userId}", "/get/{userId}"})
    public ResponseEntity<ApiResponse<List<DeploymentDto>>> getDeploymentsByUser(
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>("Deployments for user with ID: " + userId,
                        deploymentService.getDeploymentsByUser(userId))
        );
    }
}
