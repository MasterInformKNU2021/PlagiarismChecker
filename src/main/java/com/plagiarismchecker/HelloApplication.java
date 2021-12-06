package com.plagiarismchecker;

import com.plagiarismchecker.lexer.Lexer;
import com.plagiarismchecker.plagiarismchecker.Line;
import com.plagiarismchecker.plagiarismchecker.ProgramCode;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {

    private static List<ProgramCode> arrayOfAllProgram=new ArrayList<>();

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
        System.out.println("\n ");
        Lexer.Token[][] firstTokens = lexerOutputLineByLineForFirstText.tokensByLines;
        Lexer.Token[][] secondTokens = lexerOutputLineByLineForSecondText.tokensByLines;

        //Line t1=new Line();

//       ProgramCode pc=new ProgramCode("code1.txt");
//
//        for(int i=0; i<firstTokens.length; i++){
//
//            Line t1=new Line(i);
//
//            for(int j=0; j<firstTokens[i].length; j++){
//                if(firstTokens[i][j].indexInSymbolTable==-1) {
//                    t1.addToken(""+firstTokens[i][j].type);
//                }else{
//                    t1.addToken(lexerOutputLineByLineForFirstText.symbolTable[firstTokens[i][j].indexInSymbolTable]);
//                }
//
//            }
//            pc.addLine(t1);
//
//            //System.out.println("\n"+t1.toString());
//
//        }
//
//        arrayOfAllProgram.add(pc);
//        for(int i=0; i<arrayOfAllProgram.size(); i++){
//            System.out.println(arrayOfAllProgram.get(i));
//        }
        addFileToArray("code1.txt");
        for(int i=0; i<arrayOfAllProgram.size(); i++){
            System.out.println(arrayOfAllProgram.get(i));
        }


        System.exit(0);

//        launch();
    }
    public static void addFileToArray(String file){
        Lexer.LexerOutputLineByLine lexerOutputLineByLineForFirstText = Lexer.getTokensFromFileLineByLine("code1.txt");
        Lexer.outputLexerDataLineByLine(lexerOutputLineByLineForFirstText);
        Lexer.Token[][] firstTokens = lexerOutputLineByLineForFirstText.tokensByLines;

        ProgramCode pc=new ProgramCode(file);

        for(int i=0; i<firstTokens.length; i++){

            Line t1=new Line(i);

            for(int j=0; j<firstTokens[i].length; j++){
                if(firstTokens[i][j].indexInSymbolTable==-1) {
                    t1.addToken(""+firstTokens[i][j].type);
                }else{
                    t1.addToken(lexerOutputLineByLineForFirstText.symbolTable[firstTokens[i][j].indexInSymbolTable]);
                }

            }
            pc.addLine(t1);


        }

        arrayOfAllProgram.add(pc);

    }

}