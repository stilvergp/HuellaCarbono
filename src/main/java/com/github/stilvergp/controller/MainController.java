package com.github.stilvergp.controller;

import com.github.stilvergp.App;
import com.github.stilvergp.UserSession;
import com.github.stilvergp.model.Footprint;
import com.github.stilvergp.services.FootprintService;
import com.github.stilvergp.view.Scenes;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
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
    private TextField searchBox;

    private ObservableList<Footprint> footprints;

    @Override
    public void onOpen(Object input) {
        reloadFootprintsFromDatabase();
    }

    public void reloadFootprintsFromDatabase() {
        List<Footprint> footprints = new FootprintService().findByUser(UserSession.getInstance().getLoggedInUser());
        this.footprints = FXCollections.observableList(footprints);
        tableView.getItems().clear();
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

    public void saveFootprint(Footprint footprint) {
        FootprintService footprintService = new FootprintService();
        footprintService.save(footprint);
        reloadFootprintsFromDatabase();
    }

    @Override
    public void onClose(Object input) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        activityColumn.setCellValueFactory(footprint -> new SimpleStringProperty(footprint.getValue().getActivity().getName()));
        valueColumn.setCellValueFactory(footprint -> new SimpleDoubleProperty(footprint.getValue().getValue().doubleValue()).asObject());
        unitColumn.setCellValueFactory(footprint -> new SimpleStringProperty(footprint.getValue().getUnit()));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setCellFactory(_ -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            @Override
            protected void updateItem(Instant date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(formatter.format(date));
                }
            }
        });
        searchBox.textProperty().addListener((_, _, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                searchFootprints(newValue);
            }
        });
    }

}
