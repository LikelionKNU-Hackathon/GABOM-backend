package com.springboot.gabombackend.owneranalysis.scheduler;

import com.springboot.gabombackend.owneranalysis.service.CompetitionAnalysisService;
import com.springboot.gabombackend.owneranalysis.service.SentimentAnalysisService;
import com.springboot.gabombackend.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnalysisScheduler {

    private final CompetitionAnalysisService competitionService;
    private final SentimentAnalysisService sentimentService;
    private final StoreRepository storeRepo;

    // 2주(14일)에 한 번, 새벽 3시에 실행
    @Scheduled(cron = "0 0 3 */14 * *")
    public void runAnalysisEveryTwoWeeks() {
        storeRepo.findAll().forEach(store -> {
            try {
                competitionService.analyze(store.getId());
                sentimentService.analyze(store.getId());
                System.out.println("✅ 분석 완료: storeId=" + store.getId());
            } catch (Exception e) {
                System.err.println("❌ 분석 실패: storeId=" + store.getId() + ", error=" + e.getMessage());
            }
        });
    }
}
