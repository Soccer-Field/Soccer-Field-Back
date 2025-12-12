package com.community.back.domain.comment.presentation.dto.response;

import com.community.back.domain.comment.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "댓글 작성 응답")
public class CreateCommentResponse {

    @Schema(description = "댓글 ID", example = "1")
    private Long commentId;

    @Schema(description = "작성일시")
    private LocalDateTime createdAt;

    public static CreateCommentResponse from(Comment comment) {
        return CreateCommentResponse.builder()
                .commentId(comment.getCommentId())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
