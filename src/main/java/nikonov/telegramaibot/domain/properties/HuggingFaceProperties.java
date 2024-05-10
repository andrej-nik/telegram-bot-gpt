package nikonov.telegramaibot.domain.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application.hugging-face")
public class HuggingFaceProperties {

    private Boolean enabled;
    private String url;
    private String token;
}
