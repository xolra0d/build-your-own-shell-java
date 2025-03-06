public enum Command {
    ECHO("echo"),
    EXIT("exit"),
    TYPE("type"),
    NOT_FOUND("notFound");

    private final String commandName;

    Command(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

    public static Command fromString(String command) {
        for (Command cmd : Command.values()) {
            if (cmd.getCommandName().equals(command)) {
                return cmd;
            }
        }
        return NOT_FOUND;
    }
}
