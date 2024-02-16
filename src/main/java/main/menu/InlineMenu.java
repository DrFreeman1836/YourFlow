package main.menu;

import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class InlineMenu {

  private static InlineKeyboardButton see = new InlineKeyboardButton("☝Смотреть");
  private static InlineKeyboardButton delete = new InlineKeyboardButton("❌Удалить");
  private static InlineKeyboardButton done = new InlineKeyboardButton("✅Выполнено");


  public static InlineKeyboardMarkup toCategoryStorage(Long data) {
    see.setCallbackData("s" + data);
    delete.setCallbackData("d" + data);
    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
    List<InlineKeyboardButton> rowInline = new ArrayList<>();
    rowInline.add(see);
    rowInline.add(delete);
    rowsInline.add(rowInline);
    markupInline.setKeyboard(rowsInline);

    return markupInline;
  }

  public static InlineKeyboardMarkup toStorage(Long data) {
    delete.setCallbackData("d" + data);
    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
    List<InlineKeyboardButton> rowInline = new ArrayList<>();
    rowInline.add(delete);
    rowsInline.add(rowInline);
    markupInline.setKeyboard(rowsInline);

    return markupInline;
  }

  public static InlineKeyboardMarkup toTask(Long data) {
    done.setCallbackData("done" + data);
    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
    List<InlineKeyboardButton> rowInline = new ArrayList<>();
    rowInline.add(done);
    rowsInline.add(rowInline);
    markupInline.setKeyboard(rowsInline);

    return markupInline;
  }

  public static ReplyKeyboard nonMenu() {
    ReplyKeyboardMarkup replyMarkup = new ReplyKeyboardMarkup();
    List<KeyboardRow> keyboard = new ArrayList<>();
    replyMarkup.setKeyboard(keyboard);
    return replyMarkup;
  }

//  public static InlineKeyboardMarkup start(Long data) {
//    start.setCallbackData(String.valueOf(data));
//    delete.setCallbackData("d" + String.valueOf(data));
//    edit.setCallbackData("e" + String.valueOf(data));
//    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
//    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//    List<InlineKeyboardButton> rowInline = new ArrayList<>();
//    List<InlineKeyboardButton> rowInlineSecond = new ArrayList<>();
//    List<InlineKeyboardButton> rowInlineThird = new ArrayList<>();
//    rowInline.add(start);
//    rowInlineSecond.add(delete);
//    rowInlineThird.add(edit);
//    rowsInline.add(rowInline);
//    rowsInline.add(rowInlineSecond);
//    rowsInline.add(rowInlineThird);
//    markupInline.setKeyboard(rowsInline);
//
//    return markupInline;
//  }

//  public static InlineKeyboardMarkup stop(Long data) {
//    stop.setCallbackData(String.valueOf(data));
//    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
//    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//    List<InlineKeyboardButton> rowInline = new ArrayList<>();
//    rowInline.add(stop);
//    rowsInline.add(rowInline);
//    markupInline.setKeyboard(rowsInline);
//
//    return markupInline;
//  }

}
