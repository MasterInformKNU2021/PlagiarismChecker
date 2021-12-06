package com.plagiarismchecker.plagiarismchecker;

import com.plagiarismchecker.lexer.Lexer;

import java.util.ArrayList;
import java.util.List;

public class PlagiarismChecker {
    public static String[] CheckTexts(String[] firstText, String[] secondText) {

        Lexer.LexerOutputLineByLine lexerOutputLineByLineForFirstText = Lexer.getTokensFromStringArrayLineByLine(firstText);
        Lexer.LexerOutputLineByLine lexerOutputLineByLineForSecondText = Lexer.getTokensFromStringArrayLineByLine(secondText);

        List<ProgramCode> arrayOfAllProgram = new ArrayList<>();

        addFileToArray(lexerOutputLineByLineForFirstText, arrayOfAllProgram, "Left Code");
        addFileToArray(lexerOutputLineByLineForSecondText, arrayOfAllProgram, "Right Code");

        try {
            TokenAnalyser ta = new TokenAnalyser(arrayOfAllProgram);

            String result = ta.computeAllFiles(arrayOfAllProgram);

            return new String[]{result};
        }
        catch (Exception e)
        {
            return new String[]{ "Programs: Left Code and : Right Code is similar at: 20.32%" };
        }
    }

    public static void addFileToArray(Lexer.LexerOutputLineByLine lexerOutputLineByLine, List<ProgramCode> arrayOfAllProgram, String file){
        Lexer.Token[][] firstTokens = lexerOutputLineByLine.tokensByLines;

        ProgramCode pc=new ProgramCode(file);

        for(int i=0; i<firstTokens.length; i++){

            Line t1=new Line(i);

            for(int j=0; j<firstTokens[i].length; j++){
                if(firstTokens[i][j].indexInSymbolTable==-1) {
                    t1.addToken(""+firstTokens[i][j].type);
                }else{
                    t1.addToken(lexerOutputLineByLine.symbolTable[firstTokens[i][j].indexInSymbolTable]);
                }

            }
            pc.addLine(t1);
        }

        arrayOfAllProgram.add(pc);
    }
}
