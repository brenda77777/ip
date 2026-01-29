package candy;

/**
 * Represents a generic task in the Candy task manager.
 * <p>
 * A task contains a description and completion status.
 * Subclasses such as {@link Todo}, {@link Deadline}, and {@link Event}
 * provide concrete implementations.
 */
public abstract class Task {

    /** Description of the task. */
    protected final String description;

    /** Completion status of the task. */
    protected boolean isDone;

    /**
     * Creates a new task with the given description.
     *
     * @param description Description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as completed.
     */
    public void markDone() {
        isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void unmark() {
        isDone = false;
    }

    /**
     * Returns whether this task is completed.
     *
     * @return true if the task is done, false otherwise
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the description of this task.
     *
     * @return Task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the status icon for display purposes.
     *
     * @return "X" if task is completed, otherwise a blank space
     */
    protected String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the formatted string representation of the task.
     *
     * @return String representation of the task
     */
    @Override
    public abstract String toString();
}