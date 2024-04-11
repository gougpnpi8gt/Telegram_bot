package com.projectsvadim.vadimbot.service.handler;

import com.projectsvadim.vadimbot.service.factory.KeyboardFactory;
import com.projectsvadim.vadimbot.service.manager.feedback.FeedbackManager;
import com.projectsvadim.vadimbot.service.manager.help.HelpManager;
import com.projectsvadim.vadimbot.service.manager.profile.ProfileManager;
import com.projectsvadim.vadimbot.service.manager.progresControl_.ProgressControlManager;
import com.projectsvadim.vadimbot.service.manager.search.SearchManager;
import com.projectsvadim.vadimbot.service.manager.start.StartManager;
import com.projectsvadim.vadimbot.service.manager.task.TaskManager;
import com.projectsvadim.vadimbot.service.manager.timeTable.TimetableManager;
import com.projectsvadim.vadimbot.telegram.Bot;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.projectsvadim.vadimbot.service.data.Command.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommandHandler {
    final StartManager startManager;
    final FeedbackManager feedbackManager;
    final HelpManager helpManager;
    final TimetableManager timeTableManager;
    final KeyboardFactory keyboardFactory;
    final TaskManager taskManager;
    final ProgressControlManager progressControlManager;
    final ProfileManager profileManager;
    final SearchManager searchManager;

    @Autowired
    public CommandHandler(StartManager startManager,
                          KeyboardFactory keyboardFactory,
                          HelpManager helpManager,
                          FeedbackManager feedbackManager,
                          TimetableManager timeTableManager,
                          TaskManager taskManager,
                          ProgressControlManager progressControlManager,
                          ProfileManager profileManager,
                          SearchManager searchManager
    ) {
        this.startManager = startManager;
        this.keyboardFactory = keyboardFactory;
        this.feedbackManager = feedbackManager;
        this.helpManager = helpManager;
        this.timeTableManager = timeTableManager;
        this.taskManager = taskManager;
        this.progressControlManager = progressControlManager;
        this.profileManager = profileManager;
        this.searchManager = searchManager;
    }

    public BotApiMethod<?> answer(Message message, Bot bot) {
        String command = message.getText();
        switch (command) {
            case START -> {
                return startManager.answerCommand(message, bot);
            }
            case FEEDBACK_COMMAND -> {
                return feedbackManager.answerCommand(message, bot);
            }
            case HELP_COMMAND -> {
                return helpManager.answerCommand(message, bot);
            }
            case TIMETABLE -> {
                return timeTableManager.answerCommand(message, bot);
            }
            case TASK -> {
                return taskManager.answerCommand(message, bot);
            }
            case PROGRESS -> {
                return progressControlManager.answerCommand(message, bot);
            }
            case PROFILE -> {
                return profileManager.answerCommand(message, bot);
            }
            case SEARCH -> {
                return searchManager.answerCommand(message, bot);
            }
            default -> {
                return defaultAnswer(message);
            }
        }
    }

    private BotApiMethod<?> defaultAnswer(Message message) {
        return SendMessage.builder().text("""
                        Неподдерживаемая команда
                        """)
                .chatId(message.getChatId())
                .build();
    }
}
