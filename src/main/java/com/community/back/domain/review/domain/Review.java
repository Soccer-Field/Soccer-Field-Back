package com.community.back.domain.review.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "field_id", nullable = false)
    private Long fieldId;

    @Column(name = "user_id", nullable = false, length = 36)
    private Long userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Integer rating;

    @Column(name = "grass_type", nullable = false, length = 10)
    private String grassType;

    @Column(name = "grass_conditions", columnDefinition = "TEXT")
    private String grassConditions;

    @Column(name = "recommended_shoe", nullable = false, length = 10)
    private String recommendedShoe;

    @Column(name = "shoe_link", columnDefinition = "TEXT")
    private String shoeLink;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Builder
    public Review(Long fieldId, Long userId, String content, Integer rating,
                  String grassType, String grassConditions, String recommendedShoe, String shoeLink) {
        this.fieldId = fieldId;
        this.userId = userId;
        this.content = content;
        this.rating = rating;
        this.grassType = grassType;
        this.grassConditions = grassConditions;
        this.recommendedShoe = recommendedShoe;
        this.shoeLink = shoeLink;
    }

    public void update(String content, Integer rating, String grassType,
                      String grassConditions, String recommendedShoe, String shoeLink) {
        this.content = content;
        this.rating = rating;
        this.grassType = grassType;
        this.grassConditions = grassConditions;
        this.recommendedShoe = recommendedShoe;
        this.shoeLink = shoeLink;
    }
}
