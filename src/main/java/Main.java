import java.io.File;
import java.util.Arrays;
import java.util.Scanner;


public class Main {
    private static boolean commandCheck(String[] fullCommand) {
        return fullCommand.length != 0;
    }

    private static void handleCommands(String[] fullCommand, String[] paths) {
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
                }
                else {
                    for (String dirname: paths) {
                        String filePath = dirname + "/" + fullCommand[1];
                        File file = new File(filePath);
                        if (file.exists()) {
                            yield fullCommand[1] + " is " + filePath;
                        }
                    }
                    yield fullCommand[1] + ": not found";
                }
            }
            default -> fullCommand[0] + ": command not found";
        };

        System.out.println(output_msg);
    }

    public static void main(String[] args) {
        String[] paths = System.getenv("PATH").split(":");

        Scanner in = new Scanner(System.in);
        String inputCommand;
        String[] fullCommand;

        while (true) {
            System.out.print("$ ");

            inputCommand = in.nextLine();
            fullCommand = inputCommand.split("\\s+");

            if (commandCheck(fullCommand))
                handleCommands(fullCommand, paths);
        }
    }
}
