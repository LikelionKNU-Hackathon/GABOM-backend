package com.springboot.gabombackend.chatbot;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GptClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openai.api.key}")
    private String OPENAI_API_KEY;

    private final String GPT_URL = "https://api.openai.com/v1/chat/completions";

    public String classifyIntent(String userMessage) {
        String systemPrompt = """
            너는 가봄 서비스의 AI 챗봇이다.
            사용자의 질문을 반드시 다음 3가지 의도 중 하나로 분류해라:
            1. RECOMMEND_STORE → 10개 카테고리 중 하나의 맛집 추천을 원할 때
            2. STAMP_CATEGORIES_ALL → 전체 카테고리를 물어볼 때
            3. SMALL_TALK → 인사, 감사, 잡담 등 단순 대화일 때

            반드시 위 3개 중 하나만 출력해.
        """;

        return callGpt(systemPrompt, userMessage);
    }

    public String generateReply(Long userId, String userMessage, String dbResult) {
        String systemPrompt = """
            너는 가봄 서비스의 AI 챗봇이다.
            - 기본적으로는 DB 조회 결과만 사용해서 짧고 친근하게 대답한다.
            - 대화를 너무 길게 끌지 말고, 음식점 추천이나 10개 카테고리 안내로 유도한다.
            - DB 결과가 없거나 부적절하면 
              "추천할 수 있는 카테고리는 10가지뿐이에요 😅" 또는
              "저는 맛집 추천만 도와드려요 😊" 라고만 답한다.
        """;

        String dbInfo = (dbResult == null || dbResult.isBlank())
                ? "DB 조회 결과가 없음"
                : "DB 조회 결과:\n" + dbResult;

        return callGpt(systemPrompt, dbInfo + "\n\n사용자 질문: " + userMessage);
    }

    public String generateSmallTalk(Long userId, String userMessage) {
        String systemPrompt = """
            너는 가봄 서비스의 AI 챗봇이다.
            지금은 음식점 추천이 아니라, 가벼운 잡담 상황이다.
            - 인사, 감사, 날씨, 기분 같은 일상 대화를 짧고 자연스럽게 이어가라.
            - 답변은 1~2문장으로 짧게.
            - 음식점 추천이나 카테고리로 억지로 유도하지 않는다.
        """;

        return callGpt(systemPrompt, userMessage);
    }

    // 히스토리 기반 호출
    public String callWithHistory(List<Map<String, String>> history) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(OPENAI_API_KEY);

        Map<String, Object> body = Map.of(
                "model", "gpt-4o-mini",
                "messages", history
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        Map<String, Object> response = restTemplate.postForObject(GPT_URL, entity, Map.class);

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

        return message.get("content").toString().trim();
    }

    // 기존 단일 호출
    private String callGpt(String systemPrompt, String userMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(OPENAI_API_KEY);

        Map<String, Object> body = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userMessage)
                )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        Map<String, Object> response = restTemplate.postForObject(GPT_URL, entity, Map.class);

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

        return message.get("content").toString().trim();
    }
}