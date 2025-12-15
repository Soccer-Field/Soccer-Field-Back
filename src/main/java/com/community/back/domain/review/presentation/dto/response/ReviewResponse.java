package com.community.back.domain.review.presentation.dto.response;

import com.community.back.domain.review.domain.Review;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "리뷰 응답")
public class ReviewResponse {

    @Schema(description = "리뷰 ID", example = "1")
    private Long reviewId;

    @Schema(description = "축구장 ID", example = "1")
    private Long fieldId;

    @Schema(description = "작성자 ID", example = "1")
    private Long userId;

    @Schema(description = "작성자 이름", example = "홍길동")
    private String userName;

    @Schema(description = "리뷰 내용", example = "잔디 상태가 좋고 시설이 깨끗합니다.")
    private String content;

    @Schema(description = "평점", example = "5")
    private Integer rating;

    @Schema(description = "잔디 타입", example = "AG")
    private String grassType;

    @Schema(description = "잔디 상태 특징", example = "[\"딱딱함\", \"잔디 짧음\"]")
    private List<String> grassConditions;

    @Schema(description = "추천 축구화 타입", example = "AG")
    private String recommendedShoe;

    @Schema(description = "추천 축구화 링크", example = "https://www.nike.com/shoes")
    private String shoeLink;

    @Schema(description = "작성일시")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시")
    private LocalDateTime updatedAt;

    public static ReviewResponse from(Review review) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> grassConditions = new ArrayList<>();

        if (review.getGrassConditions() != null && !review.getGrassConditions().isEmpty()) {
            try {
                grassConditions = objectMapper.readValue(
                    review.getGrassConditions(),
                    new TypeReference<List<String>>() {}
                );
            } catch (JsonProcessingException e) {
                // JSON 파싱 실패 시 빈 리스트 사용
            }
        }

        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .fieldId(review.getFieldId())
                .userId(review.getUserId())
                .userName(null) // userName은 Service에서 설정
                .content(review.getContent())
                .rating(review.getRating())
                .grassType(review.getGrassType())
                .grassConditions(grassConditions)
                .recommendedShoe(review.getRecommendedShoe())
                .shoeLink(review.getShoeLink())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    public static ReviewResponse from(Review review, String userName) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> grassConditions = new ArrayList<>();

        if (review.getGrassConditions() != null && !review.getGrassConditions().isEmpty()) {
            try {
                grassConditions = objectMapper.readValue(
                    review.getGrassConditions(),
                    new TypeReference<List<String>>() {}
                );
            } catch (JsonProcessingException e) {
                // JSON 파싱 실패 시 빈 리스트 사용
            }
        }

        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .fieldId(review.getFieldId())
                .userId(review.getUserId())
                .userName(userName)
                .content(review.getContent())
                .rating(review.getRating())
                .grassType(review.getGrassType())
                .grassConditions(grassConditions)
                .recommendedShoe(review.getRecommendedShoe())
                .shoeLink(review.getShoeLink())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
