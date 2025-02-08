package com.github.stilvergp.controller;

import com.github.stilvergp.UserSession;
import com.github.stilvergp.model.entities.Activity;
import com.github.stilvergp.model.entities.Footprint;
import com.github.stilvergp.services.ActivityService;
import com.github.stilvergp.utils.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddFootprint extends Controller implements Initializable {

    @FXML
    private TextField valueLine;

    @FXML
    private Label footprintImpact;

    @FXML
    private TextField unitChoice;

    @FXML
    private ChoiceBox<Activity> activityChoice;

    @FXML
    private Spinner<Integer> hourSpinner;

    @FXML
    private Spinner<Integer> minuteSpinner;

    @FXML
    private DatePicker datePicker;

    private MainController controller;

    @Override
    public void onOpen(Object input) {
        this.controller = (MainController) input;
        reloadActivitiesFromDatabase();
    }

    private void reloadActivitiesFromDatabase() {
        this.activityChoice.getItems().clear();
        List<Activity> activities = new ActivityService().getActivities();
        ObservableList<Activity> activitiesList = FXCollections.observableArrayList(activities);
        this.activityChoice.setItems(activitiesList);
    }

    private void updateUnitText(Activity activity) {
        if (activity != null) {
            unitChoice.setText(new ActivityService().getUnit(activity));
        }
    }

    public void saveFootprint(Event event) {
        if (areFieldsValid()) {
            Footprint footprint = new Footprint();
            footprint.setActivity(activityChoice.getValue());
            footprint.setUser(UserSession.getInstance().getLoggedInUser());
            footprint.setValue(BigDecimal.valueOf(Double.parseDouble(valueLine.getText())));
            footprint.setUnit(unitChoice.getText());
            footprint.setDate(Instant.now());
            saveAndCloseWindow(footprint, event);
        } else {
            Alerts.showErrorAlert("Error al registrar la huella", "Debe rellenar todos los campos");
        }
    }

    private boolean areFieldsValid() {
        return isNumeric(valueLine.getText()) && !valueLine.getText().trim().isEmpty() && datePicker.getValue() != null && hourSpinner.getValue() != null
                && minuteSpinner.getValue() != null && activityChoice.getValue() != null;
    }

    private boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    private void calculateFootprintImpact(Activity activity) {
        if (activity != null) {
            BigDecimal emissionFactor = new ActivityService().getEmissionFactor(activity);
            Double impact = Double.parseDouble(valueLine.getText()) * emissionFactor.doubleValue();
            footprintImpact.setText(String.format("%.2f", impact));
        }
    }

    private void saveAndCloseWindow(Footprint footprint, Event event) {
        this.controller.saveFootprint(footprint);
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
        activityChoice.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            if (newValue != null) {
                updateUnitText(newValue);
            }
        });
        valueLine.textProperty().addListener((_, _, newValue) -> {
            if (!valueLine.getText().trim().isEmpty() && isNumeric(valueLine.getText()) && activityChoice.getValue() != null) {
                if (newValue != null) {
                    calculateFootprintImpact(activityChoice.getValue());
                }
            }
        });
    }
}
