package candy;

public class ParsedCommand {
    public final CommandType type;
    public String arg1;
    public String arg2;
    public String arg3; // for event to
    public int index = -1;

    public ParsedCommand(CommandType type) {
        this.type = type;
    }
}