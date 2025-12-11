package com.community.back.domain.field.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "축구장 등록 요청")
public class CreateFieldRequest {

    @NotBlank(message = "축구장 이름은 필수입니다")
    @Schema(description = "축구장 이름", example = "서울 월드컵 경기장 풋살장")
    private String name;

    @NotBlank(message = "주소는 필수입니다")
    @Schema(description = "주소", example = "서울시 마포구 월드컵로 240")
    private String address;

    @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
    private String imageUrl;

    @NotBlank(message = "잔디 유형은 필수입니다")
    @Schema(description = "잔디 유형", example = "인조잔디", allowableValues = {"천연잔디", "인조잔디"})
    private String grassType;

    @NotBlank(message = "추천 축구화는 필수입니다")
    @Schema(description = "추천 축구화", example = "TF", allowableValues = {"FG", "AG", "TF"})
    private String recommendedShoe;
}
