package nikonov.telegramaibot.domain.properties;

import lombok.Data;
import nikonov.telegramaibot.domain.AIModelType;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application.ai-model")
public class AIModelProperties {

    private AIModelType type;
}
