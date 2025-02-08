package com.github.stilvergp.controller;

import com.github.stilvergp.App;
import com.github.stilvergp.model.UserSession;
import com.github.stilvergp.model.entities.Habit;
import com.github.stilvergp.model.entities.Recommendation;
import com.github.stilvergp.services.ActivityService;
import com.github.stilvergp.services.HabitService;
import com.github.stilvergp.services.RecommendationService;
import com.github.stilvergp.utils.Alerts;
import com.github.stilvergp.utils.PDFExporter;
import com.github.stilvergp.view.Scenes;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class HabitListController extends Controller implements Initializable {
    @FXML
    private TableView<Habit> tableView;

    @FXML
    private TableColumn<Habit, String> activityColumn;

    @FXML
    private TableColumn<Habit, Integer> frequencyColumn;

    @FXML
    private TableColumn<Habit, String> typeColumn;

    @FXML
    private TableColumn<Habit, Instant> lastDateColumn;

    @FXML
    private TextField searchBox;

    @FXML
    private TextArea recommendationText;

    private ObservableList<Habit> habits;

    @Override
    public void onOpen(Object input) {
        reloadHabitsFromDatabase();
    }

    public void reloadHabitsFromDatabase() {
        tableView.getItems().clear();
        List<Habit> habits = new HabitService().findByUser(UserSession.getInstance().getLoggedInUser());
        this.habits = FXCollections.observableArrayList(habits);
        tableView.setItems(this.habits);
        habits.stream()
                .max(Comparator.comparingInt(Habit::getFrequency)).ifPresent(this::updateRecommendationText);
    }

    private void searchHabits(String filter) {
        ObservableList<Habit> filteredHabits = FXCollections.observableArrayList(
                this.habits.stream()
                        .filter(habit -> new ActivityService().getActivityByHabit(habit).getName().toLowerCase().contains(filter.toLowerCase()))
                        .toList()
        );
        tableView.setItems(filteredHabits);
    }

    public void addHabit() throws IOException {
        App.currentController.openModal(Scenes.ADDHABIT, "Registrando habito...", this, null);
    }

    public void saveHabit(Habit habit) {
        HabitService habitService = new HabitService();
        habitService.save(habit);
        reloadHabitsFromDatabase();
    }

    public void backToMain() throws IOException {
        App.currentController.changeScene(Scenes.MAIN, null);
    }

    @Override
    public void onClose(Object input) {

    }

    private void updateRecommendationText(Habit habit) {
        List<Recommendation> recommendations = new RecommendationService().getRecommendationsByHabit(habit);
        String randomRecommendationText = recommendations.get(new Random().nextInt(recommendations.size())).getDescription();
        recommendationText.setText(randomRecommendationText);
    }

    public void removeHabit() {
        Habit habit = tableView.getSelectionModel().getSelectedItem();
        if (habit == null) return;
        Alerts.showConfirmationAlert("Eliminación de habito",
                "Esta a punto de eliminar este habito, " +
                        "¿Está totalmente seguro de esta acción?").showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                HabitService habitService = new HabitService();
                habitService.delete(habit);
                reloadHabitsFromDatabase();
            }
        });
    }

    public void exportToPDF(Event event) {
        Window window = ((Node) (event.getSource())).getScene().getWindow();
        List<Habit> userHabits = new HabitService().findByUser(UserSession.getInstance().getLoggedInUser());
        if (!userHabits.isEmpty()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Exportar a PDF.");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            fileChooser.setInitialFileName("habitos.pdf");
            File file = fileChooser.showSaveDialog(window);
            if (file != null) {
                PDFExporter.exportHabitsToPDF(file, userHabits);
            }
        } else {
            Alerts.showErrorAlert("Error al exportar habitos", "No hay habitos registrados");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableView.setEditable(true);
        tableView.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            if (newValue != null) {
                updateRecommendationText(newValue);
            }
        });
        activityColumn.setCellValueFactory(habit -> new SimpleStringProperty(new ActivityService().getActivityByHabit(habit.getValue()).getName()));
        frequencyColumn.setCellValueFactory(habit -> new SimpleIntegerProperty(habit.getValue().getFrequency()).asObject());
        frequencyColumn.setOnEditCommit(event -> {
            if (event.getNewValue().equals(event.getOldValue())) return;
            if (event.getNewValue() < 100 && event.getNewValue() > 0) {
                Habit habit = event.getRowValue();
                habit.setFrequency(event.getNewValue());
                HabitService habitService = new HabitService();
                habitService.update(habit);
                reloadHabitsFromDatabase();
            } else {
                Alerts.showErrorAlert("Error al actualizar el habito", "La frecuencia introducida no puede ser mayor que 100 ni menor de 1");
            }

        });
        typeColumn.setCellValueFactory(habit -> new SimpleStringProperty(habit.getValue().getType()));
        typeColumn.setOnEditCommit(event -> {
            if (event.getNewValue().trim().equals(event.getOldValue().trim())) return;
            if (event.getNewValue().trim().isEmpty()) return;
            if (event.getNewValue().matches("diario") || event.getNewValue().matches("semanal") ||
                    event.getNewValue().matches("mensual") || event.getNewValue().matches("anual")) {
                Habit habit = event.getRowValue();
                habit.setType(event.getNewValue());
                HabitService habitService = new HabitService();
                habitService.update(habit);
                reloadHabitsFromDatabase();
            } else {
                Alerts.showErrorAlert("Error al actualizar el habito", "El intervalo es invalido, " +
                        "por favor introduce un valor correcto (diario, semanal, mensual, anual)");
            }
        });
        lastDateColumn.setCellValueFactory(new PropertyValueFactory<>("lastDate"));
        lastDateColumn.setCellFactory(_ -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            @Override
            protected void updateItem(Instant date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    LocalDateTime dateTime = date.atZone(ZoneId.systemDefault()).toLocalDateTime();
                    setText(formatter.format(dateTime));
                }
            }
        });
        searchBox.textProperty().addListener((_, _, newValue) -> {
            if (newValue != null) {
                searchHabits(newValue);
            }
        });
    }


}
