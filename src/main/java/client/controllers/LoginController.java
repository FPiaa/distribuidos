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
    private MFXTextField emailField;

    @FXML
    private MenuItem hostConfigMenuItem;

    @FXML
    private MFXButton loginButton;

    @FXML
    private MFXPasswordField passwordField;

    @FXML
    void changeToSceneCadastro(ActionEvent event) {

    }

    @FXML
    void executeLogin(ActionEvent event) {

    }

    @FXML
    void openHostConfig(ActionEvent event) {
        System.out.println("Show host stuff");
    }

}
