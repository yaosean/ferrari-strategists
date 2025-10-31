import java.util.Date;

public class Driver {
    public static void main(String[] args) {
        // Create a sample task
        Date deadline = new Date(); // Current date as deadline for demo
        Task task = new Task("Sample Task", "This is a description", deadline, 1, 10);

        // Print task details
        System.out.println("Task ID: " + task.id);
        System.out.println("Name: " + task.name);
        System.out.println("Description: " + task.description);
        System.out.println("Deadline: " + task.deadline);
        System.out.println("Priority: " + task.priority);
        System.out.println("Points: " + task.points);
        System.out.println("Is Completed: " + task.isCompleted);
    }
}