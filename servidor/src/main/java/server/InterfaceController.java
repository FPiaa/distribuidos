package server;

import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.utils.others.observables.When;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class InterfaceController implements Initializable {
    public MFXTextField portField;
    public MFXPaginatedTableView<ConnectedUser> tableView;
    private boolean serverOn = false;
    private final ConnectedUsers users = new ConnectedUsers();

    public void createServer(ActionEvent actionEvent) {
        if(!serverOn) {
            Thread th = new Thread(() -> Server.listen(Integer.parseInt(portField.getText()), users));
            th.setDaemon(true);
            th.start();
            serverOn = true;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        users.getUsersProperty().addListener((ListChangeListener<ConnectedUser>) change -> {
            Platform.runLater(() -> {
                tableView.setItems((ObservableList<ConnectedUser>) change.getList());
            });
        });

        setupTable();
        tableView.autosizeColumnsOnInitialization();

        When.onChanged(tableView.currentPageProperty())
                .then((oldValue, newValue) -> tableView.autosizeColumns())
                .listen();
    }
    private void setupTable() {
        MFXTableColumn<ConnectedUser>  ip = new MFXTableColumn<>("Ip", true);
        MFXTableColumn<ConnectedUser> email = new MFXTableColumn<>("Email", true);


        ip.setRowCellFactory(userDTO -> new MFXTableRowCell<>(ConnectedUser::getIp));
        email.setRowCellFactory(userDTO -> new MFXTableRowCell<>(ConnectedUser::getEmail));

        tableView.getTableColumns().setAll(ip, email);
    }
}
