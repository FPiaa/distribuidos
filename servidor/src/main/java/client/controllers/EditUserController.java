package client.controllers;

import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class EditUserController {

    @FXML
    private MFXTextField emailField;

    @FXML
    private Label errorLabel;

    @FXML
    private MFXTextField idField;

    @FXML
    private MFXCheckbox isAdminField;

    @FXML
    private MFXTextField nameField;

    @FXML
    private MFXPasswordField passwordField;

    @FXML
    void deleteUser(ActionEvent event) {

    }

    @FXML
    void updateUser(ActionEvent event) {

    }

}
