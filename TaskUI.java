import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TaskUI extends JFrame {
    private List<Task> tasks = new ArrayList<>();
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> taskList;
    private JTextField nameField;
    private JTextField descField;
    private JTextField deadlineField;
    private JTextField priorityField;
    private JTextField pointsField;
    private int currentYear = LocalDate.now().getYear();
    private int currentMonth = LocalDate.now().getMonthValue();
    private JLabel monthLabel;
    private String userName;
    private LocalDate startDate;

    public TaskUI() {
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            // Customize for sleek, web-like flat design with purple dark theme
            UIManager.put("nimbusBase", new Color(128, 0, 128));
            UIManager.put("nimbusBlueGrey", new Color(75, 0, 130));
            UIManager.put("control", new Color(15, 10, 25));
            UIManager.put("text", Color.WHITE);
            UIManager.put("nimbusLightBackground", new Color(25, 15, 35));
            UIManager.put("Panel.background", new Color(15, 10, 25));
            UIManager.put("Button.flat", Boolean.TRUE);
            UIManager.put("Button.border", BorderFactory.createEmptyBorder(10, 20, 10, 20));
            UIManager.put("Button.background", Color.BLACK);
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("TextField.border", BorderFactory.createLineBorder(new Color(120, 50, 150), 1));
            UIManager.put("TextField.background", new Color(25, 15, 35));
            UIManager.put("TextField.foreground", Color.WHITE);
            UIManager.put("TitledBorder.border", BorderFactory.createLineBorder(new Color(120, 50, 150), 1));
            UIManager.put("List.background", new Color(15, 10, 25));
            UIManager.put("List.foreground", Color.WHITE);
            // Sleek fonts
            UIManager.put("Button.font", new Font("Verdana", Font.BOLD, 12));
            UIManager.put("Label.font", new Font("Verdana", Font.PLAIN, 12));
            UIManager.put("TextField.font", new Font("Verdana", Font.PLAIN, 12));
            UIManager.put("List.font", new Font("Verdana", Font.PLAIN, 12));
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            // Fallback to default look and feel
        }

        // Input user name
        userName = JOptionPane.showInputDialog(this, "Enter your name:");
        if (userName == null || userName.trim().isEmpty()) {
            userName = "User";
        }

        // Input start date
        String startDateStr = JOptionPane.showInputDialog(this, "Enter the current date (yyyy-MM-dd):");
        try {
            startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            startDate = LocalDate.now();
            JOptionPane.showMessageDialog(this, "Invalid date format. Using today's date.");
        }

        setTitle("Task Manager 2025 - " + userName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(15, 10, 25));

        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        // Tasks tab
        JPanel tasksPanel = new JPanel(new BorderLayout());
        tabbedPane.addTab("Tasks", tasksPanel);

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Task"));

        inputPanel.add(new JLabel("Task Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Description:"));
        descField = new JTextField();
        inputPanel.add(descField);

        inputPanel.add(new JLabel("Deadline (yyyy-MM-dd):"));
        deadlineField = new JTextField();
        inputPanel.add(deadlineField);

        inputPanel.add(new JLabel("Priority (1-5):"));
        priorityField = new JTextField();
        inputPanel.add(priorityField);

        inputPanel.add(new JLabel("Points:"));
        pointsField = new JTextField();
        inputPanel.add(pointsField);

        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(e -> addTask());
        inputPanel.add(addButton);

        tasksPanel.add(inputPanel, BorderLayout.NORTH);

        // Task list
        taskList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Tasks"));
        tasksPanel.add(scrollPane, BorderLayout.CENTER);

        // Delete button
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteTask());
        tasksPanel.add(deleteButton, BorderLayout.SOUTH);

        // Calendar tab
        JPanel calendarPanel = createCalendarPanel();
        tabbedPane.addTab("Calendar", calendarPanel);

        setVisible(true);
    }

    private JPanel createCalendarPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Navigation panel
        JPanel navPanel = new JPanel(new FlowLayout());
        JButton prevButton = new JButton("<");
        prevButton.addActionListener(e -> changeMonth(-1));
        navPanel.add(prevButton);

        monthLabel = new JLabel(getMonthName(currentMonth) + " " + currentYear, SwingConstants.CENTER);
        monthLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        navPanel.add(monthLabel);

        JButton nextButton = new JButton(">");
        nextButton.addActionListener(e -> changeMonth(1));
        navPanel.add(nextButton);

        panel.add(navPanel, BorderLayout.NORTH);

        // Calendar grid
        JPanel gridPanel = new JPanel(new GridLayout(7, 7)); // 7 days, 6 weeks + header
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setFont(new Font("Verdana", Font.BOLD, 12));
            gridPanel.add(label);
        }

        LocalDate firstOfMonth = LocalDate.of(currentYear, currentMonth, 1);
        int startDay = firstOfMonth.getDayOfWeek().getValue() % 7; // 0=Sun
        int daysInMonth = firstOfMonth.lengthOfMonth();

        // Empty cells before first day
        for (int i = 0; i < startDay; i++) {
            gridPanel.add(new JLabel(""));
        }

        // Days
        for (int day = 1; day <= daysInMonth; day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.setPreferredSize(new Dimension(40, 40)); // make square
            dayButton.setBorder(BorderFactory.createEmptyBorder()); // sleek, no border
            LocalDate date = LocalDate.of(currentYear, currentMonth, day);
            List<Task> tasksForDay = getTasksForDate(date);
            if (date.isBefore(startDate)) {
                dayButton.setEnabled(false);
                dayButton.setBackground(Color.GRAY);
                dayButton.setForeground(Color.DARK_GRAY);
            } else if (!tasksForDay.isEmpty()) {
                dayButton.setBackground(new Color(120, 50, 150)); // highlight if has tasks
                dayButton.setForeground(Color.WHITE);
            } else {
                dayButton.setBackground(Color.BLACK);
                dayButton.setForeground(Color.WHITE);
            }
            dayButton.addActionListener(e -> showTasksForDate(date));
            gridPanel.add(dayButton);
        }

        // Fill remaining cells
        int totalCells = 7 * 7; // header + 6 rows
        int usedCells = days.length + startDay + daysInMonth;
        for (int i = usedCells; i < totalCells; i++) {
            gridPanel.add(new JLabel(""));
        }

        panel.add(gridPanel, BorderLayout.CENTER);
        return panel;
    }

    private void changeMonth(int delta) {
        currentMonth += delta;
        if (currentMonth > 12) {
            currentMonth = 1;
            currentYear++;
        } else if (currentMonth < 1) {
            currentMonth = 12;
            currentYear--;
        }
        updateCalendar();
    }

    private void updateCalendar() {
        monthLabel.setText(getMonthName(currentMonth) + " " + currentYear);
        // Rebuild the calendar panel
        JTabbedPane tabbedPane = (JTabbedPane) getContentPane().getComponent(0);
        tabbedPane.setComponentAt(1, createCalendarPanel());
        tabbedPane.revalidate();
        tabbedPane.repaint();
    }

    private String getMonthName(int month) {
        String[] months = {"", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return months[month];
    }

    private List<Task> getTasksForDate(LocalDate date) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDeadline() != null && task.getDeadline().equals(date)) {
                result.add(task);
            }
        }
        return result;
    }

    private void showTasksForDate(LocalDate date) {
        List<Task> tasksForDay = getTasksForDate(date);
        if (tasksForDay.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tasks for " + date);
        } else {
            StringBuilder sb = new StringBuilder("Tasks for " + date + ":\n");
            for (Task task : tasksForDay) {
                sb.append("- ").append(task.getName()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        }
    }

    private void addTask() {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                showError("Task name cannot be empty");
                return;
            }
            String desc = descField.getText().trim();
            String deadlineStr = deadlineField.getText().trim();
            LocalDate deadline = null;
            if (!deadlineStr.isEmpty()) {
                try {
                    deadline = LocalDate.parse(deadlineStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    if (deadline.isBefore(startDate)) {
                        showError("Deadline must be on or after the start date");
                        return;
                    }
                } catch (Exception ex) {
                    showError("Invalid deadline format. Use yyyy-MM-dd");
                    return;
                }
            }
            String priorityStr = priorityField.getText().trim();
            if (priorityStr.isEmpty()) {
                showError("Priority cannot be empty");
                return;
            }
            int priority = Integer.parseInt(priorityStr);
            if (priority < 1 || priority > 5) {
                showError("Priority must be between 1 and 5");
                return;
            }

            String pointsStr = pointsField.getText().trim();
            if (pointsStr.isEmpty()) {
                showError("Points cannot be empty");
                return;
            }
            int points = Integer.parseInt(pointsStr);

            Task task = new Task(name, desc, deadline, priority, points);
            tasks.add(task);
            listModel.addElement(task.toString());

            nameField.setText("");
            descField.setText("");
            deadlineField.setText("");
            priorityField.setText("");
            pointsField.setText("");
            nameField.requestFocus();
        } catch (NumberFormatException ex) {
            showError("Priority and Points must be numbers");
        }
    }

    private void deleteTask() {
        int index = taskList.getSelectedIndex();
        if (index < 0) {
            showError("Please select a task to delete");
            return;
        }
        tasks.remove(index);
        listModel.remove(index);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TaskUI());
    }
}