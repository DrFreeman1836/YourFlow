package main.api.telegram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RspMessageDto {

  private Boolean ok;
  private Result result;

  @Data
  public class Result {

    @JsonProperty("message_id")
    private Long MessageId;
    private From from;
    private Chat chat;
    private Integer date;
    private String text;

    @Data
    public class From {
      private Long id;
      private String firstName;
      private String username;
    }

    @Data
    public class Chat {
      private Long id;
      private String firstName;
      private String lastName;
      private String username;
      private String type;
    }

  }

}
