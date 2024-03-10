package nikonov.telegramgptbot.external.gpt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Fallback фабрика к клиенту GPT
 * 
 * @author Andrej Nikonov
 */
@Slf4j
@Component
public class GPTHttpClientFallbackFactory implements FallbackFactory<GPTHttpClient> {

    @Override 
    public GPTHttpClient create(Throwable exp) {

        return request -> {

            log.error("Ошибка запроса к GPT", exp);
            return Optional.empty();
        };
    }
}
