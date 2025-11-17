package com.vrms.ngo_posting_service.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * Utility class to generate JWT tokens for testing
 * Run this as a standalone Java application to generate test tokens
 */
public class JwtTestGenerator {

    public static void main(String[] args) {
        // IMPORTANT: Use the same secret key as in application.properties
        String secret = "your-secret-key-from-user-service-minimum-32-characters-long-change-in-production";
        
        // Token expiry: 1 hour from now
        long expirationMillis = 3600000;
        long now = System.currentTimeMillis();

        System.out.println("=".repeat(80));
        System.out.println("JWT TEST TOKEN GENERATOR");
        System.out.println("=".repeat(80));
        System.out.println();

        // ============ NGO TOKEN ============
        String ngoToken = Jwts.builder()
            .setSubject("ngo@example.com")
            .claim("userId", 1L)
            .claim("role", "NGO")
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(now + expirationMillis))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
            .compact();

        System.out.println("1. NGO TOKEN (Can create/update own postings)");
        System.out.println("-".repeat(80));
        System.out.println("Email: ngo@example.com");
        System.out.println("Role: NGO");
        System.out.println("User ID: 1");
        System.out.println("Expires in: 1 hour");
        System.out.println();
        System.out.println(ngoToken);
        System.out.println();
        System.out.println();

        // ============ ADMIN TOKEN ============
        String adminToken = Jwts.builder()
            .setSubject("admin@example.com")
            .claim("userId", 99L)
            .claim("role", "ADMIN")
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(now + expirationMillis))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
            .compact();

        System.out.println("2. ADMIN TOKEN (Can manage all postings)");
        System.out.println("-".repeat(80));
        System.out.println("Email: admin@example.com");
        System.out.println("Role: ADMIN");
        System.out.println("User ID: 99");
        System.out.println("Expires in: 1 hour");
        System.out.println();
        System.out.println(adminToken);
        System.out.println();
        System.out.println();

        // ============ VOLUNTEER TOKEN ============
        String volunteerToken = Jwts.builder()
            .setSubject("volunteer@example.com")
            .claim("userId", 2L)
            .claim("role", "VOLUNTEER")
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(now + expirationMillis))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
            .compact();

        System.out.println("3. VOLUNTEER TOKEN (Read-only access)");
        System.out.println("-".repeat(80));
        System.out.println("Email: volunteer@example.com");
        System.out.println("Role: VOLUNTEER");
        System.out.println("User ID: 2");
        System.out.println("Expires in: 1 hour");
        System.out.println();
        System.out.println(volunteerToken);
        System.out.println();
        System.out.println();

        // ============ SECOND NGO TOKEN (for testing authorization) ============
        String ngo2Token = Jwts.builder()
            .setSubject("ngo2@example.com")
            .claim("userId", 3L)
            .claim("role", "NGO")
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(now + expirationMillis))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
            .compact();

        System.out.println("4. SECOND NGO TOKEN (For testing authorization)");
        System.out.println("-".repeat(80));
        System.out.println("Email: ngo2@example.com");
        System.out.println("Role: NGO");
        System.out.println("User ID: 3");
        System.out.println("Expires in: 1 hour");
        System.out.println();
        System.out.println(ngo2Token);
        System.out.println();
        System.out.println();

        System.out.println("=".repeat(80));
        System.out.println("USAGE:");
        System.out.println("=".repeat(80));
        System.out.println("Copy any token above and use in curl requests:");
        System.out.println();
        System.out.println("curl \"http://localhost:8080/api/v1/postings\" \\");
        System.out.println("  -H \"Authorization: Bearer <TOKEN_HERE>\"");
        System.out.println();
        System.out.println("=".repeat(80));
    }
}
