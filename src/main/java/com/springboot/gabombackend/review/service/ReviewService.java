package com.springboot.gabombackend.review.service;

import com.springboot.gabombackend.review.dto.ReviewDto;
import com.springboot.gabombackend.review.entity.Review;
import com.springboot.gabombackend.review.repository.ReviewRepository;
import com.springboot.gabombackend.store.entity.Store;
import com.springboot.gabombackend.store.repository.StoreRepository;
import com.springboot.gabombackend.user.entity.User;
import com.springboot.gabombackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    // 리뷰 작성
    public ReviewDto.Response createReview(Long storeId, ReviewDto.Request request) {
        // 가게 찾기
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        // 로그인 사용자
        String loginId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // 저장
        Review review = Review.builder()
                .store(store)
                .user(user)
                .content(request.getContent())
                .build();

        return ReviewDto.Response.fromEntity(reviewRepository.save(review));
    }

    // 리뷰 조회 (페이지네이션)
    public Page<ReviewDto.Response> getReviews(Long storeId, Pageable pageable) {
        return reviewRepository.findByStoreId(storeId, pageable)
                .map(ReviewDto.Response::fromEntity);
    }
}
