package nikonov.telegramaibot.service;

import lombok.RequiredArgsConstructor;
import nikonov.telegramaibot.client.chatgpt.ChatGPTHttpClient;
import nikonov.telegramaibot.client.chatgpt.dto.ChatGPTChoices;
import nikonov.telegramaibot.client.chatgpt.dto.ChatGPTRequest;
import nikonov.telegramaibot.client.chatgpt.dto.ChatGPTResponse;
import nikonov.telegramaibot.domain.AIResponse;
import nikonov.telegramaibot.domain.AITextResponse;
import nikonov.telegramaibot.domain.properties.ChatGPTProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Optional;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Сервис chatGPT
 */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "application.chat-gpt", name = "enabled", havingValue = "true")
public class ChatGPTAIService implements AIService {
    
    private final ChatGPTProperties properties;
    private final ChatGPTHttpClient client;

    /**
     * Получить ответ на запрос от GPT
     * 
     * @param prompt запрос
     * @return ответ               
     */
    @Override
    public AIResponse getAIResponse(String prompt) {

        var response = client
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
        return new AITextResponse().setText(response);
    }
}
