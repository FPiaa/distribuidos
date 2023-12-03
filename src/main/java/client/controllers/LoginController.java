package client.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class LoginController {

    @FXML
    private MFXButton cadastrarButton;

    @FXML
    private MFXTextField email;

    @FXML
    private MenuItem hostConfig;

    @FXML
    private MFXButton loginButton;

    @FXML
    private MFXPasswordField password;

    @FXML
    void cadastrar(ActionEvent event) {
        System.err.println("CADASTRAR");
    }

    @FXML
    void configureHost(ActionEvent event) {
        System.err.println("host");
    }

    @FXML
    void login(ActionEvent event) {
        System.err.println("Login");
    }

}
