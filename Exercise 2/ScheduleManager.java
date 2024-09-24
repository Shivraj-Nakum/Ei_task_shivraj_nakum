import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

class ScheduleManager {
    private static ScheduleManager instance;
    private final ConcurrentSkipListSet<Task> tasks;
    private final List<ScheduleObserver> observers;
    private static final Logger logger = Logger.getLogger(ScheduleManager.class.getName());

    private ScheduleManager() {
        tasks = new ConcurrentSkipListSet<>(Comparator.comparing(Task::getStartTime));
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
        notifyObservers("Task added: " + task.getDescription());
        logger.log(Level.INFO, "Task added: " + task.getDescription());
    }

    public void removeTask(String description) throws TaskNotFoundException {
        Task taskToRemove = getTask(description);
        tasks.remove(taskToRemove);
        notifyObservers("Task removed: " + description);
        logger.log(Level.INFO, "Task removed: " + description);
    }

    public List<Task> viewTasks() {
        return new ArrayList<>(tasks);
    }

    public Task getTask(String description) throws TaskNotFoundException {
        return tasks.stream()
                .filter(task -> task.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException("Task not found: " + description));
    }

    public void editTask(String oldDescription, Task updatedTask) throws TaskNotFoundException, TaskConflictException {
        removeTask(oldDescription);
        try {
            addTask(updatedTask);
        } catch (TaskConflictException e) {
            // If the updated task conflicts, add the original task back
            addTask(getTask(oldDescription));
            throw e;
        }
    }

    public void markTaskAsCompleted(String description) throws TaskNotFoundException {
        Task task = getTask(description);
        task.setCompleted(true);
        notifyObservers("Task marked as completed: " + description);
        logger.log(Level.INFO, "Task marked as completed: " + description);
    }

    public List<Task> viewTasksByPriority(Priority priority) {
        return tasks.stream()
                .filter(task -> task.getPriority() == priority)
                .collect(Collectors.toList());
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
