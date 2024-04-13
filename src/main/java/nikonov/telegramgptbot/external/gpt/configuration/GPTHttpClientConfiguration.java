package nikonov.telegramgptbot.external.gpt.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * Конфигурация http клиента GPT
 */
@RequiredArgsConstructor
public class GPTHttpClientConfiguration {

    @Value("${application.gpt.token}")
    private String gptToken;
    
    @Bean
    public BearerRequestIdInterceptor bearerRequestIdInterceptor() {
        
        return new BearerRequestIdInterceptor(gptToken);
    }
}
