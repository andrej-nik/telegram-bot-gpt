package nikonov.telegramgptbot.bot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nikonov.telegramgptbot.service.GPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

import static nikonov.telegramgptbot.utils.MessageKeys.ACCESS_DENIED_MESSAGE_KEY;

/**
 * GPT бот
 * 
 * @author Andrej Nikonov
 */
@Slf4j
@Component
public class GPTBot extends TelegramLongPollingBot {
    
    @Value("${application.telegram.bot-token}")
    private String telegramBotToken;
    
    @Value("${application.telegram.bot-name}")
    private String telegramBotName;
    
    @Value("#{'${application.users-white-list}'.split(',')}")
    private Set<String> users;
    
    @Autowired
    private GPTService gptService;
    
    @Autowired
    private MessageSource messageSource;
    
    @Override
    public void onUpdateReceived(Update update) {
        
        if ( hasText(update) ) {
            
            var username = getUserName(update);
            if ( !hasUserAccess(username) ) {
                log.info("Запрос от неизвестного пользователя: {}", username);
                sendTelegramResponse(update, getMessage(ACCESS_DENIED_MESSAGE_KEY));
                return;
            }

            var message = update.getMessage();
            var response = gptService.getGPTResponse(message.getText());
            sendTelegramResponse(update, response);
        }
    }

    @Override 
    public String getBotUsername() {

        return telegramBotName;
    }

    @Override
    public String getBotToken() {
        
        return telegramBotToken;
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

    @SneakyThrows
    private void sendTelegramResponse(Update update, String response) {

        var chatId = update.getMessage().getChatId().toString();
        var responseMessage = new SendMessage(chatId, response);
        execute(responseMessage);
    }
}
