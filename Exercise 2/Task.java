import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

class Task {
    private final String description;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final Priority priority;
    private boolean completed;

    public Task(String description, LocalTime startTime, LocalTime endTime, Priority priority) {
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
        this.completed = false;
    }

    public boolean conflicts(Task other) {
        return (this.startTime.isBefore(other.endTime) && other.startTime.isBefore(this.endTime));
    }

    // Getters
    public String getDescription() { return description; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public Priority getPriority() { return priority; }
    public boolean isCompleted() { return completed; }

    // Setter for completed status
    public void setCompleted(boolean completed) { this.completed = completed; }

    @Override
    public String toString() {
        return String.format("%s - %s: %s [%s] %s", 
            startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
            endTime.format(DateTimeFormatter.ofPattern("HH:mm")),
            description, priority, completed ? "(Completed)" : "");
    }
}
