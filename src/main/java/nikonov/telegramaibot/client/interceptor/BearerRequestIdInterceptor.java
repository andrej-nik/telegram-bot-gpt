package nikonov.telegramaibot.client.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Интерсептор, добавляющий токен авторизаций
 */
@Slf4j
@RequiredArgsConstructor
public class BearerRequestIdInterceptor implements RequestInterceptor {

    private final String token;

    @Override
    public void apply(RequestTemplate template) {

        template.header("Authorization", "Bearer %s".formatted(token));
    }
}
