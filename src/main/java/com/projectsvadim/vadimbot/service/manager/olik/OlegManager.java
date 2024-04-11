package com.projectsvadim.vadimbot.service.manager.olik;

import com.projectsvadim.vadimbot.service.factory.AnswerMethodFactory;
import com.projectsvadim.vadimbot.service.factory.KeyboardFactory;
import com.projectsvadim.vadimbot.service.manager.AbstractManager;
import com.projectsvadim.vadimbot.telegram.Bot;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

import static com.projectsvadim.vadimbot.service.data.CallBackData.*;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OlegManager extends AbstractManager {
    final AnswerMethodFactory methodFactory;
    final KeyboardFactory keyboardFactory;

    @Autowired
    public OlegManager(AnswerMethodFactory methodFactory,
                       KeyboardFactory keyboardFactory) {
        this.methodFactory = methodFactory;
        this.keyboardFactory = keyboardFactory;
    }

    @Override
    public BotApiMethod<?> answerCommand(Message message, Bot bot) {
        return mainMenu(message);
    }

    @Override
    public BotApiMethod<?> answerMessage(Message message, Bot bot) {
        return null;
    }

    @Override
    public BotApiMethod<?> answerCallbackQuery(CallbackQuery callbackQuery, Bot bot) {
        String callbackData = callbackQuery.getData();
        switch (callbackData){
            case OLIK -> {
                return mainMenu(callbackQuery);
            }
            case OLIK_DRAW -> {
                return draw(callbackQuery);
            }
            case OLIK_SORRY-> {
                return sorry(callbackQuery);
            }
            case OLIK_MESORRY-> {
                return mesorry(callbackQuery);
            }
        }
        return null;
    }

    private BotApiMethod<?> mainMenu(Message message) {
        return methodFactory.getSendMessage(message.getChatId(),
                """
                        🦥 Здесь вы можете увидите Олега
                        """,
                keyboardFactory.getInlineKeyboard(
                        List.of("\uD83D\uDD8C Нарисовать Олега ",
                                "\uD83D\uDE3E Заставить извиняться",
                                "\uD83D\uDE4F Самому извиниться"),
                        List.of(1, 2),
                        List.of(OLIK_DRAW, OLIK_SORRY, OLIK_MESORRY)
                )
        );
    }
    private BotApiMethod<?> mainMenu(CallbackQuery callbackQuery) {
        return methodFactory.getEditMessageText(callbackQuery,
                """
                        🦥 Здесь вы можете увидите Олега
                        """,
                keyboardFactory.getInlineKeyboard(
                        List.of("\uD83D\uDD8C Нарисовать Олега ",
                                "\uD83D\uDE3E Заставить извиняться",
                                "\uD83D\uDE4F Самому извиниться"),
                        List.of(1, 2),
                        List.of(OLIK_DRAW, OLIK_SORRY, OLIK_MESORRY)
                )
        );
    }
    private BotApiMethod<?> draw(CallbackQuery callbackQuery) {
        return methodFactory.getEditMessageText(
                callbackQuery,
                """
                            Олик нарисован, восторгаемся!
                        """,
                keyboardFactory.getInlineKeyboard(
                        List.of("Теперь это будет всю жизнь перед твоими глазами, идем обратно кожанный"),
                        List.of(1),
                        List.of(OLIK)
                )
        );
    }
    private BotApiMethod<?> sorry(CallbackQuery callbackQuery) {
        return methodFactory.getEditMessageText(
                callbackQuery,
                """
                       Мои глубочайшие извинения, пёсики)
                        """,
                keyboardFactory.getInlineKeyboard(
                        List.of("Олик извинился, можно идти дальше"),
                        List.of(1),
                        List.of(OLIK)
                )
        );
    }
    private BotApiMethod<?> mesorry(CallbackQuery callbackQuery) {
        return methodFactory.getEditMessageText(
                callbackQuery,
                """
                        Простиииии, уважаемый Олежик. Гений, миллиардер, плэйбой, повелитель 1С!
                        """,
                keyboardFactory.getInlineKeyboard(
                        List.of("Клиент прощён, можете опустить глаза и возвращаться обратно" +
                                "в презрении перед этим великим программистом"),
                        List.of(1),
                        List.of(OLIK)
                )
        );
    }
}
