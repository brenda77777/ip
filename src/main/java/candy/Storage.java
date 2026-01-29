package candy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * Handles loading and saving task data to the local file system.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates a Storage object using the given file path.
     *
     * @param filePath Path to the data file.
     */
    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Loads all saved task lines from the data file.
     * <p>
     * If the file does not exist, a new file is created and an empty list is returned.
     *
     * @return List of lines read from the file.
     * @throws IOException If an I/O error occurs during reading.
     */
    public List<String> loadLines() throws IOException {
        ensureParentExists();
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
            return Collections.emptyList();
        }
        return Files.readAllLines(filePath);
    }

    /**
     * Saves the given list of task lines to the data file.
     *
     * @param lines List of task lines to be written.
     */
    public void saveLines(List<String> lines) {
        try {
            ensureParentExists();
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
            Files.write(filePath, lines);
        } catch (IOException e) {
            System.out.println("Warning: could not save data.");
        }
    }

    /**
     * Ensures that the parent directory of the data file exists.
     *
     * @throws IOException If directory creation fails.
     */
    private void ensureParentExists() throws IOException {
        Path parent = filePath.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }
}