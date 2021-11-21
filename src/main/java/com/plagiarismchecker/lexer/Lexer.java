package com.plagiarismchecker.lexer;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Lexer {

    public enum TokenType {
        // * numeric constants
        NumericConstantsBegin,

        IntNumber,
        FloatNumber,

        NumericConstantsEnd,

        // * literals and symbolic constants
        Character,
        String,

        // * preprocessor directives
        PreprocessorDirectivesBegin,

        SharpInclude,
        SharpDefine,
        SharpError,
        SharpImport,
        SharpLine,
        SharpPragma,
        SharpUsing,
        SharpIf,
        SharpIfdef,
        SharpIfndef,
        SharpEndif,
        SharpElif,
        SharpElse,
        SharpUndef,

        PreprocessorDirectivesEnd,

        // * comments
        SingleLineComment,
        MultyLineComment,

        // * keywords
        KeywordsBegin,

        // types
        Bool,
        Char,
        Short,
        Int,
        Long,
        Unsigned,
        Float,
        Double,
        Struct,
        Class,
        Enum,
        Auto,
        Void,

        // access modifier
        Public,
        Protected,
        Private,


        Do,
        While,
        For,
        If,
        Else,
        Continue,
        Break,
        Return,
        Default,

        Typeid,
        Using,

        True,
        False,

        Const,
        Volatile,
        Constexpr,
        Static,

        // exceptions
        Noexcept,
        Throw,

        // "Four Horsemen"
        Static_cast,
        Const_cast,
        Dynamic_cast,
        Reinterpret_cast,

        KeywordsEnd,

        // * identifiers
        Id,

        // * operators
        OperatorsBegin,

        Association,
        // arithmetic
        Add,
        Increment,
        Sub,
        Decrement,
        Multiply,
        Divide,
        Mod,
        AddAssociation,
        SubAssociation,
        MultiplyAssociation,
        DivideAssociation,
        ModAssociation,

        // logic
        And,
        Or,
        Not,
        Equal,
        Less,
        Great,
        LessEqual,
        GreatEqual,
        NotEqual,
        // bitwise
        BinaryAnd,
        BinaryOr,
        BinaryNot,
        BinaryXor,
        BinaryShiftToLeft,
        BinaryShiftToRight,
        BinaryAndAssociation,
        BinaryOrAssociation,
        BinaryNotAssociation,
        BinaryXorAssociation,
        BinaryShiftToLeftAssociation,
        BinaryShiftToRightAssociation,
        // conditional ( ? : )
        QUEST,
        Colon,
        // others
        Dot,
        MemberAccess,
        Scope,
        Dotted,

        OperatorsEnd,

        // * punctuation marks
        PunctuationMarksBegin,

        Comma,
        Semicolon,
        LeftParen,
        RightParen,
        LeftBrack,
        RightBrack,
        LeftBrace,
        RightBrace,

        PunctuationMarksEnd,

        // * invalid
        Invalid,

        CountOf
    }

    public static String[] tokenToString = {
            // * numeric constants
            "NumericConstantsBegin",

            "IntNumber",
            "FloatNumber",

            "NumericConstantsEnd",

            // * literals and symbolic constants
            "Character",
            "String",

            // * preprocessor directives
            "PreprocessorDirectivesBegin",

            "#include",
            "#define",
            "#error",
            "#import",
            "#line",
            "#pragma",
            "#using",
            "#if",
            "#ifdef",
            "#ifndef",
            "#endif",
            "#elif",
            "#else",
            "#undef",

            "PreprocessorDirectivesEnd",

            // * comments
            "// ...",
            "/* ... */",

            // * keywords
            "KeywordsBegin",

            // types
            "bool",
            "char",
            "short",
            "int",
            "long",
            "unsigned",
            "float",
            "double",
            "struct",
            "class",
            "enum",
            "auto",
            "void",

            // access modifier
            "public",
            "protected",
            "private",


            "do",
            "while",
            "for",
            "if",
            "else",
            "continue",
            "break",
            "return",
            "default",

            "typeid",
            "using",

            "true",
            "false",

            "const",
            "volatile",
            "constexpr",
            "static",

            // exceptions
            "noexcept",
            "throw",

            // "Four Horsemen"
            "static_cast",
            "const_cast",
            "dynamic_cast",
            "reinterpret_cast",

            "KeywordsEnd",

            // * identifiers
            "Id",

            // * operators
            "OperatorsBegin",

            "=",
            // arithmetic
            "+",
            "++",
            "-",
            "--",
            "*",
            "/",
            "%",
            "+=",
            "-=",
            "*=",
            "/=",
            "%=",
            // logic
            "&&",
            "||",
            "!",
            "==",
            "<",
            ">",
            "<=",
            ">=",
            "!=",
            // bitwise
            "&",
            "|",
            "~",
            "^",
            "<<",
            ">>",
            "&=",
            "|=",
            "~=",
            "^=",
            "<<=",
            ">>=",
            // conditional ( ? : )
            "?",
            ":",
            // others
            ".",
            "->",
            "::",
            "...",

            "OperatorsEnd",

            // * punctuation marks
            "PunctuationMarksBegin",

            ",",
            ";",
            "(",
            ")",
            "[",
            "]",
            "{",
            "}",

            "PunctuationMarksEnd",

            // * invalid
            "Invalid"
    };

    public static class Token {
        public int line;
        public int column;
        public int indexInSymbolTable;
        public TokenType type;

        Token(
                int line,
                int column,
                TokenType type
        ) {
            this(line, column, type, -1);
        }

        Token(
                int line,
                int column,
                TokenType type,
                int indexInSymbolTable
        ) {
            this.line = line;
            this.column = column;
            this.type = type;
            this.indexInSymbolTable = indexInSymbolTable;
        }
    }

    public static class TokenError {
        public String message;
        public String symbol;
        public int line;
        public int column;
        public int length;

        TokenError(String message, String symbol, int line, int column, int length) {
            this.message = message;
            this.symbol = symbol;
            this.line = line;
            this.column = column;
            this.length = length;
        }
    }

    public static class LexerOutput {
        public String[] symbolTable;
        public Token[] tokens;
        public TokenError[] tokenErrors;

        public LexerOutput() {
            symbolTable = new String[0];
            tokens = new Token[0];
            tokenErrors = new TokenError[0];
        }

        public LexerOutput(String[] symbolTable, Token[] tokens, TokenError[] tokenErrors) {
            this.symbolTable = symbolTable;
            this.tokens = tokens;
            this.tokenErrors = tokenErrors;
        }
    }

    private static boolean isSpace(char c) {
        return Character.isWhitespace(c);
    }

    private static boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    private static boolean isLower(char c) {
        return Character.isLowerCase(c);
    }

    private static boolean isAlpha(char c) {
        return Character.isAlphabetic(c);
    }

    private static boolean isAlphaOrDigit(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private static boolean isValidWordBegin(char c) {
        return isAlpha(c) || (c == '_');
    }

    private static boolean isValidWordPart(char c) {
        return isAlphaOrDigit(c) || (c == '_');
    }

    private static boolean isOperator(char c) {
        return switch (c) {
            case '.', '=', '+', '-', '*', '/', '%', '&', '|', '!', '<', '>', '~', '^', '?', ':' -> true;
            default -> false;
        };
    }

    private static boolean isValidNumberBegin(char c) {
        return isDigit(c) || (c == '.');
    }

    private static boolean isValidNumberPart(char c) {
        return isDigit(c) || (c == '.') || (c == '\'');
    }

    private static boolean isBinaryNumber(char c) {
        return (c == '0' || c == '1');
    }

    private static boolean isValidBinaryNumberPart(char c) {
        return (isBinaryNumber(c) || c == '\'');
    }

    private static boolean isHexNumber(char c) {
        return switch (c) {
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' -> true;
            default -> false;
        };
    }

    private static boolean isValidHexNumberPart(char c) {
        return (isHexNumber(c) || c == '\'');
    }

    private static boolean isPunctuationMarks(char c) {
        return switch (c) {
            case ',', ';', '(', ')', '[', ']', '{', '}' -> true;
            default -> false;
        };
    }

    private static boolean isValidSymbolAfterNumber(char c) {
        return isOperator(c) || isSpace(c) || isPunctuationMarks(c) || c == '/';
    }

    private static boolean isSymbolType(TokenType type) {
        return switch (type) {
            case IntNumber, FloatNumber, Character, String, SharpInclude, SharpDefine, SharpError, SharpImport,
                    SharpLine, SharpPragma, SharpUsing, SharpIf, SharpIfdef, SharpIfndef, SharpElif, SharpElse,
                    SharpUndef, SingleLineComment, MultyLineComment, Id -> true;
            default -> false;
        };
    }

    private static boolean isMultiLinePreprocessorDirectives(TokenType type) {
        return switch (type) {
            case SharpIf, SharpIfdef, SharpIfndef, SharpElif, SharpElse -> true;
            default -> false;
        };
    }

    private static boolean isEndOfMultiLinePreprocessorDirectives(TokenType type) {
        return isMultiLinePreprocessorDirectives(type) || type == TokenType.SharpEndif;
    }

    private static boolean isSingleWordPreprocessorDirectives(TokenType type) {
        return type == TokenType.SharpEndif;
    }


    private static class FAState {
        public char c;
        public TokenType type;
        public List<FAState> transitions;

        public FAState() {
            this('\0', TokenType.Invalid);
        }

        public FAState(char c, TokenType type) {
            this.c = c;
            this.type = type;
            transitions = new ArrayList<>();
        }
    }

    private static FAState faState;

    private static class StringToTokenType {
        public String string;
        public TokenType type;

        public StringToTokenType(String string, TokenType type) {
            this.string = string;
            this.type = type;
        }
    }

    private static void generateFaState(
            StringToTokenType[] stringToTokenTypes,
            FAState state,
            int offset,
            int[] position
    ) {
        do {
            int lastPosition = position[0];
            if (stringToTokenTypes[position[0]].string.length() > offset) {
                FAState newState = new FAState();
                generateFaState(stringToTokenTypes, newState, offset + 1, position);
                state.transitions.add(newState);
            } else {
                state.c = stringToTokenTypes[position[0]].string.charAt(offset - 1);
                state.type = stringToTokenTypes[position[0]].type;
                ++position[0];
            }

            for (int i = 0; i < offset && position[0] < stringToTokenTypes.length; ++i) {
                if (stringToTokenTypes[position[0]].string.length() <= i ||
                        stringToTokenTypes[position[0]].string.charAt(i) != stringToTokenTypes[lastPosition].string.charAt(i)) {
                    return;
                }
            }

        } while (position[0] < stringToTokenTypes.length);
    }

    private static void generateFa() {
        int begin = TokenType.OperatorsBegin.ordinal() + 1;
        int end = TokenType.OperatorsEnd.ordinal();

        StringToTokenType[] stringToTokenTypes = new StringToTokenType[end - begin];

        for (int i = begin; i < end; ++i) {
            stringToTokenTypes[i - begin] = new StringToTokenType(tokenToString[i], TokenType.values()[i]);
        }

        Arrays.sort(stringToTokenTypes, (a, b) -> {
            if (a.string.compareTo(b.string) == 0) {
                return a.type.ordinal() - b.type.ordinal();
            }
            return a.string.compareTo(b.string);
        });

        int[] position = {0};
        generateFaState(stringToTokenTypes, faState, 0, position);
    }

    private static Pair<TokenType, Boolean> tryGetPreprocessorDirectives(String word) {
        for (
                int i = TokenType.PreprocessorDirectivesBegin.ordinal() + 1;
                i < TokenType.PreprocessorDirectivesEnd.ordinal();
                ++i
        ) {
            if (word.equals(tokenToString[i])) {
                return new Pair<>(TokenType.values()[i], true);
            }
        }
        return new Pair<>(TokenType.Invalid, false);
    }

    private static Pair<TokenType, Boolean> tryGetKeywords(String word) {
        for (
                int i = TokenType.KeywordsBegin.ordinal() + 1;
                i < TokenType.KeywordsEnd.ordinal();
                ++i
        ) {
            if (word.equals(tokenToString[i])) {
                return new Pair<>(TokenType.values()[i], true);
            }
        }
        return new Pair<>(TokenType.Invalid, false);
    }

    private static int[] tryGetFromSymbolTable(List<String> symbolTable, String symbol) {
        for (int i = 0; i < symbolTable.size(); ++i) {
            if (symbol.equals(symbolTable.get(i))) {
                return new int[]{i, 1};
            }
        }

        return new int[]{-1, 0};
    }

    private static class CommonData {
        public List<String> symbolTable;
        public List<Token> tokens;
        public List<TokenError> tokenErrors;
        public String code;
        public int line;
        public int column;

        public CommonData() {
            symbolTable = new ArrayList<>();
            tokens = new ArrayList<>();
            tokenErrors = new ArrayList<>();
            code = "";
            line = 0;
            column = 0;
        }
    }

    private static class BetweenLinesData {
        public String data;
        public int line;
        public int column;
        public boolean isActive;
        public TokenType type;

        public BetweenLinesData() {
            data = "";
            line = 0;
            column = 0;
            isActive = false;
            type = TokenType.Invalid;
        }
    }

    private static void createNewToken(
            List<String> symbolTable,
            List<Token> tokens,
            int line,
            int column,
            TokenType type
    ) {
        createNewToken(symbolTable, tokens, line, column, type, "");
    }

    private static void createNewToken(
            List<String> symbolTable,
            List<Token> tokens,
            int line,
            int column,
            TokenType type,
            String symbol
    ) {
        if (isSymbolType(type)) {
            int[] fromSymbolTable = tryGetFromSymbolTable(symbolTable, symbol);
            if (fromSymbolTable[1] == 1) {
                tokens.add(new Token(line, column, type, fromSymbolTable[0]));
            } else {
                tokens.add(new Token(line, column, type, symbolTable.size()));
                symbolTable.add(symbol);
            }
        } else {
            tokens.add(new Token(line, column, type));
        }
    }

    private static void createNewToken(
            List<String> symbolTable,
            List<Token> tokens,
            BetweenLinesData betweenLinesData
    ) {
        createNewToken(symbolTable,
                tokens,
                betweenLinesData.line,
                betweenLinesData.column,
                betweenLinesData.type,
                betweenLinesData.data
        );
    }

    private static void createNewTokenError(
            List<TokenError> tokenErrors,
            String message,
            String symbol,
            int line,
            int column
    ) {
        int length = symbol.length();

        tokenErrors.add(new TokenError(message, symbol, line, column, length));
    }

    private static void createNewTokenError(
            List<TokenError> tokenErrors,
            String message,
            BetweenLinesData betweenLinesData
    ) {
        createNewTokenError(
                tokenErrors,
                message,
                betweenLinesData.data,
                betweenLinesData.line,
                betweenLinesData.column
        );
    }

    private static void handleDigit(CommonData data) {
        char c = data.code.charAt(data.column);

        int start = data.column;
        ++data.column;

        boolean hasDot = (c == '.');
        int dotPosition = (hasDot ? data.column : -1);
        boolean isDecimal = true;
        boolean isHex = false;
        boolean isBinary = false;
        boolean isFirstZero = (c == '0');

        int lastNumberSeparatorIndex = start;

        if (data.column >= data.code.length()) {
            if (hasDot) {
                --data.column;
                handleOperatorByFa(data);
                return;
            }
            createNewToken(data.symbolTable, data.tokens, data.line, start, TokenType.IntNumber, data.code.substring(start, start + 1));
            return;
        }

        char nextChar = data.code.charAt(data.column);
        if (hasDot && !isDigit(nextChar)) {
            --data.column;
            handleOperatorByFa(data);
            return;
        }
        if (!isFirstZero && !hasDot && !isValidNumberBegin(nextChar)) {
            createNewToken(data.symbolTable, data.tokens, data.line, start, TokenType.IntNumber, data.code.substring(start, start + 1));
            return;
        }
        if (isFirstZero && nextChar == 'b') {
            isBinary = true;
            isDecimal = false;
        } else if (isFirstZero && nextChar == 'x') {
            isHex = true;
            isDecimal = false;
        } else if (!isValidNumberPart(nextChar)) {
            createNewToken(data.symbolTable, data.tokens, data.line, start, TokenType.IntNumber, data.code.substring(start, start + 1));
            return;
        }

        if (nextChar == '\'') {
            lastNumberSeparatorIndex = data.column;
        }

        if (nextChar == '.') {
            hasDot = true;
            dotPosition = data.column;
        }

        ++data.column;

        while (data.column < data.code.length() &&
                ((isDecimal && isValidNumberPart(data.code.charAt(data.column))) ||
                        (isHex && isValidHexNumberPart(data.code.charAt(data.column))) ||
                        (isBinary && isValidBinaryNumberPart(data.code.charAt(data.column))))) {
            if (hasDot && data.code.charAt(data.column) == '.') {
                ++data.column;
                createNewTokenError(
                        data.tokenErrors,
                        "Error: double dot in number value",
                        data.code.substring(start, data.column),
                        data.line,
                        start
                );
                return;
            }
            if (!hasDot && data.code.charAt(data.column) == '.') {
                if (data.column - lastNumberSeparatorIndex == 1) {
                    ++data.column;
                    createNewTokenError(
                            data.tokenErrors,
                            "Error: number separator and dot too close",
                            data.code.substring(start, data.column),
                            data.line,
                            start
                    );
                    return;
                }
                hasDot = true;
                dotPosition = data.column;
            }
            if (data.code.charAt(data.column) == '\'') {
                if (data.column - lastNumberSeparatorIndex == 1) {
                    ++data.column;
                    createNewTokenError(
                            data.tokenErrors,
                            "Error: number separators too close",
                            data.code.substring(start, data.column),
                            data.line,
                            start
                    );
                    return;
                }
                if (data.column - dotPosition == 1) {
                    ++data.column;
                    createNewTokenError(
                            data.tokenErrors,
                            "Error: dot and number separator too close",
                            data.code.substring(start, data.column),
                            data.line,
                            start
                    );
                    return;
                }
                lastNumberSeparatorIndex = data.column;
            }
            ++data.column;
        }

        if (data.column < data.code.length() && !isValidSymbolAfterNumber(data.code.charAt(data.column))) {
            ++data.column;
            createNewTokenError(
                    data.tokenErrors,
                    "Error: invalid symbol after number",
                    data.code.substring(start, data.column),
                    data.line,
                    start
            );
            return;
        }
        if (!((isDecimal && isDigit(data.code.charAt(data.column - 1))) ||
                (isHex && isHexNumber(data.code.charAt(data.column - 1)) ||
                        (isBinary && isBinaryNumber(data.code.charAt(data.column - 1)))))) {
            createNewTokenError(
                    data.tokenErrors,
                    "Error: invalid number end",
                    data.code.substring(start, data.column),
                    data.line,
                    start
            );
            return;
        }

        String number = data.code.substring(start, data.column);
        if (hasDot) {
            createNewToken(data.symbolTable, data.tokens, data.line, start, TokenType.FloatNumber, number);
        } else {
            createNewToken(data.symbolTable, data.tokens, data.line, start, TokenType.IntNumber, number);
        }
    }

    private static void handleLiteralsConstant(CommonData data) {
        char c = data.code.charAt(data.column);

        int start = data.column;
        ++data.column;
        if (data.column >= data.code.length()) {
            createNewTokenError(
                    data.tokenErrors,
                    "Error: unfinished symbol: symbol on end of line",
                    "" + c,
                    data.line,
                    data.column
            );
            return;
        }
        char nextChar = data.code.charAt(data.column);

        if (nextChar == '\'') {
            createNewTokenError(
                    data.tokenErrors,
                    "Error: empty character constant",
                    "" + c + nextChar,
                    data.line,
                    data.column
            );
            return;
        }

        boolean isNeedAdditionalChar = (nextChar == '\\');
        char additionalChar = '\0';
        if (isNeedAdditionalChar) {
            ++data.column;
            additionalChar = nextChar;

            if (data.column >= data.code.length()) {
                createNewTokenError(
                        data.tokenErrors,
                        "Error: unfinished symbol: symbols on end of line",
                        "" + c + additionalChar,
                        data.line,
                        data.column
                );
                return;
            }
            nextChar = data.code.charAt(data.column);
        }

        ++data.column;
        if (data.column >= data.code.length()) {
            String text;
            if (isNeedAdditionalChar) {
                text = "" + c + additionalChar + nextChar;
            } else {
                text = "" + c + nextChar;
            }

            createNewTokenError(
                    data.tokenErrors,
                    "Error: unfinished symbol: symbols on end of line",
                    text,
                    data.line,
                    data.column
            );
            return;
        }
        char lastChar = data.code.charAt(data.column);

        if (lastChar != '\'') {
            String text;
            if (isNeedAdditionalChar) {
                text = "" + c + additionalChar + nextChar + lastChar;
            } else {
                text = "" + c + nextChar + lastChar;
            }

            createNewTokenError(
                    data.tokenErrors,
                    "Error: too many characters in symbol constant",
                    text,
                    data.line,
                    data.column
            );
            return;
        }
        ++data.column;

        String word = data.code.substring(start, data.column);

        createNewToken(data.symbolTable, data.tokens, data.line, start, TokenType.Character, word);
    }

    private static void handleStringConstant(CommonData data, BetweenLinesData stringConstantData) {
        int start = data.column;
        if (!stringConstantData.isActive) {
            ++data.column;
        }

        boolean isPreviousSpesialSymbol = false;

        while (data.column < data.code.length() && !(!isPreviousSpesialSymbol && data.code.charAt(data.column) == '\"')) {
            if (isPreviousSpesialSymbol) {
                isPreviousSpesialSymbol = false;
            } else if (data.code.charAt(data.column) == '\\') {
                isPreviousSpesialSymbol = true;
            }
            ++data.column;
        }

        if (data.column >= data.code.length() && isPreviousSpesialSymbol) {
            String text = data.code.substring(start, data.column - 1);
            if (stringConstantData.isActive) {
                stringConstantData.data += text;
                return;
            }
            stringConstantData.data = text;
            stringConstantData.line = data.line;
            stringConstantData.column = start;
            stringConstantData.isActive = true;
            return;
        } else if (data.column >= data.code.length()) {
            if (!stringConstantData.isActive) {
                stringConstantData.data = "";
                stringConstantData.line = data.line;
                stringConstantData.column = data.column;
            }
            stringConstantData.data += data.code.substring(start, data.column);
            stringConstantData.isActive = false;

            createNewTokenError(
                    data.tokenErrors,
                    "Error: unfinished string constant",
                    stringConstantData
            );
            return;
        }

        ++data.column;
        String word = data.code.substring(start, data.column);

        if (stringConstantData.isActive) {
            stringConstantData.data += word;
            stringConstantData.type = TokenType.String;
            createNewToken(
                    data.symbolTable,
                    data.tokens,
                    stringConstantData
            );
            stringConstantData.isActive = false;
            return;
        }

        createNewToken(data.symbolTable, data.tokens, data.line, start, TokenType.String, word);
    }

    private static Pair<TokenType, Boolean> tryHandlePreprocessorWord(CommonData data) {
        int start = data.column;
        ++data.column;
        while (data.column < data.code.length() && isLower(data.code.charAt(data.column))) {
            ++data.column;
        }

        String word = data.code.substring(start, data.column);

        return tryGetPreprocessorDirectives(word);
    }

    private static void handlePreprocessorDirectives(CommonData data, BetweenLinesData preprocessorDirectivesData) {
        int start = data.column;

        TokenType type;
        boolean isEmptpyLine = preprocessorDirectivesData.isActive;

        if (preprocessorDirectivesData.isActive) {
            type = preprocessorDirectivesData.type;
        } else {
            Pair<TokenType, Boolean> preprocessorDirectives = tryHandlePreprocessorWord(data);

            if (!preprocessorDirectives.getValue()) {
                String word = data.code.substring(start, data.column);
                createNewTokenError(
                        data.tokenErrors,
                        "Error: undefined preprocessor directives",
                        word,
                        data.line,
                        data.column
                );
                return;
            }

            type = preprocessorDirectives.getKey();
            preprocessorDirectivesData.type = type;
            if (isSingleWordPreprocessorDirectives(type)) {
                createNewToken(data.symbolTable, data.tokens, data.line, start, type);
                return;
            }
        }

        if (isMultiLinePreprocessorDirectives(type) && isEmptpyLine) {
            while (data.column < data.code.length() && isSpace(data.code.charAt(data.column))) {
                ++data.column;
            }

            if (data.column < data.code.length() && data.code.charAt(data.column) == '#') {
                int currentColumn = data.column;

                Pair<TokenType, Boolean> preprocessorDirectives = tryHandlePreprocessorWord(data);
                if (isEndOfMultiLinePreprocessorDirectives(preprocessorDirectives.getKey())) {
                    createNewToken(
                            data.symbolTable,
                            data.tokens,
                            preprocessorDirectivesData
                    );
                    preprocessorDirectivesData.isActive = false;

                    data.column = currentColumn;
                    return;
                }
            }
        }

        while (data.column < data.code.length()) {
            ++data.column;
        }

        if (isMultiLinePreprocessorDirectives(type) || data.code.charAt(data.code.length() - 1) == '\\') {
            String text;
            if (isMultiLinePreprocessorDirectives(type)) {
                text = data.code.substring(start, data.column);
            } else {
                text = data.code.substring(start, data.column - 1);
            }

            if (preprocessorDirectivesData.isActive) {
                preprocessorDirectivesData.data += text;
                return;
            }
            preprocessorDirectivesData.data = text;
            preprocessorDirectivesData.line = data.line;
            preprocessorDirectivesData.column = start;
            preprocessorDirectivesData.isActive = true;

            if (isMultiLinePreprocessorDirectives(type)) {
                // special character to separate condition and action
                preprocessorDirectivesData.data += '$';
            }
            return;
        }

        if (!isMultiLinePreprocessorDirectives(type)) {
            String text = data.code.substring(start, data.column);
            if (preprocessorDirectivesData.isActive) {
                preprocessorDirectivesData.data += text;
                createNewToken(
                        data.symbolTable,
                        data.tokens,
                        preprocessorDirectivesData
                );
                preprocessorDirectivesData.isActive = false;
                return;
            }
            createNewToken(data.symbolTable, data.tokens, data.line, start, type, text);
        }
    }

    private static void handleComments(CommonData data, BetweenLinesData commentedCodeData) {
        int start = data.column;

        TokenType type = TokenType.Invalid;

        if (commentedCodeData.isActive) {
            type = commentedCodeData.type;
        }
        if (!commentedCodeData.isActive) {
            ++data.column;
            if (data.column >= data.code.length()) {
                --data.column;
                handleOperatorByFa(data);
                return;
            }
            char nextChar = data.code.charAt(data.column);
            ++data.column;

            if (nextChar == '/') {
                type = TokenType.SingleLineComment;
                commentedCodeData.type = type;
            } else if (nextChar == '*') {
                type = TokenType.MultyLineComment;
                commentedCodeData.type = type;
            } else {
                --data.column;
                handleOperatorByFa(data);
                return;
            }
        }

        boolean isPreviousSpesialSymbol = false;
        boolean isPreviousStarSymbol = false;

        // true - comment like: // ...
        // false - comment like: /* ... */
        boolean isFirstType = (type == TokenType.SingleLineComment);

        while (data.column < data.code.length() && !(!isFirstType && isPreviousStarSymbol && data.code.charAt(data.column) == '/')) {
            if (isPreviousSpesialSymbol) {
                isPreviousSpesialSymbol = false;
            } else if (data.code.charAt(data.column) == '\\') {
                isPreviousSpesialSymbol = true;
            }

            if (isPreviousStarSymbol) {
                isPreviousStarSymbol = false;
            } else if (data.code.charAt(data.column) == '*') {
                isPreviousStarSymbol = true;
            }

            ++data.column;
        }

        if (data.column >= data.code.length() && (isPreviousSpesialSymbol || !isFirstType)) {
            String text = data.code.substring(start, data.column - 1);
            if (commentedCodeData.isActive) {
                commentedCodeData.data += text;
                return;
            }
            commentedCodeData.data = text;
            commentedCodeData.line = data.line;
            commentedCodeData.column = start;
            commentedCodeData.isActive = true;
            return;
        }
        if (data.column < data.code.length()) {
            ++data.column;
        }

        String word = data.code.substring(start, data.column);
        if (commentedCodeData.isActive) {
            commentedCodeData.data += word;
            createNewToken(
                    data.symbolTable,
                    data.tokens,
                    commentedCodeData
            );
            commentedCodeData.isActive = false;
            return;
        }
        createNewToken(data.symbolTable, data.tokens, data.line, start, type, word);
    }

    private static void handleOperatorByFa(CommonData data, int start, FAState state) {
        if (data.column >= data.code.length()) {
            if (state.type == TokenType.Invalid) {
                createNewTokenError(
                        data.tokenErrors,
                        "Error: invalid operator",
                        data.code.substring(start, data.column),
                        data.line,
                        data.column
                );
            } else {
                createNewToken(data.symbolTable, data.tokens, data.line, start, state.type);
            }
            return;
        }

        char currentChar = data.code.charAt(data.column);
        boolean isCurrentCharOperator = isOperator(currentChar);

        if (!isCurrentCharOperator) {
            createNewToken(data.symbolTable, data.tokens, data.line, start, state.type);
            return;
        }

        ++data.column;
        for (int i = 0; i < state.transitions.size(); ++i) {
            if (state.transitions.get(i).c == currentChar) {
                handleOperatorByFa(data, start, state.transitions.get(i));
                return;
            }
        }

        if (state.type == TokenType.Invalid) {
            createNewTokenError(
                    data.tokenErrors,
                    "Error: invalid operator",
                    data.code.substring(start, data.column),
                    data.line,
                    data.column
            );
        } else {
            createNewToken(data.symbolTable, data.tokens, data.line, start, state.type);
        }
        --data.column;
    }

    private static void handleOperatorByFa(CommonData data) {
        handleOperatorByFa(data, data.column, faState);
    }

    private static void handleWord(CommonData data) {
        boolean hasNumber = false;

        int start = data.column;
        ++data.column;
        while (data.column < data.code.length() && isValidWordPart(data.code.charAt(data.column))) {
            if (!hasNumber && isDigit(data.code.charAt(data.column))) {
                hasNumber = true;
            }
            ++data.column;
        }

        String word = data.code.substring(start, data.column);

        if (!hasNumber) {
            Pair<TokenType, Boolean> tryKeywords = tryGetKeywords(word);
            if (tryKeywords.getValue()) {
                createNewToken(data.symbolTable, data.tokens, data.line, start, tryKeywords.getKey());
                return;
            }
        }

        createNewToken(data.symbolTable, data.tokens, data.line, start, TokenType.Id, word);
    }

    private static void handlePunctuationMarks(CommonData data) {
        char c = data.code.charAt(data.column);
        ++data.column;

        for (
                int i = TokenType.PunctuationMarksBegin.ordinal() + 1;
                i < TokenType.PunctuationMarksEnd.ordinal();
                ++i
        ) {
            if (c == tokenToString[i].charAt(0)) {
                createNewToken(data.symbolTable, data.tokens, data.line, data.column, TokenType.values()[i]);
                return;
            }
        }
    }

    private static void handleNotClosed(
            CommonData data,
            BetweenLinesData commentedCodeData,
            BetweenLinesData stringConstantData,
            BetweenLinesData preprocessorDirectivesData
    ) {
        if (commentedCodeData.isActive) {
            createNewTokenError(
                    data.tokenErrors,
                    "Error, unfinished comment",
                    commentedCodeData
            );
        }
        if (stringConstantData.isActive) {
            createNewTokenError(
                    data.tokenErrors,
                    "Error, unfinished string constant",
                    stringConstantData
            );
        }
        if (preprocessorDirectivesData.isActive) {
            createNewTokenError(
                    data.tokenErrors,
                    "Error, unfinished preprocessor directives",
                    preprocessorDirectivesData
            );
        }
    }

    private static boolean nextToken(
            CommonData data,
            BetweenLinesData commentedCodeData,
            BetweenLinesData stringConstantData,
            BetweenLinesData preprocessorDirectivesData
    ) {
        if (stringConstantData.isActive) {
            handleStringConstant(data, stringConstantData);
            return !stringConstantData.isActive;
        }
        if (commentedCodeData.isActive) {
            handleComments(data, commentedCodeData);
            return !commentedCodeData.isActive;
        }
        if (preprocessorDirectivesData.isActive) {
            handlePreprocessorDirectives(data, preprocessorDirectivesData);
            return !preprocessorDirectivesData.isActive;
        }

        while (data.column < data.code.length() && isSpace(data.code.charAt(data.column))) {
            ++data.column;
        }

        if (data.column >= data.code.length()) {
            return false;
        }

        char c = data.code.charAt(data.column);

        if (isValidNumberBegin(c)) {
            handleDigit(data);
            return true;
        }
        if (c == '\'') {
            handleLiteralsConstant(data);
            return true;
        }
        if (c == '\"') {
            handleStringConstant(data, stringConstantData);
            return !stringConstantData.isActive;
        }
        if (c == '#') {
            handlePreprocessorDirectives(data, preprocessorDirectivesData);
            return !preprocessorDirectivesData.isActive;
        }
        if (c == '/') {
            handleComments(data, commentedCodeData);
            return !commentedCodeData.isActive;
        }
        if (isValidWordBegin(c)) {
            handleWord(data);
            return true;
        }
        if (isOperator(c)) {
            handleOperatorByFa(data);
            return true;
        }
        if (isPunctuationMarks(c)) {
            handlePunctuationMarks(data);
            return true;
        }

        createNewTokenError(
                data.tokenErrors,
                "Error: symbol could not be recognized",
                "" + c,
                data.line,
                data.column
        );
        ++data.column;
        return true;
    }


    public static LexerOutput getTokensFromFile(String filePath) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {

            if (faState == null) {
                faState = new FAState();
                generateFa();
            }

            CommonData data = new CommonData();

            BetweenLinesData commentedCodeData = new BetweenLinesData();
            BetweenLinesData stringConstantData = new BetweenLinesData();
            BetweenLinesData preprocessorDirectivesData = new BetweenLinesData();

            String code;

            while ((code = bufferedReader.readLine()) != null) {
                data.code = code;
                data.column = 0;
                while (nextToken(
                        data,
                        commentedCodeData,
                        stringConstantData,
                        preprocessorDirectivesData
                )) {

                }
                ++data.line;
            }

            handleNotClosed(
                    data,
                    commentedCodeData,
                    stringConstantData,
                    preprocessorDirectivesData);

            return new LexerOutput(data.symbolTable.toArray(String[]::new), data.tokens.toArray(Token[]::new),
                    data.tokenErrors.toArray(TokenError[]::new));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error while reading file");
        }

        return new LexerOutput();
    }

    public static LexerOutput getTokensFromLine(String line) {
        if (faState == null) {
            faState = new FAState();
            generateFa();
        }

        CommonData data = new CommonData();

        BetweenLinesData commentedCodeData = new BetweenLinesData();
        BetweenLinesData stringConstantData = new BetweenLinesData();
        BetweenLinesData preprocessorDirectivesData = new BetweenLinesData();

        data.code = line;
        data.column = 0;
        while (nextToken(
                data,
                commentedCodeData,
                stringConstantData,
                preprocessorDirectivesData
        )) {

        }
        ++data.line;

        handleNotClosed(
                data,
                commentedCodeData,
                stringConstantData,
                preprocessorDirectivesData);

        return new LexerOutput(data.symbolTable.toArray(String[]::new), data.tokens.toArray(Token[]::new),
                data.tokenErrors.toArray(TokenError[]::new));
    }

    public static void outputLexerData(LexerOutput lexerOutput) {
        String[] symbolTable = lexerOutput.symbolTable;
        Token[] tokens = lexerOutput.tokens;
        TokenError[] tokenErrors = lexerOutput.tokenErrors;

        if (tokenErrors.length == 0) {
            System.out.print("No errors\n\n");
        } else {
            System.out.println("Error: " + tokenErrors.length);
            for (TokenError tokenError : tokenErrors) {
                System.out.printf("line: %4d[%-4d] %50s Symbol: |%s|\n", tokenError.line, tokenError.column, tokenError.message, tokenError.symbol);
            }
            System.out.println();
        }

        System.out.println("Tokens:");
        for (Token token : tokens) {
            System.out.printf("( %-15s ", tokenToString[token.type.ordinal()]);
            if (isSymbolType(token.type)) {
                System.out.printf(", %-4d )", token.indexInSymbolTable);
            } else {
                System.out.print("       )");
            }

            System.out.println();
        }
        System.out.println();

        System.out.println("Symbol table:");
        for (int i = 0; i < symbolTable.length; ++i) {
            System.out.printf("Index: %-3d Symbol: |%s|\n", i, symbolTable[i]);
        }
        System.out.println();

        System.out.println("Tokens (full info):");
        for (int i = 0; i < tokens.length; ++i) {
            System.out.printf("Id: %-3d Type: %-15s Line: %4d[%-4d] ", i, tokenToString[tokens[i].type.ordinal()], tokens[i].line, tokens[i].column);
            if (isSymbolType(tokens[i].type)) {
                System.out.printf("Symbol id: %-4d Symbol: |%s|", tokens[i].indexInSymbolTable, symbolTable[tokens[i].indexInSymbolTable]);
            } else {
                System.out.print("Symbol id:      Symbol: ");
            }

            System.out.println();
        }
        System.out.println();
    }
}
