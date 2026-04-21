package aston.intensiv.userservice;

public enum Command {
    CREATE,
    GET,
    GET_ALL,
    UPDATE,
    DELETE,
    HELP,
    EXIT;

    public static Command fromString(String command) {
        try {
            return Command.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
