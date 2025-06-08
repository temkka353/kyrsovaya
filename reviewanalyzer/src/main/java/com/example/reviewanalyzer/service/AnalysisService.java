package com.example.reviewanalyzer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final OpenRouterService openRouterService;

    public String analyzeText(String text) throws IOException {
        if (text.length() < 10) {
            throw new IllegalArgumentException("Отзыв слишком короткий");
        }
        return openRouterService.analyzeReview(text);
    }
}