package com.community.back.domain.review.domain.repository;

import com.community.back.domain.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {

    // TODO: 필요한 쿼리 메서드 구현
    List<Review> findByFieldId(String fieldId);
}
