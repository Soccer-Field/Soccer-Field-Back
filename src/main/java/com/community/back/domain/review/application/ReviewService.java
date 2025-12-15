package com.community.back.domain.review.application;

import com.community.back.domain.review.domain.Review;
import com.community.back.domain.review.domain.repository.ReviewRepository;
import com.community.back.domain.review.presentation.dto.request.CreateReviewRequest;
import com.community.back.domain.review.presentation.dto.request.UpdateReviewRequest;
import com.community.back.domain.review.presentation.dto.response.ReviewResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final com.community.back.domain.field.domain.repository.FieldRepository fieldRepository;
    private final com.community.back.domain.auth.domain.repository.UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<ReviewResponse> getReviewsByFieldId(Long fieldId, Long lastId) {
        log.info("Fetching reviews for field: {} with lastId: {}", fieldId, lastId);

        List<Review> reviews;
        if(lastId == null)
            reviews = reviewRepository.findAllInfiniteScroll(fieldId, 10L);
        else
            reviews = reviewRepository.findAllInfiniteScroll(fieldId, 10L, lastId);

        return reviews.stream()
                .map(review -> {
                    String userName = userRepository.findById(String.valueOf(review.getUserId()))
                            .map(com.community.back.domain.auth.domain.User::getName)
                            .orElse("알 수 없음");
                    return ReviewResponse.from(review, userName);
                })
                .toList();
    }

    @Transactional
    public ReviewResponse createReview(Long fieldId, CreateReviewRequest request, Long userId) {
        log.info("Creating review for field: {} by user: {}", fieldId, userId);

        // 축구장 존재 여부 확인
        fieldRepository.findById(fieldId)
                .orElseThrow(() -> new com.community.back.global.exception.CustomException(
                        com.community.back.global.exception.ErrorCode.FIELD_NOT_FOUND));

        // grassConditions 리스트를 JSON 문자열로 변환
        String grassConditionsJson = null;
        if (request.getGrassConditions() != null && !request.getGrassConditions().isEmpty()) {
            try {
                grassConditionsJson = objectMapper.writeValueAsString(request.getGrassConditions());
            } catch (JsonProcessingException e) {
                log.error("Failed to convert grass conditions to JSON", e);
                throw new com.community.back.global.exception.CustomException(
                        com.community.back.global.exception.ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }

        // 리뷰 생성
        Review review = Review.builder()
                .fieldId(fieldId)
                .userId(userId)
                .content(request.getContent())
                .rating(request.getRating())
                .grassType(request.getGrassType())
                .grassConditions(grassConditionsJson)
                .recommendedShoe(request.getRecommendedShoe())
                .shoeLink(request.getShoeLink())
                .build();

        Review savedReview = reviewRepository.save(review);
        log.info("Review created successfully with id: {}", savedReview.getReviewId());

        // 축구장 평점 업데이트
        updateFieldRating(fieldId);

        // userName 조회 및 추가
        String userName = userRepository.findById(String.valueOf(userId))
                .map(com.community.back.domain.auth.domain.User::getName)
                .orElse("알 수 없음");

        return ReviewResponse.from(savedReview, userName);
    }

    private void updateFieldRating(Long fieldId) {
        List<Review> reviews = reviewRepository.findByFieldId(fieldId);
        if (!reviews.isEmpty()) {
            double averageRating = reviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);

            com.community.back.domain.field.domain.Field field = fieldRepository.findById(fieldId)
                    .orElseThrow(() -> new com.community.back.global.exception.CustomException(
                            com.community.back.global.exception.ErrorCode.FIELD_NOT_FOUND));

            field.updateRating(Math.round(averageRating * 10) / 10.0);
            log.info("Updated field {} rating to {}", fieldId, field.getRating());
        }
    }

    @Transactional
    public ReviewResponse updateReview(Long reviewId, UpdateReviewRequest request, Long userId) {
        log.info("Updating review: {} by user: {}", reviewId, userId);

        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new com.community.back.global.exception.CustomException(
                        com.community.back.global.exception.ErrorCode.REVIEW_NOT_FOUND));

        // 작성자 권한 확인
        if (!review.getUserId().equals(userId)) {
            throw new com.community.back.global.exception.CustomException(
                    com.community.back.global.exception.ErrorCode.UNAUTHORIZED_REVIEW_ACCESS);
        }

        // grassConditions 리스트를 JSON 문자열로 변환
        String grassConditionsJson = null;
        if (request.getGrassConditions() != null && !request.getGrassConditions().isEmpty()) {
            try {
                grassConditionsJson = objectMapper.writeValueAsString(request.getGrassConditions());
            } catch (JsonProcessingException e) {
                log.error("Failed to convert grass conditions to JSON", e);
                throw new com.community.back.global.exception.CustomException(
                        com.community.back.global.exception.ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }

        // 리뷰 수정
        review.update(request.getContent(), request.getRating(), request.getGrassType(),
                grassConditionsJson, request.getRecommendedShoe(), request.getShoeLink());
        log.info("Review {} updated successfully", reviewId);

        // 축구장 평점 업데이트
        updateFieldRating(review.getFieldId());

        // userName 조회 및 추가
        String userName = userRepository.findById(String.valueOf(review.getUserId()))
                .map(com.community.back.domain.auth.domain.User::getName)
                .orElse("알 수 없음");

        return ReviewResponse.from(review, userName);
    }

    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        log.info("Deleting review: {} by user: {}", reviewId, userId);

        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new com.community.back.global.exception.CustomException(
                        com.community.back.global.exception.ErrorCode.REVIEW_NOT_FOUND));

        // 작성자 권한 확인
        if (!review.getUserId().equals(userId)) {
            throw new com.community.back.global.exception.CustomException(
                    com.community.back.global.exception.ErrorCode.UNAUTHORIZED_REVIEW_ACCESS);
        }

        Long fieldId = review.getFieldId();

        // 리뷰 삭제
        reviewRepository.delete(review);
        log.info("Review {} deleted successfully", reviewId);

        // 축구장 평점 업데이트
        updateFieldRating(fieldId);
    }
}
