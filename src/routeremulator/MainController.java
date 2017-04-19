package routeremulator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    //region FXML Components
    @FXML
    private ToggleButton OnOffToggleButton;
    @FXML
    private TableView DeviceTableView;
    @FXML
    private TableColumn DeviceColumn;
    @FXML
    private TableColumn IpColumn;
    @FXML
    private TableColumn MacColumn;
    //endregion
    private ObservableList<Device> dataList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DeviceColumn.setCellValueFactory(new PropertyValueFactory<Device,String>("deviceName"));
        IpColumn.setCellValueFactory(new PropertyValueFactory<Device,String>("ipAddress"));
        MacColumn.setCellValueFactory(new PropertyValueFactory<Device,String>("macAddress"));

        dataList = FXCollections.observableArrayList();
        DeviceTableView.setItems(dataList);
    }

    @FXML
    protected void handleOnOffButtonAction(ActionEvent event) {
        boolean on = OnOffToggleButton.isSelected();

        new Alert(Alert.AlertType.CONFIRMATION, on ? "켜집니다." : "꺼집니다.").show();
    }

    @FXML
    protected void handleEditButtonAction(ActionEvent event) {
        new Alert(Alert.AlertType.CONFIRMATION, "편집").show();
    }
}
