package client.controllers;

import client.utils.HandleRequest;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import protocol.request.LoginRequest;
import protocol.response.Response;

import java.io.IOException;

public class LoginController {

    @FXML
    private MFXButton cadastrarButton;

    @FXML
    private MFXTextField email;

    @FXML
    private Label errorLabel;

    @FXML
    private MenuItem hostConfig;

    @FXML
    private MFXButton loginButton;

    @FXML
    private MFXPasswordField password;

    @FXML
    private HBox progressSpinner;

    @FXML
    void cadastrar(ActionEvent event) {

    }

    @FXML
    void login(ActionEvent event) {
        String emailStr = email.getText();
        String pass = password.getText();

        Task<Response<?>> task = new Task<>() {
            @Override
            protected Response<?> call() throws Exception {
                return HandleRequest.getInstance().makeRequest(new LoginRequest(emailStr, pass), (Void) -> {
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/teste.fxml"));
                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                });
            }
        };

        Platform.runLater(task);
    }

    @FXML
    void showConfigureHostDialog(ActionEvent event) {

    }
}

