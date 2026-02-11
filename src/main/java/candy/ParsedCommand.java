package candy;

import java.time.LocalDate;

/**
 * Represents a parsed user command.
 * <p>
 * A ParsedCommand stores the command type and its associated parameters.
 * It is responsible for executing the command on the given TaskList.
 */
public class ParsedCommand {
    /** The type of command parsed from user input. */
    public final CommandType type;

    /** Task description (used for todo, deadline, event). */
    public String description; // for todo / deadline / event
    /** Keyword used for find command. */
    public String keyword;     // for find
    /** Start time (used for event). */
    public String fromTime;    // for event
    /** End time (used for event). */
    public String toTime;      // for event
    /** Due date in yyyy-mm-dd format (used for deadline). */
    public String byDate;      // for deadline (yyyy-mm-dd)
    /** Zero-based task index (used for mark, unmark, delete). */
    public int index = -1;

    /**
     * Creates a ParsedCommand with the given command type.
     *
     * @param type The command type.
     */
    public ParsedCommand(CommandType type) {
        this.type = type;
    }

    /**
     * Executes this command using the given task list, UI and storage.
     *
     * @param tasks The TaskList to operate on.
     * @param ui The UI (not heavily used here but passed for consistency).
     * @param storage The Storage object used to persist changes.
     * @return The message to be displayed to the user.
     * @throws CandyException If command execution fails.
     */
    public String execute(TaskList tasks, Ui ui, Storage storage) throws CandyException {
        switch (type) {

        case LIST:
            return tasks.formatForDisplay();

        case TODO:
            tasks.add(new Todo(description));
            storage.saveLines(tasks.toLines());
            return "Added: " + description;

        case DEADLINE:
            LocalDate date = Parser.parseDate(byDate);
            tasks.add(new Deadline(description, date));
            storage.saveLines(tasks.toLines());
            return "Added deadline: " + description + " (by: " + date + ")";

        case EVENT:
            tasks.add(new Event(description, fromTime, toTime));
            storage.saveLines(tasks.toLines());
            return "Added event: " + description + " (from: " + fromTime + " to: " + toTime + ")";

        case MARK:
            tasks.mark(index);
            storage.saveLines(tasks.toLines());
            return "Marked task " + (index + 1);

        case UNMARK:
            tasks.unmark(index);
            storage.saveLines(tasks.toLines());
            return "Unmarked task " + (index + 1);

        case DELETE:
            tasks.remove(index); // if your method is delete(), change this line to tasks.delete(index)
            storage.saveLines(tasks.toLines());
            return "Deleted task " + (index + 1);

        case FIND:
            TaskList result = tasks.find(keyword);
            return result.formatForDisplay();

        case BYE:
            storage.saveLines(tasks.toLines()); // optional
            return "Bye. Hope to see you again soon!";

        case HELP:
            return "Available commands:\n"
                    + "list\n"
                    + "todo <task>\n"
                    + "deadline <task> /by <yyyy-mm-dd>\n"
                    + "event <task> /from <start> /to <end>\n"
                    + "mark <task number>\n"
                    + "unmark <task number>\n"
                    + "delete <task number>\n"
                    + "find <keyword>\n"
                    + "bye";

        default:
            throw new CandyException("Unknown command. Type 'help' to see available commands.");
        }
    }
}
