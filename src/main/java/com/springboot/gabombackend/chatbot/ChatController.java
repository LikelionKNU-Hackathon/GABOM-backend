package com.springboot.gabombackend.chatbot;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        String userMessage = request.get("message").toString();

        String gptResponse = chatbotService.getChatbotReply(userId, userMessage);
        return Map.of("response", gptResponse);
    }
}