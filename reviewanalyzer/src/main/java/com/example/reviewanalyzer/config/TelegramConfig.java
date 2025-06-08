package com.example.reviewanalyzer.config;

import com.example.reviewanalyzer.controller.TelegramBotController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramConfig {
    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;
    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramBotController botController) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(botController);
        return botsApi;
    }
    @Bean
    @Qualifier("botToken")
    public String getBotToken() {
        return botToken;
    }
    @Bean
    @Qualifier("botUsername")
    public String getBotUsername() {
        return botUsername;
    }
}