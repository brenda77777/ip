import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class Storage {
    private final Path filePath;

    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    public List<String> loadLines() throws IOException {
        ensureParentExists();
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
            return Collections.emptyList();
        }
        return Files.readAllLines(filePath);
    }

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

    private void ensureParentExists() throws IOException {
        Path parent = filePath.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }
}