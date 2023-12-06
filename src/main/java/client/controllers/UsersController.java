package client.controllers;

import client.Session;
import client.utils.HandleRequest;
import client.utils.Tasks;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.BooleanFilter;
import io.github.palexdev.materialfx.filter.LongFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.utils.others.observables.When;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import protocol.request.*;
import protocol.response.FindUserResponse;
import protocol.response.FindUsersResponse;
import protocol.response.Response;
import protocol.response.UpdateUserResponse;
import server.dto.UserDTO;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class UsersController implements Initializable {

    public HBox tableButtons;
    @FXML
    private Label formLabel;
    @FXML
    private VBox form;
    @FXML
    private Label errorLabel;
    @FXML
    private MFXTextField passField;
    @FXML
    private CheckBox isAdminCheck;
    @FXML
    private MFXButton modifyButton;
    @FXML
    private MFXButton deleteButton;
    @FXML
    private MFXButton cancelButton;
    @FXML
    private MFXTextField userNameField;
    @FXML
    private MFXTextField userEmailField;
    @FXML
    private MFXTextField userAdminField;
    @FXML
    private Label tableError;
    @FXML
    private MFXTextField idField;

    @FXML
    private MFXTextField emailField;

    @FXML
    private MFXTextField nameField;

    @FXML
    private MFXPaginatedTableView<UserDTO> tableView;

    private final ObservableList<UserDTO> users = FXCollections.observableArrayList();
    private final StringProperty errorProperty = new SimpleStringProperty();
    private final ObjectProperty<UserDTO> selectedUser = new SimpleObjectProperty<>();
    private final StringProperty tableErrorProperty = new SimpleStringProperty();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        users.addListener((ListChangeListener<? super UserDTO>) change -> {
            System.out.println(change.getList().toString());
            tableView.setItems((ObservableList<UserDTO>) change.getList());
        });

        tableErrorProperty.addListener((obs, old, new_v) -> {
            Platform.runLater(() -> {
                tableError.setText(new_v);
            });
        });

        errorProperty.addListener((obs, old, new_v) -> {
            Platform.runLater(() -> {
                errorLabel.setText(new_v);
            });
        });

        selectedUser.addListener((obs, old, new_v) -> {
            modifyUser(new_v);
        });
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                HandleRequest.getInstance().makeRequest(new FindUserRequest(Session.getInstance().getToken()), (Response<?> response) -> {
                    if (!(response instanceof FindUserResponse)) {
                        return;
                    }
                    Session.getInstance().setUser(((FindUserResponse) response).payload());
                    Platform.runLater(() -> {
                        userEmailField.setText(Session.getInstance().getUser().email());
                        userNameField.setText(Session.getInstance().getUser().nome());
                        userAdminField.setText(Session.getInstance().getUser().isAdmin() ? "Administrador" : "Usuario comum");
                    });
                }, tableErrorProperty::setValue);

                if (Session.getInstance().getUser().isAdmin()) {
                    HandleRequest.getInstance().makeRequest(new AdminFindUsersRequest(Session.getInstance().getToken()),
                            (Response<?> res) -> {
                                if (res instanceof FindUsersResponse) {
                                    Platform.runLater(() -> {
                                        users.setAll(((FindUsersResponse) res).payload().users());
                                        setupTable();
                                    });
                                }
                            }, tableErrorProperty::setValue);
                    System.out.println("Users set");
                }

                Platform.runLater(() -> {
                    if(!Session.getInstance().getUser().isAdmin()) {
                        tableButtons.setVisible(false);
                        tableView.setVisible(false);
                    }
                });
                return null;
            }
        };

        tableView.autosizeColumnsOnInitialization();

        When.onChanged(tableView.currentPageProperty())
                .then((oldValue, newValue) -> tableView.autosizeColumns())
                .listen();
        Tasks.run(task);
        form.setVisible(false);
    }

    private void setupTable() {
        MFXTableColumn<UserDTO> idColumn = new MFXTableColumn<>("ID", false, Comparator.comparing(UserDTO::id));
        MFXTableColumn<UserDTO> nameColumn = new MFXTableColumn<>("Nome", false, Comparator.comparing(UserDTO::nome));
        MFXTableColumn<UserDTO> emailColumn = new MFXTableColumn<>("Email", false, Comparator.comparing(UserDTO::email));
        MFXTableColumn<UserDTO> adminStatusColumn = new MFXTableColumn<>("Admin", false, Comparator.comparing(UserDTO::isAdmin));


        idColumn.setRowCellFactory(userDTO -> new MFXTableRowCell<>(UserDTO::id));
        nameColumn.setRowCellFactory(userDTO -> new MFXTableRowCell<>(UserDTO::nome));
        emailColumn.setRowCellFactory(userDTO -> new MFXTableRowCell<>(UserDTO::email));
        adminStatusColumn.setRowCellFactory(device -> new MFXTableRowCell<>(UserDTO::isAdmin));

        tableView.getTableColumns().addAll(idColumn, nameColumn, emailColumn, adminStatusColumn);
        tableView.getFilters().addAll(
                new LongFilter<>("ID", UserDTO::id),
                new StringFilter<>("Nome", UserDTO::nome),
                new StringFilter<>("Email", UserDTO::email),
                new BooleanFilter<>("Admin", UserDTO::isAdmin)
        );
        tableView.getSelectionModel().selectionProperty().addListener((MapChangeListener<? super Integer, ? super UserDTO>) change -> {
            if (change.wasAdded()) {
                selectedUser.setValue(change.getValueAdded());
            }
        });
        tableView.setItems(users);
    }


    public void refreshTable(ActionEvent actionEvent) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                HandleRequest.getInstance().makeRequest(new AdminFindUsersRequest(Session.getInstance().getToken()),
                        (Response<?> res) -> {
                            if (res instanceof FindUsersResponse) {
                                Platform.runLater(() -> {
                                    users.setAll(((FindUsersResponse) res).payload().users());
                                    tableView.autosizeColumns();
                                    errorProperty.setValue("");
                                    tableErrorProperty.setValue("");
                                });
                            }
                        }, tableErrorProperty::setValue);
                System.out.println("Users set");
                return null;
            }
        };

        Tasks.run(task);
    }

    public void criarUsuario(ActionEvent actionEvent) {
        clearForm();
        Platform.runLater(() -> {
            form.setVisible(true);
            deleteButton.setVisible(false);
            modifyButton.setText("Cadastrar");
            modifyButton.setOnAction(this::createUser);
            isAdminCheck.setVisible(true);
        });
    }

    public void modifyUser(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            form.setVisible(true);
            UserDTO user = Session.getInstance().getUser();
            idField.setText("" + user.id());
            nameField.setText(user.nome());
            emailField.setText(user.email());
            isAdminCheck.setVisible(user.isAdmin());
            isAdminCheck.setSelected(user.isAdmin());
            modifyButton.setText("Editar Usuário");
            modifyButton.setOnAction(this::updateUser);
            deleteButton.setVisible(true);
            deleteButton.setText("Deletar Usuário");
        });
    }

    public void modifyUser(UserDTO user) {
        Platform.runLater(() -> {
            errorProperty.setValue("");
            form.setVisible(true);
            idField.setText("" + user.id());
            nameField.setText(user.nome());
            emailField.setText(user.email());
            isAdminCheck.setVisible(Session.getInstance().getUser().isAdmin());
            isAdminCheck.setSelected(user.isAdmin());
            modifyButton.setText("Editar Usuário");
            modifyButton.setOnAction(this::updateUser);
            deleteButton.setVisible(true);
            deleteButton.setText("Deletar Usuário");
        });
    }

    public void createUser(ActionEvent actionEvent) {
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                Request<?> req = makeRequest();
                HandleRequest.getInstance().makeRequest(req, (Response<?> res) -> {
                    if (Session.getInstance().getUser().isAdmin()) {
                        refreshTable(null);
                    }
                    Platform.runLater(() -> {
                        clearForm();
                    });
                }, errorProperty::setValue);
                return null;
            }

            private Request<?> makeRequest() {
                String nome = nameField.getText();
                String email = emailField.getText();
                String senha = passField.getText();
                Boolean tipo = isAdminCheck.isSelected();

                String token = Session.getInstance().getToken();
                Request<?> req = null;
                if (Session.getInstance().getUser().isAdmin()) {
                    req = new AdminCreateUserRequest(token, nome, email, senha, tipo);
                } else {
                    req = new CreateUserRequest(nome, email, senha);
                }
                return req;
            }
        };

        Tasks.run(task);
    }

    public void updateUser(ActionEvent actionEvent) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Request<?> req = makeRequest();
                HandleRequest.getInstance().makeRequest(req, (Response<?> res) -> {
                    UserDTO user = Session.getInstance().getUser();
                    long id = Long.parseLong(idField.getText());
                    if (Session.getInstance().getUser().isAdmin()) {
                        refreshTable(null);
                    }

                    UpdateUserResponse response = (UpdateUserResponse) res;
                    Platform.runLater(() -> {
                        if (id == user.id()) {
                            userNameField.setText(response.payload().nome());
                            userEmailField.setText(response.payload().email());
                            userAdminField.setText(response.payload().isAdmin() ? "Administrador" : "Usuário comum");
                        }
                        clearForm();
                    });
                }, errorProperty::setValue);
                return null;
            }

            private Request<?> makeRequest() {
                Long id = Long.parseLong(idField.getText());

                String nome = nameField.getText().isBlank() ? null : nameField.getText();
                String email = emailField.getText().isBlank() ? null : emailField.getText();
                String senha = passField.getText().isBlank() ? null : passField.getText();
                Boolean tipo = isAdminCheck.isSelected();

                String token = Session.getInstance().getToken();
                Request<?> req = null;
                if (Session.getInstance().getUser().isAdmin()) {
                    req = new AdminUpdateUserRequest(token, id, email, nome, senha, tipo);
                } else {
                    req = new UpdateUserRequest(token, email, nome, senha);
                }
                return req;
            }
        };

        Tasks.run(task);
    }

    public void deleteUser(ActionEvent actionEvent) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Request<?> req = makeRequest();
                HandleRequest.getInstance().makeRequest(req, (Response<?> res) -> {
                    UserDTO user = Session.getInstance().getUser();
                    long id = Long.parseLong(idField.getText());
                    if (Session.getInstance().getUser().isAdmin()) {
                        refreshTable(null);
                    }

                    Platform.runLater(() -> {
                        if (id == user.id()) {
                            Stage stage = (Stage) form.getScene().getWindow();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Users.fxml"));
                            Parent root = null;
                            try {
                                root = loader.load();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Scene scene = new Scene(root);
                            stage.setScene(scene);
                        } else {
                            clearForm();
                        }
                    });
                }, errorProperty::setValue);
                return null;
            }

            private Request<?> makeRequest() {
                Long id = Long.parseLong(idField.getText());
                String email = emailField.getText();
                String senha = passField.getText();

                String token = Session.getInstance().getToken();
                Request<?> req = null;
                if (Session.getInstance().getUser().isAdmin()) {
                    req = new AdminDeleteUserRequest(token, id);
                } else {
                    req = new DeleteUserRequest(token, email, senha);
                }
                return req;
            }
        };

        Tasks.run(task);
    }

    public void clearForm() {
        Platform.runLater(() -> {
            errorProperty.setValue("");
            emailField.setText("");
            nameField.setText("");
            passField.setText("");
            isAdminCheck.setSelected(false);
            form.setVisible(false);
        });
    }
    public void cancelModify(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            errorProperty.setValue("");
            form.setVisible(false);
        });
    }
}
