package nikonov.telegramaibot.domain;

import lombok.Data;

/**
 * Ответ ai с текстом
 */
@Data
public class AITextResponse implements AIResponse {
    
    private String text;
}
