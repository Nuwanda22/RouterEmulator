package sample;

import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    private ToggleButton thumb;

    @FXML
    private Rectangle toggleBackground;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TranslateTransition onAnim = new TranslateTransition(Duration.millis(150), thumb);
        onAnim.setByX(50f);

        TranslateTransition offAnim = new TranslateTransition(Duration.millis(150), thumb);
        offAnim.setByX(-50f);

        FillTransition onBack = new FillTransition(Duration.millis(150), toggleBackground, Color.web("#d8d8d8"), Color.web("#55aa55"));
        FillTransition offBack = new FillTransition(Duration.millis(150), toggleBackground, Color.web("#55aa55"), Color.web("#d8d8d8"));

        thumb.setOnAction(e->{
            if(thumb.isSelected()){
                onAnim.play();
                onBack.play();
            }else{
                offAnim.play();
                offBack.play();
            }
        });
    }
}
