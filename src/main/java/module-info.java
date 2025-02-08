module com.github.stilvergp {
    requires java.naming;
    requires jakarta.persistence;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.hibernate.orm.core;
    requires kernel;
    requires layout;

    opens com.github.stilvergp.model.entities to org.hibernate.orm.core;
    opens com.github.stilvergp.controller to javafx.fxml;
    opens com.github.stilvergp.view to javafx.fxml;

    exports com.github.stilvergp;
    exports com.github.stilvergp.controller;
    exports com.github.stilvergp.view;
    exports com.github.stilvergp.model.entities;
    exports com.github.stilvergp.model;
}