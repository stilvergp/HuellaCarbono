package com.github.stilvergp.controller;

import com.github.stilvergp.UserSession;
import com.github.stilvergp.model.entities.Activity;
import com.github.stilvergp.model.entities.Habit;
import com.github.stilvergp.model.entities.HabitId;
import com.github.stilvergp.services.ActivityService;
import com.github.stilvergp.utils.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class AddHabitController extends Controller implements Initializable {

    @FXML
    private TextField frequencyLine;

    @FXML
    private ChoiceBox<Activity> activityChoice;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ChoiceBox<String> typeChoice;

    @FXML
    private Spinner<Integer> hourSpinner;

    @FXML
    private Spinner<Integer> minuteSpinner;

    private HabitListController controller;


    @Override
    public void onOpen(Object input) {
        this.controller = (HabitListController) input;
        reloadActivitiesFromDatabase();
        setTypeChoices();
    }

    private void setTypeChoices() {
        typeChoice.getItems().clear();
        ObservableList<String> types = FXCollections.observableArrayList("diario", "semanal", "mensual", "anual");
        typeChoice.setItems(types);
    }

    private void reloadActivitiesFromDatabase() {
        this.activityChoice.getItems().clear();
        List<Activity> activities = new ActivityService().getActivities();
        ObservableList<Activity> activitiesList = FXCollections.observableArrayList(activities);
        this.activityChoice.setItems(activitiesList);
    }

    public void saveHabit(Event event) {
        if (areFieldsValid()) {
            if (isDateValid()) {
                Habit habit = new Habit();
                HabitId habitId = new HabitId();
                habitId.setActivityId(activityChoice.getValue().getId());
                habitId.setUserId(UserSession.getInstance().getLoggedInUser().getId());
                habit.setId(habitId);
                habit.setActivity(activityChoice.getValue());
                habit.setFrequency(Integer.valueOf(frequencyLine.getText()));
                habit.setUser(UserSession.getInstance().getLoggedInUser());
                habit.setType(typeChoice.getValue());
                LocalDateTime dateTime = LocalDateTime.of(datePicker.getValue(), LocalTime.of(hourSpinner.getValue(), minuteSpinner.getValue()));
                Instant lastDate = dateTime.atZone(TimeZone.getDefault().toZoneId()).toInstant();
                habit.setLastDate(lastDate);
                saveAndCloseWindow(habit, event);
            } else {
                Alerts.showErrorAlert("Error al registrar el habito", "La fecha no puede ser posterior a la actual");
            }
        } else {
            Alerts.showErrorAlert("Error al registrar el habito", "Debe rellenar todos los campos");
        }
    }

    private boolean isDateValid() {
        return datePicker.getValue().isBefore(LocalDate.now()) || datePicker.getValue().isEqual(LocalDate.now());
    }

    private boolean areFieldsValid() {
        return isNumeric(frequencyLine.getText()) && !frequencyLine.getText().trim().isEmpty() && datePicker.getValue() != null && hourSpinner.getValue() != null
                && minuteSpinner.getValue() != null && activityChoice.getValue() != null && typeChoice.getValue() != null;
    }

    private boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    private void saveAndCloseWindow(Habit habit, Event event) {
        this.controller.saveHabit(habit);
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void onClose(Object input) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        activityChoice.setConverter(new StringConverter<>() {
            @Override
            public String toString(Activity activity) {
                return (activity != null) ? activity.getName() : "";
            }

            @Override
            public Activity fromString(String string) {
                return null;
            }
        });
    }
}
