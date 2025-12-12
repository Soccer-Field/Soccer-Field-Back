package com.community.back.domain.comment.application;

import com.community.back.domain.comment.domain.Comment;
import com.community.back.domain.comment.domain.repository.CommentRepository;
import com.community.back.domain.comment.domain.service.CommentDomainService;
import com.community.back.domain.comment.presentation.dto.request.CreateCommentRequest;
import com.community.back.domain.comment.presentation.dto.request.UpdateCommentRequest;
import com.community.back.domain.comment.presentation.dto.response.CommentResponse;
import com.community.back.domain.comment.presentation.dto.response.CreateCommentResponse;
import com.community.back.domain.review.domain.repository.ReviewRepository;
import com.community.back.global.exception.CustomException;
import com.community.back.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final CommentDomainService commentDomainService;

    // TODO: 댓글 목록 조회 구현
    public List<CommentResponse> getCommentsByReviewId(Long reviewId) {
        log.info("Fetching comments for review: {}", reviewId);
        // 구현 필요
        throw new UnsupportedOperationException("구현 필요");
    }

    /**
     * 댓글 작성
     * Application Service: 외부 의존성 처리 (리뷰 존재 여부 확인, 트랜잭션 관리)
     * Domain Service: 댓글 생성 비즈니스 로직 처리
     */
    @Transactional
    public CreateCommentResponse createComment(Long reviewId, CreateCommentRequest request, Long userId) {
        log.info("Creating comment for review: {} by user: {}", reviewId, userId);

        // 리뷰 존재 여부 확인 (Application Service의 책임)
        reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        // 댓글 생성 (Domain Service에 위임)
        Comment comment = commentDomainService.createComment(
                reviewId,
                userId != null ? userId : 1L, // TODO: 실제 인증된 사용자 ID로 대체
                request.getParentId(),
                request.getContent()
        );

        // 영속성 처리 (Application Service의 책임)
        Comment savedComment = commentRepository.save(comment);
        log.info("Comment created successfully: {}", savedComment.getCommentId());

        return CreateCommentResponse.from(savedComment);
    }

    // TODO: 댓글 수정 구현
    @Transactional
    public CommentResponse updateComment(Long commentId, UpdateCommentRequest request, Long userId) {
        log.info("Updating comment: {} by user: {}", commentId, userId);
        // 구현 필요
        throw new UnsupportedOperationException("구현 필요");
    }

    // TODO: 댓글 삭제 구현
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        log.info("Deleting comment: {} by user: {}", commentId, userId);
        // 구현 필요
        throw new UnsupportedOperationException("구현 필요");
    }
}
