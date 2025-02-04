package com.github.stilvergp.controller;

import com.github.stilvergp.App;
import com.github.stilvergp.UserSession;
import com.github.stilvergp.model.entities.Footprint;
import com.github.stilvergp.utils.Alerts;
import com.github.stilvergp.utils.PDFExporter;
import com.github.stilvergp.view.Scenes;
import com.github.stilvergp.view.View;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AppController extends Controller implements Initializable {

    @FXML
    private Menu export;
    @FXML
    private Menu account;

    @FXML
    private BorderPane borderPane;

    private Controller centerController;

    @Override
    public void onOpen(Object input) throws IOException {
        changeScene(Scenes.LOGIN, null);
    }

    public void changeScene(Scenes scene, Object data) throws IOException {
        if (UserSession.getInstance().isLoggedIn()) {
            account.setVisible(true);
            export.setVisible(true);
        }
        View view = loadFXML(scene);
        borderPane.setCenter(view.scene);
        this.centerController = view.controller;
        this.centerController.onOpen(data);
    }

    public void openModal(Scenes scene, String title, Controller parent, Object data) throws IOException {
        View view = loadFXML(scene);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(App.stage);
        Scene _scene = new Scene(view.scene);
        stage.setScene(_scene);
        view.controller.onOpen(parent);
        stage.showAndWait();
    }

    @Override
    public void onClose(Object output) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public static View loadFXML(Scenes scenes) throws IOException {
        String url = scenes.getUrl();
        FXMLLoader loader = new FXMLLoader(App.class.getResource(url));
        Parent p = loader.load();
        Controller c = loader.getController();
        View view = new View();
        view.scene = p;
        view.controller = c;
        return view;
    }


    public void logOff() throws IOException {
        UserSession.getInstance().logout();
        changeScene(Scenes.LOGIN, null);
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
}
