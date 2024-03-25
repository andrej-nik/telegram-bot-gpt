package nikonov.telegramgptbot.bot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nikonov.telegramgptbot.service.GPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import static nikonov.telegramgptbot.utils.MessageKeys.ACCESS_DENIED_MESSAGE_KEY;
import static nikonov.telegramgptbot.utils.MessageKeys.GREETING;
import static nikonov.telegramgptbot.utils.MessageKeys.WRITE_ANSWER_MESSAGE_KEY;

/**
 * GPT бот
 * 
 * @author Andrej Nikonov
 */
@Slf4j
@Component
@SuppressWarnings({"java:S4449"})
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
    
    @Autowired
    private TaskScheduler taskScheduler;
    
    @Override
    public void onUpdateReceived(Update update) {
        
        if ( hasText(update) && hasAccess(update)) {

            var chatId = update.getMessage().getChatId().toString();
            if (isStartCommand(update)) {
                sendMessage(chatId, getMessage(GREETING));
                return;
            }
            var messageId = sendMessage(chatId, getMessage(WRITE_ANSWER_MESSAGE_KEY, ""));
            var writeAnswerTask = taskScheduler.scheduleAtFixedRate(
                    new WriteAnswerTask(chatId, messageId), 
                    Duration.ofSeconds(1));
            var response = gptService.getGPTResponse(update.getMessage().getText());
            cancelTask(writeAnswerTask);
            sendMessage(chatId, messageId, response);
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
    private Integer sendMessage(String chatId, String text) {

        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        return execute(message).getMessageId();
    }
    
    @SneakyThrows
    private void sendMessage(String chatId, Integer messageId, String text) {

        var editMessage = new EditMessageText();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(messageId);
        editMessage.setText(text);
        execute(editMessage);
    }
    
    private void cancelTask(ScheduledFuture<?> task) {

        do {
            task.cancel(false);
        } while (!task.isCancelled());
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
    
    private String getMessage(String key, Object args) {
        return messageSource.getMessage(key, new Object[] {args}, null);
    }
    
    private class WriteAnswerTask implements Runnable {
        
        private static final String DOT = ".";
        private static final int DOT_REPEAT_MAX = 5;
        
        private final String chatId;
        private final int messageId;
        private int counter;
        
        public WriteAnswerTask(String chatId, Integer messageId) {
            
            this.chatId = chatId;
            this.messageId = messageId;
        }
        
        @Override
        @SneakyThrows
        public void run() {
            
            var message = new EditMessageText();
            message.setChatId(chatId);
            message.setMessageId(messageId);
            message.setText(getMessage(WRITE_ANSWER_MESSAGE_KEY, DOT.repeat(++counter % DOT_REPEAT_MAX)));
            execute(message);
        }
    }
}
