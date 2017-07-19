package router.controller;

import javafx.application.Platform;
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
import router.rpi.RPiController;

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

    private ObservableList<Device> devices;
    private Preferences preferences;
    private boolean isRunning;
    private Timer timer;
    private RPiController controller;

    // endregion

    // region Getter Setter

    public String getSsid(){
        return preferences.get("ssid", "");
    }

    public String getPassword(){
        return preferences.get("password", "");
    }

    // endregion

    // region Methods

    // region FXML methods

    // 초기 시작시
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 디바이스 목록 바인딩
        DeviceColumn.setCellValueFactory(new PropertyValueFactory<Device,String>("deviceName"));
        IpColumn.setCellValueFactory(new PropertyValueFactory<Device,String>("ipAddress"));
        MacColumn.setCellValueFactory(new PropertyValueFactory<Device,String>("macAddress"));

        // 디바이스 할당
        devices = FXCollections.observableArrayList();
        devices.addListener(new ListChangeListener<Device>() {
            @Override
            public void onChanged(Change<? extends Device> c) {
                DeviceCountLabel.setText("연결된 장치 : " + devices.size() + "/8");
            }
        });
        DeviceTableView.setItems(devices);

        // 기존 설정 가져오기
        preferences = Preferences.userRoot().node("RouterEmulator");
        SsidLabel.setText(SsidLabel.getText() + preferences.get("ssid", ""));
        PasswordLabel.setText(PasswordLabel.getText() + preferences.get("password", ""));

        controller = new RPiController();
        controller.addListener(response -> {
            if(response.getStatus()){
                if(response.getCommand().equals("power")) {
                    if(!(boolean)response.getResult()){
                        devices.clear();
                    }
                }
            }
        });
        controller.connect("192.168.0.21", 5000);
        // 라우터에 연결되는 디바이스
//        timer = new Timer();
//        DeviceListUpdater updater = new DeviceListUpdater(devices);
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (isRunning){
//                    Platform.runLater(()->{
//                        updater.updateList();
//                    });
//                }
//            }
//        }, 0, 3000);
    }

    // 전원버튼 누를때
    @FXML
    protected void handleOnOffButtonAction(ActionEvent event) {
        isRunning = OnOffToggleButton.isSelected();
        controller.request("power", isRunning);
    }

    // 편집버튼 누를때
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
        preferences.put("ssid", ssid);
        preferences.put("password", password);

        SsidLabel.setText("네트워크 이름 : " + ssid);
        PasswordLabel.setText("네트워크 암호 : " + password);
    }

    public void cancelTimer() {
        timer.cancel();
    }

    public boolean Authenticate(String ssid, String password, String[] output) {
        if(!isRunning) {
            output = new String[] { "Server is not ready to access user." };
            return false;
        } else {
            boolean correctSSID = preferences.get("ssid", "").equals(ssid);
            boolean correctPassword = preferences.get("password", "").equals(password);

            if(!correctSSID) {
                output = new String[] { "Wrong SSID" };
            } else if(!correctPassword) {
                output = new String[] { "Wrong Password" };
            }

            return correctSSID && correctPassword;
        }
    }

    private String[] addresses = new String[] {
            "192.168.137.168",
            "192.168.137.27",
            "192.168.137.104",
            "192.168.137.150",
            "192.168.137.41",
            "192.168.137.30",
            "192.168.137.105",
            "192.168.137.73"
    };

    public boolean AllocateDHCP(String hostName, String MAC, String[] output) {
        if(!isRunning) {
            output = new String[] { "Server is not ready to access user." };
            return false;
        } else if (devices.size() == 8){
            output = new String[] { "Fully Allocated." };
            return false;
        } else {
            String allocated = addresses[devices.size()];

            devices.add(new Device(hostName, allocated, MAC));
            output = new String[] { allocated };

            return true;
        }
    }

    // endregion
}