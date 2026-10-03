package org.example.repo2cloud.service;

import org.example.repo2cloud.dto.requestDTO.ChangePasswordRequest;
import org.example.repo2cloud.dto.requestDTO.UserProfileUpdateRequest;
import org.example.repo2cloud.dto.responseDTO.UserProfileResponse;

public interface UserProfileService {

    UserProfileResponse viewProfile(String mailId);

    UserProfileResponse updateProfile(String mailId, UserProfileUpdateRequest request);

    String changePassword(String mailId, ChangePasswordRequest request);
}
