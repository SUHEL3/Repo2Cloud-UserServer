package org.example.repo2cloud.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.repo2cloud.auth.JwtUtil;
import org.example.repo2cloud.dto.requestDTO.LoginRequest;
import org.example.repo2cloud.dto.requestDTO.RegisterUserRequest;
import org.example.repo2cloud.dto.responseDTO.LoginResponse;
import org.example.repo2cloud.entity.User;
import org.example.repo2cloud.mapper.UserMapper;
import org.example.repo2cloud.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    public String register(RegisterUserRequest request) {
        try {
            User user = mapper.toUser(request);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);
            return "User registered successfully";
        }catch (Exception e){
            throw new RuntimeException("Failed to register user:"+e.getMessage());
        }
    }

    public LoginResponse login(LoginRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails,user.getRole());
        return new LoginResponse(token,user.getEmail(),user.getRole().name());
    }
}
