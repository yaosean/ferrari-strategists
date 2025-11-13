import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class TaskUI extends JFrame {
    private List<Task> tasks = new ArrayList<>();
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> taskList;
    private JTextField nameField;
    private JTextField descField;
    private JTextField deadlineField;
    private JTextField priorityField;
    private int currentYear = LocalDate.now().getYear();
    private int currentMonth = LocalDate.now().getMonthValue();
    private JLabel monthLabel;
    private String userName;
    private LocalDate startDate;
    private int totalPoints = 0;
    private JLabel pointsLabel;
    private JProgressBar progressBar;
    private int popupYear;
    private int popupMonth;

    public TaskUI() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
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
            UIManager.put("Button.font", new Font("Verdana", Font.BOLD, 12));
            UIManager.put("Label.font", new Font("Verdana", Font.PLAIN, 12));
            UIManager.put("TextField.font", new Font("Verdana", Font.PLAIN, 12));
            UIManager.put("List.font", new Font("Verdana", Font.PLAIN, 12));
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
        }

        userName = JOptionPane.showInputDialog(this, "Enter your name:");
        if (userName == null || userName.trim().isEmpty()) {
            userName = "User";
        }

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

        JPanel tasksPanel = new JPanel(new BorderLayout());
        tabbedPane.addTab("Tasks", tasksPanel);

        JPanel pointsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        pointsPanel.setBorder(BorderFactory.createTitledBorder("Weekly Goal Progress"));
        
        pointsLabel = new JLabel("Total Points: 0 / 100", SwingConstants.CENTER);
        pointsLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        pointsPanel.add(pointsLabel);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setString("0%");
        pointsPanel.add(progressBar);
        
        tasksPanel.add(pointsPanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Task"));

        inputPanel.add(new JLabel("Task Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Description:"));
        descField = new JTextField();
        inputPanel.add(descField);

        inputPanel.add(new JLabel("Deadline (yyyy-MM-dd):"));
        JPanel deadlinePanel = new JPanel(new BorderLayout());
        deadlineField = new JTextField();
        deadlinePanel.add(deadlineField, BorderLayout.CENTER);
        JButton calendarButton = new JButton("...");
        calendarButton.setPreferredSize(new Dimension(30, 20));
        calendarButton.addActionListener(e -> showCalendarPopup());
        deadlinePanel.add(calendarButton, BorderLayout.EAST);
        inputPanel.add(deadlinePanel);

        inputPanel.add(new JLabel("Priority (1-5):"));
        priorityField = new JTextField();
        inputPanel.add(priorityField);

        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(e -> addTask());
        inputPanel.add(addButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(inputPanel, BorderLayout.NORTH);
        tasksPanel.add(centerPanel, BorderLayout.CENTER);

        taskList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Tasks"));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton completeButton = new JButton("Complete Selected");
        completeButton.addActionListener(e -> completeTask());
        buttonPanel.add(completeButton);
        
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteTask());
        buttonPanel.add(deleteButton);
        
        tasksPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel calendarPanel = createCalendarPanel();
        tabbedPane.addTab("Calendar", calendarPanel);

        setVisible(true);
    }

    private JPanel createCalendarPanel() {
        JPanel panel = new JPanel(new BorderLayout());

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

        JPanel gridPanel = new JPanel(new GridLayout(7, 7));
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setFont(new Font("Verdana", Font.BOLD, 12));
            gridPanel.add(label);
        }

        LocalDate firstOfMonth = LocalDate.of(currentYear, currentMonth, 1);
        int startDay = firstOfMonth.getDayOfWeek().getValue() % 7;
        int daysInMonth = firstOfMonth.lengthOfMonth();

        for (int i = 0; i < startDay; i++) {
            gridPanel.add(new JLabel(""));
        }

        for (int day = 1; day <= daysInMonth; day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.setPreferredSize(new Dimension(40, 40));
            dayButton.setBorder(BorderFactory.createEmptyBorder());
            LocalDate date = LocalDate.of(currentYear, currentMonth, day);
            List<Task> tasksForDay = getTasksForDate(date);
            if (date.isBefore(startDate)) {
                dayButton.setEnabled(false);
                dayButton.setBackground(Color.GRAY);
                dayButton.setForeground(Color.DARK_GRAY);
            } else if (!tasksForDay.isEmpty()) {
                dayButton.setBackground(new Color(120, 50, 150));
                dayButton.setForeground(Color.WHITE);
            } else {
                dayButton.setBackground(Color.BLACK);
                dayButton.setForeground(Color.WHITE);
            }
            dayButton.addActionListener(e -> showTasksForDate(date));
            gridPanel.add(dayButton);
        }

        int totalCells = 7 * 7;
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
            if (task.deadline != null && task.deadline.equals(date)) {
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
                sb.append("- ").append(task.name).append("\n");
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

            int points = 10;

            Task task = new Task(name, desc, deadline, priority, points);
            tasks.add(task);
            updateTaskList();

            nameField.setText("");
            descField.setText("");
            deadlineField.setText("");
            priorityField.setText("");
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
        updateTaskList();
    }
    
    private void completeTask() {
        int index = taskList.getSelectedIndex();
        if (index < 0) {
            showError("Please select a task to complete");
            return;
        }
        
        Task task = tasks.get(index);
        
        if (task.isCompleted) {
            showError("This task is already completed!");
            return;
        }
        
        task.isCompleted = true;
        totalPoints += task.points;
        
        updateTaskList();
        updatePointsDisplay();
        
        if (totalPoints >= 100) {
            JOptionPane.showMessageDialog(this, 
                "Congratulations! You've reached your weekly goal of 100 points!\nTotal Points: " + totalPoints,
                "Goal Achieved!", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void updateTaskList() {
        listModel.clear();
        for (Task task : tasks) {
            String status = task.isCompleted ? "[✓] " : "[ ] ";
            listModel.addElement(status + task.toString());
        }
    }
    
    private void updatePointsDisplay() {
        pointsLabel.setText("Total Points: " + totalPoints + " / 100");
        progressBar.setValue(Math.min(totalPoints, 100));
        progressBar.setString(Math.min(totalPoints, 100) + "%");
        
        if (totalPoints >= 100) {
            progressBar.setForeground(new Color(50, 205, 50));
        } else if (totalPoints >= 50) {
            progressBar.setForeground(new Color(255, 165, 0));
        } else {
            progressBar.setForeground(new Color(120, 50, 150));
        }
    }

    private void showCalendarPopup() {
        popupYear = currentYear;
        popupMonth = currentMonth;
        JDialog dialog = new JDialog(this, "Select Date", true);
        dialog.setSize(300, 250);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout());

        JPanel navPanel = new JPanel(new FlowLayout());
        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        JLabel popupMonthLabel = new JLabel(getMonthName(popupMonth) + " " + popupYear, SwingConstants.CENTER);
        popupMonthLabel.setFont(new Font("Verdana", Font.BOLD, 16));

        prevButton.addActionListener(e -> {
            popupMonth--;
            if (popupMonth < 1) {
                popupMonth = 12;
                popupYear--;
            }
            updatePopupCalendar(dialog, panel, popupMonthLabel);
        });
        navPanel.add(prevButton);

        navPanel.add(popupMonthLabel);

        nextButton.addActionListener(e -> {
            popupMonth++;
            if (popupMonth > 12) {
                popupMonth = 1;
                popupYear++;
            }
            updatePopupCalendar(dialog, panel, popupMonthLabel);
        });
        navPanel.add(nextButton);

        panel.add(navPanel, BorderLayout.NORTH);

        updatePopupCalendar(dialog, panel, popupMonthLabel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void updatePopupCalendar(JDialog dialog, JPanel panel, JLabel monthLabel) {
        monthLabel.setText(getMonthName(popupMonth) + " " + popupYear);
        // Remove old grid if exists
        if (panel.getComponentCount() > 1) {
            panel.remove(1);
        }

        JPanel gridPanel = new JPanel(new GridLayout(7, 7));
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setFont(new Font("Verdana", Font.BOLD, 12));
            gridPanel.add(label);
        }

        LocalDate firstOfMonth = LocalDate.of(popupYear, popupMonth, 1);
        int startDay = firstOfMonth.getDayOfWeek().getValue() % 7;
        int daysInMonth = firstOfMonth.lengthOfMonth();

        for (int i = 0; i < startDay; i++) {
            gridPanel.add(new JLabel(""));
        }

        for (int day = 1; day <= daysInMonth; day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.setPreferredSize(new Dimension(30, 30));
            dayButton.setBorder(BorderFactory.createEmptyBorder());
            LocalDate date = LocalDate.of(popupYear, popupMonth, day);
            if (date.isBefore(startDate)) {
                dayButton.setEnabled(false);
                dayButton.setBackground(Color.GRAY);
            } else {
                dayButton.setBackground(Color.BLACK);
                dayButton.setForeground(Color.WHITE);
                dayButton.addActionListener(e -> {
                    deadlineField.setText(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    dialog.dispose();
                });
            }
            gridPanel.add(dayButton);
        }

        int totalCells = 7 * 7;
        int usedCells = days.length + startDay + daysInMonth;
        for (int i = usedCells; i < totalCells; i++) {
            gridPanel.add(new JLabel(""));
        }

        panel.add(gridPanel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TaskUI());
    }
}