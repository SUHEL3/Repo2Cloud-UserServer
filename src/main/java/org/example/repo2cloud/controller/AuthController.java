package org.example.repo2cloud.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.repo2cloud.dto.requestDTO.LoginRequest;
import org.example.repo2cloud.dto.requestDTO.RegisterUserRequest;
import org.example.repo2cloud.dto.responseDTO.LoginResponse;
import org.example.repo2cloud.service.AuthService;
import org.example.repo2cloud.wrapper.ApiResponse;
import org.example.repo2cloud.wrapper.ApiRoute;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiRoute.AUTH)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(
            @RequestBody RegisterUserRequest request
            ){
        return ResponseEntity.ok(
                new ApiResponse<>("User registeration response",
                        authService.register(request))
        );
    }

    @GetMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @RequestBody LoginRequest request
    ){
        return ResponseEntity.ok(
                new ApiResponse<>("Login successful",
                        authService.login(request))
        );
    }
}
