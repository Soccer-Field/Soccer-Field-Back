package com.community.back.domain.review.domain.repository;

import com.community.back.domain.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByFieldId(Long fieldId);

    @Query(
            value = "select * from reviews " +
                    "where field_id = :fieldId " +
                    "order by review_id desc " +
                    "limit :limit",
            nativeQuery = true
    )
    List<Review> findAllInfiniteScroll(
            @Param("fieldId") Long fieldId,
            @Param("limit") Long limit
    );

    @Query(
            value = "select * from reviews " +
                    "where field_id = :fieldId and review_id < :lastReviewId " +
                    "order by review_id desc " +
                    "limit :limit",
            nativeQuery = true
    )
    List<Review> findAllInfiniteScroll(
            @Param("fieldId") Long fieldId,
            @Param("limit") Long limit,
            @Param("lastReviewId") Long lastReviewId
    );
}
