package nikonov.telegramaibot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nikonov.telegramaibot.domain.properties.HttpProxyProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

import static org.springframework.util.StringUtils.hasText;

/**
 * Компонент, настраивающий http-proxy для ВСЕХ вызовов
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HttpProxySettingsInitializer {

    private static final String HTTPS_HOST = "https.proxyHost";
    private static final String HTTPS_PORT = "https.proxyPort";
    private static final String HTTP_HOST = "http.proxyHost";
    private static final String HTTP_PORT = "http.proxyPort";
    private static final String DISABLED_SCHEMES = "jdk.http.auth.tunneling.disabledSchemes";
    
    private final HttpProxyProperties settings;
    
    @PostConstruct
    public void init() {
        
        if (Boolean.TRUE.equals(settings.getEnabled())) {
            log.info("Application use http proxy. Host: {}, port: {}", settings.getHost(), settings.getPort());
            System.setProperty(HTTPS_HOST, settings.getHost());
            System.setProperty(HTTPS_PORT, String.valueOf(settings.getPort()));
            System.setProperty(HTTP_HOST, settings.getHost());
            System.setProperty(HTTP_PORT, String.valueOf(settings.getPort()));
            if (hasText(settings.getUser()) && hasText(settings.getPassword())) {
                // нужно для того чтобы проити аутентификацию на proxy
                // https://stackoverflow.com/questions/1626549/authenticated-http-proxy-with-java
                Authenticator.setDefault(
                        new Authenticator() {
                            @Override
                            public PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(
                                        settings.getUser(),
                                        settings.getPassword().toCharArray());
                            }
                        }
                );
                System.setProperty(DISABLED_SCHEMES, "");
            }
        }
    }
}
