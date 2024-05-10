package nikonov.telegramaibot.service;

import nikonov.telegramaibot.domain.AIResponse;

/**
 * Интерфейс ai сервиса
 */
public interface AIService {

    /**
     * Получить ответ ai
     * 
     * @param prompt промпт
     */
    AIResponse getAIResponse(String prompt);
}
