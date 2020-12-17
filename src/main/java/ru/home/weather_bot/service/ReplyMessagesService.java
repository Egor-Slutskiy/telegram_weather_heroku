package ru.home.weather_bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class ReplyMessagesService {
    public SendMessage getReplyMessage(long chatId, String replyMessage) {
        return new SendMessage(chatId, replyMessage);
    }
}
