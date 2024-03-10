package nikonov.telegramgptbot.external.gpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Запрос к GPT
 * https://platform.openai.com/docs/api-reference/completions/create
 * 
 * @author Andrej Nikonov
 */
@Data
public class GPTRequest {
    
    @JsonProperty("model")
    private String model;
    
    @JsonProperty("prompt")
    private String prompt;
    
    @JsonProperty("max_tokens")
    private int maxTokens;
    
    @JsonProperty("temperature")
    private int temperature;
}
