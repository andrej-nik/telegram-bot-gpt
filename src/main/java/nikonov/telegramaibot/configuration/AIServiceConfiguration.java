package nikonov.telegramaibot.configuration;

import nikonov.telegramaibot.client.chatgpt.ChatGPTHttpClient;
import nikonov.telegramaibot.domain.properties.ChatGPTProperties;
import nikonov.telegramaibot.service.AIService;
import nikonov.telegramaibot.service.ChatGPTService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIServiceConfiguration {
    
    @Bean
    @ConditionalOnProperty(prefix = "application.chat-gpt", name = "enabled", havingValue = "true")
    public AIService chatGPTService(ChatGPTHttpClient client, ChatGPTProperties properties) {
        
        return new ChatGPTService(properties, client);
    }
}
