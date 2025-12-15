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

    @Schema(description = "리뷰 ID", example = "1")
    private Long reviewId;

    @Schema(description = "작성자 ID", example = "1")
    private Long userId;

    @Schema(description = "작성자 이름", example = "홍길동")
    private String userName;

    @Schema(description = "댓글 내용", example = "좋은 리뷰 감사합니다!")
    private String content;

    @Schema(description = "작성일시")
    private LocalDateTime createdAt;

    @Schema(description = "부모 댓글 ID (대댓글인 경우)", example = "1", nullable = true)
    private Long parentId;

    public static CreateCommentResponse from(Comment comment) {
        return CreateCommentResponse.builder()
                .commentId(comment.getCommentId())
                .reviewId(comment.getReviewId())
                .userId(comment.getUserId())
                .userName(null) // userName은 Service에서 설정
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .parentId(comment.getParentId())
                .build();
    }

    public static CreateCommentResponse from(Comment comment, String userName) {
        return CreateCommentResponse.builder()
                .commentId(comment.getCommentId())
                .reviewId(comment.getReviewId())
                .userId(comment.getUserId())
                .userName(userName)
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .parentId(comment.getParentId())
                .build();
    }
}
