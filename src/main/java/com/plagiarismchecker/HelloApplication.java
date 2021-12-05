package com.plagiarismchecker;

import com.plagiarismchecker.lexer.Lexer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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
        Lexer.LexerOutputLineByLine lexerOutputLineByLineForFirstText = Lexer.getTokensFromFileLineByLine("code1.txt");
        Lexer.LexerOutputLineByLine lexerOutputLineByLineForSecondText = Lexer.getTokensFromFileLineByLine("code2.txt");

        Lexer.outputLexerDataLineByLine(lexerOutputLineByLineForFirstText);
        Lexer.outputLexerDataLineByLine(lexerOutputLineByLineForSecondText);

        Lexer.Token[][] firstTokens = lexerOutputLineByLineForFirstText.tokensByLines;
        Lexer.Token[][] secondTokens = lexerOutputLineByLineForSecondText.tokensByLines;

        System.exit(0);

//        launch();
    }
}