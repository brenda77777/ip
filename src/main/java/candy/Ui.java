package candy;

import java.util.Scanner;

/**
 * Handles user interaction text for both terminal and GUI.
 * <p>
 * Terminal mode uses:
 * <ul>
 *   <li>{@link #readCommand()}</li>
 *   <li>{@link #showMessage(String)}</li>
 *   <li>{@link #showError(String)}</li>
 * </ul>
 * GUI mode does NOT print directly; it calls the {@code getXxxText(...)} methods
 * and displays the returned strings.
 */
public class Ui {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Reads one trimmed line of input from standard input (terminal mode).
     *
     * @return The trimmed user command.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Prints a normal message to standard output (terminal mode).
     *
     * @param message Message to be printed.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Prints an error message to standard output (terminal mode).
     *
     * @param message Error message to be printed.
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * @return Welcome message shown at startup.
     */
    public String getWelcomeText() {
        return "Hello! I'm Candy\nWhat can I do for you?";
    }

    /**
     * @return Exit message when the user types bye.
     */
    public String getByeText() {
        return "Bye. Hope to see you again soon!";
    }

    /**
     * Builds the text shown after adding a task.
     *
     * @param task The newly added task.
     * @param size The updated total number of tasks.
     * @return Formatted message.
     */
    public String getAddText(Task task, int size) {
        return "Got it. I've added this task:\n"
                + task + "\n"
                + "Now you have " + size + " tasks in the list.";
    }

    /**
     * Builds the text shown after deleting a task.
     *
     * @param task The removed task.
     * @param size The updated total number of tasks.
     * @return Formatted message.
     */
    public String getDeleteText(Task task, int size) {
        return "Noted. I've removed this task:\n"
                + task + "\n"
                + "Now you have " + size + " tasks in the list.";
    }

    /**
     * Builds the text shown after marking a task as done.
     *
     * @param task The task that was marked done.
     * @return Formatted message.
     */
    public String getMarkText(Task task) {
        return "Nice! I've marked this task as done:\n" + task;
    }

    /**
     * Builds the text shown after unmarking a task.
     *
     * @param task The task that was unmarked.
     * @return Formatted message.
     */
    public String getUnmarkText(Task task) {
        return "OK, I've marked this task as not done yet:\n" + task;
    }

    /**
     * Builds the text for listing all tasks.
     *
     * @param tasks The current task list.
     * @return Formatted message.
     */
    public String getListText(TaskList tasks) {
        if (tasks.size() == 0) {
            return "Your list is empty.";
        }
        return "Here are your tasks:\n" + tasks.formatForDisplay();
    }

    /**
     * Builds the text for find results.
     *
     * @param keyword The keyword searched for.
     * @param matches The list of matched tasks.
     * @return Formatted message.
     */
    public String getFindText(String keyword, TaskList matches) {
        if (matches.size() == 0) {
            return "No matching tasks found for: " + keyword;
        }
        return "Here are the matching tasks in your list:\n" + matches.formatForDisplay();
    }

    /**
     * @return Help text listing supported commands.
     */
    public String getHelpText() {
        return "Available commands:\n"
                + "list\n"
                + "todo <task>\n"
                + "deadline <task> /by <yyyy-mm-dd>\n"
                + "event <task> /from <start> /to <end>\n"
                + "mark <task number>\n"
                + "unmark <task number>\n"
                + "delete <task number>\n"
                + "find <keyword>\n"
                + "sort\n"
                + "bye";
    }
}