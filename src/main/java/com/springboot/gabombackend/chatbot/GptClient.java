package com.springboot.gabombackend.chatbot;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GptClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openai.api.key}")
    private String OPENAI_API_KEY;

    private final String GPT_URL = "https://api.openai.com/v1/chat/completions";

    /**
     * 사용자 질문 의도를 GPT로 분류
     */
    public String classifyIntent(String userMessage) {
        String systemPrompt = """
            너는 가봄 서비스의 AI 챗봇이다.
            사용자의 질문을 반드시 다음 4가지 의도 중 하나로 분류해라:
            1. RECOMMEND_STORE → 음식점 추천/존재 여부를 묻는 경우
            2. USER_STAMP_TOTAL → 내가 가진 총 스탬프 개수를 묻는 경우
            3. USER_STAMPS_BY_CATEGORY → 내가 가진 스탬프 종류별 현황을 묻는 경우
            4. STAMP_CATEGORIES_ALL → 전체 스탬프 종류를 묻는 경우

            반드시 위 4개 중 하나만 출력해.
        """;

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

    /**
     * 최종 답변 생성 (DB 결과만 기반)
     */
    public String generateReply(Long userId, String userMessage, String dbResult) {

        String systemPrompt = """
            너는 가봄 서비스의 AI 챗봇이다.
            - 반드시 DB 조회 결과만 사용해서 답한다.
            - DB에 없는 정보는 절대 지어내지 않는다.
            - DB 결과를 변형하지 않고 그대로 활용해서 짧고 친근하게 대답한다.
            - DB 결과가 부적절하거나 부족하면 "추천할 매장이 없어요 😅" 또는 
              "저는 맛집/스탬프 도우미예요 😊" 라고만 답한다.
        """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(OPENAI_API_KEY);

        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", systemPrompt));
        messages.add(Map.of("role", "system", "content", "DB 조회 결과:\n" + dbResult));
        messages.add(Map.of("role", "user", "content", userMessage));

        Map<String, Object> body = Map.of(
                "model", "gpt-4o-mini",
                "messages", messages
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        Map<String, Object> response = restTemplate.postForObject(GPT_URL, entity, Map.class);

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

        return message.get("content").toString().trim();
    }
}
