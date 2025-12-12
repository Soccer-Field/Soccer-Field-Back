package com.community.back.domain.field.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "축구장 등록 응답")
public class CreateFieldResponse {

    @Schema(description = "생성된 축구장 ID", example = "1")
    private Long fieldId;

    @Schema(description = "승인 상태", example = "pending_approval")
    private String status;
}
