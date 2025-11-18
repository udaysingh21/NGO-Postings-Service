package com.vrms.ngo_posting_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "ngo_posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NgoPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private String domain; // e.g., Education, Health, Environment

    @Column(nullable = false)
    private String location; // City or full address

    private String city;
    private String state;
    private String country;
    private String pincode; 

    @Column(name = "effort_required")
    private String effortRequired; // e.g., "5 hours", "2 days"

    @Column(name = "volunteers_needed")
    private Integer volunteersNeeded;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "ngo_id", nullable = false)
    private Long ngoId; // Reference to User Service NGO ID

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status = PostStatus.ACTIVE;

    // New fields
    @ElementCollection
    @CollectionTable(name = "ngo_post_volunteers", joinColumns = @JoinColumn(name = "posting_id"))
    @Column(name = "volunteer_id")
    private Set<Long> volunteersRegistered = new HashSet<>();

    @Column(name = "volunteers_spot_left")
    private Integer volunteersSpotLeft;

    @PrePersist
    public void prePersist() {
        if (volunteersSpotLeft == null && volunteersNeeded != null) {
            volunteersSpotLeft = volunteersNeeded;
        }
    }

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Auto-update timestamp on modification
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
