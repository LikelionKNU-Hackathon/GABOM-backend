package com.springboot.gabombackend.chatbot.controller;

import com.springboot.gabombackend.chatbot.service.ChatbotService;
import com.springboot.gabombackend.user.entity.User;
import com.springboot.gabombackend.user.service.UserService;
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
    private final UserService userService;

    @PostMapping
    public Map<String, String> chat(@RequestBody Map<String, Object> request) {
        // 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName(); // principal = loginId

        // loginId 로 유저 조회 후 userId 가져오기
        User user = userService.getByLoginId(loginId);
        Long userId = user.getId();

        // 프론트에서 넘어온 message 꺼내기
        String userMessage = request.get("message").toString();

        // 챗봇 서비스 호출
        String gptResponse = chatbotService.getChatbotReply(userId, userMessage);

        // JSON 응답
        return Map.of("response", gptResponse);
    }
}
