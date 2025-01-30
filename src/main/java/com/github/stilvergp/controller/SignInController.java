package com.github.stilvergp.controller;

import com.github.stilvergp.model.User;
import com.github.stilvergp.services.UserService;
import com.github.stilvergp.utils.Alerts;
import com.github.stilvergp.utils.Security;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.Instant;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInController extends Controller implements Initializable {

    @FXML
    private TextField name;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    private LoginController controller;

    @Override
    public void onOpen(Object input) {
        this.controller = (LoginController) input;
    }

    public void addUser(Event event) {
        if (!areFieldsValid()) {
            return;
        }
        User userExists = new UserService().getUserByName(name.getText());
        if (userExists == null) {
            if (isValidEmail(email.getText())) {
                if (isValidPassword(password.getText())) {
                    User user = new User();
                    user.setName(name.getText());
                    user.setEmail(email.getText());
                    user.setPassword(Security.hashPassword(password.getText()));
                    user.setRegistrationDate(Instant.now());
                    this.controller.saveUser(user);
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                } else {
                    Alerts.showErrorAlert("Error al crear el usuario",
                            "Formato de la contraseña incorrecto. La contraseña debe contener:",
                            "Al menos una mayúscula \n" +
                                    "Al menos un número \n" +
                                    "Al menos un carácter especial \n" +
                                    "Longitud mínima de 8 caracteres.");
                }
            } else {
                Alerts.showErrorAlert("Error al crear el usuario", "Formato del email incorrecto. (email@ejemplo.com)");
            }
        }
    }

    private boolean areFieldsValid() {
        return name.getText().trim().isEmpty() || email.getText().trim().isEmpty() || password.getText().trim().isEmpty();
    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    public void onClose(Object input) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
