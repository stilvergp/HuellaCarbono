package com.github.stilvergp.controller;

import com.github.stilvergp.services.CategoryService;
import com.github.stilvergp.model.entities.Category;
import com.github.stilvergp.model.entities.User;
import com.github.stilvergp.services.UserService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CompareFootprintsController extends Controller implements Initializable {

    @FXML
    private TextField searchBox;

    @FXML
    private ChoiceBox<Category> categoryChoice;

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, String> userColumn;

    @FXML
    private TableColumn<User, String> impactColumn;

    private ObservableList<User> users;

    @Override
    public void onOpen(Object input) throws IOException {
        reloadCategoriesFromDB();
    }

    private void reloadCategoriesFromDB() {
        this.categoryChoice.getItems().clear();
        List<Category> categories = new CategoryService().getCategories();
        ObservableList<Category> categoryList = FXCollections.observableList(categories);
        this.categoryChoice.setItems(categoryList);
    }

    private void reloadTableWithData() {
        this.tableView.getItems().clear();
        List<User> users = new UserService().getUsers();
        this.users = FXCollections.observableList(users);
        this.tableView.setItems(this.users);
    }

    private void searchUsers(String filter) {
        ObservableList<User> filteredUsers = FXCollections.observableList(
                this.users.stream()
                        .filter(user -> user.getName().toLowerCase().contains(filter.toLowerCase())).toList()
        );
        this.tableView.setItems(filteredUsers);
    }

    @Override
    public void onClose(Object input) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoryChoice.setConverter(new StringConverter<>() {
            @Override
            public String toString(Category category) {
                return (category != null ? category.getName() : "");
            }

            @Override
            public Category fromString(String string) {
                return null;
            }
        });
        categoryChoice.getSelectionModel().selectedItemProperty().addListener((_, _, _) -> reloadTableWithData());
        userColumn.setCellValueFactory(user -> new SimpleStringProperty(user.getValue().getName()));
        impactColumn.setCellValueFactory(user -> new SimpleStringProperty(String.format(
                "%.2f", new UserService().getImpact(user.getValue(), categoryChoice.getSelectionModel().getSelectedItem())) + " Kg CO²"));
        searchBox.textProperty().addListener((_,_,newValue) -> {
            if (newValue != null) {
                searchUsers(newValue);
            }
        });
    }
}
