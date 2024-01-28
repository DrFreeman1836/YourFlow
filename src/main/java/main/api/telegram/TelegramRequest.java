package main.api.telegram;

import main.api.telegram.dto.RspMessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "telegram-api", url = "${bot.url}${bot.token}")
public interface TelegramRequest {

  @GetMapping(value = "/sendmessage")
  ResponseEntity<RspMessageDto> sendMessage(
      @RequestParam("chat_id") Long chatId, @RequestParam("text") String text,
      @RequestParam("parse_mode") String parseMode, @RequestParam("reply_markup") String replyMarkup);

  @PostMapping("/sendPhoto")
  ResponseEntity<RspMessageDto> sendPhoto(
      @RequestParam("chat_id") Long chatId, @RequestParam("photo") String photo,
      @RequestParam("caption") String caption, @RequestParam("parse_mode") String parseMode,
      @RequestParam("reply_markup")String replyMarkup);

  @PostMapping("/sendInvoice")
  ResponseEntity<RspMessageDto> sendInvoice(@RequestParam("chat_id") Long chatId,
      @RequestParam("title") String title, @RequestParam("description") String desc,
      @RequestParam("payload") String payload, @RequestParam("currency") String currency,
      @RequestParam("provider_token") String token, @RequestParam("prices") String price);

  @PostMapping("/answerPreCheckoutQuery")
  ResponseEntity<RspMessageDto> answerPreCheckoutQuery(
      @RequestParam("pre_checkout_query_id") String preCheckoutQuery,
      @RequestParam("ok") Boolean ok, @RequestParam("error_message") String errorMessage
  );

  @PostMapping("/editMessageText")
  ResponseEntity<RspMessageDto> editMessageCaption(@RequestParam("chat_id") Long chatId,
      @RequestParam("message_id") Long messageId, @RequestParam("text") String caption,
      @RequestParam("parse_mode") String parseMode, @RequestParam("reply_markup") String replyMarkup);

  @PostMapping("/deleteMessage")
  ResponseEntity<?> deleteMessage(@RequestParam("chat_id") Long chatId,
      @RequestParam("message_id") Long messageId);

}
