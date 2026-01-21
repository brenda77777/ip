import java.util.Scanner;

public class Candy {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Task[] tasks = new Task[100];
        int noTask = 0;

        System.out.println("Hello! I'm Candy");
        System.out.println("What can I do for you?");

        while (true) {
            String input = scanner.nextLine();
            // EXIT
            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            } else if (input.equals("list")) { //LIST
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < noTask; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
            } else if (input.startsWith("mark")) { // MARK DONE
                String[] parts = input.split(" ");
                int idx = Integer.parseInt(parts[1]) - 1;

                tasks[idx].markDone();

                System.out.println("Nice! I've marked this task as done:");
                System.out.println(tasks[idx]);
            } else if (input.startsWith("unmark")) { // UNMARK
                String[] parts = input.split(" ");
                int idx = Integer.parseInt(parts[1]) - 1;

                tasks[idx].unmark();

                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println(tasks[idx]);
            } else { //ADD TASK
                tasks[noTask] = new Task(input);
                noTask++;

                System.out.println("added: " + input);
            }
        }
        scanner.close();
    }
}
