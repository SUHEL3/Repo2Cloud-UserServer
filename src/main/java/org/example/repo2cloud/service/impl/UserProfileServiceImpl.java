package org.example.repo2cloud.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.repo2cloud.dto.requestDTO.ChangePasswordRequest;
import org.example.repo2cloud.dto.requestDTO.UserProfileUpdateRequest;
import org.example.repo2cloud.dto.responseDTO.UserProfileResponse;
import org.example.repo2cloud.entity.User;
import org.example.repo2cloud.mapper.UserMapper;
import org.example.repo2cloud.repository.UserRepository;
import org.example.repo2cloud.service.UserProfileService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.midi.Patch;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserProfileResponse viewProfile(String mailId) {
        User user = userRepository.findByEmail(mailId).orElseThrow(
                ()-> new RuntimeException("User not found.")
        );
        return mapper.toUserProfile(user);
    }

    @Override
    public UserProfileResponse updateProfile(String mailId, UserProfileUpdateRequest request) {
        User user = userRepository.findByEmail(mailId).orElseThrow(
                ()-> new UsernameNotFoundException("User not found.")
        );
        mapper.updateUserFromDto(request, user);
        User savedUser = userRepository.save(user);
        return mapper.toUserProfile(savedUser);
    }

    @Override
    public String changePassword(String mailId, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(mailId).orElseThrow(
                ()-> new RuntimeException("User not found.")
        );
        if(passwordEncoder.matches(request.old_password(), user.getPassword())){
            user.setPassword(passwordEncoder.encode(request.new_password()));
        }else {
            throw new RuntimeException("Incorrect password.");
        }
        userRepository.save(user);
        return "Password changed successfully.";
    }
}
