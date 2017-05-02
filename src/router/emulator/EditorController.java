package router.emulator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

/**
 * Created by Nuwanda on 2017-04-19.
 */
public class EditorController {
    @FXML
    private Button OKButton;
    @FXML
    private TextField SsidTextField;
    @FXML
    private TextField PasswordTextField;

    private MainController controller;
    public void setController(MainController controller) {
        this.controller = controller;

        SsidTextField.setText(controller.getSsid());
        PasswordTextField.setText(controller.getPassword());
    }

    @FXML
    protected void handleOKButtonAction(ActionEvent event) {
        if(event.getTarget() == OKButton) {
            controller.updateInformation(SsidTextField.getText(), PasswordTextField.getText());
        }

        StackPane root = (StackPane) OKButton.getScene().getRoot();
        root.getChildren().remove(1);
    }
}
