package com.example.reviewanalyzer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OpenRouterService {
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${openrouter.api.key}")
    private String apiKey;

    public String analyzeReview(String reviewText) throws IOException {
        String json = String.format("""
        {
            "model": "deepseek/deepseek-chat-v3-0324:free",
            "messages": [
                {
                    "role": "system",
                    "content": "Анализируй отзывы. Формат:\\n1. Тональность\\n2. Плюсы\\n3. Минусы\\n4. Рекомендации"
                },
                {
                    "role": "user",
                    "content": "%s"
                }
            ],
            "temperature": 0.7,
            "max_tokens": 500
        }
        """, reviewText.replace("\"", "\\\""));

        Request request = new Request.Builder()
                .url("https://openrouter.ai/api/v1/chat/completions")
                .post(RequestBody.create(json, MediaType.get("application/json")))
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("API Error: " + response.code());

            JsonNode rootNode = mapper.readTree(response.body().string());
            return rootNode.path("choices").get(0).path("message").path("content").asText();
        }
    }
}
