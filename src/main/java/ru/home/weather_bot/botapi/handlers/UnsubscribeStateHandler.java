package ru.home.weather_bot.botapi.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.home.weather_bot.botapi.BotState;
import ru.home.weather_bot.botapi.EveryDayWeather;
import ru.home.weather_bot.botapi.InputMessageHandler;
import ru.home.weather_bot.cache.UserDataCache;
import ru.home.weather_bot.service.ReplyMessagesService;

@Slf4j
@Component
public class UnsubscribeStateHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;

    public UnsubscribeStateHandler(UserDataCache userDataCache,
                                 ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.UNSUBSCRIBE_STATE;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        UserSubscribeData subscribeData = userDataCache.getUserProfileData(userId);

        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "Вы отписались от рассылки.");
        userDataCache.setUserCurrentBotState(userId, BotState.START_STATE);
        subscribeData.setIs_subscribe(true);
        EveryDayWeather asd = new EveryDayWeather(false, userId, chatId, userDataCache, messagesService);
        Thread subThread = new Thread(asd);
        subThread.start();

        userDataCache.saveUserProfileData(userId, subscribeData);

        return replyToUser;
    }


}
