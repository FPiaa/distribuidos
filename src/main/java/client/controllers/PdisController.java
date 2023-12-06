package client.controllers;

import client.Session;
import client.utils.HandleRequest;
import client.utils.Tasks;
import io.github.palexdev.materialfx.controls.*;
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
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import protocol.request.*;
import protocol.response.FindPoisResponse;
import protocol.response.Response;
import server.dto.PoiDTO;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class PdisController implements Initializable {

    public MFXPaginatedTableView<PoiDTO> tableView;
    public Label tableError;
    public VBox form;
    public MFXTextField idField;
    @FXML
    private Label errorLabel;
    @FXML
    private MFXButton deleteButton;

    @FXML
    private MFXCheckbox isAcessibleCheck;

    @FXML
    private MFXButton modifyButton;

    @FXML
    private MFXTextField nameField;

    @FXML
    private MFXButton novoPdi;

    @FXML
    private MFXTextField warningField;

    @FXML
    private MFXTextField xField;

    @FXML
    private MFXTextField yField;

    private final ObservableList<PoiDTO> pdis = FXCollections.observableArrayList();
    private final StringProperty errorProperty = new SimpleStringProperty();
    private final StringProperty tableErrorProperty = new SimpleStringProperty();

    private final ObjectProperty<PoiDTO> selectedPdi = new SimpleObjectProperty<>();

    @FXML
    void cancel(ActionEvent event) {
        clearForm();
    }

    @FXML
    void createPdi(ActionEvent event) {
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                Request<?> req = makeRequest();
                HandleRequest.getInstance().makeRequest(req, (Response<?> res) -> {
                    refreshTable(null);
                    Platform.runLater(() -> {
                        clearForm();
                    });
                }, errorProperty::setValue);
                return null;
            }

            private Request<?> makeRequest() {
                String nome = nameField.getText();
                Double x = Double.parseDouble(xField.getText());
                Double y = Double.parseDouble(yField.getText());
                String aviso = warningField.getText();
                Boolean acessivel = isAcessibleCheck.isSelected();

                String token = Session.getInstance().getToken();
                return new CreatePoiRequest(token, nome, x, y, aviso, acessivel);
            }
        };

        Tasks.run(task);
    }

    @FXML
    void deletePdi(ActionEvent event) {
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                Request<?> req = makeRequest();
                HandleRequest.getInstance().makeRequest(req, (Response<?> res) -> {
                    refreshTable(null);
                    Platform.runLater(() -> {
                        clearForm();
                    });
                }, errorProperty::setValue);
                return null;
            }

            private Request<?> makeRequest() {
                String nome = nameField.getText();
                Double x = Double.parseDouble(xField.getText());
                Double y = Double.parseDouble(yField.getText());
                String aviso = warningField.getText();
                Boolean acessivel = isAcessibleCheck.isSelected();

                String token = Session.getInstance().getToken();
                return new DeletePoiRequest(token, Long.parseLong(idField.getText()));
            }
        };

        Tasks.run(task);
    }

    void updatePdi(ActionEvent event) {
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                Request<?> req = makeRequest();
                HandleRequest.getInstance().makeRequest(req, (Response<?> res) -> {
                    refreshTable(null);
                    Platform.runLater(() -> {
                        clearForm();
                    });
                }, errorProperty::setValue);
                return null;
            }

            private Request<?> makeRequest() {
                String nome = nameField.getText();
                Double x = Double.parseDouble(xField.getText());
                Double y = Double.parseDouble(yField.getText());
                String aviso = warningField.getText();
                Boolean acessivel = isAcessibleCheck.isSelected();
                Long id = Long.parseLong(idField.getText());
                String token = Session.getInstance().getToken();
                return new UpdatePoiRequest(token, id, nome, x, y, aviso, acessivel);
            }
        };

        Tasks.run(task);
    }

    @FXML
    void formCreatePdi(ActionEvent event) {

        clearForm();
        Platform.runLater(() -> {
            form.setVisible(true);
            deleteButton.setVisible(false);
            modifyButton.setVisible(true);
            modifyButton.setText("Cadastrar PDI");
            modifyButton.setOnAction(this::createPdi);
        });
    }

    @FXML
    void refreshTable(ActionEvent event) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                HandleRequest.getInstance().makeRequest(new FindPoisRequest(Session.getInstance().getToken()),
                        (Response<?> res) -> {
                            System.out.println(res.toString());
                            if (res instanceof FindPoisResponse) {
                                Platform.runLater(() -> {
                                    pdis.setAll(((FindPoisResponse) res).payload().pdis());
                                    tableView.autosizeColumns();
                                });
                            }
                        }, tableErrorProperty::setValue);
                System.out.println("Pdis set");
                return null;
            }
        };

        Tasks.run(task);
    }

    void clearForm() {
        Platform.runLater(() -> {
            nameField.setText(null);
            xField.setText(null);
            yField.setText(null);
            isAcessibleCheck.setSelected(false);
            warningField.setText(null);
            form.setVisible(false);
        });
    }

    void modifyPdi(PoiDTO poi) {
        Platform.runLater(() -> {
            idField.setText("" + poi.id());
            nameField.setText(poi.nome());
            xField.setText("" + poi.posicao().x());
            yField.setText("" + poi.posicao().y());
            isAcessibleCheck.setSelected(poi.acessivel());
            warningField.setText(poi.aviso());
            form.setVisible(true);
            modifyButton.setOnAction(this::updatePdi);
            modifyButton.setText("Atualizar");
            deleteButton.setVisible(true);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        pdis.addListener((ListChangeListener<? super PoiDTO>) change -> {
            System.out.println(change.getList().toString());
            tableView.setItems((ObservableList<PoiDTO>) change.getList());
        });

        errorProperty.addListener((obs, old, new_v) -> {

            System.out.println("ErrorProperty");
            System.out.println(new_v);
            Platform.runLater(() -> {
                errorLabel.setText(new_v);
            });
        });

        tableErrorProperty.addListener((obs, old, new_v) -> {

            System.out.println("table Error:");
            System.out.println(new_v);
            Platform.runLater(() -> {
                tableError.setText(new_v);
            });
        });

        selectedPdi.addListener((obs, old, new_v) -> {
            if (Session.getInstance().getUser() == null || Session.getInstance().getUser().isAdmin())
                if(new_v != null)
                    modifyPdi(new_v);
        });
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                HandleRequest.getInstance().makeRequest(new FindPoisRequest(Session.getInstance().getToken()), (Response<?> response) -> {
                    if (!(response instanceof FindPoisResponse)) {
                        return;
                    }
                    Platform.runLater(() -> {
                        pdis.setAll(((FindPoisResponse) response).payload().pdis());
                        setupTable();
                    });
                }, tableErrorProperty::setValue);

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
        MFXTableColumn<PoiDTO> idColumn = new MFXTableColumn<>("ID", true, Comparator.comparing(PoiDTO::id));
        MFXTableColumn<PoiDTO> nameColumn = new MFXTableColumn<>("Nome", true, Comparator.comparing(PoiDTO::nome));
        MFXTableColumn<PoiDTO> positionColumn = new MFXTableColumn<>("Posição", true);
        MFXTableColumn<PoiDTO> warningColumn = new MFXTableColumn<>("Aviso", true, Comparator.comparing(PoiDTO::aviso));
        MFXTableColumn<PoiDTO> acessibleColumn = new MFXTableColumn<>("Acessível", true, Comparator.comparing(PoiDTO::acessivel));


        idColumn.setRowCellFactory(userDTO -> new MFXTableRowCell<>(PoiDTO::id));
        nameColumn.setRowCellFactory(userDTO -> new MFXTableRowCell<>(PoiDTO::nome));
        positionColumn.setRowCellFactory(userDTO -> new MFXTableRowCell<>(PoiDTO::posicao));
        warningColumn.setRowCellFactory(device -> new MFXTableRowCell<>(PoiDTO::aviso));
        acessibleColumn.setRowCellFactory(device -> new MFXTableRowCell<>(PoiDTO::acessivel));

        tableView.getTableColumns().addAll(idColumn, nameColumn, positionColumn, warningColumn, acessibleColumn);
        tableView.getFilters().addAll(
                new LongFilter<>("ID", PoiDTO::id),
                new StringFilter<>("Nome", PoiDTO::nome),
                new StringFilter<>("Aviso", PoiDTO::aviso),
                new BooleanFilter<>("Acessível", PoiDTO::acessivel)
        );
        tableView.getSelectionModel().selectionProperty().addListener((MapChangeListener<? super Integer, ? super PoiDTO>) change -> {
            if (change.wasAdded()) {
                selectedPdi.setValue(change.getValueAdded());
            }
        });
        tableView.setItems(pdis);
    }

}
