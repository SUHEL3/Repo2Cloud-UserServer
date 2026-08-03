package org.example.repo2cloud.controller;

import lombok.RequiredArgsConstructor;
import org.example.repo2cloud.dto.requestDTO.RegisterUserRequest;
import org.example.repo2cloud.service.AuthService;
import org.example.repo2cloud.wrapper.ApiResponse;
import org.example.repo2cloud.wrapper.ApiRoute;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoute.AUTH)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(
            @RequestBody RegisterUserRequest request
            ){
        return ResponseEntity.ok(
                new ApiResponse<>("User registeration response",
                        userService.register(request))
        );
    }

}
