package ru.home.weather_bot.botapi.handlers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.home.weather_bot.botapi.BotState;
import ru.home.weather_bot.botapi.InputMessageHandler;
import ru.home.weather_bot.botapi.WeatherParser;
import ru.home.weather_bot.cache.UserDataCache;
import ru.home.weather_bot.service.ReplyMessagesService;

@Slf4j
@Component
public class GettingLocationHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;

    public GettingLocationHandler(UserDataCache userDataCache, ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.GETTING_LOCATION;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        UserSubscribeData subscribeData = userDataCache.getUserProfileData(userId);

        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "Это не геолокация.");

        if(inputMsg.hasLocation()) {
            userDataCache.setUserCurrentBotState(userId, BotState.START_STATE);
            float lat = inputMsg.getLocation().getLatitude();
            float lon = inputMsg.getLocation().getLongitude();
            subscribeData.setDefault_location(lat, lon);
            WeatherParser parseRes = new WeatherParser();
            replyToUser = new SendMessage(chatId, parseRes.parse(lat, lon));
        }

        userDataCache.saveUserProfileData(userId, subscribeData);

        return replyToUser;
    }
}