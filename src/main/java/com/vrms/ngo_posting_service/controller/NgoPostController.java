package com.vrms.ngo_posting_service.controller;

import com.vrms.ngo_posting_service.dto.CreateNgoPostRequest;
import com.vrms.ngo_posting_service.dto.NgoPostResponse;
import com.vrms.ngo_posting_service.dto.UpdateNgoPostRequest;
import com.vrms.ngo_posting_service.service.NgoPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/postings")
@RequiredArgsConstructor
public class NgoPostController {

    private final NgoPostService service;

    // TODO: Extract ngoId from JWT token in production
    // For now, pass as header for testing
    
    @PostMapping
    public ResponseEntity<NgoPostResponse> createPost(
            @Valid @RequestBody CreateNgoPostRequest request,
            @RequestHeader("X-NGO-ID") Long ngoId) {
        NgoPostResponse response = service.createPost(request, ngoId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NgoPostResponse> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPostById(id));
    }

    @GetMapping
    public ResponseEntity<Page<NgoPostResponse>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("ASC") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(service.getAllPosts(pageable));
    }

    @GetMapping("/ngo/{ngoId}")
    public ResponseEntity<Page<NgoPostResponse>> getPostsByNgo(
            @PathVariable Long ngoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.getPostsByNgoId(ngoId, pageable));
    }

    @GetMapping("/domain/{domain}")
    public ResponseEntity<Page<NgoPostResponse>> getPostsByDomain(
            @PathVariable String domain,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.getPostsByDomain(domain, pageable));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<Page<NgoPostResponse>> getPostsByCity(
            @PathVariable String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.getPostsByCity(city, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<NgoPostResponse>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.searchPosts(keyword, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NgoPostResponse> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody UpdateNgoPostRequest request,
            @RequestHeader("X-NGO-ID") Long ngoId) {
        
        return ResponseEntity.ok(service.updatePost(id, request, ngoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            @RequestHeader("X-NGO-ID") Long ngoId) {
        
        service.deletePost(id, ngoId);
        return ResponseEntity.noContent().build();
    }
}
