package com.springboot.gabombackend.chatbot;

import com.springboot.gabombackend.store.StoreRepository;
import com.springboot.gabombackend.stamp.UserStampRepository;
import com.springboot.gabombackend.stamp.dto.UserStampResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final StoreRepository storeRepository;
    private final UserStampRepository userStampRepository;
    private final GptClient gptClient;

    // ✅ 카테고리 추출 함수
    private String extractCategory(String message) {
        List<String> categories = List.of("분식", "한식", "중식", "일식", "양식", "디저트", "요리주점", "오락", "편의시설", "아시안");
        return categories.stream()
                .filter(message::contains) // message 안에 카테고리 단어가 포함돼 있으면 매칭
                .findFirst()
                .orElse(null);
    }

    public String getChatbotReply(Long userId, String userMessage) {
        // 1. GPT로 intent 분류
        String intent = gptClient.classifyIntent(userMessage);

        String dbResult = null;

        // 2. 음식점 추천
        if ("RECOMMEND_STORE".equalsIgnoreCase(intent)) {
            // 입력에서 카테고리 추출
            String category = extractCategory(userMessage);

            var store = (category != null)
                    ? storeRepository.findRandomByCategory(category).orElse(null)
                    : storeRepository.findRandom().orElse(null);

            if (store != null) {
                dbResult = store.getName() + " (" + store.getCategory() + "), "
                        + store.getOpeningHours() + ", "
                        + store.getAddress();
            }
        }

        // 3. 스탬프 총 개수
        else if ("USER_STAMP_TOTAL".equalsIgnoreCase(intent) && userId != null) {
            int stampCount = userStampRepository.sumStampCountByUser(userId);
            dbResult = "현재 보유 스탬프는 총 " + stampCount + "개입니다.";
        }

        // 4. 스탬프 종류별 현황
        else if ("USER_STAMPS_BY_CATEGORY".equalsIgnoreCase(intent) && userId != null) {
            List<UserStampResponse> stamps = userStampRepository.findUserStampsWithSum(userId);
            if (!stamps.isEmpty()) {
                StringBuilder sb = new StringBuilder("보유 스탬프 현황:\n");
                for (UserStampResponse s : stamps) {
                    sb.append("- ").append(s.getName())
                            .append(" (").append(s.getCount()).append("개)\n");
                }
                dbResult = sb.toString();
            }
        }

        // 5. 전체 스탬프 종류 안내 (고정)
        else if ("STAMP_CATEGORIES_ALL".equalsIgnoreCase(intent)) {
            dbResult = "스탬프 종류는 분식, 한식, 중식, 일식, 양식, 디저트, 요리주점, 오락, 편의시설, 아시안 이렇게 10가지가 있어요!";
        }

        // 6. DB 결과 없으면 GPT 호출하지 않고 fallback
        if (dbResult == null || dbResult.isBlank()) {
            return "추천할 매장이 없어요 😅 (저는 DB 기반으로만 답해요)";
        }

        // 7. GPT 최종 답변 생성
        return gptClient.generateReply(userId, userMessage, dbResult);
    }
}
