import java.util.Arrays;
import java.util.Scanner;


public class Main {
    private static boolean commandCheck(String[] fullCommand) {
        return fullCommand.length != 0;
    }

    private static void handleCommands(String[] fullCommand) {
        Command command = Command.fromString(fullCommand[0]);

        String output_msg = switch (command) {
            case ECHO -> String.join(" ", Arrays.copyOfRange(fullCommand, 1, fullCommand.length));
            case EXIT -> {
                try {
                    int exitCode = Integer.parseInt(fullCommand[1]);
                    System.exit(exitCode);
                    yield "";
                } catch (NumberFormatException e) {
                    yield  "Invalid exit code. Please provide a valid integer.";
                }
            }
            case TYPE -> {
                Command command_type = Command.fromString(fullCommand[1]);
                if (command_type != Command.NOT_FOUND) {
                    yield command_type.getCommandName() + " is a shell builtin";
                } else {
                    yield fullCommand[0] + ": command not found";
                }
            }

            default -> fullCommand[0] + ": command not found";
        };

        System.out.println(output_msg);
    }

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        String inputCommand;
        String[] fullCommand;

        while (true) {
            System.out.print("$ ");

            inputCommand = in.nextLine();
            fullCommand = inputCommand.split("\\s+");

            if (commandCheck(fullCommand))
                handleCommands(fullCommand);
        }
    }
}
