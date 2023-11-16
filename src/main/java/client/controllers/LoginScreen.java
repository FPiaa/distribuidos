package client.controllers;

import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.beans.binding.Bindings;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import org.apache.commons.validator.routines.EmailValidator;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginScreen implements Initializable {
    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");
    public Text actionTarget;
    public DialogPane hostConfig;
    public MFXPasswordField passwordField;
    public MFXTextField emailField;
    public Label validatePassword;
    public Label validateEmail;

    public void login(ActionEvent actionEvent) {
        System.out.println("Tentando logar usuário: ");
        System.out.println("Email: " + emailField.getText());
        System.out.println("Senha: " + passwordField.getHideCharacter());
    }

    public void cadastrar(ActionEvent actionEvent) {
        System.out.println("cadastro");
    }

    public void showHostDialog(ActionEvent actionEvent) {
        System.out.println("Showing host dialog");

        hostConfig.setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        var emailConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Deve conter um email válido")
                // usando apache commons validator, pq regex de email é dor
                .setCondition(Bindings.createBooleanBinding(() ->
                    EmailValidator.getInstance(true, true).isValid(emailField.getText()),
                                emailField.textProperty()))
                .get();

        emailField.getValidator()
                .constraint(emailConstraint);

        emailField.getValidator().validProperty().addListener((obs, oldValue, newValue) -> {
            System.out.println("Valid property");
            if (newValue) {
                validateEmail.setVisible(false);
                validateEmail.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
            }
        });


        emailField.delegateFocusedProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                List<Constraint> constraints = emailField.validate();
                System.out.println("email: " + emailField.getText());
                if (!constraints.isEmpty()) {
                    emailField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    validateEmail.setVisible(true);
                    validateEmail.setText(constraints.get(0).getMessage());
                }
            }
        });
    }
}
