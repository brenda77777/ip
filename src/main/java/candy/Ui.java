package candy;

import java.util.Scanner;

/**
 * Handles all user interface interactions for the Candy application.
 * <p>
 * This class is responsible for reading user input and displaying
 * messages, errors, and task-related outputs to the console.
 */
public class Ui {

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Displays the welcome message when the program starts.
     */
    public void showWelcome() {
        System.out.println("Hello! I'm Candy");
        System.out.println("What can I do for you?");
    }

    /**
     * Reads a command input from the user.
     *
     * @return Trimmed user input string
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Displays a normal message to the user.
     *
     * @param msg Message to be displayed
     */
    public void showMessage(String msg) {
        System.out.println(msg);
    }

    /**
     * Displays an error message to the user.
     *
     * @param msg Error message to be displayed
     */
    public void showError(String msg) {
        System.out.println(msg);
    }

    /**
     * Displays the full list of tasks.
     *
     * @param tasks TaskList containing all tasks
     */
    public void showList(TaskList tasks) {
        if (tasks.size() == 0) {
            System.out.println("Your list is empty.");
            return;
        }

        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            try {
                System.out.println((i + 1) + ". " + tasks.get(i));
            } catch (CandyException e) {
                System.out.println("Error displaying task " + (i + 1));
            }
        }
    }

    /**
     * Displays confirmation after a task is added.
     *
     * @param task Task that was added
     * @param size Updated total number of tasks
     */
    public void showAdd(Task task, int size) {
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.println("Now you have " + size + " tasks in the list.");
    }

    /**
     * Displays confirmation after a task is deleted.
     *
     * @param task Task that was removed
     * @param size Updated total number of tasks
     */
    public void showDelete(Task task, int size) {
        System.out.println("Noted. I've removed this task:");
        System.out.println(task);
        System.out.println("Now you have " + size + " tasks in the list.");
    }

    /**
     * Displays confirmation after a task is marked as done.
     *
     * @param task Task that was marked
     */
    public void showMark(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
    }

    /**
     * Displays confirmation after a task is unmarked.
     *
     * @param task Task that was unmarked
     */
    public void showUnmark(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task);
    }

    /**
     * Displays the list of tasks matching a keyword.
     *
     * @param keyword Search keyword
     * @param matches TaskList containing matched tasks
     */
    public void showFindResults(String keyword, TaskList matches) {
        if (matches.size() == 0) {
            System.out.println("No matching tasks found for: " + keyword);
            return;
        }

        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            try {
                System.out.println((i + 1) + ". " + matches.get(i));
            } catch (CandyException e) {
                System.out.println("Error displaying match " + (i + 1));
            }
        }
    }
}