package com.springboot.gabombackend.chatbot;

import com.springboot.gabombackend.chatbot.dto.ChatRequest;
import com.springboot.gabombackend.chatbot.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    @Value("${openai.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private final RestTemplate restTemplate = new RestTemplate();

    public String askToGpt(String userMessage) {
        // 1. 시스템 프롬프트
        ChatRequest.Message systemMessage = new ChatRequest.Message(
                "system",
                "너는 가봄 서비스의 AI 챗봇이다. 음식점 추천, 스탬프/티어 안내만 대답하고, " +
                        "다른 질문엔 '저는 맛집/스탬프 도우미예요 😊' 라고 답해라."
        );

        // 2. 사용자 입력
        ChatRequest.Message userMsg = new ChatRequest.Message("user", userMessage);

        // 3. 요청 DTO
        ChatRequest request = new ChatRequest(
                "gpt-4o-mini",   // 모델명 (속도/비용 고려)
                List.of(systemMessage, userMsg)
        );

        // 4. HTTP 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // 5. 요청 보내기
        HttpEntity<ChatRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<ChatResponse> response = restTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                entity,
                ChatResponse.class
        );

        // 6. 결과 반환
        if (response.getBody() != null && !response.getBody().getChoices().isEmpty()) {
            return response.getBody().getChoices().get(0).getMessage().getContent();
        }
        return "죄송합니다. 응답을 가져올 수 없습니다.";
    }
}