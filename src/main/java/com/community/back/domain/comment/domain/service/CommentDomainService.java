package com.community.back.domain.comment.domain.service;

import com.community.back.domain.comment.domain.Comment;
import com.community.back.domain.comment.domain.repository.CommentRepository;
import com.community.back.global.exception.CustomException;
import com.community.back.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentDomainService {

    private final CommentRepository commentRepository;

    /**
     * 댓글 생성 (도메인 로직)
     * - 부모 댓글 검증 후 Comment 객체 생성
     * @param reviewId 리뷰 ID
     * @param userId 작성자 ID
     * @param parentId 부모 댓글 ID (null이면 최상위 댓글)
     * @param content 댓글 내용
     * @return 생성된 Comment 객체
     */
    public Comment createComment(Long reviewId, Long userId, Long parentId, String content) {
        // 부모 댓글 검증
        Comment parent = findAndValidateParent(parentId);

        // Comment 생성
        return Comment.create(reviewId, userId, parentId, content);
    }

    /**
     * 부모 댓글 조회 및 검증 (투댑스 구조)
     * - parentId가 null인 경우: null 반환 (최상위 댓글)
     * - parentId가 있는 경우: 부모 댓글이 존재하고, 부모가 최상위 댓글인지 검증
     * @return 부모 댓글 (없으면 null)
     */
    private Comment findAndValidateParent(Long parentId) {
        if (parentId == null) {
            return null;
        }

        Comment parentComment = commentRepository.findById(parentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // 투댑스 구조: 대댓글의 대댓글은 불가능
        if (!parentComment.isRoot()) {
            log.warn("Cannot create nested comment. Parent comment {} already has a parent", parentId);
            throw new CustomException(ErrorCode.INVALID_COMMENT_DEPTH);
        }

        return parentComment;
    }

    /**
     * 부모 댓글 검증 (투댑스 구조)
     * - parentId가 null인 경우: 부모 댓글로 생성 가능
     * - parentId가 있는 경우: 부모 댓글이 존재하고, 부모가 최상위 댓글인지 검증
     */
    public void validateParentComment(Long parentId) {
        findAndValidateParent(parentId);
    }

    /**
     * 댓글 소유자 검증
     */
    public void validateCommentOwner(Comment comment, Long userId) {
        if (!comment.getUserId().equals(userId)) {
            log.warn("User {} is not the owner of comment {}", userId, comment.getCommentId());
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    /**
     * 리뷰에 속한 댓글인지 검증
     */
    public void validateCommentBelongsToReview(Comment comment, Long reviewId) {
        if (!comment.getReviewId().equals(reviewId)) {
            log.warn("Comment {} does not belong to review {}", comment.getCommentId(), reviewId);
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
    }
}
