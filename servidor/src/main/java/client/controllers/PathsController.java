package client.controllers;

import client.Session;
import client.utils.HandleRequest;
import client.utils.Tasks;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.materialfx.utils.others.observables.When;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.util.StringConverter;
import protocol.commons.Command;
import protocol.commons.dto.PoiDTO;
import protocol.request.FindPoisRequest;
import protocol.request.FindRouteRequest;
import protocol.response.FindPoisResponse;
import protocol.response.FindRouteResponse;
import protocol.response.Response;

import java.net.URL;
import java.util.ResourceBundle;

public class PathsController implements Initializable {

    @FXML
    private MFXComboBox<PoiDTO> destinyField;

    @FXML
    private MFXComboBox<PoiDTO> originField;

    @FXML
    private MFXPaginatedTableView<Command> tableView;


    private final ObservableList<PoiDTO> pdis = FXCollections.observableArrayList();
    private final ObservableList<Command> commands = FXCollections.observableArrayList();

    @FXML
    void findRoute(ActionEvent event) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                var start = originField.getValue().id();
                var end = destinyField.getValue().id();
                HandleRequest.getInstance().makeRequest(new FindRouteRequest(Session.getInstance().getToken(), start, end),
                        (Response<?> res) -> {
                            if (res instanceof FindRouteResponse) {
                                Platform.runLater(() -> {
                                    var path = ((FindRouteResponse) res).payload();
                                    commands.setAll(path.comandos());
                                    tableView.setCurrentPage(0);
                                    tableView.autosize();

                                });
                            }
                        }, (String) -> {
                        });
                return null;
            }
        };

        Tasks.run(task);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pdis.addListener((ListChangeListener<? super PoiDTO>) change -> {
            originField.setItems((ObservableList<PoiDTO>) change.getList());
            destinyField.setItems((ObservableList<PoiDTO>) change.getList());

            System.out.println("origin field items");
            System.out.println(originField.getItems());
        });

        commands.addListener((ListChangeListener<? super Command>) change -> tableView.setItems((ObservableList<Command>) change.getList()));
        StringConverter<PoiDTO> converter = FunctionalStringConverter.to(poi -> poi == null ? "" : poi.nome());
        originField.setItems(pdis);
        originField.setConverter(converter);

        destinyField.setItems(pdis);
        destinyField.setConverter(converter);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {

                HandleRequest.getInstance().makeRequest(new FindPoisRequest(Session.getInstance().getToken()), (Response<?> response) -> {
                    if (!(response instanceof FindPoisResponse)) {
                        return;
                    }
                    Platform.runLater(() -> pdis.setAll(((FindPoisResponse) response).payload().pdis()));
                }, (String) -> {
                });

                return null;
            }
        };

        setupTable();
        tableView.autosizeColumnsOnInitialization();

        When.onChanged(tableView.currentPageProperty())
                .then((oldValue, newValue) -> tableView.autosizeColumns())
                .listen();

        Tasks.run(task);


    }

    public void setupTable() {
        MFXTableColumn<Command> nome_inicio = new MFXTableColumn<>("Ponto Inicial", true);
        MFXTableColumn<Command> nome_final = new MFXTableColumn<>("Ponto Final", true);
        MFXTableColumn<Command> distancia = new MFXTableColumn<>("Distancia", true);
        MFXTableColumn<Command> avisos = new MFXTableColumn<>("Aviso", true);
        MFXTableColumn<Command> comando = new MFXTableColumn<>("Direcao", true);


        nome_inicio.setRowCellFactory(userDTO -> new MFXTableRowCell<>(Command::getNome_inicio));
        nome_final.setRowCellFactory(userDTO -> new MFXTableRowCell<>(Command::getNome_final));
        distancia.setRowCellFactory(userDTO -> new MFXTableRowCell<>(Command::getDistancia));
        avisos.setRowCellFactory(device -> new MFXTableRowCell<>(Command::getAviso));
        comando.setRowCellFactory(device -> new MFXTableRowCell<>(Command::getDirecao));
        tableView.getTableColumns().addAll(nome_inicio, nome_final, distancia, avisos, comando);


    }

    public void refreshPois(ActionEvent actionEvent) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                HandleRequest.getInstance().makeRequest(new FindPoisRequest(Session.getInstance().getToken()),
                        (Response<?> res) -> {
                            if (res instanceof FindPoisResponse) {
                                Platform.runLater(() -> pdis.setAll(((FindPoisResponse) res).payload().pdis()));
                            }
                        }, (String) -> {
                        });
                return null;
            }
        };

        Tasks.run(task);
    }
}
