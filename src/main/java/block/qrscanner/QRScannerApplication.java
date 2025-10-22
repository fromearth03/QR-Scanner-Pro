package block.qrscanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class QRScannerApplication extends Application {

    private QRScannerController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(QRScannerApplication.class.getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();

        Scene scene = new Scene(root);
        stage.setTitle("QR Scanner Pro");
        stage.setScene(scene);

        // --- CODE TO ADD THE APPLICATION ICON ---
        try (InputStream iconStream = QRScannerApplication.class.getResourceAsStream("icon.png")) {
            if (iconStream != null) {
                stage.getIcons().add(new Image(iconStream));
            } else {
                System.err.println("Warning: Could not find 'logo.png' in resources. The application icon will not be set.");
            }
        } catch (Exception e) {
            System.err.println("Error loading application icon.");
            e.printStackTrace();
        }

        // Open the application in maximized mode for an immersive experience
        stage.setMaximized(true);
        stage.show();
    }

    @Override
    public void stop() {
        // This is crucial to shut down the camera thread properly when the app closes
        if (controller != null) {
            controller.shutdown();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

