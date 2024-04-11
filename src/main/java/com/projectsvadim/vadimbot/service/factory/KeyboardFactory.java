package com.projectsvadim.vadimbot.service.factory;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardFactory {
    /*
    Два типа клавиатур
    1. Клавиатура появляется под сообщением бота, нажимая
    на ее кнопки нам приходит обновление callbackQuery
    2. Подсказывает пользователя сообщения
     */
    public InlineKeyboardMarkup getInlineKeyboard(
            /*
            Каждый индекс списка config будет
            отображать кол-во кнопок в ряду
             */
            List<String> text,
            List<Integer> configuration,
            List<String> data){
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        int index = 0;//нужна для других двух списков
        for (Integer rowNumber : configuration){
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (int i = 0; i < rowNumber; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(text.get(index));
                button.setCallbackData(data.get(index));
                row.add(button);
                index++;
            }

            keyboard.add(row);
        }
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
    public ReplyKeyboardMarkup getReplyKeyboard(
            List<String> text,
            List<Integer> configuration
    ){
        List<KeyboardRow> keyboard = new ArrayList<>();
        int index = 0;//нужна для других двух списков
        for (Integer rowNumber : configuration){
            KeyboardRow row = new KeyboardRow();
            for (int i = 0; i < rowNumber; i++) {
                KeyboardButton button = new KeyboardButton();
                button.setText(text.get(index));
                row.add(button);
                index++;
            }
            keyboard.add(row);
        }
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
}
