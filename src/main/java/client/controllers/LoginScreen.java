package client.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginScreen {

    public Text actionTarget;
    public TextField email;
    public PasswordField password;
    public Label lblLogin;
    public DialogPane hostConfig;

    public void login(ActionEvent actionEvent) {
        System.out.println("login");
    }

    public void cadastrar(ActionEvent actionEvent) {
        System.out.println("cadastro");
    }

    public void showHostDialog(ActionEvent actionEvent) {
        System.out.println("Showing host dialog");
        hostConfig.setVisible(true);
    }
}
