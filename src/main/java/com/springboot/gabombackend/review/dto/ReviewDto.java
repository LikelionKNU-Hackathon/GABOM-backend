package com.springboot.gabombackend.review.dto;

import com.springboot.gabombackend.review.entity.Review;
import lombok.*;

import java.time.LocalDateTime;

public class ReviewDto {

    @Getter
    @Setter
    public static class Request {
        private String content;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String nickname;
        private String content;
        private LocalDateTime createdAt;

        public static Response fromEntity(Review review) {
            return Response.builder()
                    .id(review.getId())
                    .nickname(review.getUser().getNickname())
                    .content(review.getContent())
                    .createdAt(review.getCreatedAt())
                    .build();
        }
    }
}
