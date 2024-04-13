package nikonov.telegramgptbot.external.gpt;

import nikonov.telegramgptbot.external.gpt.configuration.GPTHttpClientConfiguration;
import nikonov.telegramgptbot.external.gpt.dto.GPTRequest;
import nikonov.telegramgptbot.external.gpt.dto.GPTResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Http клиент к GPT
 */
@FeignClient(
        name = "gpt-http-client", 
        url = "${application.gpt.url}", 
        configuration = GPTHttpClientConfiguration.class)
public interface GPTHttpClient {

    @PostMapping(
            value = "/v1/completions", 
            produces = APPLICATION_JSON_VALUE, 
            consumes = APPLICATION_JSON_VALUE)
    Optional<GPTResponse> getGPTResponse(@RequestBody GPTRequest request);
}
