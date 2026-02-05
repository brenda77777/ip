package candy;

import java.time.LocalDate;

public class ParsedCommand {
    public final CommandType type;
    public String arg1;
    public String arg2;
    public String arg3; // used for event end time
    public int index = -1;

    public ParsedCommand(CommandType type) {
        this.type = type;
    }

    public String execute(TaskList tasks, Ui ui, Storage storage) throws CandyException {
        switch (type) {

        case LIST:
            return tasks.formatForDisplay();

        case TODO:
            tasks.add(new Todo(arg1));
            storage.saveLines(tasks.toLines());
            return "Added: " + arg1;

        case DEADLINE:
            LocalDate date = Parser.parseDate(arg2);
            tasks.add(new Deadline(arg1, date));
            storage.saveLines(tasks.toLines());
            return "Added deadline: " + arg1 + " (by: " + date + ")";

        case EVENT:
            tasks.add(new Event(arg1, arg2, arg3));
            storage.saveLines(tasks.toLines());
            return "Added event: " + arg1 + " (from: " + arg2 + " to: " + arg3 + ")";

        case MARK:
            tasks.mark(index);
            storage.saveLines(tasks.toLines());
            return "Marked task " + (index + 1);

        case UNMARK:
            tasks.unmark(index);
            storage.saveLines(tasks.toLines());
            return "Unmarked task " + (index + 1);

        case DELETE:
            tasks.remove(index);   // IMPORTANT: your TaskList likely calls it delete()
            storage.saveLines(tasks.toLines());
            return "Deleted task " + (index + 1);

        case FIND:
            TaskList result = tasks.find(arg1);
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