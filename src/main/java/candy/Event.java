package candy;

/**
 * Represents a task that occurs during a specific time period.
 */
public class Event extends Task {
    private final String fromTime;
    private final String toTime;

    /**
     * Creates an Event task with the given description and time range.
     *
     * @param description Description of the event.
     * @param fromTime Start time of the event.
     * @param toTime End time of the event.
     */
    public Event(String description, String fromTime, String toTime) {
        super(description);
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    /**
     * Returns the start time of the event.
     *
     * @return Start time.
     */
    public String getFromTime() {
        return fromTime;
    }

    /**
     * Returns the end time of the event.
     *
     * @return End time.
     */
    public String getToTime() {
        return toTime;
    }

    /**
     * Returns the string representation of the event task.
     *
     * @return Formatted string of this event.
     */
    @Override
    public String toString() {
        return "[E][" + getStatusIcon() + "] " + description + " (from: " + fromTime + " to: " + toTime + ")";
    }
}