package com.community.back.domain.review.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @Column(name = "review_id", columnDefinition = "VARCHAR(36)")
    private String reviewId;

    @Column(name = "field_id", nullable = false, columnDefinition = "VARCHAR(36)")
    private String fieldId;

    @Column(name = "user_id", nullable = false, columnDefinition = "VARCHAR(36)")
    private String userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Integer rating;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.reviewId == null) {
            this.reviewId = UUID.randomUUID().toString();
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Builder
    public Review(String fieldId, String userId, String content, Integer rating) {
        this.fieldId = fieldId;
        this.userId = userId;
        this.content = content;
        this.rating = rating;
    }

    // TODO: 리뷰 수정 메서드 구현
    public void update(String content, Integer rating) {
        // 구현 필요
    }
}
