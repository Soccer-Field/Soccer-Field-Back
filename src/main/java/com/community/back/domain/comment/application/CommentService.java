package com.community.back.domain.comment.application;

import com.community.back.domain.comment.domain.repository.CommentRepository;
import com.community.back.domain.comment.presentation.dto.request.CreateCommentRequest;
import com.community.back.domain.comment.presentation.dto.request.UpdateCommentRequest;
import com.community.back.domain.comment.presentation.dto.response.CommentResponse;
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

    // TODO: 댓글 목록 조회 구현
    public List<CommentResponse> getCommentsByReviewId(Long reviewId) {
        log.info("Fetching comments for review: {}", reviewId);
        // 구현 필요
        throw new UnsupportedOperationException("구현 필요");
    }

    // TODO: 댓글 작성 구현
    @Transactional
    public CommentResponse createComment(Long reviewId, CreateCommentRequest request, Long userId) {
        log.info("Creating comment for review: {} by user: {}", reviewId, userId);
        // 구현 필요
        throw new UnsupportedOperationException("구현 필요");
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
