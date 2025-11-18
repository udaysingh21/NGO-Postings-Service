package com.vrms.ngo_posting_service.dto;

import com.vrms.ngo_posting_service.entity.PostStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class NgoPostResponse {
    private Long id;
    private String title;
    private String description;
    private String domain;
    private String location;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String effortRequired;
    private Integer volunteersNeeded;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long ngoId;
    private String contactEmail;
    private String contactPhone;
    private PostStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer volunteersSpotLeft;
    private Set<Long> volunteersRegistered;

}
