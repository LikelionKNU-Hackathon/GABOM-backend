package com.springboot.gabombackend.owneranalysis.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.gabombackend.owneranalysis.dto.SentimentAnalysisDto;
import com.springboot.gabombackend.owneranalysis.entity.SentimentResultEntity;
import com.springboot.gabombackend.owneranalysis.repository.SentimentResultRepository;
import com.springboot.gabombackend.review.repository.ReviewRepository;
import com.springboot.gabombackend.store.entity.Store;
import com.springboot.gabombackend.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SentimentAnalysisService {

    private final ReviewRepository reviewRepo;
    private final StoreRepository storeRepo;
    private final SentimentResultRepository sentiRepo;
    private final OpenAiClient aiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final int MAX_CHARS = 8000;

    // 리뷰들을 일정 글자수까지만 합치는 유틸
    private String joinWithLimit(List<String> reviews) {
        StringBuilder sb = new StringBuilder();
        for (String r : reviews) {
            if (r == null) continue;
            if (sb.length() + r.length() > MAX_CHARS) break;
            sb.append(r).append("\n");
        }
        return sb.toString();
    }

    // 긍/부정 비율 분석
    @Transactional
    public SentimentAnalysisDto analyze(Long storeId) throws Exception {
        Store store = storeRepo.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));

        List<String> reviews = reviewRepo.findContentsByStoreId(storeId);

        // GPT 프롬프트
        String systemPrompt = "당신은 음식점 리뷰를 분석하는 AI입니다. " +
                "전체 리뷰의 긍정/부정 비율을 %로 계산하고 반드시 JSON만 출력하세요. " +
                "{ \"positive\": 65, \"negative\": 35 } 형식으로. " +
                "positive+negative=100 이어야 합니다.";
        String userPrompt = "[리뷰]\n" + joinWithLimit(reviews);

        // GPT 호출 → JSON 응답
        String json = aiClient.callAnalysis(systemPrompt, userPrompt);
        JsonNode root = objectMapper.readTree(json);

        // DB 저장 (업데이트 or 신규)
        SentimentResultEntity entity = sentiRepo.findByStoreId(storeId)
                .orElse(new SentimentResultEntity());
        entity.setStoreId(storeId);
        entity.setReviewCount(reviews.size());
        entity.setPositive(root.path("positive").asInt());
        entity.setNegative(root.path("negative").asInt());
        entity.setAnalyzedAt(LocalDateTime.now());
        sentiRepo.save(entity);

        // DTO 변환 후 반환
        return SentimentAnalysisDto.builder()
                .storeId(storeId)
                .storeName(store.getName())
                .reviewCount(reviews.size())
                .positive(entity.getPositive())
                .negative(entity.getNegative())
                .analyzedAt(entity.getAnalyzedAt())
                .build();
    }
}
