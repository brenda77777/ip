public abstract class Task {
    protected final String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void markDone() {
        isDone = true;
    }

    public void unmark() {
        isDone = false;
    }

    public boolean isDone() {
        return isDone;
    }

    public String getDescription() {
        return description;
    }

    protected String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    @Override
    public abstract String toString();
}