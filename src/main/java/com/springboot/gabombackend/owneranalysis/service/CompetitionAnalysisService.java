package com.springboot.gabombackend.owneranalysis.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.gabombackend.owneranalysis.dto.CompetitionAnalysisDto;
import com.springboot.gabombackend.owneranalysis.entity.CompetitionResultEntity;
import com.springboot.gabombackend.owneranalysis.repository.CompetitionResultRepository;
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
public class CompetitionAnalysisService {

    private final ReviewRepository reviewRepo;
    private final StoreRepository storeRepo;
    private final CompetitionResultRepository compRepo;
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

    // 내 가게 vs 같은 카테고리 경쟁 가게 리뷰 비교 분석
    @Transactional
    public CompetitionAnalysisDto analyze(Long storeId) throws Exception {
        Store store = storeRepo.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));

        // 내 가게 리뷰
        List<String> myReviews = reviewRepo.findContentsByStoreId(storeId);

        // 같은 카테고리(문자열) 내 다른 가게들의 리뷰
        List<String> compReviews = reviewRepo.findContentsByCategoryCompetitors(
                store.getCategory(), storeId);

        // GPT 프롬프트
        String systemPrompt = "당신은 음식점 리뷰를 분석하는 AI입니다. " +
                "키워드(가성비, 양, 맛, 서비스, 위생)별로 0~100 점수를 계산하고 JSON만 출력하세요.\n" +
                "{ \"myStore\": { ... }, \"competitors\": { ... } } 형식으로.";
        String userPrompt = "[내 가게 리뷰]\n" + joinWithLimit(myReviews) +
                "\n\n[경쟁 가게 리뷰]\n" + joinWithLimit(compReviews);

        // GPT 호출 → JSON 응답
        String json = aiClient.callAnalysis(systemPrompt, userPrompt);
        JsonNode root = objectMapper.readTree(json);

        // DB 저장 (업데이트 or 신규)
        CompetitionResultEntity entity = compRepo.findByStoreId(storeId)
                .orElse(new CompetitionResultEntity());
        entity.setStoreId(storeId);
        entity.setCategoryName(store.getCategory());
        entity.setMyPrice(root.path("myStore").path("가성비").asInt());
        entity.setMyQuantity(root.path("myStore").path("양").asInt());
        entity.setMyTaste(root.path("myStore").path("맛").asInt());
        entity.setMyService(root.path("myStore").path("서비스").asInt());
        entity.setMyClean(root.path("myStore").path("위생").asInt());
        entity.setCompPrice(root.path("competitors").path("가성비").asInt());
        entity.setCompQuantity(root.path("competitors").path("양").asInt());
        entity.setCompTaste(root.path("competitors").path("맛").asInt());
        entity.setCompService(root.path("competitors").path("서비스").asInt());
        entity.setCompClean(root.path("competitors").path("위생").asInt());
        entity.setAnalyzedAt(LocalDateTime.now());
        compRepo.save(entity);

        // DTO 변환 후 반환
        return CompetitionAnalysisDto.builder()
                .storeId(storeId)
                .storeName(store.getName())
                .category(store.getCategory())
                .myPrice(entity.getMyPrice())
                .myQuantity(entity.getMyQuantity())
                .myTaste(entity.getMyTaste())
                .myService(entity.getMyService())
                .myClean(entity.getMyClean())
                .compPrice(entity.getCompPrice())
                .compQuantity(entity.getCompQuantity())
                .compTaste(entity.getCompTaste())
                .compService(entity.getCompService())
                .compClean(entity.getCompClean())
                .analyzedAt(entity.getAnalyzedAt())
                .build();
    }
}
