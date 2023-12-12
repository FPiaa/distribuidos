package client.controllers;

import client.utils.HandleRequest;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class DialogController {

    @FXML
    private MFXTextField hostField;

    @FXML
    private MFXGenericDialog dialogPane;

    @FXML
    private MFXTextField portField;

    @FXML
    void changeHost(ActionEvent event) {
        HandleRequest.getInstance().updateEndpoit(hostField.getText(), Integer.parseInt(portField.getText()));
    }

}
