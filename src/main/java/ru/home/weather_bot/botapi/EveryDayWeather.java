package ru.home.weather_bot.botapi;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.home.weather_bot.botapi.handlers.UserSubscribeData;
import ru.home.weather_bot.cache.UserDataCache;
import ru.home.weather_bot.service.ReplyMessagesService;
import ru.home.weather_bot.botapi.TelegramFacade;


import java.util.concurrent.TimeUnit;


public class EveryDayWeather implements Runnable {
    boolean SendEveryDay;
    int userId;
    long chatId;
    UserDataCache userDataCache;
    ReplyMessagesService messagesService;

    public EveryDayWeather(boolean SendEveryDay, int userId, long chatId, UserDataCache userDataCache, ReplyMessagesService messagesService){
        this.chatId = chatId;
        this.SendEveryDay = SendEveryDay;
        this.userId = userId;
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public void run() {
        while (SendEveryDay) {
            UserSubscribeData subscribeData = userDataCache.getUserProfileData(userId);

            float lat = subscribeData.getDefault_lat();
            float lon = subscribeData.getDefault_lon();
            WeatherParser parseRes = new WeatherParser();
            SendMessage replyToUser = new SendMessage(chatId, parseRes.parse(lat, lon));
            subMsg(parseRes.parse(lat, lon));

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
        }
    }

    public SendMessage subMsg(String msg){
        SendMessage replyToUser = messagesService.getReplyMessage(chatId, msg);
        return replyToUser;
    }
}
