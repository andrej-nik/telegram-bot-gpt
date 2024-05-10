package nikonov.telegramaibot.domain;

import lombok.Data;

/**
 * Ответ ai с изображением
 */
@Data
public class AIImageResponse implements AIResponse {

    private byte[] payload;
}
