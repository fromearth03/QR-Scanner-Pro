package block.qrscanner;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QRScannerController {

    @FXML private ImageView cameraView;
    @FXML private Label statusLabel;
    @FXML private VBox resultPane;
    @FXML private TextArea resultTextArea;
    @FXML private HBox actionButtonsBox;
    @FXML private Button scanButton;
    @FXML private VBox noCameraPane;

    private Webcam webcam;
    private Task<Void> webcamTask;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final SimpleBooleanProperty isCameraActive = new SimpleBooleanProperty(false);
    private String lastQRCode = "";

    @FXML
    public void initialize() {
        // Asynchronously initialize the webcam to prevent UI freezes
        executor.submit(() -> {
            webcam = Webcam.getDefault();
            Platform.runLater(() -> {
                if (webcam == null) {
                    noCameraPane.setManaged(true);
                    noCameraPane.setVisible(true);
                    scanButton.setDisable(true);
                    statusLabel.setText("No webcam detected. Try scanning a file.");
                } else {
                    webcam.setViewSize(webcam.getViewSizes()[webcam.getViewSizes().length - 1]);
                }
            });
        });
    }

    @FXML
    private void onScanButtonClick() {
        if (isCameraActive.get()) {
            stopCamera();
        } else {
            startCamera();
        }
    }

    @FXML
    private void onScanFromFileClick() {
        if (isCameraActive.get()) {
            stopCamera();
        }
        hideResultPane();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open QR Code Image");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(cameraView.getScene().getWindow());

        if (file != null) {
            try {
                BufferedImage image = ImageIO.read(file);
                if (image == null) {
                    statusLabel.setText("Error: Could not read image file.");
                    return;
                }
                cameraView.setImage(SwingFXUtils.toFXImage(image, null));
                decodeQRCode(image);
            } catch (IOException e) {
                statusLabel.setText("Error reading file.");
                e.printStackTrace();
            }
        }
    }

    private void startCamera() {
        if (webcam == null) return;
        lastQRCode = "";
        hideResultPane();

        webcamTask = new Task<>() {
            @Override
            protected Void call() {
                if (!webcam.isOpen()) webcam.open();
                while (!isCancelled()) {
                    BufferedImage image = webcam.getImage();
                    if (image != null) {
                        Platform.runLater(() -> cameraView.setImage(SwingFXUtils.toFXImage(image, null)));
                        decodeQRCode(image);
                    }
                }
                if (webcam.isOpen()) webcam.close();
                return null;
            }
        };

        isCameraActive.set(true);
        statusLabel.setText("Live camera feed active...");
        scanButton.setText("Stop Camera");
        executor.submit(webcamTask);
    }

    private void stopCamera() {
        if (webcamTask != null) {
            webcamTask.cancel(true);
        }
        isCameraActive.set(false);
        lastQRCode = "";
        Platform.runLater(() -> {
            cameraView.setImage(null);
            statusLabel.setText("Camera is off. Ready to scan.");
            scanButton.setText("Start Camera");
        });
    }

    private void decodeQRCode(BufferedImage image) {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            Result result = new MultiFormatReader().decode(bitmap);
            String qrCodeText = result.getText();
            if (!qrCodeText.equals(lastQRCode)) {
                lastQRCode = qrCodeText;
                Platform.runLater(() -> handleDecodedQRCode(qrCodeText));
            }
        } catch (NotFoundException e) {
            // No QR code found, which is normal. Do nothing.
        }
    }

    private void handleDecodedQRCode(String text) {
        resultTextArea.setText(text);
        actionButtonsBox.getChildren().clear();
        statusLabel.setText("QR Code successfully detected!");

        if (isValidURL(text)) {
            Button openButton = new Button("Open Link");
            openButton.getStyleClass().add("action-button");
            openButton.setOnAction(e -> {
                try {
                    Desktop.getDesktop().browse(new URI(text));
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            });
            actionButtonsBox.getChildren().add(openButton);
        }

        Button scanNextButton = new Button("Scan Next");
        scanNextButton.getStyleClass().add("action-button-secondary");
        scanNextButton.setOnAction(e -> hideResultPane());
        actionButtonsBox.getChildren().add(scanNextButton);

        showResultPane();
    }

    private void showResultPane() {
        if (resultPane.isVisible()) return;
        resultPane.setManaged(true);
        resultPane.setVisible(true);

        FadeTransition ft = new FadeTransition(Duration.millis(300), resultPane);
        ft.setFromValue(0);
        ft.setToValue(1);

        ScaleTransition st = new ScaleTransition(Duration.millis(300), resultPane);
        st.setFromX(0.9);
        st.setFromY(0.9);
        st.setToX(1);
        st.setToY(1);

        st.play();
        ft.play();
    }

    private void hideResultPane() {
        lastQRCode = "";
        if (!resultPane.isVisible()) return;

        FadeTransition ft = new FadeTransition(Duration.millis(200), resultPane);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setOnFinished(e -> {
            resultPane.setVisible(false);
            resultPane.setManaged(false);
        });
        ft.play();
    }

    private boolean isValidURL(String urlString) {
        try {
            new URI(urlString).toURL();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void shutdown() {
        stopCamera();
        executor.shutdownNow();
    }
}
