package com.github.stilvergp.controller;

import com.github.stilvergp.model.UserSession;
import com.github.stilvergp.model.entities.User;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdateUserController extends Controller implements Initializable {

    @FXML
    private TextField name;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    private AppController controller;

    private User loggedUser;

    @Override
    public void onOpen(Object input) throws IOException {
        this.controller = (AppController) input;
        this.loggedUser = UserSession.getInstance().getLoggedInUser();
        name.setText(loggedUser.getName());
        email.setText(loggedUser.getEmail());
    }

    @Override
    public void onClose(Object input) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void updateUser(Event event) {
        boolean updated = false;

        if (!name.getText().trim().isEmpty() && !name.getText().equals(loggedUser.getName())) {
            loggedUser.setName(name.getText());
            updated = true;
        }
        if (!email.getText().trim().isEmpty() && !email.getText().equals(loggedUser.getEmail())) {
            loggedUser.setEmail(email.getText());
            updated = true;
        }
        if (!password.getText().trim().isEmpty() && !password.getText().equals(loggedUser.getPassword())) {
            loggedUser.setPassword(password.getText());
            updated = true;
        }

        if (updated) {
            this.controller.updateUser(loggedUser);
        }
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
