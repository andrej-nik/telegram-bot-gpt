package nikonov.telegramaibot.domain.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application.http-proxy")
public class HttpProxyProperties {
    
    private Boolean enabled;
    private String host;
    private Integer port;
    private String user;
    private String password;
}
