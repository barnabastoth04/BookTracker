package com.booktracker.controllers;

import com.booktracker.config.FxmlView;
import com.booktracker.config.StageManager;
import javafx.fxml.FXML;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class StartController {
    private final StageManager stageManager;

    @Lazy
    public StartController(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @FXML
    private void onSignInClicked() {
        stageManager.switchToNextScene(FxmlView.SIGN_IN);
    }

    @FXML
    private void onSignUpClicked() {
        stageManager.switchToNextScene(FxmlView.SIGN_UP);
    }
}
