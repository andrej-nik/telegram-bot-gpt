package nikonov.telegramaibot.client.chatgpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChatGPTChoices {

    @JsonProperty("text")
    private String text;
}
