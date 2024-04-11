package com.projectsvadim.vadimbot.service.handler;

import com.projectsvadim.vadimbot.repository.UserRepo;
import com.projectsvadim.vadimbot.service.manager.search.SearchManager;
import com.projectsvadim.vadimbot.service.manager.task.TaskManager;
import com.projectsvadim.vadimbot.service.manager.timeTable.TimetableManager;
import com.projectsvadim.vadimbot.telegram.Bot;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageHandler {
    final SearchManager searchManager;
    final UserRepo userRepo;
    final TimetableManager timetableManager;
    final TaskManager taskManager;

    @Autowired
    public MessageHandler(SearchManager searchManager, UserRepo userRepo, TimetableManager timetableManager, TaskManager taskManager) {
        this.searchManager = searchManager;
        this.userRepo = userRepo;
        this.timetableManager = timetableManager;
        this.taskManager = taskManager;
    }

    public BotApiMethod<?> answer(Message message, Bot bot) {
        var user = userRepo.findUserByChatId(message.getChatId());
        switch (user.getAction()) {
            case SENDING_TOKEN -> {
                return searchManager.answerMessage(message, bot);
            }
            case SENDING_DESCRIPTION,
                    SENDING_TITTLE -> {
                return timetableManager.answerMessage(message, bot);
            }
            case SENDING_TASK,
                    SENDING_MEDIA,
                    SENDING_TEXT -> {
                return taskManager.answerMessage(message, bot);
            }
        }
        return null;
    }
}
