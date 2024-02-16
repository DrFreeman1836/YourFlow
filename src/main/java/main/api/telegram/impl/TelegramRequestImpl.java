package main.api.telegram.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.api.telegram.TelegramRequest;
import main.api.telegram.dto.RspMessageDto;
import main.api.telegram.enums.ParseMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Service
public class TelegramRequestImpl {

  private TelegramRequest request;

  private ObjectMapper mapper;

//  @Value("${bot.tokenPay}")
//  private String tokenPayment;

  @Autowired
  public TelegramRequestImpl(TelegramRequest request) {
    this.request = request;
    mapper = new ObjectMapper();
  }

  /**
   * Отправка сообщения
   * @param chatId
   * @param text
   * @param parseMode
   * @param markup
   * @return
   */
  public ResponseEntity<RspMessageDto> sendMessage(Long chatId, String text, ParseMode parseMode, ReplyKeyboard markup) {
    if (markup == null) {
      ResponseEntity<RspMessageDto> response = request.sendMessage(chatId, text, parseMode.getParseMode(), null);
      return response;
    }
    String markupString = "";
    try {
      markupString = mapper.writeValueAsString(markup);
    } catch (JsonProcessingException ex) {
      ex.printStackTrace();
    }
    ResponseEntity<RspMessageDto> response = request.sendMessage(chatId, text, parseMode.getParseMode(), markupString);
    return response;
  }

//  public ResponseEntity<RspMessageDto> sendMessage(Long chatId, String text, ReplyKeyboard markup) {
//    return sendMessage(chatId, text, ParseMode.NON, markup);
//  }
//
//  public ResponseEntity<RspMessageDto> sendMessage(Long chatId, String text) {
//    return request.sendMessage(chatId, text, ParseMode.NON.getParseMode(), null);
//  }

  /**
   * Отправка фото
   * @param chatId
   * @param photo
   * @param caption
   * @param parseMode
   * @param markup
   * @return
   */
  public ResponseEntity<RspMessageDto> sendPhoto(Long chatId, String photo, String caption, ParseMode parseMode, ReplyKeyboard markup) {
    String markupString = "";
    try{
      markupString = mapper.writeValueAsString(markup);
    } catch (JsonProcessingException ex) {
      ex.printStackTrace();
    }
    ResponseEntity<RspMessageDto> response = request.sendPhoto(chatId, photo, caption, parseMode.getParseMode(), markupString);
    return response;
  }

//  public ResponseEntity<RspMessageDto> sendPhoto(String photo, String caption, ParseMode parseMode, ReplyKeyboard markup) {
//    return sendPhoto(chatId, photo, caption, parseMode, markup);
//  }

  public ResponseEntity<RspMessageDto> editMessage(Long chatId, Long messageId, String newText, ParseMode parseMode, ReplyKeyboard markup) {
    String markupString = "";
    try{
      markupString = mapper.writeValueAsString(markup);
    } catch (JsonProcessingException ex) {
      ex.printStackTrace();
    }
    return request.editMessageCaption(chatId, messageId, newText, parseMode.getParseMode(), markupString);
  }

  public ResponseEntity<?> deleteMessage(Long chatId, Long messageId) {
    return request.deleteMessage(chatId, messageId);
  }

//  /**
//   * Отправка счета
//   * @param chatId
//   * @param nameProduct
//   * @param desc
//   * @param ListPrice
//   * @return
//   */
//  public ResponseEntity<RspMessageDto> sendInvoice(Long chatId, String nameProduct, String desc, List<Price> ListPrice) {
//    String priceString = "";
//    try {
//      priceString = mapper.writeValueAsString(ListPrice);
//    } catch (JsonProcessingException e) {
//      e.printStackTrace();
//    }
//    return request.sendInvoice(chatId,nameProduct, desc, "128",
//        "RUB", tokenPayment, priceString);
//  }

//  /**
//   * Подтверждение платежа
//   * @param preCheckoutId
//   * @param ok
//   * @param errorMessage
//   * @return
//   */
//  public ResponseEntity<RspMessageDto> answerPreCheckoutQuery(String preCheckoutId,
//      Boolean ok, String errorMessage) {
//    return request.answerPreCheckoutQuery(preCheckoutId, ok, errorMessage);
//  }

}
