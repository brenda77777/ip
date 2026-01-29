package candy;

/**
 * Represents a task that occurs during a specific time period.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an Event task with the given description and time range.
     *
     * @param description Description of the event.
     * @param from Start time of the event.
     * @param to End time of the event.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the start time of the event.
     *
     * @return Start time.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the end time of the event.
     *
     * @return End time.
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns the string representation of the event task.
     *
     * @return Formatted string of this event.
     */
    @Override
    public String toString() {
        return "[E][" + getStatusIcon() + "] " + description + " (from: " + from + " to: " + to + ")";
    }
}