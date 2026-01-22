import java.util.Scanner;

public class Candy {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Task[] tasks = new Task[100];
        int noTask = 0;

        System.out.println("Hello! I'm Candy");
        System.out.println("What can I do for you?");

        while (true) {
            String input = scanner.nextLine().trim();

            try {
                // EXIT
                if (input.equals("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    break;
                }
                // LIST
                if (input.equals("list")) {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < noTask; i++) {
                        System.out.println((i + 1) + ". " + tasks[i]);
                    }
                    continue;
                }
                // MARK DONE
                if (input.startsWith("mark")) {
                    String[] splited = input.split(" ");

                    if (splited.length < 2) {
                        throw new CandyException("Please use format: mark <task number>");
                    }

                    int idx = Integer.parseInt(splited[1]) - 1;

                    if (idx < 0 || idx >= noTask) {
                        throw new CandyException("Task number does not exist.");
                    }

                    tasks[idx].markDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println(tasks[idx]);
                    continue;
                }
                // UNMARK
                if (input.startsWith("unmark")) {
                    String[] splited = input.split(" ");

                    if (splited.length < 2) {
                        throw new CandyException("Please use format: unmark <task number>");
                    }

                    int idx = Integer.parseInt(splited[1]) - 1;

                    if (idx < 0 || idx >= noTask) {
                        throw new CandyException("Task number does not exist.");
                    }

                    tasks[idx].unmark();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println(tasks[idx]);
                    continue;
                }
                // todolist
                if (input.startsWith("todo")) {
                    if (input.equals("todo")) {
                        throw new CandyException("Please use format: todo <task>");
                    }

                    String name = input.substring(4).trim();

                    if (name.isEmpty()) {
                        throw new CandyException("Please use format: todo <task>");
                    }

                    tasks[noTask] = new Todo(name);
                    noTask++;

                    System.out.println("Got it. I've added this task:");
                    System.out.println(tasks[noTask - 1]);
                    System.out.println("Now you have " + noTask + " tasks in the list.");
                    continue;
                }
                // DEADLINE
                if (input.startsWith("deadline")) {
                    if (input.equals("deadline")) {
                        throw new CandyException("Please use format: deadline <task> /by <time>");
                    }

                    String rest = input.substring(8).trim(); // after "deadline"
                    String[] splited = rest.split(" /by ", 2);

                    if (splited.length < 2) {
                        throw new CandyException("Please use format: deadline <task> /by <time>");
                    }

                    String name = splited[0].trim();
                    String by = splited[1].trim();

                    if (name.isEmpty() || by.isEmpty()) {
                        throw new CandyException("Please use format: deadline <task> /by <time>");
                    }

                    tasks[noTask] = new Deadline(name, by);
                    noTask++;

                    System.out.println("Got it. I've added this task:");
                    System.out.println(tasks[noTask - 1]);
                    System.out.println("Now you have " + noTask + " tasks in the list.");
                    continue;
                }
                // EVENT
                if (input.startsWith("event")) {
                    if (input.equals("event")) {
                        throw new CandyException("Please use format: event <task> /from <start> /to <end>");
                    }

                    String rest = input.substring(5).trim(); // skip event

                    if (!rest.contains(" /from ") || !rest.contains(" /to ")) {
                        throw new CandyException("Please use format: event <task> /from <start> /to <end>");
                    }

                    String[] fromSplit = rest.split(" /from ", 2);
                    String name = fromSplit[0].trim();

                    String[] toSplit = fromSplit[1].split(" /to ", 2);
                    if (toSplit.length < 2) {
                        throw new CandyException("Please use format: event <task> /from <start> /to <end>");
                    }

                    String dateFrom = toSplit[0].trim();
                    String dateTo = toSplit[1].trim();

                    if (name.isEmpty() || dateFrom.isEmpty() || dateTo.isEmpty()) {
                        throw new CandyException("Please use format: event <task> /from <start> /to <end>");
                    }

                    tasks[noTask] = new Event(name, dateFrom, dateTo);
                    noTask++;

                    System.out.println("Got it. I've added this task:");
                    System.out.println(tasks[noTask - 1]);
                    System.out.println("Now you have " + noTask + " tasks in the list.");
                    continue;
                }
                // UNKNOWN COMMAND
                throw new CandyException("OOPS!!! I'm sorry, but I don't know what that means :-(");

            } catch (CandyException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Please use format: mark <task number>");
            } catch (Exception e) {
                System.out.println("Something went wrong. Please try again.");
            }
        }
        scanner.close();
    }
}
