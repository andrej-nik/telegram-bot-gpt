package nikonov.telegramaibot.client.chatgpt.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * Конфигурация http клиента GPT
 */
@RequiredArgsConstructor
public class ChatGPTHttpClientConfiguration {

    @Value("${application.chat-gpt.token}")
    private String gptToken;
    
    @Bean
    public BearerRequestIdInterceptor bearerRequestIdInterceptor() {
        
        return new BearerRequestIdInterceptor(gptToken);
    }
}
