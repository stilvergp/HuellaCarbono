package com.github.stilvergp.view;

public enum Scenes {
    ROOT("view/layout.fxml"),
    MAIN("view/main.fxml"),
    LOGIN("view/login.fxml"),
    SIGNIN("view/signIn.fxml"),
    ADDFOOTPRINT("view/addFootprint.fxml"),
    ADDHABIT("view/addHabit.fxml"),
    HABITLIST("view/HabitList.fxml"),
    COMPAREFOOTPRINTS("view/compareFootprints.fxml"),
    UPDATEUSER("view/updateUser.fxml"),;

    private final String url;

    Scenes(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
