package com.community.back.domain.comment.domain.repository;

import com.community.back.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // TODO: 필요한 쿼리 메서드 구현
    List<Comment> findByReviewId(Long reviewId);
}
