package com.community.back.domain.auth.application;

import com.community.back.domain.auth.domain.User;
import com.community.back.domain.auth.domain.repository.UserRepository;
import com.community.back.domain.auth.presentation.dto.request.LoginRequest;
import com.community.back.domain.auth.presentation.dto.request.LogoutRequest;
import com.community.back.domain.auth.presentation.dto.request.SignupRequest;
import com.community.back.domain.auth.presentation.dto.response.LoginResponse;
import com.community.back.domain.auth.presentation.dto.response.LogoutResponse;
import com.community.back.domain.auth.presentation.dto.response.SignupResponse;
import com.community.back.global.exception.CustomException;
import com.community.back.global.exception.ErrorCode;
import com.community.back.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // Create user
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .build();

        User savedUser = userRepository.save(user);

        // Generate token with role
        String token = jwtTokenProvider.createAccessToken(savedUser.getUserId(), savedUser.getEmail(), savedUser.getRole().name());

        return SignupResponse.builder()
                .userId(savedUser.getUserId())
                .token(token)
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .build();
    }

    public LoginResponse login(LoginRequest request) {
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // Generate token with role
        String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), user.getEmail(), user.getRole().name());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getName())
                .role(user.getRole().name())
                .build();
    }

    public LogoutResponse logout(LogoutRequest request) {
        // Validate token
        if (!jwtTokenProvider.validateToken(request.getToken())) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        // In a real application, you might want to:
        // 1. Add token to blacklist
        // 2. Store in Redis with expiration time
        // For now, we just return success

        return LogoutResponse.builder()
                .success(true)
                .build();
    }
}
