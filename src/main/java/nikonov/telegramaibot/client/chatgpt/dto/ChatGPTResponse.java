package nikonov.telegramaibot.client.chatgpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Ответ от GPT
 * https://platform.openai.com/docs/api-reference/completions/create
 */
@Data
public class ChatGPTResponse {

    @JsonProperty("model")
    private String model;
    
    @JsonProperty("choices")
    private List<ChatGPTChoices> choices;
}
