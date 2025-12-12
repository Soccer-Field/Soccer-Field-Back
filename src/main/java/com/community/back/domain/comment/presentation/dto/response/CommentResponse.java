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
@Schema(description = "댓글 응답")
public class CommentResponse {

    @Schema(description = "댓글 ID", example = "1")
    private Long commentId;

    @Schema(description = "리뷰 ID", example = "1")
    private Long reviewId;

    @Schema(description = "작성자 ID", example = "1")
    private Long userId;

    @Schema(description = "부모 댓글 ID (대댓글인 경우)", example = "1", nullable = true)
    private Long parentId;

    @Schema(description = "댓글 내용", example = "좋은 리뷰 감사합니다!")
    private String content;

    @Schema(description = "작성일시")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시")
    private LocalDateTime updatedAt;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .reviewId(comment.getReviewId())
                .userId(comment.getUserId())
                .parentId(comment.getParentId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
