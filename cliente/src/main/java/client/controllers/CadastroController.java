package client.controllers;

import client.utils.HandleRequest;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
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
import javafx.stage.Stage;
import protocol.request.CreateUserRequest;
import protocol.response.Response;

import java.io.IOException;

public class CadastroController {

    @FXML
    private Label errorLabel;
    @FXML
    private MFXPasswordField checkPassField;

    @FXML
    private MFXTextField emailField;

    @FXML
    private MFXCheckbox isAdminCheck;

    @FXML
    private MFXTextField nameField;

    @FXML
    private MFXPasswordField passField;

    @FXML
    void cadastrarUsuario(ActionEvent event) {
        String email = emailField.getText();
        String pass = passField.getText();
        String name = nameField.getText();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                HandleRequest.getInstance().makeRequest(new CreateUserRequest(name, email, pass), (Response<?> resp) -> {
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginScreen.fxml"));
                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                }, (String s) -> {
                    errorLabel.setText(s);
                });
                return null;
            }
        };

        Platform.runLater(task);
    }

    void showAdminCheck(boolean show) {
        isAdminCheck.setVisible(show);
    }
    @FXML
    void changeLoginScreen(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginScreen.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}
