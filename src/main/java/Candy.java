import java.time.LocalDate;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Candy {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();
        //System.out.println("Working dir = " + System.getProperty("user.dir"));
        Storage storage = new Storage("data/candy.txt");

        try {
            List<String> lines = storage.loadLines();
            for (String line : lines) {
                Task t = lineToTask(line);
                if (t != null) {
                    tasks.add(t);
                }
            }
        } catch (Exception e) {
            System.out.println("Warning: Could not load data");
        }

        System.out.println("Hello! I'm Candy");
        System.out.println("What can I do for you?");

        while (true) {
            String input = scanner.nextLine().trim();
            try {
                // EXIT
                if (input.equals("bye")) {
                    saveAll(storage, tasks);
                    System.out.println("Bye. Hope to see you again soon!");
                    break;
                }


                // LIST
                if (input.equals("list")) {
                    System.out.println("Here are the tasks in your list:");

                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + ". " + tasks.get(i));
                    }
                    continue;
                }

                // MARK
                if (input.startsWith("mark")) {
                    String[] splited = input.split(" ");

                    if (splited.length < 2) {
                        throw new CandyException("Please use format: mark <task number>");
                    }

                    int idx = Integer.parseInt(splited[1]) - 1;

                    if (idx < 0 || idx >= tasks.size()) {
                        throw new CandyException("Task number does not exist.");
                    }

                    tasks.get(idx).markDone();

                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println(tasks.get(idx));
                    continue;
                }

                // UNMARK
                if (input.startsWith("unmark")) {
                    String[] splited = input.split(" ");

                    if (splited.length < 2) {
                        throw new CandyException("Please use format: unmark <task number>");
                    }

                    int idx = Integer.parseInt(splited[1]) - 1;

                    if (idx < 0 || idx >= tasks.size()) {
                        throw new CandyException("Task number does not exist.");
                    }

                    tasks.get(idx).unmark();

                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println(tasks.get(idx));
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

                    tasks.add(new Todo(name));

                    System.out.println("Got it. I've added this task:");
                    System.out.println(tasks.get(tasks.size() - 1));
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    continue;
                }

                // DEADLINE
                if (input.startsWith("deadline")) {
                    if (input.equals("deadline")) {
                        throw new CandyException("Please use format: deadline <task> /by <time>");
                    }

                    String rest = input.substring(8).trim();

                    String[] splited = rest.split(" /by ", 2);

                    if (splited.length < 2) {
                        throw new CandyException("Please use format: deadline <task> /by <time>");
                    }

                    String name = splited[0].trim();
                    String by = splited[1].trim();

                    if (name.isEmpty() || by.isEmpty()) {
                        throw new CandyException("Please use format: deadline <task> /by <time>");
                    }

                    tasks.add(new Deadline(name, by));

                    System.out.println("Got it. I've added this task:");
                    System.out.println(tasks.get(tasks.size() - 1));
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    continue;
                }

                // EVENT
                if (input.startsWith("event")) {
                    if (input.equals("event")) {
                        throw new CandyException("Please use format: event <task> /from <start> /to <end>");
                    }

                    String rest = input.substring(5).trim();

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

                    tasks.add(new Event(name, dateFrom, dateTo));

                    System.out.println("Got it. I've added this task:");
                    System.out.println(tasks.get(tasks.size() - 1));
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    continue;
                }

                // DELETE
                if (input.startsWith("delete")) {
                    String[] splited = input.split(" ");

                    if (splited.length < 2) {
                        throw new CandyException("Please use format: delete <task number>");
                    }

                    int idx = Integer.parseInt(splited[1]) - 1;

                    if (idx < 0 || idx >= tasks.size()) {
                        throw new CandyException("Task number does not exist.");
                    }

                    Task removed = tasks.remove(idx);

                    System.out.println("Noted. I've removed this task:");
                    System.out.println(removed);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    continue;
                }
                // UNKNOWN COMMAND
                throw new CandyException("OOPS!!! I'm sorry, but I don't know what that means :-(");

            }
            catch (CandyException e) {
                System.out.println(e.getMessage());
            }
            catch (NumberFormatException e) {
                System.out.println("Please enter a valid task number.");
            }
            catch (Exception e) {
                System.out.println("Something went wrong. Please try again.");
            }
        }
        scanner.close();
    }
    private static void saveAll(Storage storage, ArrayList<Task> tasks) {
        List<String> lines = new ArrayList<>();
        for (Task t : tasks) {
            lines.add(taskToLine(t));
        }
        storage.saveLines(lines);
    }

    private static String taskToLine(Task t) {
        String done = t.isDone() ? "1" : "0";

        if (t instanceof Todo) {
            return "T | " + done + " | " + t.getDescription();
        } else if (t instanceof Deadline) {
            Deadline d = (Deadline) t;
            return "D | " + done + " | " + d.getDescription() + " | " + d.getBy();
        } else if (t instanceof Event) {
            Event e = (Event) t;
            return "E | " + done + " | " + e.getDescription() + " | " + e.getFrom() + " | " + e.getTo();
        } else {
            return "T | " + done + " | " + t.getDescription();
        }
    }

    private static Task lineToTask(String line) {
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String desc = parts[2];

        Task t;
        if (type.equals("T")) {
            t = new Todo(desc);
        } else if (type.equals("D")) {
            if (parts.length < 4) return null;
            t = new Deadline(desc, parts[3]);
        } else if (type.equals("E")) {
            if (parts.length < 5) return null;
            t = new Event(desc, parts[3], parts[4]);
        } else {
            t = new Todo(desc);
        }

        if (isDone) {
            t.markDone();
        }
        return t;
    }
}
