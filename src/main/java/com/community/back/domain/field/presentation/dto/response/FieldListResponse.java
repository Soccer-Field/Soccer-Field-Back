package com.community.back.domain.field.presentation.dto.response;

import com.community.back.domain.field.domain.Field;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "축구장 목록 항목")
public class FieldListResponse {

    @Schema(description = "축구장 ID", example = "field-uuid-1")
    private String id;

    @Schema(description = "축구장 이름", example = "서울 월드컵 경기장 풋살장")
    private String name;

    @Schema(description = "주소", example = "서울시 마포구 월드컵로 240")
    private String address;

    @Schema(description = "위도", example = "37.5683")
    private Double lat;

    @Schema(description = "경도", example = "126.8975")
    private Double lng;

    @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
    private String image;

    @Schema(description = "잔디 유형", example = "인조잔디")
    private String grassType;

    @Schema(description = "추천 축구화", example = "TF")
    private String shoeType;

    @Schema(description = "평점", example = "4.5")
    private Double rating;

    public static FieldListResponse from(Field field) {
        return FieldListResponse.builder()
                .id(field.getFieldId())
                .name(field.getName())
                .address(field.getAddress())
                .lat(field.getLat())
                .lng(field.getLng())
                .image(field.getImage())
                .grassType(field.getGrassType())
                .shoeType(field.getShoeType())
                .rating(field.getRating())
                .build();
    }
}
