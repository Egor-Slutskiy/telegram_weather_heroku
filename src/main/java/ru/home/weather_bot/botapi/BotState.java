package ru.home.weather_bot.botapi;

public enum BotState {
    START_STATE,
    GETTING_LOCATION,
    WAIT_LOCATION,
    SUBSCRIBE_STATE,
    UNSUBSCRIBE_STATE,
}
