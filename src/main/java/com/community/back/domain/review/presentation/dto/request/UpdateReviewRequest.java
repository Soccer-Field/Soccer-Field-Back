package com.community.back.domain.review.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "리뷰 수정 요청")
public class UpdateReviewRequest {

    @NotBlank(message = "리뷰 내용은 필수입니다")
    @Schema(description = "리뷰 내용", example = "수정된 리뷰 내용입니다.")
    private String content;

    @NotNull(message = "평점은 필수입니다")
    @Min(value = 1, message = "평점은 1 이상이어야 합니다")
    @Max(value = 5, message = "평점은 5 이하여야 합니다")
    @Schema(description = "평점 (1-5)", example = "4")
    private Integer rating;

    @NotBlank(message = "잔디 타입은 필수입니다")
    @Schema(description = "잔디 타입", example = "FG", allowableValues = {"AG", "FG", "MG", "TF"})
    private String grassType;

    @Schema(description = "잔디 상태 특징 (복수 선택 가능)", example = "[\"부드러움\", \"관리 양호\"]")
    private List<String> grassConditions;

    @NotBlank(message = "추천 축구화는 필수입니다")
    @Schema(description = "추천 축구화 타입", example = "FG", allowableValues = {"AG", "FG", "MG", "TF"})
    private String recommendedShoe;

    @Schema(description = "추천 축구화 링크 (선택)", example = "https://www.adidas.com/shoes")
    private String shoeLink;
}
