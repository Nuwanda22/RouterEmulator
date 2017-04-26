package routeremulator;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.prefs.Preferences;

public class MainController implements Initializable {

    // region Fields

    //region FXML Components
    @FXML
    private Label SsidLabel;
    @FXML
    private Label PasswordLabel;
    @FXML
    private Label DeviceCountLabel;
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
    private Preferences prefs;
    private Thread user;
    private boolean isRunning;

    // endregion

    // region Getter Setter

    public ObservableList<Device> getDataList() {
        return dataList;
    }

    public String getSsid(){
        return prefs.get("ssid", "");
    }

    public String getPassword(){
        return prefs.get("password", "");
    }

    // endregion

    // region Methods

    // region FXML methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DeviceColumn.setCellValueFactory(new PropertyValueFactory<Device,String>("deviceName"));
        IpColumn.setCellValueFactory(new PropertyValueFactory<Device,String>("ipAddress"));
        MacColumn.setCellValueFactory(new PropertyValueFactory<Device,String>("macAddress"));

        dataList = FXCollections.observableArrayList();
        dataList.addListener(new ListChangeListener<Device>() {
            @Override
            public void onChanged(Change<? extends Device> c) {
                DeviceCountLabel.setText("연결된 장치 : " + dataList.size() + "/8");
            }
        });
        DeviceTableView.setItems(dataList);

        prefs = Preferences.userRoot().node("RouterEmulator");
        SsidLabel.setText(SsidLabel.getText() + prefs.get("ssid", ""));
        PasswordLabel.setText(PasswordLabel.getText() + prefs.get("password", ""));
    }

    @FXML
    protected void handleOnOffButtonAction(ActionEvent event) {
        isRunning = OnOffToggleButton.isSelected();

//        if (isRunning){
//            user = new Thread(()->{
//                while (isRunning){
//                    if(dataList.size() != 8){
//                        dataList.add(new Device("", "", ""));
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });
//            user.start();
//        }
//        else {
//            dataList.clear();
//        }
    }

    @FXML
    protected void handleEditButtonAction(ActionEvent event) throws IOException {
        StackPane root = (StackPane)OnOffToggleButton.getScene().getRoot();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("editor.fxml"));
        Parent parent = loader.load();
        loader.<EditorController>getController().setController(this);
        root.getChildren().add(parent);
    }

    // endregion

    public void updateInformation(String ssid, String password) {
        prefs.put("ssid", ssid);
        prefs.put("password", password);

        SsidLabel.setText("네트워크 이름 : " + ssid);
        PasswordLabel.setText("네트워크 암호 : " + password);
    }
    // endregion

}
