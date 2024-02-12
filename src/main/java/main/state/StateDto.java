package main.state;

import lombok.Data;
import main.command.AbstractCommand;
import org.telegram.telegrambots.meta.api.objects.Update;

@Data
public class StateDto {

  private AbstractCommand command;

  private Update update;

  private String name;

  private String callbackQueryData;

}
