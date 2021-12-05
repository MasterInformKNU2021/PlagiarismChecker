package com.plagiarismchecker.plagiarismchecker;

import com.plagiarismchecker.lexer.Lexer;

import java.util.ArrayList;
import java.util.List;

public class PlagiarismChecker {
    public static String[] CheckTexts(String[] firstText, String[] secondText) {

        Lexer.LexerOutputLineByLine lexerOutputLineByLineForFirstText = Lexer.getTokensFromStringArrayLineByLine(firstText);
        Lexer.LexerOutputLineByLine lexerOutputLineByLineForSecondText = Lexer.getTokensFromStringArrayLineByLine(secondText);

        List<String> output = new ArrayList<>();

        output.add("Result: ");
        output.add("30 % of plagiarism");



        return output.toArray(String[]::new);
    }
}
