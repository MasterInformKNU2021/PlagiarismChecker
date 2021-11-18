module com.example.plagiarismchecker {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.plagiarismchecker to javafx.fxml;
    exports com.plagiarismchecker;
}