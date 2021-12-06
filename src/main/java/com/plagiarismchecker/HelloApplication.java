package com.plagiarismchecker;

import com.plagiarismchecker.lexer.Lexer;
import com.plagiarismchecker.plagiarismchecker.Line;
import com.plagiarismchecker.plagiarismchecker.ProgramCode;
import com.plagiarismchecker.plagiarismchecker.TokenAnalyser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1277, 904);
        stage.setTitle("Plagiarism Checker");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}