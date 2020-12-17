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
public class SubscribeStateHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;

    public SubscribeStateHandler(UserDataCache userDataCache,
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
        return BotState.SUBSCRIBE_STATE;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();
        SendMessage replyToUser = null;
        UserSubscribeData subscribeData = userDataCache.getUserProfileData(userId);



        if(subscribeData.getDefault_location()!="null") {
            replyToUser = messagesService.getReplyMessage(chatId, "Вы подписались на рассылку. Чтобы отписаться введите /unsubscribe.");
            userDataCache.setUserCurrentBotState(userId, BotState.START_STATE);

            subscribeData.setIs_subscribe(true);
            EveryDayWeather asd = new EveryDayWeather(true, userId, chatId, userDataCache, messagesService);
            Thread subThread = new Thread(asd);
            subThread.start();
        }else{
            replyToUser = messagesService.getReplyMessage(chatId, "Вы не можете подписаться.");
            userDataCache.setUserCurrentBotState(userId, BotState.START_STATE);
        }

        userDataCache.saveUserProfileData(userId, subscribeData);

        return replyToUser;
    }


}
