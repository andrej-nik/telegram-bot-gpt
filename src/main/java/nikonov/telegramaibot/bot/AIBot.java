package nikonov.telegramaibot.bot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nikonov.telegramaibot.service.AIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static nikonov.telegramaibot.domain.MessageKey.ERROR_MESSAGE_KEY;
import static nikonov.telegramaibot.domain.MessageKey.START_MESSAGE_KEY;

/**
 * AI бот
 */
@Slf4j
@Component
@SuppressWarnings({"java:S4449"})
public class AIBot extends TelegramLongPollingBot {
    
    private final String telegramBotName;
    private final AIService aiService;
    private final MessageSource messageSource;

    public AIBot(
            @Value("${application.telegram.bot-token}") String telegramBotToken,
            @Value("${application.telegram.bot-name}") String telegramBotName,
            AIService aiService, 
            MessageSource messageSource) {

        super(telegramBotToken);
        
        this.telegramBotName = telegramBotName;
        this.aiService = aiService;
        this.messageSource = messageSource;
    }

    @Override
    public void onUpdateReceived(Update update) {
        
        if (hasText(update)) {

            var chatId = update.getMessage().getChatId().toString();
            if (isStartCommand(update)) {
                sendMessage(chatId, getMessage(START_MESSAGE_KEY));
                return;
            }
            sendAction(chatId, ActionType.TYPING);
            var response = getAIResponse(update.getMessage().getText());
            sendMessage(chatId, response);
        }
    }
    
    private String getAIResponse(String prompt) {
        
        try {
            return aiService.getAIResponse(prompt);
        } catch (Exception exp) {
            
            log.error("Error prompt {} GPT", prompt, exp);
            return messageSource.getMessage(ERROR_MESSAGE_KEY, null, null);
        }
    }
    
    private static boolean isStartCommand(Update update) {
        
        return update.getMessage().getText().equals("/start");
    }
    
    @SneakyThrows
    private void sendMessage(String chatId, String text) {

        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        execute(message);
    }

    @SneakyThrows
    private void sendAction(String chatId, ActionType type) {

        var action = new SendChatAction();
        action.setChatId(chatId);
        action.setAction(type);
        execute(action);
    }

    @Override 
    public String getBotUsername() {

        return telegramBotName;
    }

    private static boolean hasText(Update update) {

        return update.hasMessage() && update.getMessage().hasText();
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, null);
    }
}
