package com.example.reviewanalyzer.controller;

import com.example.reviewanalyzer.service.AnalysisService;
import com.example.reviewanalyzer.service.TelegramBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class TelegramBotController extends TelegramLongPollingBot {
    private final AnalysisService analysisService;
    private final TelegramBotService telegramBotService;
    private final String botToken;
    private final String botUsername;

    public TelegramBotController(
            AnalysisService analysisService,
            TelegramBotService telegramBotService,
            @Qualifier("botToken") String botToken,
            @Qualifier("botUsername") String botUsername
    ) {
        this.analysisService = analysisService;
        this.telegramBotService = telegramBotService;
        this.botToken = botToken;
        this.botUsername = botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        Long chatId = update.getMessage().getChatId();

        try {
            SendMessage message = telegramBotService.handleUpdate(update);
            if (message != null) {
                execute(message);
            }
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения", e);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}