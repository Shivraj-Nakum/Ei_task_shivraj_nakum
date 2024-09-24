import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AstronautScheduleOrganizer {
    // Logger for logging application events and errors
    private static final Logger logger = Logger.getLogger(AstronautScheduleOrganizer.class.getName());
    // Scanner for user input
    private static final Scanner scanner = new Scanner(System.in);
    // Singleton instance of ScheduleManager to manage tasks
    private static final ScheduleManager scheduleManager = ScheduleManager.getInstance();

    public static void main(String[] args) {
        // Load configuration settings from the properties file
        ConfigurationManager.loadConfiguration("config.properties");
        // Add an observer to notify when tasks change
        scheduleManager.addObserver(message -> System.out.println("Notification: " + message));

        boolean running = true; // Flag to control the main loop
        while (running) {
            try {
                displayMenu(); // Display the main menu options
                // Get user input for menu choice
                int choice = InputValidator.getIntInput(scanner, "Enter your choice: ", 1, 7);
                // Process the user's choice and determine if the application should keep running
                running = processChoice(choice);
            } catch (Exception e) {
                // Handle unexpected errors and log them
                System.out.println("An error occurred: " + e.getMessage());
                logger.log(Level.SEVERE, "Unexpected error", e);
            }
        }
    }

    // Display the main menu options to the user
    private static void displayMenu() {
        System.out.println("\nAstronaut Daily Schedule Organizer");
        System.out.println("1. Add Task");
        System.out.println("2. Remove Task");
        System.out.println("3. View Tasks");
        System.out.println("4. Edit Task");
        System.out.println("5. Mark Task as Completed");
        System.out.println("6. View Tasks by Priority");
        System.out.println("7. Exit");
    }

    // Process the user's menu choice and execute corresponding actions
    private static boolean processChoice(int choice) throws Exception {
        switch (choice) {
            case 1:
                addTask(); // Add a new task
                break;
            case 2:
                removeTask(); // Remove an existing task
                break;
            case 3:
                viewTasks(); // View all tasks
                break;
            case 4:
                editTask(); // Edit an existing task
                break;
            case 5:
                markTaskAsCompleted(); // Mark a task as completed
                break;
            case 6:
                viewTasksByPriority(); // View tasks by priority
                break;
            case 7:
                System.out.println("Exiting the application."); // Exit the application
                return false; // Stop the main loop
            default:
                System.out.println("Invalid choice. Please try again."); // Handle invalid choice
        }
        return true; // Continue running the application
    }

    // Add a new task to the schedule
    private static void addTask() {
        try {
            // Get task details from the user
            String description = InputValidator.getStringInput(scanner, "Enter task description: ");
            String startTime = InputValidator.getTimeInput(scanner, "Enter start time (HH:mm): ");
            String endTime = InputValidator.getTimeInput(scanner, "Enter end time (HH:mm): ");
            String priority = InputValidator.getPriorityInput(scanner, "Enter priority (LOW/MEDIUM/HIGH): ");

            // Create a new task and add it to the schedule
            Task task = TaskFactory.createTask(description, startTime, endTime, priority);
            scheduleManager.addTask(task);
            System.out.println("Task added successfully.");
        } catch (IllegalArgumentException | TaskConflictException e) {
            // Handle errors related to task creation and log them
            System.out.println("Error: " + e.getMessage());
            logger.log(Level.WARNING, "Error adding task", e);
        }
    }

    // Remove an existing task from the schedule
    private static void removeTask() {
        String description = InputValidator.getStringInput(scanner, "Enter task description to remove: ");
        try {
            // Remove the task from the schedule
            scheduleManager.removeTask(description);
            System.out.println("Task removed successfully.");
        } catch (TaskNotFoundException e) {
            // Handle errors related to task removal and log them
            System.out.println("Error: " + e.getMessage());
            logger.log(Level.WARNING, "Error removing task", e);
        }
    }

    // View all tasks in the schedule
    private static void viewTasks() {
        scheduleManager.viewTasks().forEach(System.out::println); // Print each task
    }

    // Edit an existing task's details
    private static void editTask() {
        String description = InputValidator.getStringInput(scanner, "Enter task description to edit: ");
        try {
            // Get the current task and display its details
            Task task = scheduleManager.getTask(description);
            System.out.println("Current task details: " + task);
            
            // Get new details for the task, allowing empty input to keep current values
            String newDescription = InputValidator.getStringInput(scanner, "Enter new description (press enter to keep current): ");
            String newStartTime = InputValidator.getTimeInput(scanner, "Enter new start time (HH:mm, press enter to keep current): ");
            String newEndTime = InputValidator.getTimeInput(scanner, "Enter new end time (HH:mm, press enter to keep current): ");
            String newPriority = InputValidator.getPriorityInput(scanner, "Enter new priority (LOW/MEDIUM/HIGH, press enter to keep current): ");

            // Create an updated task with the new details
            Task updatedTask = TaskFactory.createTask(
                newDescription.isEmpty() ? task.getDescription() : newDescription,
                newStartTime.isEmpty() ? task.getStartTime().toString() : newStartTime,
                newEndTime.isEmpty() ? task.getEndTime().toString() : newEndTime,
                newPriority.isEmpty() ? task.getPriority().toString() : newPriority
            );

            // Update the task in the schedule
            scheduleManager.editTask(description, updatedTask);
            System.out.println("Task updated successfully.");
        } catch (TaskNotFoundException | TaskConflictException e) {
            // Handle errors related to task editing and log them
            System.out.println("Error: " + e.getMessage());
            logger.log(Level.WARNING, "Error editing task", e);
        }
    }

    // Mark a task as completed
    private static void markTaskAsCompleted() {
        String description = InputValidator.getStringInput(scanner, "Enter task description to mark as completed: ");
        try {
            // Mark the specified task as completed
            scheduleManager.markTaskAsCompleted(description);
            System.out.println("Task marked as completed successfully.");
        } catch (TaskNotFoundException e) {
            // Handle errors related to marking tasks and log them
            System.out.println("Error: " + e.getMessage());
            logger.log(Level.WARNING, "Error marking task as completed", e);
        }
    }

    // View tasks filtered by priority level
    private static void viewTasksByPriority() {
        String priority = InputValidator.getPriorityInput(scanner, "Enter priority level to view (LOW/MEDIUM/HIGH): ");
        // Print tasks that match the specified priority level
        scheduleManager.viewTasksByPriority(Priority.valueOf(priority.toUpperCase())).forEach(System.out::println);
    }
}
