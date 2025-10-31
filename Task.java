import java.time.LocalDate;

public class Task {
    private int id;
    private String name;
    private String description;
    private LocalDate deadline;
    private int priority;
    private int points;
    private boolean isCompleted;

    // Constructors, getters, and setters
    public Task(String name, String description, LocalDate deadline, int priority, int points) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.points = points;
        this.isCompleted = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { this.isCompleted = completed; }

    @Override
    public String toString() {
        return name + " - " + description + " (Priority: " + priority + ", Points: " + points + ", Deadline: " + (deadline != null ? deadline : "None") + ")";
    }
}