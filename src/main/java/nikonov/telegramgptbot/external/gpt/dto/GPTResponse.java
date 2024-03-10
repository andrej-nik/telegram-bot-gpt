package nikonov.telegramgptbot.external.gpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Ответ от GPT
 * https://platform.openai.com/docs/api-reference/completions/create
 * 
 * @author Andrej Nikonov
 */
@Data
public class GPTResponse {

    @JsonProperty("model")
    private String model;
    
    @JsonProperty("choices")
    private List<GPTChoices> choices;
}
