package com.github.stilvergp.controller;

import com.github.stilvergp.App;
import com.github.stilvergp.UserSession;
import com.github.stilvergp.model.entities.Footprint;
import com.github.stilvergp.services.ActivityService;
import com.github.stilvergp.services.FootprintService;
import com.github.stilvergp.utils.Alerts;
import com.github.stilvergp.utils.PDFExporter;
import com.github.stilvergp.view.Scenes;
import javafx.beans.property.SimpleDoubleProperty;
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
import java.math.BigDecimal;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class MainController extends Controller implements Initializable {
    @FXML
    private TableView<Footprint> tableView;

    @FXML
    private TableColumn<Footprint, String> activityColumn;

    @FXML
    private TableColumn<Footprint, Double> valueColumn;

    @FXML
    private TableColumn<Footprint, String> unitColumn;

    @FXML
    private TableColumn<Footprint, Instant> dateColumn;

    @FXML
    private TableColumn<Footprint, String> footprintImpactColumn;

    @FXML
    private TextField searchBox;

    private ObservableList<Footprint> footprints;

    @Override
    public void onOpen(Object input) {
        reloadFootprintsFromDatabase();
    }

    public void reloadFootprintsFromDatabase() {
        tableView.getItems().clear();
        List<Footprint> footprints = new FootprintService().findByUser(UserSession.getInstance().getLoggedInUser());
        this.footprints = FXCollections.observableList(footprints);
        tableView.setItems(this.footprints);
    }

    private void searchFootprints(String filter) {
        ObservableList<Footprint> filteredFootprints = FXCollections.observableArrayList(
                footprints.stream()
                        .filter(footprint -> footprint.getActivity().getName().toLowerCase().contains(filter.toLowerCase()))
                        .toList()
        );
        tableView.setItems(filteredFootprints);
    }

    public void addFootprint() throws IOException {
        App.currentController.openModal(Scenes.ADDFOOTPRINT, "Registrando huella de carbono...", this, null);
    }

    public void myHabits() throws IOException {
        App.currentController.changeScene(Scenes.HABITLIST, null);
    }

    public void saveFootprint(Footprint footprint) {
        FootprintService footprintService = new FootprintService();
        footprintService.save(footprint);
        reloadFootprintsFromDatabase();
    }

    @Override
    public void onClose(Object input) {

    }

    public void removeFootprint() {
        Footprint footprint = tableView.getSelectionModel().getSelectedItem();
        if (footprint == null) return;
        Alerts.showConfirmationAlert("Eliminación de huella",
                "Esta a punto de eliminar esta huella, " +
                        "¿Está totalmente seguro de esta acción?").showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                FootprintService footprintService = new FootprintService();
                footprintService.delete(footprint);
                reloadFootprintsFromDatabase();
            }
        });
    }

    public void exportToPDF(Event event) {
        Window window = ((Node) (event.getSource())).getScene().getWindow();
        List<Footprint> userFootprints = UserSession.getInstance().getLoggedInUser().getFootprints();
        if (!userFootprints.isEmpty()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Exportar a PDF.");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            fileChooser.setInitialFileName("huellas.pdf");
            File file = fileChooser.showSaveDialog(window);
            if (file != null) {
                PDFExporter.exportFootprintsToPDF(file,userFootprints);
            } else {
                Alerts.showErrorAlert("Error al exportar huellas", "Ruta inválida");
            }
        } else {
            Alerts.showErrorAlert("Error al exportar huellas", "No hay huellas registradas");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableView.setEditable(true);
        valueColumn.setEditable(true);
        activityColumn.setCellValueFactory(footprint -> new SimpleStringProperty(footprint.getValue().getActivity().getName()));
        valueColumn.setCellValueFactory(footprint -> new SimpleDoubleProperty(footprint.getValue().getValue().doubleValue()).asObject());
        valueColumn.setOnEditCommit(event -> {
            if (event.getNewValue().equals(event.getOldValue())) return;
            if (event.getNewValue() < 9999) {
                Footprint footprint = tableView.getSelectionModel().getSelectedItem();
                footprint.setValue(BigDecimal.valueOf(event.getNewValue()));
                FootprintService footprintService = new FootprintService();
                footprintService.update(footprint);
                tableView.refresh();
            } else {
                Alerts.showErrorAlert("Error al actualizar la huella", "El valor introducido es mayor de lo permitido");
            }
        });
        unitColumn.setCellValueFactory(footprint -> new SimpleStringProperty(footprint.getValue().getUnit()));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setCellFactory(_ -> new TableCell<>() {
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
                searchFootprints(newValue);
            }
        });
        footprintImpactColumn.setCellValueFactory(footprint -> new SimpleStringProperty(calculateFootprintImpact(footprint.getValue())));
    }

    private String calculateFootprintImpact(Footprint footprint) {
        BigDecimal emissionFactor = new ActivityService().getEmissionFactor(footprint.getActivity());
        Double impact = footprint.getValue().doubleValue() * emissionFactor.doubleValue();
        return String.format("%.2f", impact) + " Kg CO²";
    }


}
