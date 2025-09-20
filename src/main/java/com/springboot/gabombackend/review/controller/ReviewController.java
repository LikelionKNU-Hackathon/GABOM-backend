package com.springboot.gabombackend.review.controller;

import com.springboot.gabombackend.review.dto.ReviewDto;
import com.springboot.gabombackend.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores/{storeId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성
    @PostMapping
    public ResponseEntity<ReviewDto.Response> createReview(@PathVariable Long storeId, @RequestBody ReviewDto.Request request) {
        return ResponseEntity.ok(reviewService.createReview(storeId, request));
    }

    // 리뷰 조회
    @GetMapping
    public ResponseEntity<List<ReviewDto.Response>> getReviews(@PathVariable Long storeId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

        Page<ReviewDto.Response> reviewPage = reviewService.getReviews(storeId, PageRequest.of(page, size));

        return ResponseEntity.ok(reviewPage.getContent());
    }
}
