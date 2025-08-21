package com.springboot.gabombackend.chatbot;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatbotService chatbotService;

    @PostMapping
    public Map<String, String> chat(@RequestBody Map<String, Object> request) {
        // 프론트에서 userId, message 함께 전송해야 함
        Long userId = Long.valueOf(request.get("userId").toString());
        String userMessage = request.get("message").toString();

        String gptResponse = chatbotService.getChatbotReply(userId, userMessage);
        return Map.of("response", gptResponse);
    }
}
