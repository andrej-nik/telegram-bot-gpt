package nikonov.telegramaibot.service;

import lombok.RequiredArgsConstructor;
import nikonov.telegramaibot.client.chatgpt.ChatGPTHttpClient;
import nikonov.telegramaibot.client.chatgpt.dto.ChatGPTChoices;
import nikonov.telegramaibot.client.chatgpt.dto.ChatGPTRequest;
import nikonov.telegramaibot.client.chatgpt.dto.ChatGPTResponse;
import nikonov.telegramaibot.domain.properties.ChatGPTProperties;

import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Сервис chatGPT
 */
@RequiredArgsConstructor
public class ChatGPTService implements AIService {
    
    private final ChatGPTProperties properties;
    
    private final ChatGPTHttpClient client;

    /**
     * Получить ответ на запрос от GPT
     * 
     * @param prompt запрос
     * @return ответ               
     */
    @Override
    public String getAIResponse(String prompt) {

        return client
                .getChatGPTResponse(
                        new ChatGPTRequest()
                                .setModel(properties.getModel())
                                .setPrompt(prompt)
                                .setMaxTokens(properties.getMaxTokens())
                                .setTemperature(properties.getTemperature()))
                .map(ChatGPTResponse::getChoices)
                .flatMap(choices ->
                        isEmpty(choices) ? 
                                Optional.empty() : 
                                Optional.of(choices.get(0))).map(ChatGPTChoices::getText)
                .orElseThrow();
    }
}
