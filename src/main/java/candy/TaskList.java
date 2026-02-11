package candy;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;


/**
 * Represents a list of tasks in the Candy application.
 * <p>
 * This class manages adding, removing, searching, and updating tasks.
 * It also handles converting tasks into file storage format.
 */
public class TaskList {

    /** Internal list storing all tasks. */
    private final ArrayList<Task> tasks = new ArrayList<>();

    /**
     * Adds a task to the list.
     *
     * @param t Task to be added
     */
    public void add(Task t) {
        tasks.add(t);
    }

    /**
     * Returns the task at the specified index.
     *
     * @param idx Index of the task (zero-based)
     * @return Task at the given index
     * @throws CandyException If index is invalid
     */
    public Task get(int idx) throws CandyException {
        checkIndex(idx);
        return tasks.get(idx);
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param idx Index of the task (zero-based)
     * @return Removed task
     * @throws CandyException If index is invalid
     */
    public Task remove(int idx) throws CandyException {
        checkIndex(idx);
        return tasks.remove(idx);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Marks the task at the given index as completed.
     *
     * @param idx Index of the task
     * @throws CandyException If index is invalid
     */
    public void mark(int idx) throws CandyException {
        checkIndex(idx);
        tasks.get(idx).markDone();
    }

    /**
     * Marks the task at the given index as not completed.
     *
     * @param idx Index of the task
     * @throws CandyException If index is invalid
     */
    public void unmark(int idx) throws CandyException {
        checkIndex(idx);
        tasks.get(idx).unmark();
    }

    /**
     * Converts all tasks into file storage format lines.
     *
     * @return List of formatted task lines
     */
    public List<String> toLines() {
        List<String> lines = new ArrayList<>();
        for (Task t : tasks) {
            lines.add(Parser.toLine(t));
        }
        return lines;
    }

    /**
     * Checks whether the given index is valid.
     *
     * @param idx Index to be checked
     * @throws CandyException If index is out of range
     */
    private void checkIndex(int idx) throws CandyException {
        if (idx < 0 || idx >= tasks.size()) {
            throw new CandyException("Task number does not exist.");
        }
    }

    /**
     * Finds tasks whose descriptions contain the given keyword.
     *
     * @param keyword Search keyword
     * @return A TaskList containing matching tasks
     */
    public TaskList find(String keyword) {
        TaskList result = new TaskList();
        String k = keyword.toLowerCase();

        for (Task t : tasks) {
            if (t.getDescription().toLowerCase().contains(k)) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Formats the task list for GUI/text display.
     *
     * @return formatted task list string
     */
    public String formatForDisplay() {
        if (tasks.isEmpty()) {
            return "Your task list is empty.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Here are your tasks:\n");

        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1)
                    .append(". ")
                    .append(tasks.get(i).toString())
                    .append("\n");
        }

        return sb.toString();
    }

    public String formatSortedForDisplay() {
        if (tasks.isEmpty()) {
            return "Your task list is empty.";
        }

        ArrayList<Deadline> urgent = new ArrayList<>();
        ArrayList<Deadline> completedDeadlines = new ArrayList<>();
        ArrayList<Todo> todos = new ArrayList<>();
        ArrayList<Event> events = new ArrayList<>();

        // Separate tasks into groups
        for (Task t : tasks) {
            if (t instanceof Deadline) {
                Deadline d = (Deadline) t;
                if (!d.isDone()) {
                    urgent.add(d);
                } else {
                    completedDeadlines.add(d);
                }
            } else if (t instanceof Todo) {
                todos.add((Todo) t);
            } else if (t instanceof Event) {
                events.add((Event) t);
            }
        }

        // Sort deadlines by date
        urgent.sort(Comparator.comparing(Deadline::getBy));
        completedDeadlines.sort(Comparator.comparing(Deadline::getBy));

        StringBuilder sb = new StringBuilder();

        sb.append(" ~~Incomplete Urgent Tasks~~ \n");
        if (urgent.isEmpty()) {
            sb.append("None\n");
        } else {
            for (Deadline d : urgent) {
                sb.append("- ").append(d).append("\n");
            }
        }

        sb.append("\n ~~Completed Deadlines~~ \n");
        if (completedDeadlines.isEmpty()) {
            sb.append("None\n");
        } else {
            for (Deadline d : completedDeadlines) {
                sb.append("- ").append(d).append("\n");
            }
        }

        sb.append("\n ~~Todos~~ \n");
        if (todos.isEmpty()) {
            sb.append("None\n");
        } else {
            for (Todo t : todos) {
                sb.append("- ").append(t).append("\n");
            }
        }

        sb.append("\n ~~Events~~ \n");
        if (events.isEmpty()) {
            sb.append("None\n");
        } else {
            for (Event e : events) {
                sb.append("- ").append(e).append("\n");
            }
        }

        return sb.toString();
    }


}