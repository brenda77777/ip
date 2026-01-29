package candy;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final ArrayList<Task> tasks = new ArrayList<>();

    public void add(Task t) {
        tasks.add(t);
    }

    public Task get(int idx) throws CandyException {
        checkIndex(idx);
        return tasks.get(idx);
    }

    public Task remove(int idx) throws CandyException {
        checkIndex(idx);
        return tasks.remove(idx);
    }

    public int size() {
        return tasks.size();
    }

    public void mark(int idx) throws CandyException {
        checkIndex(idx);
        tasks.get(idx).markDone();
    }

    public void unmark(int idx) throws CandyException {
        checkIndex(idx);
        tasks.get(idx).unmark();
    }

    public List<String> toLines() {
        List<String> lines = new ArrayList<>();
        for (Task t : tasks) {
            lines.add(Parser.toLine(t));
        }
        return lines;
    }

    private void checkIndex(int idx) throws CandyException {
        if (idx < 0 || idx >= tasks.size()) {
            throw new CandyException("candy.Task number does not exist.");
        }
    }

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
}