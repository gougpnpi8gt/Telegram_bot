package com.projectsvadim.vadimbot.service.manager.progresControl_;

import com.projectsvadim.vadimbot.enity.User.Role;
import com.projectsvadim.vadimbot.enity.User.User;
import com.projectsvadim.vadimbot.enity.task.CompleteStatus;
import com.projectsvadim.vadimbot.repository.TaskRepo;
import com.projectsvadim.vadimbot.repository.UserRepo;
import com.projectsvadim.vadimbot.service.factory.AnswerMethodFactory;
import com.projectsvadim.vadimbot.service.factory.KeyboardFactory;
import com.projectsvadim.vadimbot.service.manager.AbstractManager;
import com.projectsvadim.vadimbot.telegram.Bot;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.support.BooleanTypedValue;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static com.projectsvadim.vadimbot.service.data.CallBackData.*;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProgressControlManager  extends AbstractManager {
    final AnswerMethodFactory methodFactory;
    final UserRepo userRepo;
    final TaskRepo taskRepo;
    final KeyboardFactory keyboardFactory;
    @Autowired
    public ProgressControlManager(AnswerMethodFactory methodFactory, UserRepo userRepo, TaskRepo taskRepo,
                                  KeyboardFactory keyboardFactory) {
        this.methodFactory = methodFactory;
        this.userRepo = userRepo;
        this.taskRepo = taskRepo;
        this.keyboardFactory = keyboardFactory;
    }

    @Override
    public BotApiMethod<?> answerCommand(Message message, Bot bot) {
        var user = userRepo.findUserByChatId(message.getChatId());
        if (Role.STUDENT.equals(user.getRole())) {
            return null;
        }
        return mainMenu(message);
    }

    @Override
    public BotApiMethod<?> answerMessage(Message message, Bot bot) {
        return null;
    }

    @Override
    public BotApiMethod<?> answerCallbackQuery(CallbackQuery callbackQuery, Bot bot) {
        String callbackData = callbackQuery.getData();
        switch (callbackData) {
            case PROGRESS -> {
                return mainMenu(callbackQuery);
            }
            case PROGRESS_STAT -> {
                return stat(callbackQuery);
            }
        }
        String[] splitCallbackData = callbackData.split("_");
        switch (splitCallbackData[1]) {
            case USER -> {
                return showUserStat(callbackQuery, splitCallbackData[2]);
            }
        }
        return null;
    }

    private BotApiMethod<?> showUserStat(CallbackQuery callbackQuery, String id) {
        var student = userRepo.findUserByToken(id);
        var details = student.getDetails();
        StringBuilder text = new StringBuilder("\uD83D\uDD39Статистика по пользователю \"")
                .append(details.getFirstName())
                .append("\"")
                .append("\n\n");
        int success = taskRepo.countAllByUsersContainingAndIsFinishedAndCompleteStatus(
                student, true, CompleteStatus.SUCCESS
        );
        int fail = taskRepo.countAllByUsersContainingAndIsFinishedAndCompleteStatus(
                student, true, CompleteStatus.FAIL
        );
        int sum = fail + success;
        text.append("\uD83D\uDCCDРешено - ")
                .append(success);
        text.append("\n\uD83D\uDCCDПровалено - ")
                .append(fail);
        text.append("\n\uD83D\uDCCDВсего - ")
                .append(sum);
        return methodFactory.getEditMessageText(
                callbackQuery,
                text.toString(),
                keyboardFactory.getInlineKeyboard(
                        List.of(" \uD83D\uDD19 Назад"),
                        List.of(1),
                        List.of(PROGRESS_STAT)
                )
        );
    }

    private BotApiMethod<?> mainMenu(CallbackQuery callbackQuery) {
        return methodFactory.getEditMessageText(
                callbackQuery,
                """
                        📈📉 Здесь вы можете увидеть статистику по каждому ученику""",
                keyboardFactory.getInlineKeyboard(
                        List.of("\uD83D\uDCCA Статистика успеваемости", "Вернуться в главное меню"),
                        List.of(2),
                        List.of(PROGRESS_STAT, START)
                )
        );
    }

    private BotApiMethod<?> mainMenu(Message message) {
        return methodFactory.getSendMessage(
                message.getChatId(),
                """
                        📈📉 Здесь вы можете увидеть статистику по каждому ученику""",
                keyboardFactory.getInlineKeyboard(
                        List.of("\uD83D\uDCCA Статистика успеваемости", "Вернуться в главное меню"),
                        List.of(2),
                        List.of(PROGRESS_STAT, START)
                )
        );
    }
    private BotApiMethod<?> stat(CallbackQuery callbackQuery) {
        var teacher = userRepo.findUserByChatId(callbackQuery.getMessage().getChatId());
        List<User> students = teacher.getUsers();
        List<String> text = new ArrayList<>();
        List<String> data = new ArrayList<>();
        List<Integer> cfg = new ArrayList<>();
        int index = 0;
        for (User student: students) {
            text.add(student.getDetails().getFirstName());
            data.add(PROGRESS_USER + student.getToken());
            if (index == 4) {
                cfg.add(index);
                index = 0;
            } else {
                index++;
            }
        }
        if (index != 0) {
            cfg.add(index);
        }
        data.add(PROGRESS);
        text.add("\uD83D\uDD19 Назад");
        cfg.add(1);
        return methodFactory.getEditMessageText(
                callbackQuery,
                "\uD83D\uDC66 \uD83D\uDC69\u200D\uD83E\uDDB0 Выберите ученика",
                keyboardFactory.getInlineKeyboard(
                        text,
                        cfg,
                        data
                )
        );
    }
}
