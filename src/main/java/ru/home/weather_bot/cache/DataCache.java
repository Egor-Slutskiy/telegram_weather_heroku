package ru.home.weather_bot.cache;

import ru.home.weather_bot.botapi.BotState;
import ru.home.weather_bot.botapi.handlers.UserSubscribeData;


public interface DataCache {
    void setUserCurrentBotState(int userId, BotState botState);

    BotState getUsersCurrentBotState(int userId);

    UserSubscribeData getUserProfileData(int userId);

    void saveUserProfileData(int userId, UserSubscribeData userSubscribeData);
}
