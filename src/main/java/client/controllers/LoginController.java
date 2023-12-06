package client.controllers;

import client.Session;
import client.utils.HandleRequest;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcore.controls.Text;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import protocol.request.LoginRequest;
import protocol.response.LoginResponse;
import protocol.response.Response;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    @FXML
    private HBox buttons;
    @FXML
    private MFXButton cadastrarButton;

    @FXML

    private MFXTextField email;

    @FXML
    private Text errorLabel;

    @FXML
    private MenuItem hostConfig;

    @FXML
    private MFXTextField hostField;

    @FXML
    private MFXPasswordField password;

    @FXML
    private MFXTextField portField;

    @FXML
    private HBox progressSpinner;
    private final BooleanProperty requesting = new SimpleBooleanProperty();
    private final BooleanProperty changeScene = new SimpleBooleanProperty();
    private final StringProperty errorProperty = new SimpleStringProperty();

    @FXML
    void cadastrar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CadastroScreen.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    void login(ActionEvent event) {
        String host = hostField.getText();
        int port = Integer.parseInt(portField.getText());
        HandleRequest.getInstance().updateEndpoit(host, port);
        String emailStr = email.getText();
        String pass = password.getText();


        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                requesting.setValue(true);
                HandleRequest.getInstance().makeRequest(new LoginRequest(emailStr, pass), (Response<?> response) -> {
                    if (!(response instanceof LoginResponse)) {
                        return;
                    }
                    Session.getInstance().setToken(((LoginResponse) response).payload().token());
                    changeScene.setValue(true);
                }, errorProperty::setValue);

                requesting.setValue(false);
                return null;
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        requesting.addListener((obs, old, new_v) -> {
            System.out.println("Requesting listener");
            if (new_v) {
                Platform.runLater(() -> {
                    buttons.setVisible(false);
                    progressSpinner.setVisible(true);
                });
            } else {
                Platform.runLater(() -> {
                    buttons.setVisible(true);
                    progressSpinner.setVisible(false);
                });
            }
        });

        changeScene.addListener((obs, old, new_v) -> {
            if (new_v) {
                Platform.runLater(() -> {
                    Stage stage = (Stage) progressSpinner.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Pdis.fxml"));
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
        });

        errorProperty.addListener((obs, old, new_v) -> {
            Platform.runLater(() -> {
                errorLabel.setText(new_v);
            });
        });
    }
}