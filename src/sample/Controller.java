package sample;

import com.sun.org.apache.xpath.internal.operations.String;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleButton;

public class Controller {
    @FXML
    private ToggleButton OnOffToggleButton;

    @FXML
    protected void handleButtonAction(ActionEvent event) {
        boolean isOn = OnOffToggleButton.isSelected();

        new Alert(Alert.AlertType.INFORMATION, isOn ? "is On" : "is Off").show();
    }
}
