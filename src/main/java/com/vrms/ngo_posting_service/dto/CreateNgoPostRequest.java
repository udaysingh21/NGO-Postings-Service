package com.vrms.ngo_posting_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateNgoPostRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotBlank(message = "Domain is required")
    private String domain;

    @NotBlank(message = "Location is required")
    private String location;

    private String city;
    private String state;
    private String country;

    private String effortRequired;

    @Min(value = 1, message = "At least 1 volunteer is needed")
    private Integer volunteersNeeded;

    @NotNull(message = "Start date is required")
    @Future(message = "Start date must be in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Email(message = "Invalid email format")
    private String contactEmail;

    private String contactPhone;
}
