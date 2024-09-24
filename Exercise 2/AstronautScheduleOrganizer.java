import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

// Singleton ScheduleManager
class ScheduleManager {
    private static ScheduleManager instance;
    private List<Task> tasks;
    private List<ScheduleObserver> observers;
    private static final Logger logger = Logger.getLogger(ScheduleManager.class.getName());

    private ScheduleManager() {
        tasks = new ArrayList<>();
        observers = new ArrayList<>();
    }

    public static synchronized ScheduleManager getInstance() {
        if (instance == null) {
            instance = new ScheduleManager();
        }
        return instance;
    }

    public void addTask(Task task) throws TaskConflictException {
        for (Task existingTask : tasks) {
            if (task.conflicts(existingTask)) {
                String errorMessage = "Task conflicts with existing task: " + existingTask.getDescription();
                logger.log(Level.WARNING, errorMessage);
                throw new TaskConflictException(errorMessage);
            }
        }
        tasks.add(task);
        tasks.sort(Comparator.comparing(Task::getStartTime));
        notifyObservers("Task added: " + task.getDescription());
        logger.log(Level.INFO, "Task added: " + task.getDescription());
    }

    public void removeTask(String description) throws TaskNotFoundException {
        boolean removed = tasks.removeIf(task -> task.getDescription().equals(description));
        if (!removed) {
            String errorMessage = "Task not found: " + description;
            logger.log(Level.WARNING, errorMessage);
            throw new TaskNotFoundException(errorMessage);
        }
        notifyObservers("Task removed: " + description);
        logger.log(Level.INFO, "Task removed: " + description);
    }

    public List<Task> viewTasks() {
        return new ArrayList<>(tasks);
    }

    public void addObserver(ScheduleObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ScheduleObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String message) {
        for (ScheduleObserver observer : observers) {
            observer.update(message);
        }
    }
}

// Task class
class Task {
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private Priority priority;

    public Task(String description, LocalTime startTime, LocalTime endTime, Priority priority) {
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
    }

    public boolean conflicts(Task other) {
        return (this.startTime.isBefore(other.endTime) && other.startTime.isBefore(this.endTime));
    }

    // Getters
    public String getDescription() { return description; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public Priority getPriority() { return priority; }

    @Override
    public String toString() {
        return String.format("%s - %s: %s [%s]", 
            startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
            endTime.format(DateTimeFormatter.ofPattern("HH:mm")),
            description, priority);
    }
}

// TaskFactory for creating Task objects
class TaskFactory {
    public static Task createTask(String description, String startTime, String endTime, String priority) 
            throws IllegalArgumentException {
        try {
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            Priority prio = Priority.valueOf(priority.toUpperCase());
            return new Task(description, start, end, prio);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format. Use HH:mm.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid priority level. Use LOW, MEDIUM, or HIGH.");
        }
    }
}

// Priority enum
enum Priority {
    LOW, MEDIUM, HIGH
}

// Observer interface
interface ScheduleObserver {
    void update(String message);
}

// Custom exceptions
class TaskConflictException extends Exception {
    public TaskConflictException(String message) {
        super(message);
    }
}

class TaskNotFoundException extends Exception {
    public TaskNotFoundException(String message) {
        super(message);
    }
}

// Main application class
public class AstronautScheduleOrganizer {
    private static final Logger logger = Logger.getLogger(AstronautScheduleOrganizer.class.getName());
    private static final Scanner scanner = new Scanner(System.in);
    private static final ScheduleManager scheduleManager = ScheduleManager.getInstance();

    public static void main(String[] args) {
        scheduleManager.addObserver(message -> System.out.println("Notification: " + message));

        while (true) {
            try {
                displayMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                processChoice(choice);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                logger.log(Level.WARNING, "Invalid menu input", e);
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                logger.log(Level.SEVERE, "Unexpected error", e);
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\nAstronaut Daily Schedule Organizer");
        System.out.println("1. Add Task");
        System.out.println("2. Remove Task");
        System.out.println("3. View Tasks");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void processChoice(int choice) throws Exception {
        switch (choice) {
            case 1:
                addTask();
                break;
            case 2:
                removeTask();
                break;
            case 3:
                viewTasks();
                break;
            case 4:
                System.out.println("Exiting the application.");
                System.exit(0);
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void addTask() {
        try {
            System.out.print("Enter task description: ");
            String description = scanner.nextLine();
            System.out.print("Enter start time (HH:mm): ");
            String startTime = scanner.nextLine();
            System.out.print("Enter end time (HH:mm): ");
            String endTime = scanner.nextLine();
            System.out.print("Enter priority (LOW/MEDIUM/HIGH): ");
            String priority = scanner.nextLine();

            Task task = TaskFactory.createTask(description, startTime, endTime, priority);
            scheduleManager.addTask(task);
            System.out.println("Task added successfully.");
        } catch (IllegalArgumentException | TaskConflictException e) {
            System.out.println("Error: " + e.getMessage());
            logger.log(Level.WARNING, "Error adding task", e);
        }
    }

    private static void removeTask() {
        System.out.print("Enter task description to remove: ");
        String description = scanner.nextLine();
        try {
            scheduleManager.removeTask(description);
            System.out.println("Task removed successfully.");
        } catch (TaskNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            logger.log(Level.WARNING, "Error removing task", e);
        }
    }

    private static void viewTasks() {
        List<Task> tasks = scheduleManager.viewTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks scheduled for the day.");
        } else {
            System.out.println("Scheduled tasks:");
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
    }
}