package candy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that must be completed by a specific date.
 */
public class Deadline extends Task {
    private final LocalDate by;
    private static final DateTimeFormatter OUT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    /**
     * Creates a Deadline task with the given description and due date.
     *
     * @param description Description of the task.
     * @param by Due date of the task.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the due date of this deadline task.
     *
     * @return Due date of the task.
     */
    public LocalDate getBy() {
        return by;
    }

    /**
     * Returns the string representation of the deadline task.
     *
     * @return Formatted string of this deadline.
     */
    @Override
    public String toString() {
        return "[D][" + getStatusIcon() + "] " + description + " (by: " + by.format(OUT) + ")";
    }
}