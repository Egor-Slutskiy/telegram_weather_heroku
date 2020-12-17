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

        botState = switch (inputMsg) {
            case "/start" -> BotState.START_STATE;
            case "/weather" -> BotState.WAIT_LOCATION;
            case "/subscribe" -> BotState.SUBSCRIBE_STATE;
            case "/unsubscribe" -> BotState.UNSUBSCRIBE_STATE;
            default -> userDataCache.getUsersCurrentBotState(userId);
        };

        userDataCache.setUserCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }
}
