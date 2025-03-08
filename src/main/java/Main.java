import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;


public class Main {
    private static boolean commandCheck(String[] fullCommand) {
        return fullCommand.length != 0;
    }

    private static void executeBinary(String[] params) {
        ProcessBuilder processBuilder = new ProcessBuilder(params);

        try {
            Process process = processBuilder.start();
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = stdoutReader.readLine()) != null) {
                System.out.println(line);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Optional<String> getFilePath(String filename) {
        String[] paths = System.getenv("PATH").split(":");
        for (String dirname: paths) {
            String filePath = dirname + "/" + filename;
            File file = new File(filePath);
//            System.out.println(filePath);
            if (file.exists()) {
                return Optional.of(dirname + "/" + filename);
            }
        }
        return Optional.empty();
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
                }
                else {
                    Optional<String> filePath = getFilePath(fullCommand[1]);
                    if (filePath.isPresent()) {
                        yield fullCommand[1] + " is " + filePath.get();
                    }
                    yield fullCommand[1] + ": not found";
                }
            }
            case PWD -> {
                yield System.getProperty("user.dir");
            }
            default -> { // either not found or call of external command
                Optional<String> filePath = getFilePath(fullCommand[0]);
                if (filePath.isEmpty()) {
                    yield fullCommand[0] + ": command not found";
                }
                executeBinary(fullCommand);
                yield "";
            }
        };
        if (!output_msg.isEmpty()) {
            System.out.println(output_msg);
        }
    }

    public static void main(String[] args) {
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
