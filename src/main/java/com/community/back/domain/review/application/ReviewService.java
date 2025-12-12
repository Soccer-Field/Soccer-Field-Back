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

    // TODO: 리뷰 작성 구현
    @Transactional
    public ReviewResponse createReview(Long fieldId, CreateReviewRequest request, Long userId) {
        log.info("Creating review for field: {} by user: {}", fieldId, userId);
        // 구현 필요
        throw new UnsupportedOperationException("구현 필요");
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
