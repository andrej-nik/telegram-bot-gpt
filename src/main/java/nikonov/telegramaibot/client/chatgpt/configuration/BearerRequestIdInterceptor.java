package nikonov.telegramaibot.client.chatgpt.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Интерсептор, добавляющий токен авторизаций GPT
 */
@Slf4j
@RequiredArgsConstructor
public class BearerRequestIdInterceptor implements RequestInterceptor {

    private final String gptToken;
    
    @Override 
    public void apply(RequestTemplate template) {
        
        template.header("Authorization", "Bearer %s".formatted(gptToken));
    }
}
