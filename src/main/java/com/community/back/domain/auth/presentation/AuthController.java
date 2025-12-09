package com.community.back.domain.auth.presentation;

import com.community.back.domain.auth.application.AuthService;
import com.community.back.domain.auth.presentation.dto.request.LoginRequest;
import com.community.back.domain.auth.presentation.dto.request.LogoutRequest;
import com.community.back.domain.auth.presentation.dto.request.SignupRequest;
import com.community.back.domain.auth.presentation.dto.response.LoginResponse;
import com.community.back.domain.auth.presentation.dto.response.LogoutResponse;
import com.community.back.domain.auth.presentation.dto.response.SignupResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = SignupResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        log.info("Signup request for email: {}", request.getEmail());
        SignupResponse response = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "로그인", description = "사용자 로그인을 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request for email: {}", request.getEmail());
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그아웃", description = "사용자 로그아웃을 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공",
                    content = @Content(schema = @Schema(implementation = LogoutResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@Valid @RequestBody LogoutRequest request) {
        log.info("Logout request");
        LogoutResponse response = authService.logout(request);
        return ResponseEntity.ok(response);
    }
}
