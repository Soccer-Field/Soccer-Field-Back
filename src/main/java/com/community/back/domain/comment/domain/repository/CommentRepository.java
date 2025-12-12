package com.community.back.domain.comment.domain.repository;

import com.community.back.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 특정 리뷰의 댓글을 생성일시 기준 오름차순으로 조회
     * @param reviewId 리뷰 ID
     * @return 정렬된 댓글 목록
     */
    List<Comment> findByReviewIdOrderByCreatedAtAsc(Long reviewId);
}
