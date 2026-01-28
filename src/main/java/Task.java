public class Task {
    private String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void markDone() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    public String getTaskStatus() {
        return isDone ? "X" : " ";
    }

    public boolean isDone() {
        return this.isDone;
    }

    public String getDescription() {
        return this.description;
    }


    @Override
    public String toString() {
        return "[" + getTaskStatus() + "] " + description;
    }
}
