package com.community.back.domain.review.presentation.dto.response;

import com.community.back.domain.review.domain.Review;
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
@Schema(description = "리뷰 응답")
public class ReviewResponse {

    @Schema(description = "리뷰 ID", example = "review-uuid-1")
    private String reviewId;

    @Schema(description = "축구장 ID", example = "field-uuid-1")
    private String fieldId;

    @Schema(description = "작성자 ID", example = "user-uuid-1")
    private String userId;

    @Schema(description = "리뷰 내용", example = "잔디 상태가 좋고 시설이 깨끗합니다.")
    private String content;

    @Schema(description = "평점", example = "5")
    private Integer rating;

    @Schema(description = "작성일시")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시")
    private LocalDateTime updatedAt;

    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .fieldId(review.getFieldId())
                .userId(review.getUserId())
                .content(review.getContent())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
