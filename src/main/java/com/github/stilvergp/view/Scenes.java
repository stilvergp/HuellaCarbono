package com.github.stilvergp.view;

public enum Scenes {
    ROOT("view/layout.fxml"),
    MAIN("view/main.fxml"),
    LOGIN("view/login.fxml"),
    FORMSIGNIN("view/formSignIn.fxml"),
    ADDFOOTPRINT("view/addFootprint.fxml");

    private String url;

    Scenes(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
