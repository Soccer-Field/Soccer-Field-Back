package com.community.back.domain.comment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    @Column(name = "user_id", nullable = false, length = 36)
    private Long userId;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

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
    private Comment(Long reviewId, Long userId, Long parentId, String content) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.parentId = parentId;
        this.content = content;
    }

    /**
     * 댓글 생성 정적 팩토리 메서드
     * @param reviewId 리뷰 ID
     * @param userId 작성자 ID
     * @param parentId 부모 댓글 ID (null이면 최상위 댓글)
     * @param content 댓글 내용
     * @return 생성된 Comment 객체
     */
    public static Comment create(Long reviewId, Long userId, Long parentId, String content) {
        return Comment.builder()
                .reviewId(reviewId)
                .userId(userId)
                .parentId(parentId)
                .content(content)
                .build();
    }

    /**
     * 최상위 댓글인지 확인
     */
    public boolean isRoot() {
        return this.parentId == null;
    }

    /**
     * 댓글 내용 수정
     */
    public void update(String content) {
        this.content = content;
    }
}
