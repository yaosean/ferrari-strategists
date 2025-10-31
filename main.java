import java.util.Date;

public class Task {
    public int id;
    public String name;
    public String description;
    public Date deadline;
    public int priority;
    public int points;
    public boolean isCompleted;

    // Constructors, getters, and setters
    public Task(String name, String description, Date deadline, int priority, int points) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.points = points;
        this.isCompleted = false;
    }

}