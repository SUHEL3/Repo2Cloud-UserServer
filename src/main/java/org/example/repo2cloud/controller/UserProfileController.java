package org.example.repo2cloud.controller;

import lombok.RequiredArgsConstructor;
import org.example.repo2cloud.dto.requestDTO.ChangePasswordRequest;
import org.example.repo2cloud.dto.requestDTO.UserProfileUpdateRequest;
import org.example.repo2cloud.entity.User;
import org.example.repo2cloud.wrapper.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.example.repo2cloud.auth.JwtUtil;
import org.example.repo2cloud.dto.responseDTO.UserProfileResponse;
import org.example.repo2cloud.service.UserProfileService;
import org.example.repo2cloud.wrapper.ApiRoute;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiRoute.USER)
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;
    private final JwtUtil jwtUtil;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile(Authentication authentication){
        String mailId = authentication.getName();
        return ResponseEntity.ok(
                new ApiResponse<>("Profile",
                        userProfileService.viewProfile(mailId))
        );
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateProfile(Authentication authentication,
                                             @RequestBody UserProfileUpdateRequest request){
        String mailId = authentication.getName();
        return ResponseEntity.ok(
                new ApiResponse<>("Profile updated successfully",
                        userProfileService.updateProfile(mailId,request))
        );
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<ApiResponse<String>> changePassword(Authentication authentication,
                                 @RequestBody ChangePasswordRequest request){
        String mail = authentication.getName();
        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Password changed successfully",
                        userProfileService.changePassword(mail,request)
                )
        );
    }
}
