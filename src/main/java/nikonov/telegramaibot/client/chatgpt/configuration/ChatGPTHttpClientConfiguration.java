package nikonov.telegramaibot.client.chatgpt.configuration;

import lombok.RequiredArgsConstructor;
import nikonov.telegramaibot.client.interceptor.BearerRequestIdInterceptor;
import nikonov.telegramaibot.domain.properties.ChatGPTProperties;
import org.springframework.context.annotation.Bean;

/**
 * Конфигурация http клиента GPT
 */
@RequiredArgsConstructor
public class ChatGPTHttpClientConfiguration {

    private final ChatGPTProperties properties;
    
    @Bean
    public BearerRequestIdInterceptor bearerRequestIdInterceptor() {
        
        return new BearerRequestIdInterceptor(properties.getToken());
    }
}
