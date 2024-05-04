package nikonov.telegramaibot.domain.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application.chat-gpt")
public class ChatGPTProperties {

    private Boolean enabled;
    private String url;
    private String token;
    private String model;
    private Integer maxTokens;
    private Double temperature;
}
