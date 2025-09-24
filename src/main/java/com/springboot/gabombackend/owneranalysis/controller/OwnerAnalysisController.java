package com.springboot.gabombackend.owneranalysis.controller;

import com.springboot.gabombackend.owneranalysis.dto.CompetitionAnalysisDto;
import com.springboot.gabombackend.owneranalysis.dto.SentimentAnalysisDto;
import com.springboot.gabombackend.owneranalysis.entity.CompetitionResultEntity;
import com.springboot.gabombackend.owneranalysis.entity.SentimentResultEntity;
import com.springboot.gabombackend.owneranalysis.repository.CompetitionResultRepository;
import com.springboot.gabombackend.owneranalysis.repository.SentimentResultRepository;
import com.springboot.gabombackend.owneranalysis.service.CompetitionAnalysisService;
import com.springboot.gabombackend.owneranalysis.service.SentimentAnalysisService;
import com.springboot.gabombackend.store.repository.StoreRepository;
import com.springboot.gabombackend.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/owners/{storeId}/analysis")
@RequiredArgsConstructor
public class OwnerAnalysisController {

    private final CompetitionAnalysisService competitionService;
    private final SentimentAnalysisService sentimentService;
    private final CompetitionResultRepository competitionRepo;
    private final SentimentResultRepository sentimentRepo;
    private final StoreRepository storeRepo;

    // 레이더 차트: 경쟁 가게 분석
    @GetMapping("/competition")
    public ResponseEntity<?> getCompetition(@PathVariable Long storeId,
                                            @RequestParam(defaultValue = "false") boolean refresh) throws Exception {
        if (refresh) {
            CompetitionAnalysisDto dto = competitionService.analyze(storeId);
            return ResponseEntity.ok(dto);
        }
        Optional<CompetitionResultEntity> opt = competitionRepo.findByStoreId(storeId);
        if (opt.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        Store store = storeRepo.findById(storeId).orElse(null);
        CompetitionResultEntity e = opt.get();
        CompetitionAnalysisDto dto = CompetitionAnalysisDto.builder()
                .storeId(storeId)
                .storeName(store != null ? store.getName() : "")
                .category(store != null ? store.getCategory() : "")
                .myPrice(e.getMyPrice())
                .myQuantity(e.getMyQuantity())
                .myTaste(e.getMyTaste())
                .myService(e.getMyService())
                .myClean(e.getMyClean())
                .compPrice(e.getCompPrice())
                .compQuantity(e.getCompQuantity())
                .compTaste(e.getCompTaste())
                .compService(e.getCompService())
                .compClean(e.getCompClean())
                .analyzedAt(e.getAnalyzedAt())
                .build();
        return ResponseEntity.ok(dto);
    }

    // 파이 차트: 긍/부정 분석
    @GetMapping("/sentiment")
    public ResponseEntity<?> getSentiment(@PathVariable Long storeId,
                                          @RequestParam(defaultValue = "false") boolean refresh) throws Exception {
        if (refresh) {
            SentimentAnalysisDto dto = sentimentService.analyze(storeId);
            return ResponseEntity.ok(dto);
        }
        Optional<SentimentResultEntity> opt = sentimentRepo.findByStoreId(storeId);
        if (opt.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        Store store = storeRepo.findById(storeId).orElse(null);
        SentimentResultEntity e = opt.get();
        SentimentAnalysisDto dto = SentimentAnalysisDto.builder()
                .storeId(storeId)
                .storeName(store != null ? store.getName() : "")
                .reviewCount(e.getReviewCount())
                .positive(e.getPositive())
                .negative(e.getNegative())
                .analyzedAt(e.getAnalyzedAt())
                .build();
        return ResponseEntity.ok(dto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
