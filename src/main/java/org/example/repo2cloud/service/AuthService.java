package org.example.repo2cloud.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.repo2cloud.dto.requestDTO.RegisterUserRequest;
import org.example.repo2cloud.entity.User;
import org.example.repo2cloud.mapper.UserMapper;
import org.example.repo2cloud.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper mapper;

    public String register(RegisterUserRequest request) {
        try {
            User user = mapper.toUser(request);
            userRepository.save(user);
            return "User registered successfully";
        }catch (Exception e){
            throw new RuntimeException("Failed to register user:"+e.getMessage());
        }
    }
}
