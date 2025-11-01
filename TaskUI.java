import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
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

    public TaskUI() {
        setTitle("Task Manager 2025");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 950);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(25, 25, 35), 0, getHeight(), new Color(15, 15, 25));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout(0, 12));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setContentPane(mainPanel);

        JPanel titleBar = new JPanel(new BorderLayout(10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setPaint(new Color(35, 35, 45, 200));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2d.dispose();
            }
        };
        titleBar.setOpaque(false);
        titleBar.setPreferredSize(new Dimension(850, 45));
        titleBar.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 10));

        JLabel titleLabel = new JLabel("Task Manager 2025", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(200, 220, 255));
        titleBar.add(titleLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setOpaque(false);

        JButton minimizeButton = createWindowButton("--", new Color(80, 80, 80));
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));
        buttonPanel.add(minimizeButton);

        JButton closeButton = createWindowButton("(X)", new Color(180, 80, 80));
        closeButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(closeButton);

        titleBar.add(buttonPanel, BorderLayout.EAST);
        mainPanel.add(titleBar, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel inputPanel = createPanelWithGlass(350);
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 12);

        addInputField(inputPanel, "Task Name:", labelFont);
        nameField = createStyledTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        inputPanel.add(nameField);
        inputPanel.add(Box.createVerticalStrut(12));

        addInputField(inputPanel, "Description:", labelFont);
        descField = createStyledTextField();
        descField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        inputPanel.add(descField);
        inputPanel.add(Box.createVerticalStrut(12));

        addInputField(inputPanel, "Deadline (yyyy-MM-dd):", labelFont);
        deadlineField = createStyledTextField();
        deadlineField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        inputPanel.add(deadlineField);
        inputPanel.add(Box.createVerticalStrut(12));

        addInputField(inputPanel, "Priority (1-5):", labelFont);
        priorityField = createStyledTextField();
        priorityField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        inputPanel.add(priorityField);
        inputPanel.add(Box.createVerticalStrut(12));

        addInputField(inputPanel, "Points:", labelFont);
        pointsField = createStyledTextField();
        pointsField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        inputPanel.add(pointsField);
        inputPanel.add(Box.createVerticalStrut(16));

        JButton addButton = createStyledButton("Add Task", new Color(100, 200, 100));
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(e -> addTask());
        inputPanel.add(addButton);

        contentPanel.add(inputPanel);
        contentPanel.add(Box.createVerticalStrut(12));

        JPanel listPanel = createPanelWithGlass(Integer.MAX_VALUE);
        listPanel.setLayout(new BorderLayout(0, 10));
        listPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel listLabel = new JLabel("Your Tasks", SwingConstants.CENTER);
        listLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        listLabel.setForeground(new Color(180, 200, 255));
        listPanel.add(listLabel, BorderLayout.NORTH);

        taskList = new JList<>(listModel);
        taskList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        taskList.setBackground(new Color(255, 255, 255, 8));
        taskList.setForeground(new Color(210, 220, 245));
        taskList.setSelectionBackground(new Color(100, 150, 255, 80));
        taskList.setSelectionForeground(Color.WHITE);
        taskList.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(100, 150, 255, 100);
            }
        });
        listPanel.add(scrollPane, BorderLayout.CENTER);

        JButton deleteButton = createStyledButton("Delete Selected", new Color(200, 100, 100));
        deleteButton.addActionListener(e -> deleteTask());
        JPanel deletePanel = new JPanel();
        deletePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        deletePanel.setOpaque(false);
        deletePanel.add(deleteButton);
        listPanel.add(deletePanel, BorderLayout.SOUTH);

        contentPanel.add(listPanel);

        JScrollPane mainScroll = new JScrollPane(contentPanel);
        mainScroll.setOpaque(false);
        mainScroll.getViewport().setOpaque(false);
        mainScroll.setBorder(BorderFactory.createEmptyBorder());
        mainScroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(100, 150, 255, 100);
            }
        });
        mainPanel.add(mainScroll, BorderLayout.CENTER);

        setVisible(true);
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
        System.out.println("Delete button clicked");
        int index = taskList.getSelectedIndex();
        System.out.println("Selected index: " + index);
        if (index < 0) {
            showError("Please select a task to delete");
            return;
        }
        tasks.remove(index);
        listModel.remove(index);
        System.out.println("Task deleted. Remaining tasks: " + tasks.size());
    }

    private void addInputField(JPanel panel, String labelText, Font font) {
        JLabel label = new JLabel(labelText);
        label.setFont(font);
        label.setForeground(new Color(200, 210, 240));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(4));
    }

    private JPanel createPanelWithGlass(int maxHeight) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setPaint(new Color(255, 255, 255, 16));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2d.setStroke(new BasicStroke(1.2f));
                g2d.setPaint(new Color(255, 255, 255, 32));
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 14, 14);
                g2d.dispose();
            }
        };
        panel.setOpaque(false);
        if (maxHeight != Integer.MAX_VALUE) {
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, maxHeight));
        }
        return panel;
    }

    private JButton createWindowButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2d.setPaint(new Color(Math.min(color.getRed() + 50, 255), 
                                          Math.min(color.getGreen() + 50, 255), 
                                          Math.min(color.getBlue() + 50, 255)));
                } else if (getModel().isRollover()) {
                    g2d.setPaint(new Color(Math.min(color.getRed() + 30, 255), 
                                          Math.min(color.getGreen() + 30, 255), 
                                          Math.min(color.getBlue() + 30, 255)));
                } else {
                    g2d.setPaint(color);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        button.setFont(new Font("Arial", Font.BOLD, 11));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(33, 28));
        return button;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setPaint(new Color(255, 255, 255, 28));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 9, 9);
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setForeground(Color.WHITE);
        field.setCaretColor(new Color(150, 200, 255));
        field.setOpaque(false);
        field.setBorder(BorderFactory.createEmptyBorder(7, 11, 7, 11));
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2d.setPaint(new Color(Math.max(bgColor.getRed() - 30, 0), 
                                          Math.max(bgColor.getGreen() - 30, 0), 
                                          Math.max(bgColor.getBlue() - 30, 0)));
                } else if (getModel().isRollover()) {
                    g2d.setPaint(new Color(Math.min(bgColor.getRed() + 20, 255), 
                                          Math.min(bgColor.getGreen() + 20, 255), 
                                          Math.min(bgColor.getBlue() + 20, 255)));
                } else {
                    g2d.setPaint(bgColor);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        return button;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TaskUI());
    }
}