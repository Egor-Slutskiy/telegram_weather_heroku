package ru.home.weather_bot.botapi.handlers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.home.weather_bot.botapi.BotState;
import ru.home.weather_bot.botapi.InputMessageHandler;
import ru.home.weather_bot.cache.UserDataCache;
import ru.home.weather_bot.service.ReplyMessagesService;

@Slf4j
@Component
public class WaitLocationHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;

    public WaitLocationHandler(UserDataCache userDataCache, ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.WAIT_LOCATION;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        UserSubscribeData subscribeData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "Я не знаю что ответить.");

        if (inputMsg.hasText() && inputMsg.getText().equals("/weather")) {
            replyToUser = messagesService.getReplyMessage(chatId, "Прикрепите геолокацию.");
            userDataCache.setUserCurrentBotState(userId, BotState.GETTING_LOCATION);
        }
        return replyToUser;
    }
}