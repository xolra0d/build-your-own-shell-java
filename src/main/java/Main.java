import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        while (true) {
            System.out.print("$ ");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            String[] split_input = input.split(" ");

            if (Objects.equals(split_input[0], "exit")) {
                break;
            }
        }
    }
}
