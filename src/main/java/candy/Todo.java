package candy;

/**
 * Represents a to-do task without any date or time constraints.
 * <p>
 * A Todo task only contains a description and completion status.
 */
public class Todo extends Task {

    /**
     * Creates a Todo task with the given description.
     *
     * @param description Description of the todo task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the string representation of the todo task.
     *
     * @return Formatted todo task string
     */
    @Override
    public String toString() {
        return "[T][" + getStatusIcon() + "] " + description;
    }
}