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
@Schema(description = "축구장 승인 응답")
public class ApproveFieldResponse {

    @Schema(description = "축구장 ID", example = "field-uuid-1")
    private String fieldId;

    @Schema(description = "승인 상태", example = "approved")
    private String status;

    @Schema(description = "메시지", example = "Field approved successfully")
    private String message;
}
