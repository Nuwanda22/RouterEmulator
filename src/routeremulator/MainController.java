package routeremulator;

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

        // 라우터에 연결되는 디바이스
        timer = new Timer();
        DeviceListUpdater updater = new DeviceListUpdater(devices);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isRunning){
                    Platform.runLater(()->{
                        updater.updateList();
                    });
                }
            }
        }, 0, 3000);
    }

    @FXML
    protected void handleOnOffButtonAction(ActionEvent event) {
        isRunning = OnOffToggleButton.isSelected();

        if(!isRunning) {
            devices.clear();
        }
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
        preferences.put("ssid", ssid);
        preferences.put("password", password);

        SsidLabel.setText("네트워크 이름 : " + ssid);
        PasswordLabel.setText("네트워크 암호 : " + password);
    }

    public void cancelTimer() {
        timer.cancel();
    }

    // endregion
}

class DeviceListUpdater {
    private ObservableList<Device> devices;
    private static ArrayList<Device> users;
    private Random random = new Random();

    static {
        users = new ArrayList<>(8);
        users.add(new Device("Wilson", "192.168.137.168", "58:27:e4:ec:c7:d1"));
        users.add(new Device("Jones", "192.168.137.27", "71:91:5a:84:77:b8"));
        users.add(new Device("Thompson", "192.168.137.104", "8c:68:b9:41:24:d3"));
        users.add(new Device("Phillip", "192.168.137.150", "0d:01:70:54:25:da"));
        users.add(new Device("Alice", "192.168.137.41", "14:eb:03:43:83:90"));
        users.add(new Device("Louise", "192.168.137.30", "f0:e6:bf:13:ad:8e"));
        users.add(new Device("Alexander", "192.168.137.105", "a6:29:d1:5f:c6:60"));
        users.add(new Device("Michael", "192.168.137.73", "14:eb:03:43:83:90"));
    }

    public DeviceListUpdater(ObservableList<Device> devices) {
        this.devices = devices;
    }

    public void updateList() {
        int size = devices.size();

        if (size == 0){
            devices.add(users.get(random.nextInt(8)));
        } else if (size == 8) {
            devices.remove(random.nextInt(size));
        } else {
            int percent = random.nextInt(10);
            if(percent > 3){
                devices.add(users.get(random.nextInt(8)));
            } else {
                devices.remove(random.nextInt(size));
            }
        }
    }
}