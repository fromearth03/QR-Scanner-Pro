module block.qrscanner {
    // Standard JavaFX + ControlsFX/BootstrapFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    // --- ADDED FOR QR SCANNER ---
    // Required for webcam access
    requires webcam.capture;
    // Required by the webcam library for logging
    requires org.slf4j;
    // Required for QR code processing
    requires com.google.zxing;
    requires com.google.zxing.javase;
    // Required to convert webcam images (AWT) to JavaFX images
    requires javafx.swing;
    // Required by the webcam library for image handling
    requires java.desktop;


    // Opens your package to be used by JavaFX FXML loader
    opens block.qrscanner to javafx.fxml;
    // Exports your package for use by other modules
    exports block.qrscanner;
}
