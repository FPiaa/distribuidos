package client.controllers;

import client.Session;
import client.utils.HandleRequest;
import client.utils.Tasks;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.BooleanFilter;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
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
import javafx.util.StringConverter;
import protocol.request.*;
import protocol.response.FindPoisResponse;
import protocol.response.FindSegmentsResponse;
import protocol.response.Response;
import server.dto.PoiDTO;
import server.dto.SegmentDTO;

import java.net.URL;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;

public class SegmentsController implements Initializable {

    @FXML
    private MFXButton deleteButton;

    @FXML
    private MFXComboBox<PoiDTO> destinyField;

    @FXML
    private Label errorLabel;

    @FXML
    private VBox form;


    @FXML
    private MFXCheckbox isAcessibleCheck;

    @FXML
    private MFXButton modifyButton;

    @FXML
    private MFXButton novoPdi;

    @FXML
    private MFXComboBox<PoiDTO> originField;

    @FXML
    private Label tableError;

    @FXML
    private MFXPaginatedTableView<SegmentDTO> tableView;

    @FXML
    private MFXTextField warningField;


    private final ObservableList<PoiDTO> pdis = FXCollections.observableArrayList();
    private final ObservableList<SegmentDTO> segments = FXCollections.observableArrayList();
    private final StringProperty errorProperty = new SimpleStringProperty();
    private final StringProperty tableErrorProperty = new SimpleStringProperty();

    private final ObjectProperty<SegmentDTO> selectedSegment = new SimpleObjectProperty<>();

    @FXML
    void cancel(ActionEvent ignoredEvent) {
        clearForm();
    }

    @FXML
    void createSegment(ActionEvent event) {
        Task<Void> task = new Task<>() {

            @Override
            protected Void call() {
                Request<?> req = makeRequest();
                HandleRequest.getInstance().makeRequest(req, (Response<?> res) -> {
                    refreshTable(null);
                    Platform.runLater(() -> clearForm());
                }, errorProperty::setValue);
                return null;
            }

            private Request<?> makeRequest() {
                Long id1 = originField.getValue().id();
                Long id2 = destinyField.getValue().id();
                String aviso = warningField.getText();
                Boolean acessivel = isAcessibleCheck.isSelected();

                String token = Session.getInstance().getToken();
                return new CreateSegmentRequest(token, id1, id2, aviso, acessivel);
            }
        };

        Tasks.run(task);
    }

    @FXML
    void deleteSegment(ActionEvent ignoredEvent) {
        Task<Void> task = new Task<>() {

            @Override
            protected Void call() {
                Request<?> req = makeRequest();
                HandleRequest.getInstance().makeRequest(req, (Response<?> res) -> Platform.runLater(() -> {
                    refreshTable(null);
                    clearForm();
                }), errorProperty::setValue);
                return null;
            }

            private Request<?> makeRequest() {
                System.out.println(originField.getValue());
                Long id1 = originField.getValue().id();
                Long id2 = destinyField.getValue().id();

                String token = Session.getInstance().getToken();
                return new DeleteSegmentRequest(token, id1, id2);
            }
        };

        Tasks.run(task);
    }

    void updateSegment(ActionEvent event) {
        Task<Void> task = new Task<>() {

            @Override
            protected Void call() {
                Request<?> req = makeRequest();
                HandleRequest.getInstance().makeRequest(req, (Response<?> res) -> {
                    if (res instanceof FindSegmentsResponse) {
                        Platform.runLater(() -> {
                            segments.setAll(((FindSegmentsResponse) res).payload().segmentos());
                            refreshTable(null);
                            clearForm();
                        });
                    }
                }, errorProperty::setValue);
                return null;
            }

            private Request<?> makeRequest() {
                Long id1 = originField.getValue().id();
                Long id2 = destinyField.getValue().id();
                String aviso = warningField.getText();
                Boolean acessivel = isAcessibleCheck.isSelected();
                String token = Session.getInstance().getToken();
                return new UpdateSegmentRequest(token, id1, id2, aviso, acessivel);
            }
        };

        Tasks.run(task);
    }

    @FXML
    void formCreatePdi(ActionEvent ignoredEvent) {

        clearForm();
        Platform.runLater(() -> {
            form.setVisible(true);
            deleteButton.setVisible(false);
            modifyButton.setVisible(true);
            modifyButton.setText("Cadastrar PDI");
            modifyButton.setOnAction(this::createSegment);
        });
    }

    @FXML

    void refreshTable(ActionEvent ignoredEvent) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                HandleRequest.getInstance().makeRequest(new FindSegmentsRequest(Session.getInstance().getToken()),
                        (Response<?> res) -> {
                            if (res instanceof FindSegmentsResponse) {
                                Platform.runLater(() -> {
                                    segments.setAll(((FindSegmentsResponse) res).payload().segmentos());
                                    tableView.autosizeColumns();
                                    tableView.setCurrentPage(0);
                                    tableErrorProperty.set("");
                                });
                            }
                        }, tableErrorProperty::setValue);
                HandleRequest.getInstance().makeRequest(new FindPoisRequest(Session.getInstance().getToken()),
                        (Response<?> res) -> {
                            if (res instanceof FindPoisResponse) {
                                Platform.runLater(() -> {
                                    pdis.setAll(((FindPoisResponse) res).payload().pdis());
                                });
                            }
                        }, tableErrorProperty::setValue);
                return null;
            }
        };

        Tasks.run(task);
    }

    void clearForm() {
        Platform.runLater(() -> {
           originField.clearSelection();
           destinyField.clearSelection();
           isAcessibleCheck.setSelected(false);
           warningField.setText("");
           form.setVisible(false);
        });
    }

    void modifySegment(SegmentDTO segment) {
        Platform.runLater(() -> {
            originField.setValue(pdis.stream().filter((v) -> Objects.equals(v.id(), segment.pdi_inicial())).findFirst().orElse(null));
            destinyField.setValue(pdis.stream().filter((v) -> Objects.equals(v.id(), segment.pdi_final())).findFirst().orElse(null));
            isAcessibleCheck.setSelected(segment.acessivel());
            warningField.setText(segment.aviso());
            form.setVisible(true);
            modifyButton.setOnAction(this::updateSegment);
            modifyButton.setText("Atualizar");
            deleteButton.setVisible(true);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pdis.addListener((ListChangeListener<? super PoiDTO>) change -> {
            originField.setItems((ObservableList<PoiDTO>) change.getList());
            destinyField.setItems((ObservableList<PoiDTO>) change.getList());
        });

        segments.addListener((ListChangeListener<? super SegmentDTO>) change -> {
            System.err.println("Segments changed");
            tableView.setItems((ObservableList<SegmentDTO>) change.getList());
        });

        errorProperty.addListener((obs, old, new_v) -> {
            System.out.println(new_v);
            Platform.runLater(() -> errorLabel.setText(new_v));
        });

        tableErrorProperty.addListener((obs, old, new_v) -> Platform.runLater(() -> tableError.setText(new_v)));

        selectedSegment.addListener((obs, old, new_v) -> {
            if (Session.getInstance().getUser() == null || Session.getInstance().getUser().isAdmin())
                if(new_v != null)
                    modifySegment(new_v);
        });

        StringConverter<PoiDTO> converter = FunctionalStringConverter.to(poi -> poi == null ? "": poi.nome());
        originField.setItems(pdis);
        originField.setConverter(converter);

        destinyField.setItems(pdis);
        destinyField.setConverter(converter);


        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                Platform.runLater(() -> setupTable());
                HandleRequest.getInstance().makeRequest(new FindSegmentsRequest(Session.getInstance().getToken()), (Response<?> response) -> {
                    if (!(response instanceof FindSegmentsResponse)) {
                        return;
                    }
                    Platform.runLater(() -> {
                        segments.setAll(((FindSegmentsResponse) response).payload().segmentos());
                        refreshTable(null);
                    });
                }, tableErrorProperty::setValue);

                HandleRequest.getInstance().makeRequest(new FindPoisRequest(Session.getInstance().getToken()), (Response<?> response) -> {
                    if (!(response instanceof FindPoisResponse)) {
                        return;
                    }
                    Platform.runLater(() -> {
                        pdis.setAll(((FindPoisResponse) response).payload().pdis());
                        refreshTable(null);
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
        MFXTableColumn<SegmentDTO> pdi_inicial = new MFXTableColumn<>("ID Inicial", true, Comparator.comparing(SegmentDTO::pdi_inicial));
        MFXTableColumn<SegmentDTO> pdi_final = new MFXTableColumn<>("Id Final", true, Comparator.comparing(SegmentDTO::pdi_final));
        MFXTableColumn<SegmentDTO> distancia = new MFXTableColumn<>("Distancia", true);
        MFXTableColumn<SegmentDTO> warningColumn = new MFXTableColumn<>("Aviso", true, Comparator.comparing(SegmentDTO::aviso));
        MFXTableColumn<SegmentDTO> acessibleColumn = new MFXTableColumn<>("Acessível", true, Comparator.comparing(SegmentDTO::acessivel));


        pdi_inicial.setRowCellFactory(userDTO -> new MFXTableRowCell<>(SegmentDTO::pdi_inicial));
        pdi_final.setRowCellFactory(userDTO -> new MFXTableRowCell<>(SegmentDTO::pdi_final));
        distancia.setRowCellFactory(userDTO -> new MFXTableRowCell<>(SegmentDTO::distancia));
        warningColumn.setRowCellFactory(device -> new MFXTableRowCell<>(SegmentDTO::aviso));
        acessibleColumn.setRowCellFactory(device -> new MFXTableRowCell<>(SegmentDTO::acessivel));

        tableView.getTableColumns().addAll(pdi_inicial, pdi_final, distancia, warningColumn, acessibleColumn);
        tableView.getFilters().addAll(
                new DoubleFilter<>("Distância", SegmentDTO::distancia),
                new StringFilter<>("Aviso", SegmentDTO::aviso),
                new BooleanFilter<>("Acessível", SegmentDTO::acessivel)
        );
        tableView.getSelectionModel().selectionProperty().addListener((MapChangeListener<? super Integer, ? super SegmentDTO>) change -> {
            if (change.wasAdded()) {
                selectedSegment.setValue(change.getValueAdded());
            }
        });
    }

}
