package com.springboot.gabombackend.chatbot.controller;

import com.springboot.gabombackend.chatbot.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatbotService chatbotService;

    @PostMapping
    public Map<String, String> chat(@RequestBody Map<String, Object> request) {
        // 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // principal 은 기본적으로 String (username) 또는 UserDetails 객체
        String principal = authentication.getPrincipal().toString();

        Long userId;
        try {
            // principal 이 userId(String) 라면 Long 변환
            userId = Long.valueOf(principal);
        } catch (NumberFormatException e) {
            // userId가 숫자가 아닐 경우 (ex. username/email일 경우) → 예외 처리
            throw new IllegalArgumentException("JWT principal 값이 숫자(Long)가 아닙니다: " + principal);
        }

        // 프론트에서 넘어온 message 꺼내기
        String userMessage = request.get("message").toString();

        // 챗봇 서비스 호출
        String gptResponse = chatbotService.getChatbotReply(userId, userMessage);

        // JSON 응답
        return Map.of("response", gptResponse);
    }
}
