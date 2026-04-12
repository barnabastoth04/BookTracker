package com.booktracker.controllers;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

public class Dialog {
    public void display(String title, String text) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/booktracker/view/backgrounds/dialog.png"))));

        Label label = new Label();
        label.setText(text);

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> stage.close());
        okButton.setPrefWidth(80);

        VBox layout = new VBox(20, label, okButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 500, 200);

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
