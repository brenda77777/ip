package candy;

import java.time.LocalDate;

/**
 * Parses raw user input into structured {@link ParsedCommand} objects.
 * <p>
 * Also supports converting between save-file lines and {@link Task} objects.
 */
public class Parser {

    /**
     * Parses one user input line into a {@link ParsedCommand}.
     *
     * @param input Raw user input.
     * @return ParsedCommand containing command type and required parameters.
     * @throws CandyException If input is empty or command format is invalid.
     */
    public static ParsedCommand parse(String input) throws CandyException {
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            throw new CandyException("Please enter a command. Type 'help' to see available commands.");
        }

        String command = getCommand(trimmed);

        switch (command) {
        case "bye":
            return new ParsedCommand(CommandType.BYE);
        case "list":
            return new ParsedCommand(CommandType.LIST);
        case "help":
            return new ParsedCommand(CommandType.HELP);
        case "sort":
            return new ParsedCommand(CommandType.SORT);
        case "mark":
            return parseIndexCommand(trimmed, CommandType.MARK);
        case "unmark":
            return parseIndexCommand(trimmed, CommandType.UNMARK);
        case "delete":
            return parseIndexCommand(trimmed, CommandType.DELETE);
        case "todo":
            return parseTodo(trimmed);
        case "deadline":
            return parseDeadline(trimmed);
        case "event":
            return parseEvent(trimmed);
        case "find":
            return parseFind(trimmed);
        default:
            throw new CandyException("Unknown command. Type 'help' to see available commands.");
        }
    }

    /**
     * Extracts the command word (first token) from an input line.
     *
     * @param input Trimmed user input.
     * @return Command word.
     */
    private static String getCommand(String input) {
        String[] parts = input.split("\\s+", 2);
        return parts[0];
    }

    /**
     * Extracts everything after the command word.
     *
     * @param input Trimmed user input.
     * @return Arguments string (may be empty).
     */
    private static String getArguments(String input) {
        String[] parts = input.split("\\s+", 2);
        return parts.length < 2 ? "" : parts[1].trim();
    }

    /**
     * Parses commands that require an index: mark/unmark/delete.
     *
     * @param input Full user input.
     * @param type Command type to assign.
     * @return ParsedCommand containing the parsed index.
     * @throws CandyException If index is missing or invalid.
     */
    private static ParsedCommand parseIndexCommand(String input, CommandType type) throws CandyException {
        ParsedCommand parsedCommand = new ParsedCommand(type);
        parsedCommand.index = parseIndex(input);
        return parsedCommand;
    }

    /**
     * Parses a todo command: todo &lt;description&gt;
     */
    private static ParsedCommand parseTodo(String input) throws CandyException {
        String description = getArguments(input);
        if (description.isEmpty()) {
            throw new CandyException("Please use format: todo <task>");
        }

        ParsedCommand parsedCommand = new ParsedCommand(CommandType.TODO);
        parsedCommand.description = description;
        return parsedCommand;
    }

    /**
     * Parses a deadline command: deadline &lt;description&gt; /by &lt;yyyy-mm-dd&gt;
     */
    private static ParsedCommand parseDeadline(String input) throws CandyException {
        String arguments = getArguments(input);
        String[] parts = arguments.split(" /by ", 2);

        if (parts.length < 2) {
            throw new CandyException("Please use format: deadline <task> /by <yyyy-mm-dd>");
        }

        String description = parts[0].trim();
        String byDate = parts[1].trim();

        if (description.isEmpty() || byDate.isEmpty()) {
            throw new CandyException("Please use format: deadline <task> /by <yyyy-mm-dd>");
        }

        parseDate(byDate); // validate format

        ParsedCommand parsedCommand = new ParsedCommand(CommandType.DEADLINE);
        parsedCommand.description = description;
        parsedCommand.byDate = byDate;
        return parsedCommand;
    }

    /**
     * Parses an event command: event &lt;description&gt; /from &lt;start&gt; /to &lt;end&gt;
     */
    private static ParsedCommand parseEvent(String input) throws CandyException {
        String arguments = getArguments(input);
        String[] parts = arguments.split(" /from ", 2);

        if (parts.length < 2) {
            throw new CandyException("Please use format: event <task> /from <start> /to <end>");
        }

        String description = parts[0].trim();

        String[] times = parts[1].split(" /to ", 2);
        if (times.length < 2) {
            throw new CandyException("Please use format: event <task> /from <start> /to <end>");
        }

        String fromTime = times[0].trim();
        String toTime = times[1].trim();

        LocalDate fromDate = parseDate(fromTime);
        LocalDate toDate = parseDate(toTime);

        if (fromDate.isAfter(toDate)) {
            throw new CandyException("Event start date must be before end date.");
        }

        if (description.isEmpty() || fromTime.isEmpty() || toTime.isEmpty()) {
            throw new CandyException("Please use format: event <task> /from <start> /to <end>");
        }

        ParsedCommand parsedCommand = new ParsedCommand(CommandType.EVENT);
        parsedCommand.description = description;
        parsedCommand.fromTime = fromTime;
        parsedCommand.toTime = toTime;
        return parsedCommand;
    }

    /**
     * Parses a find command: find &lt;keyword&gt;
     */
    private static ParsedCommand parseFind(String input) throws CandyException {
        String keyword = getArguments(input);
        if (keyword.isEmpty()) {
            throw new CandyException("Please use format: find <keyword>");
        }

        ParsedCommand parsedCommand = new ParsedCommand(CommandType.FIND);
        parsedCommand.keyword = keyword;
        return parsedCommand;
    }

    /**
     * Parses a 1-based task number from input (e.g., "mark 2") and converts it to 0-based index.
     *
     * @param input Full command string.
     * @return 0-based index.
     * @throws CandyException If task number is missing, non-numeric, or &lt; 1.
     */
    public static int parseIndex(String input) throws CandyException {
        String[] parts = input.trim().split("\\s+");
        if (parts.length < 2) {
            throw new CandyException("Please provide a task number.");
        }

        try {
            int oneBased = Integer.parseInt(parts[1]);
            int index = oneBased - 1;

            if (index < 0) {
                throw new CandyException("Task number must be >= 1.");
            }
            return index;

        } catch (NumberFormatException e) {
            throw new CandyException("Please enter a valid task number.");
        }
    }

    /**
     * Parses a date string in yyyy-mm-dd format.
     *
     * @param dateString Date string.
     * @return Parsed {@link LocalDate}.
     * @throws CandyException If format is invalid.
     */
    public static LocalDate parseDate(String dateString) throws CandyException {
        try {
            return LocalDate.parse(dateString);
        } catch (Exception e) {
            throw new CandyException("Date must be yyyy-mm-dd (example: 2019-10-15)");
        }
    }

    /**
     * Converts a saved file line into a {@link Task}.
     *
     * @param line One line from the save file.
     * @return Task object, or null if line format is invalid.
     * @throws CandyException If date format is invalid.
     */
    public static Task parseLine(String line) throws CandyException {
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String description = parts[2].trim();

        Task task;
        switch (type) {
        case "T":
            task = new Todo(description);
            break;

        case "D":
            if (parts.length < 4) {
                return null;
            }
            LocalDate by = parseDate(parts[3].trim());
            task = new Deadline(description, by);
            break;

        case "E":
            if (parts.length < 5) {
                return null;
            }
            task = new Event(description, parts[3].trim(), parts[4].trim());
            break;

        default:
            return null;
        }

        if (isDone) {
            task.markDone();
        }
        return task;
    }

    /**
     * Converts a {@link Task} into one storage line.
     *
     * @param task Task to convert.
     * @return Storage line.
     */
    public static String toLine(Task task) {
        String done = task.isDone() ? "1" : "0";

        if (task instanceof Todo) {
            return "T | " + done + " | " + task.getDescription();
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return "D | " + done + " | " + deadline.getDescription()
                    + " | " + deadline.getByTime();
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return "E | " + done + " | " + event.getDescription()
                    + " | " + event.getFromTime()
                    + " | " + event.getToTime();
        } else {
            return "T | " + done + " | " + task.getDescription();
        }
    }
}