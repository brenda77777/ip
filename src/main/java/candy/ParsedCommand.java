package candy;

/**
 * Represents a parsed user command with its associated arguments.
 * <p>
 * This class is used by {@link Parser} to store the result of parsing user input.
 */
public class ParsedCommand {
    public final CommandType type;
    public String arg1;
    public String arg2;
    public String arg3; // used for event end time
    public int index = -1;

    /**
     * Creates a ParsedCommand with the specified command type.
     *
     * @param type The type of command parsed from user input.
     */
    public ParsedCommand(CommandType type) {
        this.type = type;
    }
}