package com.projectsvadim.vadimbot.service.manager.start;

import com.projectsvadim.vadimbot.service.factory.AnswerMethodFactory;
import com.projectsvadim.vadimbot.service.factory.KeyboardFactory;
import com.projectsvadim.vadimbot.service.manager.AbstractManager;
import com.projectsvadim.vadimbot.telegram.Bot;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

import static com.projectsvadim.vadimbot.service.data.CallBackData.FEEDBACK;
import static com.projectsvadim.vadimbot.service.data.CallBackData.HELP;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StartManager extends AbstractManager {
    final AnswerMethodFactory methodFactory;

    final KeyboardFactory keyboardFactory;

    public StartManager(AnswerMethodFactory methodFactory,
                        KeyboardFactory keyboardFactory) {
        this.methodFactory = methodFactory;
        this.keyboardFactory = keyboardFactory;
    }

    @Override
    public SendMessage answerCommand(Message message, Bot bot) {
        return methodFactory.getSendMessage(
                message.getChatId(),
                """
                        🖖Приветствую в New-Bot,
                         инструменте для упрощения взаимодействия репититора и ученика.
                                                
                        Что бот умеет?
                        📌 Составлять расписание
                        📌 Прикреплять домашние задания
                        📌 Ввести контроль успеваемости                       
                        """,
                keyboardFactory.getInlineKeyboard(
                        List.of(" \uD83C\uDD98 Помощь", " \uD83D\uDCE1 Обратная связь"),
                        List.of(2),
                        List.of(HELP, FEEDBACK)
                )
        );
    }

    @Override
    public BotApiMethod<?> answerMessage(Message message, Bot bot) {
        return null;
    }

    @Override
    public BotApiMethod<?> answerCallbackQuery(CallbackQuery callbackQuery, Bot bot) {
        return methodFactory.getEditMessageText(
                callbackQuery,
                """
                        🖖Приветствую в Tutor-Bot, инструменте для упрощения взаимодействия репититора и ученика.
                                                
                        Что бот умеет?
                        📌 Составлять расписание
                        📌 Прикреплять домашние задания
                        📌 Ввести контроль успеваемости                        
                        """,
                keyboardFactory.getInlineKeyboard(
                        List.of(" \uD83C\uDD98 Помощь", " \uD83D\uDCE1 Обратная связь"),
                        List.of(2),
                        List.of(HELP, FEEDBACK)
                )
        );
    }
}
