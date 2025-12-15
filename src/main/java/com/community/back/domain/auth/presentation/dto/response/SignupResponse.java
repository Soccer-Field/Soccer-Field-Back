package com.community.back.domain.auth.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SignupResponse {
    private Long userId;
    private String token;
    private String name;
    private String email;
    private String role;
}
