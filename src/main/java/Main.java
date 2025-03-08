import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;


public class Main {
    private static File currentDirectory = new File(System.getProperty("user.dir"));

    private static String type(String command) {
        Command command_type = Command.fromString(command);
        if (command_type != Command.NOT_FOUND) {
            return command_type.getCommandName() + " is a shell builtin";
        }
        else {
            Optional<String> filePath = getFilePath(command);
            if (filePath.isPresent()) {
                return command + " is " + filePath.get();
            }
            return command + ": not found";
        }
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
            if (file.exists()) {
                return Optional.of(dirname + "/" + filename);
            }
        }
        return Optional.empty();
    }
    private static String notFoundOrCall(String[] fullCommand) {
        // either not found or call of external command
        Optional<String> filePath = getFilePath(fullCommand[0]);
        if (filePath.isEmpty()) {
            return fullCommand[0] + ": command not found";
        }
        executeBinary(fullCommand);
        return "";
    }
    private static String exit(int exitCode) {
        try {
            System.exit(exitCode);
            return "";
        } catch (NumberFormatException e) {
            throw new ArithmeticException("Invalid exit code. Please provide a valid integer.");
        }
    }
    private static String cd(String newPath) {
        if (!(new File(newPath).exists())) {
            return "cd: " + newPath + ": No such file or directory";
        }
        currentDirectory = new File(newPath);
        return "";
    }

    private static void handleCommands(String[] fullCommand) {
        Command command = Command.fromString(fullCommand[0]);

        String output_msg = switch (command) {
            case ECHO -> String.join(" ", Arrays.copyOfRange(fullCommand, 1, fullCommand.length));
            case EXIT -> exit(Integer.parseInt(fullCommand[1]));
            case TYPE -> type(fullCommand[1]);
            case PWD -> currentDirectory.getAbsolutePath();
            case CD -> cd(fullCommand[1]);
            default -> notFoundOrCall(fullCommand);
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

            handleCommands(fullCommand);
        }
    }
}
