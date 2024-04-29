package nikonov.telegramgptbot.domain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application.http-proxy")
public class HttpProxySettings {
    
    private Boolean enabled;
    private String host;
    private Integer port;
    private String user;
    private String password;
}
