package dominoes.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * TextBoxControl Controller
 *
 * @author Hisham Khalifa
 */
public class TextBoxControl implements Initializable {

    @FXML
    Pane pane;
    @FXML
    private Label textLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void mousePressedAction(MouseEvent event) {
        event.consume();
        performEffects(1.0);
    }

    private void performEffects(double rate) {
        ScaleTransition st = new ScaleTransition(Duration.seconds(0.25), pane);
        st.setFromX(1.0);
        st.setToX(0.0);
        st.setFromY(1.0);
        st.setToY(0.0);
        
        st.setRate(rate);

        st.interpolatorProperty().set(Interpolator.LINEAR);


        st.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pane.getScene().getWindow().hide();
            }
        });

        st.play();

    }

    public void setTextLabel(Label textLabel) {
        this.textLabel = textLabel;
    }
}
