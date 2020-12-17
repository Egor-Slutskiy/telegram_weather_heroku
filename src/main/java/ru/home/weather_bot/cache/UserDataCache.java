package ru.home.weather_bot.cache;

import org.springframework.stereotype.Component;
import ru.home.weather_bot.botapi.BotState;
import ru.home.weather_bot.botapi.handlers.UserSubscribeData;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataCache implements DataCache{
    private Map<Integer, BotState> usersBotStates = new HashMap<>();
    private Map<Integer, UserSubscribeData> usersSubscribeData = new HashMap<>();


    @Override
    public void setUserCurrentBotState(int userId, BotState botState) { usersBotStates.put(userId, botState); }

    @Override
    public BotState getUsersCurrentBotState(int userId) {
        BotState botState = usersBotStates.get(userId);
        if (botState == null) {
            botState = BotState.START_STATE;
        }
        return botState;
    }

    @Override
    public UserSubscribeData getUserProfileData(int userId) {
        UserSubscribeData userSubscribeData = usersSubscribeData.get(userId);
        if (userSubscribeData == null) {
            userSubscribeData = new UserSubscribeData();
        }
        return userSubscribeData;
    }

    @Override
    public void saveUserProfileData(int userId, UserSubscribeData userSubscribeData) {
        usersSubscribeData.put(userId, userSubscribeData);
    }
}
