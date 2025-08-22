package com.springboot.gabombackend.chatbot.client;

import java.util.*;

public class ChatMemory {
    // 유저 ID별 대화 기록 저장
    private static final Map<Long, List<Map<String, String>>> memory = new HashMap<>();

    public static void addMessage(Long userId, String role, String content) {
        memory.computeIfAbsent(userId, k -> new ArrayList<>())
                .add(Map.of("role", role, "content", content));
    }

    public static List<Map<String, String>> getMessages(Long userId) {
        return memory.getOrDefault(userId, new ArrayList<>());
    }

    public static void clear(Long userId) {
        memory.remove(userId);
    }
}