// ParsedCommand.java
package candy;

import java.time.LocalDate;

public class ParsedCommand {
    public final CommandType type;


    public String description; // for todo / deadline / event
    public String keyword;     // for find
    public String fromTime;    // for event
    public String toTime;      // for event
    public String byDate;      // for deadline (yyyy-mm-dd)

    public int index = -1;

    public ParsedCommand(CommandType type) {
        this.type = type;
    }

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
