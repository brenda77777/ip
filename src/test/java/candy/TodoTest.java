package candy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TodoTest {

    @Test
    public void todo_descriptionStoredCorrectly() {
        Todo t = new Todo("read book");
        assertEquals("read book", t.getDescription());
    }
}