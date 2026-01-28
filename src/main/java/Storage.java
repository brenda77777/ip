import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path filePath;

    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }


    //Load lines from the file. If file/folder doesn't exist, return empty list.
    public List<String> loadLines() {
        try {
            if (!Files.exists(filePath)) {
                // create ./data folder if needed
                if (filePath.getParent() != null) {
                    Files.createDirectories(filePath.getParent());
                }
                // create empty file
                Files.createFile(filePath);
                return new ArrayList<>();
            }
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            // if any IO error, don't crash; just start empty
            return new ArrayList<>();
        }
    }


    //Save lines to the file, overwriting old content.
    public void saveLines(List<String> lines) {
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            Files.write(filePath, lines);
        } catch (IOException e) {
            // do not crash the whole app
            System.out.println("Warning: Could not save data to file.");
        }
    }
}
