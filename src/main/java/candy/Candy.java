package candy;
import java.time.LocalDate;

public class Candy {
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    public Candy(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.tasks = new TaskList();
        loadFromFile();
    }

    public static void main(String[] args) {
        new Candy("data/candy.txt").run();
    }

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
                        tasks.add(new Todo(cmd.arg1));
                        ui.showAdd(tasks.get(tasks.size() - 1), tasks.size());
                        saveAll();
                        break;

                    case DEADLINE: {
                        LocalDate by = Parser.parseDate(cmd.arg2);
                        tasks.add(new Deadline(cmd.arg1, by));
                        ui.showAdd(tasks.get(tasks.size() - 1), tasks.size());
                        saveAll();
                        break;
                    }

                    case EVENT:
                        tasks.add(new Event(cmd.arg1, cmd.arg2, cmd.arg3));
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

    private void saveAll() {
        storage.saveLines(tasks.toLines());
    }
}