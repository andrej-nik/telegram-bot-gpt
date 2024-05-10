package nikonov.telegramaibot.bot;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nikonov.telegramaibot.domain.AIImageResponse;
import nikonov.telegramaibot.domain.AIResponse;
import nikonov.telegramaibot.domain.AITextResponse;
import nikonov.telegramaibot.domain.properties.AIModelProperties;
import nikonov.telegramaibot.service.AIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.ByteArrayInputStream;

import static nikonov.telegramaibot.domain.AIModelType.IMAGE;
import static nikonov.telegramaibot.domain.AIModelType.TEXT;
import static nikonov.telegramaibot.domain.MessageKey.IMAGE_BOT_DESCRIPTION;
import static nikonov.telegramaibot.domain.MessageKey.IMAGE_BOT_ERROR;
import static nikonov.telegramaibot.domain.MessageKey.IMAGE_BOT_PROCESS_FINISH;
import static nikonov.telegramaibot.domain.MessageKey.IMAGE_BOT_PROCESS_START;
import static nikonov.telegramaibot.domain.MessageKey.TEXT_BOT_DESCRIPTION;
import static nikonov.telegramaibot.domain.MessageKey.TEXT_BOT_ERROR;
import static nikonov.telegramaibot.domain.MessageKey.TEXT_BOT_PROCESS_START;

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
    private final ModelMessageKeys messageKeys;

    public AIBot(
            @Value("${application.telegram.bot-token}") String telegramBotToken,
            @Value("${application.telegram.bot-name}") String telegramBotName,
            AIService aiService, 
            MessageSource messageSource,
            AIModelProperties modelProperties) {

        super(telegramBotToken);
        this.telegramBotName = telegramBotName;
        this.aiService = aiService;
        this.messageSource = messageSource;
        this.messageKeys = getModelMessageKey(modelProperties);
    }

    @Override
    public void onUpdateReceived(Update update) {
        
        if (hasText(update)) {
            var chatId = update.getMessage().getChatId().toString();
            if (isStartCommand(update)) {
                sendMessage(chatId, getMessage(messageKeys.getDescriptionKey()));
                return;
            }
            var userMessageId = update.getMessage().getMessageId();
            var processMessageId = sendProcessMessage(chatId, userMessageId).getMessageId();
            var aiResponse = getAIResponse(update.getMessage().getText());
            sendAIResponse(chatId, userMessageId, processMessageId, aiResponse);
        }
    }

    @Override
    public String getBotUsername() {

        return telegramBotName;
    }
    
    private AIResponse getAIResponse(String prompt) {
        
        try {
            return aiService.getAIResponse(prompt);
        } catch (Exception exp) {
            log.error("Error prompt {}", prompt, exp);
            return new AITextResponse().setText(getMessage(messageKeys.getErrorKey()));
        }
    }
    
    private static boolean isStartCommand(Update update) {
        
        return update.getMessage().getText().equals("/start");
    }
    
    private void sendAIResponse(
            String chatId, 
            Integer userMessageId, 
            Integer processMessageId, 
            AIResponse aiResponse) {
        
        if (aiResponse instanceof AITextResponse aiTextResponse) {
            sendMessage(chatId, processMessageId, aiTextResponse);
        } else if (aiResponse instanceof AIImageResponse aiImageResponse) {
            sendDeleteMessage(chatId, processMessageId);
            sendPhoto(chatId, userMessageId, aiImageResponse);
        } else {
            throw new IllegalArgumentException("unknown ai response type %s".formatted(aiResponse.getClass().getSimpleName()));
        }
    }
    
    @SneakyThrows
    private void sendDeleteMessage(String chatId, Integer messageId) {

        var deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        execute(deleteMessage);
    }
    
    @SneakyThrows
    private Message sendProcessMessage(String chatId, Integer messageId) {

        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(getMessage(messageKeys.getProcessStartKey()));
        message.setReplyToMessageId(messageId);
        return execute(message);
    }
    
    @SneakyThrows
    private void sendMessage(String chatId, String text) {

        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        execute(message);
    }
    
    @SneakyThrows
    private void sendMessage(String chatId, Integer messageId, AITextResponse aiTextResponse) {

        var newMessage = new EditMessageText();
        newMessage.setChatId(chatId);
        newMessage.setMessageId(messageId);
        newMessage.setText(aiTextResponse.getText());
        execute(newMessage);
    }
    
    @SneakyThrows
    private void sendPhoto(String chatId, Integer userMessageId, AIImageResponse aiResponse) {
        
        var sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(
                new InputFile(new ByteArrayInputStream(aiResponse.getPayload()), "image.jpg"));
        sendPhoto.setReplyToMessageId(userMessageId);
        sendPhoto.setCaption(getMessage(messageKeys.getProcessFinishKey()));
        execute(sendPhoto);
    }
    
    private String getMessage(String key) {
        
        return messageSource.getMessage(key, null, null);
    }
    
    private static boolean hasText(Update update) {
        
        return update.hasMessage() && update.getMessage().hasText();
    }
    
    private static ModelMessageKeys getModelMessageKey(AIModelProperties modelProperties) {
        
        var type = modelProperties.getType();
        if (type == TEXT) {
            return new ModelMessageKeys()
                    .setDescriptionKey(TEXT_BOT_DESCRIPTION)
                    .setProcessStartKey(TEXT_BOT_PROCESS_START)
                    .setErrorKey(TEXT_BOT_ERROR);
        } else if (type == IMAGE) {
            return new ModelMessageKeys()
                    .setDescriptionKey(IMAGE_BOT_DESCRIPTION)
                    .setProcessStartKey(IMAGE_BOT_PROCESS_START)
                    .setProcessFinishKey(IMAGE_BOT_PROCESS_FINISH)
                    .setErrorKey(IMAGE_BOT_ERROR);
        }
        throw new UnsupportedOperationException("unknown model type %s".formatted(modelProperties.getType()));
    }
    
    @Data
    private static class ModelMessageKeys {
        
        private String descriptionKey;
        private String processStartKey;
        private String processFinishKey;
        private String errorKey;
    }
}
