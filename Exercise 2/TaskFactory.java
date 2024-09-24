import java.time.LocalTime;
import java.time.format.DateTimeParseException;

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