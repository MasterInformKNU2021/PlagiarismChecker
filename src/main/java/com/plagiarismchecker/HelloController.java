package com.plagiarismchecker;

import com.plagiarismchecker.plagiarismchecker.PlagiarismChecker;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class HelloController {
    @FXML
    private TextArea firstText;

    @FXML
    private TextArea secondText;

    @FXML
    private TextArea resultText;

    @FXML
    protected void onButtonClick() {
        String[] firstTextLines = firstText.getText().split("\\r?\\n");
        String[] secondTextLines = secondText.getText().split("\\r?\\n");

        String[] result = PlagiarismChecker.CheckTexts(firstTextLines, secondTextLines);

        resultText.setText(String.join("\n", result));
    }
}