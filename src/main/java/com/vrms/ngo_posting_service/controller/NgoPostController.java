package com.vrms.ngo_posting_service.controller;

import com.vrms.ngo_posting_service.dto.CreateNgoPostRequest;
import com.vrms.ngo_posting_service.dto.NgoPostResponse;
import com.vrms.ngo_posting_service.dto.UpdateNgoPostRequest;
import com.vrms.ngo_posting_service.exception.ForbiddenException;
import com.vrms.ngo_posting_service.service.NgoPostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/postings")
@RequiredArgsConstructor
public class NgoPostController {

    private final NgoPostService service;

    /**
     * Create a new posting (NGO or ADMIN only)
     */
    @PostMapping
    public ResponseEntity<NgoPostResponse> createPost(
            @Valid @RequestBody CreateNgoPostRequest request,
            HttpServletRequest httpRequest) {

        Long userId = (Long) httpRequest.getAttribute("userId");
        String role = (String) httpRequest.getAttribute("role");
        String username = (String) httpRequest.getAttribute("username");

        log.info("CREATE POST - User: {}, UserId: {}, Role: {}", username, userId, role);

        // Only NGO and ADMIN can create postings
        if (!("NGO".equals(role) || "ADMIN".equals(role))) {
            log.warn("FORBIDDEN: User {} with role {} attempted to create posting", username, role);
            throw new ForbiddenException("Only NGO and ADMIN users can create postings");
        }

        NgoPostResponse response = service.createPost(request, userId);
        log.info("POST created successfully with ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get posting by ID (all authenticated users can view)
     */
    @GetMapping("/{id}")
    public ResponseEntity<NgoPostResponse> getPost(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {

        Long userId = (Long) httpRequest.getAttribute("userId");
        log.debug("GET POST - PostId: {}, UserId: {}", id, userId);

        return ResponseEntity.ok(service.getPostById(id));
    }

    /**
     * Get all active postings (paginated)
     */
    @GetMapping
    public ResponseEntity<Page<NgoPostResponse>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            HttpServletRequest httpRequest) {

        Long userId = (Long) httpRequest.getAttribute("userId");
        String role = (String) httpRequest.getAttribute("role");

        log.debug("GET ALL POSTS - UserId: {}, Role: {}, Page: {}, Size: {}", userId, role, page, size);

        Sort sort = sortDir.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(service.getAllPosts(pageable));
    }

    /**
     * Get postings by NGO ID (ADMIN can view all, NGO can only view their own)
     */
    @GetMapping("/ngo/{ngoId}")
    public ResponseEntity<Page<NgoPostResponse>> getPostsByNgo(
            @PathVariable Long ngoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest httpRequest) {

        Long userId = (Long) httpRequest.getAttribute("userId");
        String role = (String) httpRequest.getAttribute("role");

        log.debug("GET NGO POSTS - NGO_ID: {}, UserId: {}, Role: {}", ngoId, userId, role);

        // NGO can only view their own postings
        if ("NGO".equals(role) && !ngoId.equals(userId)) {
            log.warn("FORBIDDEN: NGO {} attempted to view postings of NGO {}", userId, ngoId);
            throw new ForbiddenException("You can only view your own postings");
        }

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.getPostsByNgoId(ngoId, pageable));
    }

    /**
     * Get postings by domain
     */
    @GetMapping("/domain/{domain}")
    public ResponseEntity<Page<NgoPostResponse>> getPostsByDomain(
            @PathVariable String domain,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest httpRequest) {

        Long userId = (Long) httpRequest.getAttribute("userId");
        log.debug("GET POSTS BY DOMAIN - Domain: {}, UserId: {}", domain, userId);

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.getPostsByDomain(domain, pageable));
    }

    /**
     * Update posting (NGO can update own, ADMIN can update any)
     */
    @PutMapping("/{id}")
    public ResponseEntity<NgoPostResponse> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody UpdateNgoPostRequest request,
            HttpServletRequest httpRequest) {

        Long userId = (Long) httpRequest.getAttribute("userId");
        String role = (String) httpRequest.getAttribute("role");
        String username = (String) httpRequest.getAttribute("username");

        log.info("UPDATE POST - PostId: {}, UserId: {}, Role: {}", id, userId, role);

        return ResponseEntity.ok(service.updatePost(id, request, userId, role));
    }

    /**
     * Delete posting (NGO can delete own, ADMIN can delete any)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {

        Long userId = (Long) httpRequest.getAttribute("userId");
        String role = (String) httpRequest.getAttribute("role");

        log.info("DELETE POST - PostId: {}, UserId: {}, Role: {}", id, userId, role);

        service.deletePost(id, userId, role);
        return ResponseEntity.noContent().build();
    }
}
