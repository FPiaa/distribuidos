package client.controllers;

import client.Session;
import client.utils.HandleRequest;
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.BooleanFilter;
import io.github.palexdev.materialfx.filter.LongFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import protocol.request.AdminFindUsersRequest;
import protocol.response.FindUsersResponse;
import server.dto.UserDTO;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class UsersController implements Initializable {

    @FXML
    private MFXTextField adminField;

    @FXML
    private MFXTextField emailField;

    @FXML
    private MFXTextField nameField;

    @FXML
    private MFXPaginatedTableView<UserDTO> tableView;

    private ObservableList<UserDTO> users;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        Task<List<UserDTO>> task = new Task<List<UserDTO>>() {
            @Override
            protected List<UserDTO> call() throws Exception {
                var payload = HandleRequest.getInstance().makeRequest(new AdminFindUsersRequest(Session.getInstance().getToken()),
                        (Void) -> System.out.println(), (String) -> System.out.println());
                return ((FindUsersResponse) payload).payload().users();
            }
        };
    }

    private void setupTable() {
        MFXTableColumn<UserDTO> idColumn = new MFXTableColumn<>("ID", false, Comparator.comparing(UserDTO::id));
        MFXTableColumn<UserDTO> nameColumn = new MFXTableColumn<>("Nome", false, Comparator.comparing(UserDTO::nome));
        MFXTableColumn<UserDTO> emailColumn = new MFXTableColumn<>("Email", false, Comparator.comparing(UserDTO::email));
        MFXTableColumn<UserDTO> adminStatusColumn = new MFXTableColumn<>("Admin", false, Comparator.comparing(UserDTO::isAdmin));

        idColumn.setRowCellFactory(userDTO -> new MFXTableRowCell<>(UserDTO::id));
        nameColumn.setRowCellFactory(userDTO -> new MFXTableRowCell<>(UserDTO::nome));
        emailColumn.setRowCellFactory(userDTO -> new MFXTableRowCell<>(UserDTO::email) {{
            setAlignment(Pos.CENTER_RIGHT);
        }});
        adminStatusColumn.setRowCellFactory(device -> new MFXTableRowCell<>(UserDTO::isAdmin));
        emailColumn.setAlignment(Pos.CENTER_RIGHT);

        tableView.getTableColumns().addAll(idColumn, nameColumn, emailColumn, adminStatusColumn);
        tableView.getFilters().addAll(
                new LongFilter<>("ID", UserDTO::id),
                new StringFilter<>("Nome", UserDTO::nome),
                new StringFilter<>("Email", UserDTO::email),
                new BooleanFilter<>("Admin", UserDTO::isAdmin)
        );
        tableView.setItems(users);
    }
}
