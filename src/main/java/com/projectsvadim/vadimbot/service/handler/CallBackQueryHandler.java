package com.projectsvadim.vadimbot.service.handler;

import com.projectsvadim.vadimbot.service.manager.auth.AuthManager;
import com.projectsvadim.vadimbot.service.manager.feedback.FeedbackManager;
import com.projectsvadim.vadimbot.service.manager.help.HelpManager;
import com.projectsvadim.vadimbot.service.manager.olik.OlegManager;
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
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static com.projectsvadim.vadimbot.service.data.CallBackData.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CallBackQueryHandler {
    final HelpManager helpManager;
    final FeedbackManager feedbackManager;
    final TimetableManager timeTableManager;
    final TaskManager taskManager;
    final ProgressControlManager progressControlManager;
    final AuthManager authManager;
    final ProfileManager profileManager;
    final SearchManager searchManager;
    final StartManager startManager;

    @Autowired
    public CallBackQueryHandler(HelpManager helpManager,
                                FeedbackManager feedbackManager, TimetableManager timeTableManager,
                                TaskManager taskManager,
                                ProgressControlManager progressControlManager,
                                AuthManager authManager,
                                ProfileManager profileManager,
                                SearchManager searchManager, StartManager startManager) {
        this.helpManager = helpManager;
        this.feedbackManager = feedbackManager;
        this.timeTableManager = timeTableManager;
        this.taskManager = taskManager;
        this.progressControlManager = progressControlManager;
        this.authManager = authManager;
        this.profileManager = profileManager;
        this.searchManager = searchManager;
        this.startManager = startManager;
    }

    public BotApiMethod<?> answer(CallbackQuery callBackQuery, Bot bot) {
        String callbackData = callBackQuery.getData();
        String keyWord = callbackData.split("_")[0];
        switch (keyWord) {
            case TIMETABLE -> {
                return timeTableManager.answerCallbackQuery(callBackQuery, bot);
            }
            case TASK -> {
                return taskManager.answerCallbackQuery(callBackQuery, bot);
            }
            case PROGRESS -> {
                return progressControlManager.answerCallbackQuery(callBackQuery, bot);
            }
            case AUTH -> {
                return authManager.answerCallbackQuery(callBackQuery, bot);
            }
            case PROFILE -> {
                return profileManager.answerCallbackQuery(callBackQuery, bot);
            }
            case SEARCH -> {
                return searchManager.answerCallbackQuery(callBackQuery, bot);
            }
        }
        switch (callbackData) {
            case FEEDBACK -> {
                return feedbackManager.answerCallbackQuery(callBackQuery, bot);
            }
            case HELP -> {
                return helpManager.answerCallbackQuery(callBackQuery, bot);
            }
            case START -> {
                return startManager.answerCallbackQuery(callBackQuery, bot);
            }
        }
        return null;
    }
}
