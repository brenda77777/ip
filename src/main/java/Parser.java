import java.time.LocalDate;

public class Parser {

    public static ParsedCommand parse(String input) throws CandyException {
        input = input.trim();

        if (input.equals("bye")) {
            return new ParsedCommand(CommandType.BYE);
        }

        if (input.equals("list")) {
            return new ParsedCommand(CommandType.LIST);
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
            cmd.arg1 = desc;
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

            // validate date format here
            parseDate(by);

            ParsedCommand cmd = new ParsedCommand(CommandType.DEADLINE);
            cmd.arg1 = desc;
            cmd.arg2 = by;
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
            cmd.arg1 = desc;
            cmd.arg2 = from;
            cmd.arg3 = to;
            return cmd;
        }

        throw new CandyException("OOPS!!! I'm sorry, but I don't know what that means :-(");
    }

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

    public static LocalDate parseDate(String s) throws CandyException {
        try {
            return LocalDate.parse(s); // expects yyyy-mm-dd
        } catch (Exception e) {
            throw new CandyException("Date must be yyyy-mm-dd (example: 2019-10-15)");
        }
    }

    // Load from file line -> Task object
    public static Task parseLine(String line) throws CandyException {
        // format: TYPE | doneFlag | desc | extra...
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
                if (parts.length < 4) return null;
                LocalDate by = parseDate(parts[3].trim());
                t = new Deadline(desc, by);
                break;
            case "E":
                if (parts.length < 5) return null;
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

    //Save Task object -> file line
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