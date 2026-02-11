package candy;

import java.time.LocalDate;

/**
 * The main entry point of the Candy task manager application.
 * <p>
 * Candy reads user commands, delegates parsing to {@link Parser}, stores tasks in {@link TaskList},
 * shows output via {@link Ui}, and persists data using {@link Storage}.
 */
public class Candy {
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Constructs a Candy application with the given storage file path.
     * Loads any existing tasks from the file into memory.
     *
     * @param filePath Path to the data file used for loading and saving tasks.
     */
    public Candy(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.tasks = new TaskList();
        loadFromFile();
    }

    // another constructor (JavaFX uses this)
    public Candy() {
        this("data/candy.txt");
    }

    /**
     * Starts the Candy application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new Candy("data/candy.txt").run();
    }

    /**
     * Runs the main command loop of the application.
     * Reads commands from the user, parses them, executes the corresponding action,
     * and displays messages or errors accordingly.
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();

            try {
                ParsedCommand cmd = Parser.parse(input);

                switch (cmd.type) {
                case BYE:
                    saveAll();
                    ui.showMessage("Bye. Hope to see you again soon!");
                    return;

                case LIST:
                    ui.showList(tasks);
                    break;

                case TODO:
                    tasks.add(new Todo(cmd.description));
                    ui.showAdd(tasks.get(tasks.size() - 1), tasks.size());
                    saveAll();
                    break;

                case DEADLINE: {
                    LocalDate by = Parser.parseDate(cmd.byDate);
                    tasks.add(new Deadline(cmd.description, by));
                    ui.showAdd(tasks.get(tasks.size() - 1), tasks.size());
                    saveAll();
                    break;
                }

                case EVENT:
                    tasks.add(new Event(cmd.description, cmd.fromTime, cmd.toTime));
                    ui.showAdd(tasks.get(tasks.size() - 1), tasks.size());
                    saveAll();
                    break;

                case MARK:
                    tasks.mark(cmd.index);
                    ui.showMark(tasks.get(cmd.index));
                    saveAll();
                    break;

                case UNMARK:
                    tasks.unmark(cmd.index);
                    ui.showUnmark(tasks.get(cmd.index));
                    saveAll();
                    break;

                case DELETE:
                    Task removed = tasks.remove(cmd.index);
                    ui.showDelete(removed, tasks.size());
                    saveAll();
                    break;

                case FIND:
                    TaskList matches = tasks.find(cmd.keyword);
                    ui.showFindResults(cmd.keyword, matches);
                    break;

                case SORT:
                    ui.showMessage(tasks.formatSortedForDisplay());
                    break;

                case HELP:
                    ui.showMessage("Available commands:\n"
                            + "list\n"
                            + "todo <task>\n"
                            + "deadline <task> /by <yyyy-mm-dd>\n"
                            + "event <task> /from <start> /to <end>\n"
                            + "mark <task number>\n"
                            + "unmark <task number>\n"
                            + "delete <task number>\n"
                            + "find <keyword>\n"
                            + "sort\n"
                            + "bye");
                    break;



                default:
                    throw new CandyException("OOPS!!! I'm sorry, but I don't know what that means :-(");
                }

            } catch (CandyException e) {
                ui.showError(e.getMessage());
            } catch (Exception e) {
                ui.showError("Something went wrong. Please try again.");
            }
        }
    }

    /**
     * Loads tasks from storage into the current task list.
     * If loading fails, the app continues with an empty list.
     */
    private void loadFromFile() {
        try {
            for (String line : storage.loadLines()) {
                Task t = Parser.parseLine(line);
                if (t != null) {
                    tasks.add(t);
                }
            }
        } catch (Exception e) {
            ui.showError("Warning: Could not load data (starting with empty list).");
        }
    }

    /**
     * Saves the current task list to storage.
     */
    private void saveAll() {
        storage.saveLines(tasks.toLines());
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        try {
            ParsedCommand command = Parser.parse(input);
            return command.execute(tasks, ui, storage);
        } catch (CandyException e) {
            return e.getMessage();
        }
    }
}