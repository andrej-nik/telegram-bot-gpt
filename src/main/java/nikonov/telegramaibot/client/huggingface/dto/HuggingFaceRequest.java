package nikonov.telegramaibot.client.huggingface.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HuggingFaceRequest {

    @JsonProperty("inputs")
    private String prompt;
}
