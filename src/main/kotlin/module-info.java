module com.kiralin.toast {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires javafx.media;


    opens com.kiralin.toast to javafx.fxml;
    exports com.kiralin.toast;
}