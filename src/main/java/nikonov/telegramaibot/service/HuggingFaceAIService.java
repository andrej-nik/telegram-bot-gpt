package nikonov.telegramaibot.service;

import lombok.RequiredArgsConstructor;
import nikonov.telegramaibot.client.huggingface.HuggingFaceHttpClient;
import nikonov.telegramaibot.client.huggingface.dto.HuggingFaceRequest;
import nikonov.telegramaibot.domain.AIImageResponse;
import nikonov.telegramaibot.domain.AIResponse;
import nikonov.telegramaibot.domain.AITextResponse;
import nikonov.telegramaibot.domain.properties.AIModelProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import static nikonov.telegramaibot.domain.AIModelType.IMAGE;
import static nikonov.telegramaibot.domain.AIModelType.TEXT;

/**
 * Сервис к HuggingFaceApi
 */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "application.hugging-face", name = "enabled", havingValue = "true")
public class HuggingFaceAIService implements AIService {
    
    private final HuggingFaceHttpClient client;
    private final AIModelProperties modelProperties;
    
    @Override 
    public AIResponse getAIResponse(String prompt) {
        
        var request = new HuggingFaceRequest().setPrompt(prompt);
        
        if (modelProperties.getType() == IMAGE) {
            var image = client.getAIImage(request).getBody().getByteArray();
            return new AIImageResponse().setPayload(image);
        } else if (modelProperties.getType() == TEXT) {
            var text = client.getAIText(request).getBody().get(0).getGeneratedText();
            return new AITextResponse().setText(text);
        } else {
            throw new UnsupportedOperationException("unsupported model type %s".formatted(modelProperties.getType()));
        }
    }
}
