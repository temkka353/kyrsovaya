package com.example.reviewanalyzer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class TelegramBotService {

    private final AnalysisService analysisService;

    public SendMessage handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if (messageText.startsWith("/start")) {
                return createWelcomeMessage(chatId);
            } else {
                return analyzeReviewMessage(chatId, messageText);
            }
        }
        return null;
    }

    private SendMessage createWelcomeMessage(Long chatId) {
        String welcomeText = """
                Привет! Я бот для анализа отзывов на товары. 🛍️📊
                
                Просто отправь мне текст отзыва, и я проанализирую его:
                - Определю общий тон отзыва
                - Выделю основные достоинства и недостатки
                - Дам рекомендации (если возможно)
                
                Попробуй отправить мне отзыв прямо сейчас!""";

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(welcomeText);
        return message;
    }

    private SendMessage analyzeReviewMessage(Long chatId, String reviewText) {
        try {
            String analysisResult = analysisService.analyzeText(reviewText);

            SendMessage message = new SendMessage();
            message.setChatId(chatId.toString());
            message.setText("🔍 Результат анализа:\n\n" + analysisResult); // Добавил заголовок
            return message;
        } catch (Exception e) {
            SendMessage errorMessage = new SendMessage();
            errorMessage.setChatId(chatId.toString());
            errorMessage.setText("⚠️ Произошла ошибка при анализе отзыва. Пожалуйста, попробуйте позже.");
            return errorMessage;
        }
    }
}