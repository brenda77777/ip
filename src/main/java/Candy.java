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
            } else if (input.equals("list")) { // LIST
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
            } else if (input.startsWith("todo ")) { // todolist
                String desc = input.substring(5);

                tasks[noTask] = new Todo(desc);
                noTask++;

                System.out.println("Got it. I've added this task:");
                System.out.println(tasks[noTask - 1]);
                System.out.println("Now you have " + noTask + " tasks in the list.");
            } else if (input.startsWith("deadline ")) {  // DEADLINE
                String rest = input.substring(9);
                String[] splited = rest.split(" /by ");

                if (splited.length < 2) {
                    System.out.println("Please use format: deadline <task> /by <time>");
                    continue;
                }

                String desc = splited[0];
                String by = splited[1];

                tasks[noTask] = new Deadline(desc, by);
                noTask++;

                System.out.println("Got it. I've added this task:");
                System.out.println(tasks[noTask - 1]);
                System.out.println("Now you have " + noTask + " tasks in the list.");
            } else if (input.startsWith("event ")) { // EVENT
                String rest = input.substring(6);

                if (!rest.contains(" /from ") || !rest.contains(" /to ")) {
                    System.out.println("Please use format: event <task> /from <start> /to <end>");
                    continue;
                }

                String[] fromString = rest.split(" /from ");
                String name = fromString[0];

                String[] part2 = fromString[1].split(" /to ");
                String dateFrom = part2[0];
                String dateTo = part2[1];

                tasks[noTask] = new Event(name, dateFrom, dateTo);
                noTask++;

                System.out.println("Got it. I've added this task:");
                System.out.println(tasks[noTask - 1]);
                System.out.println("Now you have " + noTask + " tasks in the list.");
            } else { // FALLBACK
                System.out.println("Sorry, I don't understand that command.");
            }
        }
        scanner.close();
    }
}
