package com.community.back.domain.review.application;

import com.community.back.domain.review.domain.Review;
import com.community.back.domain.review.domain.repository.ReviewRepository;
import com.community.back.domain.review.presentation.dto.request.CreateReviewRequest;
import com.community.back.domain.review.presentation.dto.request.UpdateReviewRequest;
import com.community.back.domain.review.presentation.dto.response.ReviewResponse;
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

    public List<ReviewResponse> getReviewsByFieldId(Long fieldId, Long lastId) {
        log.info("Fetching reviews for field: {} with lastId: {}", fieldId, lastId);

        List<Review> reviews;
        if(lastId == null)
            reviews = reviewRepository.findAllInfiniteScroll(fieldId, 10L);
        else
            reviews = reviewRepository.findAllInfiniteScroll(fieldId, 10L, lastId);

        return reviews.stream()
                .map(ReviewResponse::from)
                .toList();
    }

    @Transactional
    public ReviewResponse createReview(Long fieldId, CreateReviewRequest request, Long userId) {
        log.info("Creating review for field: {} by user: {}", fieldId, userId);

        // 축구장 존재 여부 확인
        fieldRepository.findById(fieldId)
                .orElseThrow(() -> new com.community.back.global.exception.CustomException(
                        com.community.back.global.exception.ErrorCode.FIELD_NOT_FOUND));

        // 리뷰 생성
        Review review = Review.builder()
                .fieldId(fieldId)
                .userId(userId)
                .content(request.getContent())
                .rating(request.getRating())
                .build();

        Review savedReview = reviewRepository.save(review);
        log.info("Review created successfully with id: {}", savedReview.getReviewId());

        // 축구장 평점 업데이트
        updateFieldRating(fieldId);

        return ReviewResponse.from(savedReview);
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

    // TODO: 리뷰 수정 구현
    @Transactional
    public ReviewResponse updateReview(Long reviewId, UpdateReviewRequest request, Long userId) {
        log.info("Updating review: {} by user: {}", reviewId, userId);
        // 구현 필요
        throw new UnsupportedOperationException("구현 필요");
    }

    // TODO: 리뷰 삭제 구현
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        log.info("Deleting review: {} by user: {}", reviewId, userId);
        // 구현 필요
        throw new UnsupportedOperationException("구현 필요");
    }
}
