package nikonov.telegramgptbot.external.gpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Andrej Nikonov
 */
@Data
public class GPTChoices {

    @JsonProperty("text")
    private String text;
}
