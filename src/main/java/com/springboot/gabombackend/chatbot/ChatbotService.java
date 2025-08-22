package com.springboot.gabombackend.chatbot;

import com.springboot.gabombackend.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final StoreRepository storeRepository;
    private final GptClient gptClient;

    // 세션별 대화 기록 저장 (메모리)
    private final Map<String, List<Map<String, String>>> sessionHistories = new HashMap<>();

    // 고정된 10개 카테고리
    private static final List<String> CATEGORIES = List.of(
            "분식", "한식", "중식", "일식", "양식",
            "디저트", "요리주점", "오락", "편의시설", "아시안"
    );

    // fallback 메시지 후보 (랜덤 출력)
    private static final List<String> FALLBACK_MESSAGES = List.of(
            "저는 맛집 추천만 도와드릴 수 있어요 😊 지금은 분식, 한식, 중식, 일식, 양식, 디저트, 요리주점, 오락, 편의시설, 아시안 중에서 고르실 수 있습니다!",
            "제가 해드릴 수 있는 건 맛집 추천이에요 🍴 원하는 카테고리를 알려주시면 근처 가게를 소개해드릴게요!",
            "혹시 맛집 찾으시는 거 맞나요? 😅 저는 다른 건 못하지만, 10가지 카테고리 안에서 좋은 가게를 골라드릴 수 있어요.",
            "가봄 챗봇은 음식점 추천 전용이에요! 📍 카테고리는 분식, 한식, 중식, 일식, 양식, 디저트, 요리주점, 오락, 편의시설, 아시안입니다.",
            "음… 그건 잘 모르겠어요 😅 대신 제가 도와드릴 수 있는 건 강남대 근처 맛집 추천이에요. 카테고리를 말씀해주시면 소개해드릴게요!"
    );

    // 메시지에서 카테고리 추출
    private String extractCategory(String message) {
        return CATEGORIES.stream()
                .filter(message::contains)
                .findFirst()
                .orElse(null);
    }

    public String getChatbotReply(String sessionId, Long userId, String userMessage) {
        // 세션별 history 가져오기
        List<Map<String, String>> history = sessionHistories.computeIfAbsent(sessionId, k -> new ArrayList<>());

        // GPT로 intent 분류
        String intent = gptClient.classifyIntent(userMessage);
        String dbResult = null;

        // 음식점 추천
        if ("RECOMMEND_STORE".equalsIgnoreCase(intent)) {
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
        // 전체 카테고리 안내
        else if ("STAMP_CATEGORIES_ALL".equalsIgnoreCase(intent)) {
            dbResult = "제가 추천할 수 있는 카테고리는 "
                    + String.join(", ", CATEGORIES)
                    + " 이렇게 10가지예요!";
        }
        // 스몰토크 처리 → 별도 메서드로 (히스토리 기록 안 해도 됨)
        else if ("SMALL_TALK".equalsIgnoreCase(intent)) {
            String reply = gptClient.generateSmallTalk(userId, userMessage);
            history.add(Map.of("role", "user", "content", userMessage));
            history.add(Map.of("role", "assistant", "content", reply));
            return reply;
        }

        // DB 결과 없으면 fallback
        if (dbResult == null || dbResult.isBlank()) {
            String fallback = FALLBACK_MESSAGES.get((int) (Math.random() * FALLBACK_MESSAGES.size()));
            history.add(Map.of("role", "user", "content", userMessage));
            history.add(Map.of("role", "assistant", "content", fallback));
            return fallback;
        }

        // 히스토리 업데이트 (DB 결과도 context에 포함)
        history.add(Map.of("role", "user", "content", userMessage));
        history.add(Map.of("role", "system", "content", "DB 조회 결과:\n" + dbResult));

        // GPT 호출 (히스토리 전체 전달)
        String reply = gptClient.callWithHistory(history);

        // 응답 기록
        history.add(Map.of("role", "assistant", "content", reply));

        return reply;
    }
}
