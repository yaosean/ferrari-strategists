import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;

public class TaskUI extends Application {
    private ObservableList<String> taskList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Modern Task Manager");

        // Input fields
        Label nameLabel = new Label("Task Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter task name");

        Label descLabel = new Label("Description:");
        TextField descField = new TextField();
        descField.setPromptText("Enter description");

        Label deadlineLabel = new Label("Deadline:");
        DatePicker deadlinePicker = new DatePicker();
        deadlinePicker.setPromptText("Select deadline");

        Label priorityLabel = new Label("Priority:");
        TextField priorityField = new TextField();
        priorityField.setPromptText("e.g., 1-5");

        Label pointsLabel = new Label("Points:");
        TextField pointsField = new TextField();
        pointsField.setPromptText("e.g., 10");

        Button addButton = new Button("Add Task");
        addButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        ListView<String> listView = new ListView<>(taskList);
        listView.setPrefHeight(300);

        // Layout
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.setPadding(new Insets(20));
        inputGrid.add(nameLabel, 0, 0);
        inputGrid.add(nameField, 1, 0);
        inputGrid.add(descLabel, 0, 1);
        inputGrid.add(descField, 1, 1);
        inputGrid.add(deadlineLabel, 0, 2);
        inputGrid.add(deadlinePicker, 1, 2);
        inputGrid.add(priorityLabel, 0, 3);
        inputGrid.add(priorityField, 1, 3);
        inputGrid.add(pointsLabel, 0, 4);
        inputGrid.add(pointsField, 1, 4);
        inputGrid.add(addButton, 1, 5);

        VBox vbox = new VBox(10, inputGrid, listView);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #e0e0e0, #f0f0f0);");

        Scene scene = new Scene(vbox, 500, 600);
        scene.getStylesheets().add("styles.css");
        primaryStage.setScene(scene);
        primaryStage.show();

        addButton.setOnAction(e -> {
            try {
                String name = nameField.getText();
                String desc = descField.getText();
                LocalDate deadline = deadlinePicker.getValue();
                int priority = Integer.parseInt(priorityField.getText());
                int points = Integer.parseInt(pointsField.getText());

                Task task = new Task(name, desc, deadline, priority, points);
                taskList.add(task.toString());

                // Clear fields
                nameField.clear();
                descField.clear();
                deadlinePicker.setValue(null);
                priorityField.clear();
                pointsField.clear();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter valid numbers for priority and points.");
                alert.showAndWait();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}