package nikonov.telegramaibot.client.huggingface.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HuggingFaceResponse {

    @JsonProperty("generated_text")
    private String generatedText;
}
