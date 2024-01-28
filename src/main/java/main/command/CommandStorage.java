package main.command;

import java.util.HashMap;

public class CommandStorage {

  private static final HashMap<String, AbstractCommand> mapCommand = new HashMap<>();

  public static void addCommand(String name, AbstractCommand command) {
    mapCommand.put(name, command);
  }

  public static HashMap<String, AbstractCommand> getMapCommand() {
    return mapCommand;
  }

}
