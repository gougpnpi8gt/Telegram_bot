package com.projectsvadim.vadimbot.service;

import com.projectsvadim.vadimbot.enity.User.User;
import com.projectsvadim.vadimbot.repository.UserRepo;
import com.projectsvadim.vadimbot.service.handler.CallBackQueryHandler;
import com.projectsvadim.vadimbot.service.handler.CommandHandler;
import com.projectsvadim.vadimbot.service.handler.MessageHandler;
import com.projectsvadim.vadimbot.telegram.Bot;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@FieldDefaults(level =  AccessLevel.PRIVATE)
@Slf4j // логирование необработанных обновлений
public class UpdateDispatcher {
    final MessageHandler messageHandler;

    final CommandHandler commandHandler;

    final CallBackQueryHandler callBackQueryHandler;
    @Autowired
    public UpdateDispatcher(MessageHandler messageHandler,
                            CommandHandler commandHandler, UserRepo userRepo,
                            CallBackQueryHandler callBackQueryHandler) {
        this.messageHandler = messageHandler;
        this.commandHandler = commandHandler;
        this.callBackQueryHandler = callBackQueryHandler;
    }

    public BotApiMethod<?> distribute(Update update, Bot bot){
    /* событие описывающее некоторый оносложный запрос,
    дату, информацию в текстовом формате
         */
        if (update.hasCallbackQuery()){
            return callBackQueryHandler.answer(update.getCallbackQuery(), bot);
        }
        /* message - объект
        содержащий информацию о новом сообщении
        в чате
         */
        if (update.hasMessage()){
            Message message = update.getMessage();
            if (message.hasText()){
                if (message.getText().charAt(0) == '/'){
                    return commandHandler.answer(message, bot);
                }
            }
            return messageHandler.answer(message, bot);
        }
        log.info("Unsupported update: " + update);
        return null;
    }
}
