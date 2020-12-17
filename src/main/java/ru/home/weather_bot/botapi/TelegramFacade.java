package ru.home.weather_bot.botapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.weather_bot.botapi.handlers.UserSubscribeData;
import ru.home.weather_bot.cache.UserDataCache;

@Component
@Slf4j
public class TelegramFacade {
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
    }

    public SendMessage handleUpdate(Update update) {
        SendMessage replyMessage = null;

        Message message = update.getMessage();
        if (message != null) {
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMsg = null;
        int userId = message.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;

        if(message.hasText()){inputMsg=message.getText();}
        if(message.hasLocation()){
            inputMsg = message.getLocation().toString();
        }

        switch (inputMsg) {
            case "/start":
                botState = BotState.START_STATE;
                break;
            case "/weather":
                botState = BotState.WAIT_LOCATION;
                break;
            case "/subscribe":
                botState = BotState.SUBSCRIBE_STATE;
                break;
            case "/unsubscribe":
                botState = BotState.UNSUBSCRIBE_STATE;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
        };

        userDataCache.setUserCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }
}
