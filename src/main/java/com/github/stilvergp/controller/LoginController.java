package com.github.stilvergp.controller;

import com.github.stilvergp.App;
import com.github.stilvergp.UserSession;
import com.github.stilvergp.model.User;
import com.github.stilvergp.services.UserService;
import com.github.stilvergp.utils.Alerts;
import com.github.stilvergp.view.Scenes;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends Controller implements Initializable {

    @FXML
    private TextField name;

    @FXML
    private PasswordField password;

    public void login(User user) {
        UserSession.getInstance().login(user);
    }

    public void goToMain() throws IOException {
        if (name.getText().trim().isEmpty() || password.getText().trim().isEmpty()) {
            return;
        }
        User user = new UserService().getUserByName(name.getText());
        if (user != null && user.isMyPassword(password.getText())) {
            login(user);
            App.currentController.changeScene(Scenes.MAIN, null);
        } else {
            Alerts.showErrorAlert("Error de inicio de sesión",
                    "Usuario o contraseña incorrectos, " +
                            "por favor intente nuevamente");
        }
    }

    public void saveUser(User user) {
        UserService userService = new UserService();
        userService.save(user);
    }

    public void signIn() throws IOException {
        name.clear();
        password.clear();
        App.currentController.openModal(Scenes.SIGNIN, "Agregando usuario...", this, null);
    }

    @Override
    public void onOpen(Object input) {

    }

    @Override
    public void onClose(Object input) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
