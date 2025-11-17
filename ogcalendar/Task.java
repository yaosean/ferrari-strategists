import java.time.LocalDate;

public class Task {
    public int id;
    public String name;
    public String description;
    public LocalDate deadline;
    public int priority;
    public int points;
    public boolean isCompleted;

    public Task(String name, String description, LocalDate deadline, int priority, int points) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.points = points;
        this.isCompleted = false;
    }

    @Override
    public String toString() {
        return name + " - " + description + " (Priority: " + priority + ", Points: " + points + ", Deadline: " + (deadline != null ? deadline : "None") + ")";
    }
}