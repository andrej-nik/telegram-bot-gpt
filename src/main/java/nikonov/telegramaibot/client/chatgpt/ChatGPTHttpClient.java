package nikonov.telegramaibot.client.chatgpt;

import nikonov.telegramaibot.client.chatgpt.configuration.ChatGPTHttpClientConfiguration;
import nikonov.telegramaibot.client.chatgpt.dto.ChatGPTRequest;
import nikonov.telegramaibot.client.chatgpt.dto.ChatGPTResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Http клиент к ChatGPT
 */
@FeignClient(
        name = "chatgpt-client", 
        url = "${application.chat-gpt.url}", 
        configuration = ChatGPTHttpClientConfiguration.class)
public interface ChatGPTHttpClient {

    @PostMapping(
            value = "/v1/completions", 
            produces = APPLICATION_JSON_VALUE, 
            consumes = APPLICATION_JSON_VALUE)
    Optional<ChatGPTResponse> getChatGPTResponse(@RequestBody ChatGPTRequest request);
}
