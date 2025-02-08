package com.github.stilvergp.controller;

import com.github.stilvergp.App;
import com.github.stilvergp.UserSession;
import com.github.stilvergp.model.entities.User;
import com.github.stilvergp.services.UserService;
import com.github.stilvergp.utils.Alerts;
import com.github.stilvergp.view.Scenes;
import com.github.stilvergp.view.View;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController extends Controller implements Initializable {

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
        account.setVisible(UserSession.getInstance().isLoggedIn());
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

    public void openUpdateUser() throws IOException {
        App.currentController.openModal(Scenes.UPDATEUSER, "Actualizando usuario...", this, null);
    }

    public void updateUser(User user) {
        UserService userService = new UserService();
        userService.update(user);
    }

    public void deleteUser() {
        Alerts.showConfirmationAlert("Eliminación de cuenta", "Esta a punto de de eliminar su cuenta, " +
                "todos sus datos serán borrados, ¿esta totalmente seguro?").showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                User loggedInUser = UserSession.getInstance().getLoggedInUser();
                UserSession.getInstance().logout();
                UserService userService = new UserService();
                userService.delete(loggedInUser);
                try {
                    App.currentController.changeScene(Scenes.LOGIN, null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
