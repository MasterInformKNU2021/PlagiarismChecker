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
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Lexer.LexerOutput lexerOutput = Lexer.getTokensFromFile("code.txt");

        System.out.println("\n**************************  Lexer result for file:  *******************************\n");

        Lexer.outputLexerData(lexerOutput);

        lexerOutput = Lexer.getTokensFromLine(
                "std::pair<size_t, std::pair<size_t, std::pair<size_t, std::pair<size_t," +
                        " std::pair<size_t, std::pair<size_t, size_t>>>>>> a;");

        System.out.println("\n***********************  Lexer result for single Line:  ***************************\n");

        System.out.println("Lexer result for single Line:");
        Lexer.outputLexerData(lexerOutput);

        Lexer.LexerOutputLineByLine lexerOutputLineByLine = Lexer.getTokensFromFileLineByLine("code.txt");

        System.out.println("\n********************  Lexer result for file line by line:  ************************\n");

        Lexer.outputLexerDataLineByLine(lexerOutputLineByLine);

        System.exit(0);
    }
}