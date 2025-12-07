package com.booktracker.controllers;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Dialog {
    public void display(String title, String text) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        Label label = new Label();
        label.setText(text);

        VBox layout = new VBox(label);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 500, 200);

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
