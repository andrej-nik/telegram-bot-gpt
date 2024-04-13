package nikonov.telegramgptbot.bot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nikonov.telegramgptbot.service.GPTService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

import static nikonov.telegramgptbot.utils.MessageKey.ACCESS_DENIED_MESSAGE_KEY;
import static nikonov.telegramgptbot.utils.MessageKey.ERROR_MESSAGE_KEY;
import static nikonov.telegramgptbot.utils.MessageKey.START_MESSAGE_KEY;

/**
 * GPT бот
 */
@Slf4j
@Component
@SuppressWarnings({"java:S4449"})
public class GPTBot extends TelegramLongPollingBot {
    
    private final String telegramBotName;
    private final Set<String> users;
    private final GPTService gptService;
    private final MessageSource messageSource;

    public GPTBot(
            @Value("${application.telegram.bot-token}") String telegramBotToken,
            @Value("${application.telegram.bot-name}") String telegramBotName,
            @Value("#{'${application.users-white-list}'.split(',')}") Set<String> users,
            GPTService gptService, 
            MessageSource messageSource) {

        super(telegramBotToken);
        this.telegramBotName = telegramBotName;

        this.users = users;
        this.gptService = gptService;
        this.messageSource = messageSource;
    }

    @Override
    public void onUpdateReceived(Update update) {
        
        if ( hasText(update) && hasAccess(update)) {

            var chatId = update.getMessage().getChatId().toString();
            if (isStartCommand(update)) {
                sendMessage(chatId, getMessage(START_MESSAGE_KEY));
                return;
            }
            sendAction(chatId, ActionType.TYPING);
            var gptResponse = getGPTResponse(update.getMessage().getText());
            sendMessage(chatId, gptResponse);
        }
    }
    
    private String getGPTResponse(String prompt) {
        
        try {
            return gptService.getGPTResponse(prompt);
        } catch (Exception exp) {
            
            log.error("Error prompt {} GPT", prompt, exp);
            return messageSource.getMessage(ERROR_MESSAGE_KEY, null, null);
        }
    }
    
    private static boolean isStartCommand(Update update) {
        
        return update.getMessage().getText().equals("/start");
    }

    private boolean hasAccess(Update update) {

        var username = getUserName(update);
        if ( !hasUserAccess(username) ) {
            log.info("Запрос от неизвестного пользователя: {}", username);
            sendMessage(
                    update.getMessage().getChatId().toString(),
                    getMessage(ACCESS_DENIED_MESSAGE_KEY));
            return false;
        }
        return true;
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

    private static String getUserName(Update update) {

        return update.getMessage().getFrom().getUserName();
    }

    private boolean hasUserAccess(String username) {

        return users.contains(username);
    }

    private static boolean hasText(Update update) {

        return update.hasMessage() && update.getMessage().hasText();
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, null);
    }
}
