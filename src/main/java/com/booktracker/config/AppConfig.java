package com.booktracker.config;

import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;

@Configuration
public class AppConfig {
    private final FxmlLoader fxmlLoader;
    private final String applicationName;

    public AppConfig(FxmlLoader fxmlLoader, @Value("${application.title}") String applicationName) {
        this.fxmlLoader = fxmlLoader;
        this.applicationName = applicationName;
    }

    @Bean
    @Lazy
    public StageManager stageManager(Stage stage) throws IOException {
        return new StageManager(fxmlLoader, stage, applicationName);
    }
}
