package com.vrms.ngo_posting_service.repository;

import com.vrms.ngo_posting_service.entity.NgoPost;
import com.vrms.ngo_posting_service.entity.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NgoPostRepository extends JpaRepository<NgoPost, Long> {

    // Find all posts by NGO ID
    List<NgoPost> findByNgoId(Long ngoId);

    // Find posts by NGO ID with pagination
    Page<NgoPost> findByNgoId(Long ngoId, Pageable pageable);

    // Find active posts only
    Page<NgoPost> findByStatus(PostStatus status, Pageable pageable);

    // Find posts by domain
    Page<NgoPost> findByDomain(String domain, Pageable pageable);

    // Find posts by location (city)
    Page<NgoPost> findByCity(String city, Pageable pageable);
    
    // Find posts by location (pincode)
    Page<NgoPost> findByPincode(String pincode, Pageable pageable);

    // Complex query: Find posts by domain and city
    Page<NgoPost> findByDomainAndCity(String domain, String city, Pageable pageable);

    // Find posts with upcoming start dates
    @Query("SELECT p FROM NgoPost p WHERE p.startDate >= :date AND p.status = :status")
    Page<NgoPost> findUpcomingPosts(
        @Param("date") LocalDate date, 
        @Param("status") PostStatus status, 
        Pageable pageable
    );

    // Search posts by title or description
    @Query("SELECT p FROM NgoPost p WHERE " +
           "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND p.status = :status")
    Page<NgoPost> searchPosts(
        @Param("keyword") String keyword, 
        @Param("status") PostStatus status, 
        Pageable pageable
    );
}
