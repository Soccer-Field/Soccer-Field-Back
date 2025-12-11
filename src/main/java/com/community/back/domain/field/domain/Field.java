package com.community.back.domain.field.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "fields")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Field {

    @Id
    @Column(name = "field_id", columnDefinition = "VARCHAR(36)")
    private String fieldId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lng;

    @Column(length = 1000)
    private String image;

    @Column(name = "grass_type", nullable = false, length = 50)
    private String grassType;

    @Column(name = "shoe_type", nullable = false, length = 10)
    private String shoeType;

    @Column(name = "grass_condition", length = 50)
    private String grassCondition;

    @Column(columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double rating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FieldStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.fieldId == null) {
            this.fieldId = UUID.randomUUID().toString();
        }
        if (this.rating == null) {
            this.rating = 0.0;
        }
        if (this.status == null) {
            this.status = FieldStatus.PENDING_APPROVAL;
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Builder
    public Field(String name, String address, Double lat, Double lng, String image,
                 String grassType, String shoeType, String grassCondition, FieldStatus status) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.image = image;
        this.grassType = grassType;
        this.shoeType = shoeType;
        this.grassCondition = grassCondition;
        this.status = status;
    }

    public void approve() {
        this.status = FieldStatus.APPROVED;
    }

    public void updateRating(Double rating) {
        this.rating = rating;
    }
}
