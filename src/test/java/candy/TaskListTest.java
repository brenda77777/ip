package candy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskListTest {
    @Test
    public void add_increasesSize() {
        TaskList list = new TaskList();

        int before = list.size();

        list.add(new Todo("read book"));

        assertEquals(before + 1, list.size());
    }

    @Test
    public void add_twoTasks_sizeIsTwo() {
        TaskList list = new TaskList();

        list.add(new Todo("task one"));
        list.add(new Todo("task two"));

        assertEquals(2, list.size());
    }
}