package candy;

import java.time.LocalDate;

/**
 * The main entry point of the Candy task manager application.
 * <p>
 * Candy supports both:
 * <ul>
 *   <li>Terminal mode via {@link #run()}</li>
 *   <li>GUI mode via {@link #getResponse(String)}</li>
 * </ul>
 * The core logic is shared: user input is parsed by {@link Parser} into a {@link ParsedCommand},
 * then executed to produce a message for display.
 */
public class Candy {
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Creates a Candy application using the given storage file path.
     * Loads existing tasks from the storage file if available.
     *
     * @param filePath Path to the save file used for loading and saving tasks.
     */
    public Candy(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.tasks = new TaskList();
        loadFromFile();
    }

    /**
     * Creates a Candy application using the default storage path.
     * Intended for GUI usage.
     */
    public Candy() {
        this("data/candy.txt");
    }

    /**
     * Launches Candy in terminal mode.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new Candy("data/candy.txt").run();
    }

    /**
     * Runs Candy in terminal mode.
     * Reads input from {@link Ui#readCommand()}, parses it using {@link Parser#parse(String)},
     * executes it, and prints the returned message.
     */
    public void run() {
        ui.showMessage(ui.getWelcomeText());

        while (true) {
            try {
                String input = ui.readCommand();
                ParsedCommand command = Parser.parse(input);

                String message = command.execute(tasks, ui, storage);
                ui.showMessage(message);

                if (command.type == CommandType.BYE) {
                    return;
                }
            } catch (CandyException e) {
                ui.showError(e.getMessage());
            } catch (Exception e) {
                ui.showError("Something went wrong. Please try again.");
            }
        }
    }

    /**
     * Loads tasks from storage into {@link #tasks}.
     * If loading fails, Candy continues with an empty list.
     */
    private void loadFromFile() {
        try {
            for (String line : storage.loadLines()) {
                Task task = Parser.parseLine(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (Exception e) {
            ui.showError("Warning: Could not load data (starting with empty list).");
        }
    }

    /**
     * Generates a reply string for the GUI.
     * <p>
     * The GUI will call this method, then display the returned message in the chat.
     *
     * @param input User input from the GUI text field.
     * @return Response text to display to the user.
     */
    public String getResponse(String input) {
        try {
            ParsedCommand parsedCommand = Parser.parse(input);
            return parsedCommand.execute(tasks, ui, storage);
        } catch (CandyException e) {
            return e.getMessage();
        }
    }
}