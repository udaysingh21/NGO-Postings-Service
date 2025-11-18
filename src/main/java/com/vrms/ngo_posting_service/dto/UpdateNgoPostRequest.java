package com.vrms.ngo_posting_service.dto;

import com.vrms.ngo_posting_service.entity.PostStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateNgoPostRequest {

    @Size(max = 255)
    private String title;

    @Size(max = 2000)
    private String description;

    private String domain;
    private String location;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String effortRequired;
    
    @Min(1)
    private Integer volunteersNeeded;

    private LocalDate startDate;
    private LocalDate endDate;

    @Email
    private String contactEmail;

    private String contactPhone;
    private PostStatus status;
}
