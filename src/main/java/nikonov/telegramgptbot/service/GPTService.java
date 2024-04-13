package nikonov.telegramgptbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nikonov.telegramgptbot.external.gpt.GPTHttpClient;
import nikonov.telegramgptbot.external.gpt.dto.GPTChoices;
import nikonov.telegramgptbot.external.gpt.dto.GPTRequest;
import nikonov.telegramgptbot.external.gpt.dto.GPTResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Сервис для работы с GPT
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GPTService {
    
    @Value("${application.gpt.model}")
    private final String gptModel;
    
    @Value("${application.gpt.maxTokens}")
    private final int maxTokens;
    
    @Value("${application.gpt.temperature}")
    private final double temperature;
    
    private final GPTHttpClient gptHttpClient;

    /**
     * Получить ответ на запрос от GPT
     * 
     * @param prompt запрос
     * @return ответ               
     */
    public String getGPTResponse(String prompt) {

        return gptHttpClient
                .getGPTResponse(
                        new GPTRequest()
                                .setModel(gptModel)
                                .setPrompt(prompt)
                                .setMaxTokens(maxTokens)
                                .setTemperature(temperature))
                .map(GPTResponse::getChoices)
                .flatMap(choices ->
                        isEmpty(choices) ? Optional.empty() : Optional.of(choices.get(0))).map(GPTChoices::getText)
                .orElseThrow();
    }
}
