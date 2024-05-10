package nikonov.telegramaibot.domain;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageKey {

    public static final String TEXT_BOT_ERROR = "text-bot-error";
    public static final String TEXT_BOT_DESCRIPTION = "text-bot-description";
    public static final String TEXT_BOT_PROCESS_START = "text-bot-process-start";
    
    public static final String IMAGE_BOT_ERROR = "image-bot-error";
    public static final String IMAGE_BOT_DESCRIPTION = "image-bot-description";
    public static final String IMAGE_BOT_PROCESS_START = "image-bot-process-start";
    public static final String IMAGE_BOT_PROCESS_FINISH = "image-bot-process-finish";
}
