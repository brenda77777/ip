import java.util.Scanner;

public class Candy {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] tasks = new String[100];
        int noTask = 0;

        System.out.println("Hello! I'm Candy");
        System.out.println("What can I do for you?");

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            } else if (input.equals("list")) {
                for (int i = 0; i < noTask ; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
            } else {
                tasks[noTask] = input;
                noTask++;

                System.out.println("added: " + input);
            }
        }
        scanner.close();
    }
}
