package com.community.back.domain.review.presentation;

import com.community.back.domain.review.application.ReviewService;
import com.community.back.domain.review.presentation.dto.request.CreateReviewRequest;
import com.community.back.domain.review.presentation.dto.request.UpdateReviewRequest;
import com.community.back.domain.review.presentation.dto.response.ReviewResponse;
import com.community.back.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰 관리 API")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 목록 조회", description = "특정 축구장의 리뷰 목록을 조회합니다. 무한 스크롤을 위해 lastId를 사용합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReviewResponse.class))))
    })
    @GetMapping("/fields/{fieldId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getReviews(
            @Parameter(description = "축구장 ID", required = true)
            @PathVariable Long fieldId,
            @Parameter(description = "마지막 리뷰 ID (무한 스크롤용)", required = false)
            @RequestParam(required = false) Long lastId) {
        log.info("GET /fields/{}/reviews?lastId={} - 리뷰 목록 조회", fieldId, lastId);
        List<ReviewResponse> reviews = reviewService.getReviewsByFieldId(fieldId, lastId);
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "리뷰 작성", description = "축구장에 대한 리뷰를 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "작성 성공",
                    content = @Content(schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/fields/{fieldId}/reviews")
    public ResponseEntity<ReviewResponse> createReview(
            @Parameter(description = "축구장 ID", required = true)
            @PathVariable Long fieldId,
            @Valid @RequestBody CreateReviewRequest request) {
        log.info("POST /fields/{}/reviews - 리뷰 작성", fieldId);
        Long userId = getUserIdFromAuth();
        ReviewResponse response = reviewService.createReview(fieldId, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "리뷰 수정", description = "작성한 리뷰를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @Parameter(description = "리뷰 ID", required = true)
            @PathVariable Long reviewId,
            @Valid @RequestBody UpdateReviewRequest request) {
        log.info("PUT /reviews/{} - 리뷰 수정", reviewId);
        Long userId = getUserIdFromAuth();
        ReviewResponse response = reviewService.updateReview(reviewId, request, userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "리뷰 삭제", description = "작성한 리뷰를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "리뷰 ID", required = true)
            @PathVariable Long reviewId) {
        log.info("DELETE /reviews/{} - 리뷰 삭제", reviewId);
        Long userId = getUserIdFromAuth();
        reviewService.deleteReview(reviewId, userId);
        return ResponseEntity.noContent().build();
    }

    private Long getUserIdFromAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication: {}", authentication);

        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("User is not authenticated");
            throw new com.community.back.global.exception.CustomException(
                com.community.back.global.exception.ErrorCode.INVALID_TOKEN);
        }

        String userIdStr = authentication.getPrincipal().toString();
        log.info("UserId from authentication: {}", userIdStr);

        try {
            return Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            log.error("Failed to parse userId: {}", userIdStr);
            throw new com.community.back.global.exception.CustomException(
                com.community.back.global.exception.ErrorCode.INVALID_TOKEN);
        }
    }
}
