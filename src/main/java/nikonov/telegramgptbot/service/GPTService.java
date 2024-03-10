package nikonov.telegramgptbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nikonov.telegramgptbot.external.gpt.GPTHttpClient;
import nikonov.telegramgptbot.external.gpt.dto.GPTChoices;
import nikonov.telegramgptbot.external.gpt.dto.GPTRequest;
import nikonov.telegramgptbot.external.gpt.dto.GPTResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static nikonov.telegramgptbot.utils.MessageKeys.ERROR_MESSAGE_KEY;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Сервис для работы с GPT
 * 
 * @author Andrej Nikonov
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
    private final int temperature;
    
    private final GPTHttpClient gptHttpClient;
    
    private final MessageSource messageSource;

    /**
     * Получить ответ на запрос от GPT
     * 
     * @param request запрос
     * @return ответ               
     */
    public String getGPTResponse(String request) {
        
        try {
            return gptHttpClient
                    .getGPTResponse(
                            new GPTRequest()
                                    .setModel(gptModel)
                                    .setPrompt(request)
                                    .setMaxTokens(maxTokens)
                                    .setTemperature(temperature))
                    .map(GPTResponse::getChoices)
                    .flatMap(choices -> 
                            isEmpty(choices) ? Optional.empty() : Optional.of(choices.get(0))).map(GPTChoices::getText)
                    .orElse(getMessage(ERROR_MESSAGE_KEY));
        } catch (Exception exp) {
            
            log.error("При запросе к GPR произошла ошибка", exp);
            return getMessage(ERROR_MESSAGE_KEY);
        }
    }
    
    private String getMessage(String key) {
        
        return messageSource.getMessage(key, null, null);
    }
}
