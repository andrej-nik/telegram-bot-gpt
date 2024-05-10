package nikonov.telegramaibot.client.huggingface.configuration;

import lombok.RequiredArgsConstructor;
import nikonov.telegramaibot.client.interceptor.BearerRequestIdInterceptor;
import nikonov.telegramaibot.domain.properties.HuggingFaceProperties;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
public class HuggingFaceHttpClientConfiguration {

    private final HuggingFaceProperties properties;
    
    @Bean
    public BearerRequestIdInterceptor bearerRequestIdInterceptor() {

        return new BearerRequestIdInterceptor(properties.getToken());
    }
}
