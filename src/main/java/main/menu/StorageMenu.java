package main.menu;

import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class StorageMenu {

  //https://apps.timwhitlock.info/emoji/tables/unicode ссылка на список имоджи
  private static KeyboardButton storage = new KeyboardButton("\uD83D\uDCBEХранилище");
  private static KeyboardButton back = new KeyboardButton("\uD83D\uDD19Назад");
  private static KeyboardButton addStorage = new KeyboardButton("➕Добавить категорию");

  /**
   * Главное меню
   */
  public static ReplyKeyboardMarkup getMainMenu() {
    ReplyKeyboardMarkup replyMarkup = new ReplyKeyboardMarkup();
    List<KeyboardRow> keyboard = new ArrayList<>();
    KeyboardRow keyboardRow1 = new KeyboardRow();
    keyboardRow1.add(storage);
//    KeyboardRow keyboardRow2 = new KeyboardRow();
//    keyboardRow2.add(info);
//    keyboardRow2.add(replenishBalance);
//    KeyboardRow keyboardRow3 = new KeyboardRow();
//    keyboardRow3.add(enterKey);
    keyboard.add(keyboardRow1);
    //keyboard.add(keyboardRow2);
    //keyboard.add(keyboardRow3);
    replyMarkup.setKeyboard(keyboard);
    replyMarkup.setSelective(true);
    replyMarkup.setOneTimeKeyboard(true);
    replyMarkup.setResizeKeyboard(true);
    return replyMarkup;
  }

  /**
   * Вернуться в главное меню
   * @return
   */
  public static ReplyKeyboard backMenu() {
    ReplyKeyboardMarkup replyMarkup = new ReplyKeyboardMarkup();
    List<KeyboardRow> keyboard = new ArrayList<>();
    KeyboardRow keyboardFirstRow = new KeyboardRow();
    keyboardFirstRow.add(back);
    keyboard.add(keyboardFirstRow);
    replyMarkup.setKeyboard(keyboard);
    replyMarkup.setSelective(true);
    replyMarkup.setOneTimeKeyboard(true);
    replyMarkup.setResizeKeyboard(true);
    return replyMarkup;
  }

  /**
   * Основное меню хранилища
   * @return
   */
  public static ReplyKeyboard storageCategoryMenu() {
    ReplyKeyboardMarkup replyMarkup = new ReplyKeyboardMarkup();
    List<KeyboardRow> keyboard = new ArrayList<>();
    KeyboardRow keyboardFirstRow = new KeyboardRow();
    keyboardFirstRow.add(addStorage);
    keyboardFirstRow.add(back);
    keyboard.add(keyboardFirstRow);
    replyMarkup.setKeyboard(keyboard);
    replyMarkup.setSelective(true);
    replyMarkup.setOneTimeKeyboard(true);
    replyMarkup.setResizeKeyboard(true);
    return replyMarkup;
  }

}
