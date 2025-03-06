import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        while (true) {
            System.out.print("$ ");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            String[] split = input.split(" ", 2);
            String command = split[0];

            if (Objects.equals(command, "exit")) {
                break;
            } else if (Objects.equals(command, "echo")) {
                if (input.contains(" ")) {
                    String params = split[1];
                    System.out.println(params);
                }

            } else {
                System.out.println(command + ": command not found");
            }
        }
    }
}
