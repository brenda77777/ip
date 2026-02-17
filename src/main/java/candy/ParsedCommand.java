package candy;

import java.time.LocalDate;

/**
 * Represents a parsed user command.
 * <p>
 * A ParsedCommand is created by {@link Parser} and contains:
 * <ul>
 *   <li>{@link #type} - what command to run</li>
 *   <li>extra fields (description, index, etc.) needed to execute the command</li>
 * </ul>
 * Execution returns a String message to display (terminal or GUI).
 */
public class ParsedCommand {
    /** The command type. */
    public final CommandType type;

    /** Task description for todo/deadline/event. */
    public String description;

    /** Keyword for find. */
    public String keyword;

    /** Start time for event. */
    public String fromTime;

    /** End time for event. */
    public String toTime;

    /** Due date string for deadline (yyyy-mm-dd). */
    public String byDate;

    /** Zero-based task index for mark/unmark/delete. */
    public int index = -1;

    /**
     * Creates a ParsedCommand of the given type.
     *
     * @param type Command type.
     */
    public ParsedCommand(CommandType type) {
        this.type = type;
    }

    /**
     * Executes this command using the given task list, UI helper, and storage.
     * Returns a message string to be displayed by either terminal or GUI.
     *
     * @param tasks Task list to operate on.
     * @param ui UI helper that formats messages.
     * @param storage Storage used to persist tasks.
     * @return Message to display to the user.
     * @throws CandyException If execution fails (e.g., invalid index).
     */
    public String execute(TaskList tasks, Ui ui, Storage storage) throws CandyException {
        switch (type) {
        case LIST:
            return ui.getListText(tasks);

        case TODO: {
            Task task = new Todo(description);
            tasks.add(task);
            storage.saveLines(tasks.toLines());
            return ui.getAddText(task, tasks.size());
        }

        case DEADLINE: {
            LocalDate date = Parser.parseDate(byDate);
            Task task = new Deadline(description, date);
            tasks.add(task);
            storage.saveLines(tasks.toLines());
            return ui.getAddText(task, tasks.size());
        }

        case EVENT: {
            Task task = new Event(description, fromTime, toTime);
            tasks.add(task);
            storage.saveLines(tasks.toLines());
            return ui.getAddText(task, tasks.size());
        }

        case MARK: {
            tasks.mark(index);
            storage.saveLines(tasks.toLines());
            return ui.getMarkText(tasks.get(index));
        }

        case UNMARK: {
            tasks.unmark(index);
            storage.saveLines(tasks.toLines());
            return ui.getUnmarkText(tasks.get(index));
        }

        case DELETE: {
            Task removed = tasks.remove(index);
            storage.saveLines(tasks.toLines());
            return ui.getDeleteText(removed, tasks.size());
        }

        case FIND: {
            TaskList matches = tasks.find(keyword);
            return ui.getFindText(keyword, matches);
        }

        case SORT:
            return tasks.formatSortedForDisplay();

        case HELP:
            return ui.getHelpText();

        case BYE:
            storage.saveLines(tasks.toLines());
            return ui.getByeText();

        default:
            throw new CandyException("Unknown command. Type 'help' to see available commands.");
        }
    }
}