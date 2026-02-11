package candy;

import java.time.LocalDate;

/**
 * Parses user input and converts it into commands and task objects.
 * <p>
 * This class is responsible for:
 * converting raw text commands into {@link ParsedCommand}, and
 * converting stored file lines into {@link Task} objects (and back).
 */
public class Parser {

    public static ParsedCommand parse(String input) throws CandyException {
        input = input.trim();

        if (input.equals("bye")) {
            return new ParsedCommand(CommandType.BYE);
        }

        if (input.equals("list")) {
            return new ParsedCommand(CommandType.LIST);
        }

        if (input.equals("help")) {
            return new ParsedCommand(CommandType.HELP);
        }

        if (input.startsWith("mark")) {
            ParsedCommand cmd = new ParsedCommand(CommandType.MARK);
            cmd.index = parseIndex(input);
            return cmd;
        }

        if (input.startsWith("unmark")) {
            ParsedCommand cmd = new ParsedCommand(CommandType.UNMARK);
            cmd.index = parseIndex(input);
            return cmd;
        }

        if (input.startsWith("delete")) {
            ParsedCommand cmd = new ParsedCommand(CommandType.DELETE);
            cmd.index = parseIndex(input);
            return cmd;
        }

        if (input.startsWith("todo")) {
            String desc = input.substring(4).trim();
            if (desc.isEmpty()) {
                throw new CandyException("Please use format: todo <task>");
            }
            ParsedCommand cmd = new ParsedCommand(CommandType.TODO);
            cmd.description = desc;
            return cmd;
        }

        if (input.startsWith("deadline")) {
            String rest = input.substring(8).trim();
            String[] parts = rest.split(" /by ", 2);
            if (parts.length < 2) {
                throw new CandyException("Please use format: deadline <task> /by <yyyy-mm-dd>");
            }

            String desc = parts[0].trim();
            String by = parts[1].trim();
            if (desc.isEmpty() || by.isEmpty()) {
                throw new CandyException("Please use format: deadline <task> /by <yyyy-mm-dd>");
            }

            // validate date format
            parseDate(by);

            ParsedCommand cmd = new ParsedCommand(CommandType.DEADLINE);
            cmd.description = desc;
            cmd.byDate = by;
            return cmd;
        }

        if (input.startsWith("event")) {
            String rest = input.substring(5).trim();
            String[] parts = rest.split(" /from ", 2);
            if (parts.length < 2) {
                throw new CandyException("Please use format: event <task> /from <start> /to <end>");
            }

            String desc = parts[0].trim();
            String[] times = parts[1].split(" /to ", 2);
            if (times.length < 2) {
                throw new CandyException("Please use format: event <task> /from <start> /to <end>");
            }

            String from = times[0].trim();
            String to = times[1].trim();

            if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                throw new CandyException("Please use format: event <task> /from <start> /to <end>");
            }

            ParsedCommand cmd = new ParsedCommand(CommandType.EVENT);
            cmd.description = desc;
            cmd.fromTime = from;
            cmd.toTime = to;
            return cmd;
        }

        if (input.startsWith("find")) {
            String keyword = input.substring(4).trim();
            if (keyword.isEmpty()) {
                throw new CandyException("Please use format: find <keyword>");
            }
            ParsedCommand cmd = new ParsedCommand(CommandType.FIND);
            cmd.keyword = keyword;
            return cmd;
        }

        if (input.equals("sort")) {
            return new ParsedCommand(CommandType.SORT);
        }

        throw new CandyException("Unknown command. Type 'help' to see available commands");
    }

    /**
     * Extracts a task index from commands like "mark 2".
     *
     * @param input Full command string.
     * @return Zero-based index.
     * @throws CandyException If index is missing or invalid.
     */
    public static int parseIndex(String input) throws CandyException {
        String[] parts = input.trim().split("\\s+");
        if (parts.length < 2) {
            throw new CandyException("Please provide a task number.");
        }
        try {
            int oneBased = Integer.parseInt(parts[1]);
            int idx = oneBased - 1;
            if (idx < 0) {
                throw new CandyException("Task number must be >= 1.");
            }
            return idx;
        } catch (NumberFormatException e) {
            throw new CandyException("Please enter a valid task number.");
        }
    }

    /**
     * Parses a yyyy-mm-dd date string into LocalDate.
     *
     * @param s Date string.
     * @return Parsed LocalDate.
     * @throws CandyException If format is invalid.
     */
    public static LocalDate parseDate(String s) throws CandyException {
        try {
            return LocalDate.parse(s); // expects yyyy-mm-dd
        } catch (Exception e) {
            throw new CandyException("Date must be yyyy-mm-dd (example: 2019-10-15)");
        }
    }

    /**
     * Converts a saved file line into a Task object.
     *
     * @param line Line from storage file.
     * @return Task object or null if invalid format.
     * @throws CandyException If date format is invalid.
     */

    public static Task parseLine(String line) throws CandyException {
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String desc = parts[2].trim();

        Task t;
        switch (type) {
        case "T":
            t = new Todo(desc);
            break;
        case "D":
            if (parts.length < 4) {
                return null;
            }
            LocalDate by = parseDate(parts[3].trim());
            t = new Deadline(desc, by);
            break;
        case "E":
            if (parts.length < 5) {
                return null;
            }
            t = new Event(desc, parts[3].trim(), parts[4].trim());
            break;
        default:
            return null;
        }

        if (isDone) {
            t.markDone();
        }
        return t;
    }

    /**
     * Converts a Task into a storage-friendly string format.
     *
     * @param t Task to convert.
     * @return Formatted storage line.
     */
    public static String toLine(Task t) {
        String done = t.isDone() ? "1" : "0";

        if (t instanceof Todo) {
            return "T | " + done + " | " + t.getDescription();
        } else if (t instanceof Deadline) {
            Deadline d = (Deadline) t;
            return "D | " + done + " | " + d.getDescription() + " | " + d.getBy().toString();
        } else if (t instanceof Event) {
            Event e = (Event) t;
            return "E | " + done + " | " + e.getDescription() + " | " + e.getFrom() + " | " + e.getTo();
        } else {
            return "T | " + done + " | " + t.getDescription();
        }
    }
}
