package dominoes;

import dominoes.gui.DominoUIImp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * JavaFX Application launch class for application
 * 
 * @author Hisham Khalifa
 */
public class SpaceBones extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        
        //DominoUI.fxml controller class is DominoUIImp as defined in DominoUI.fxml
        Parent root = FXMLLoader.load(DominoUIImp.class.getResource("DominoUI.fxml"));

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        
        primaryStage.setScene(scene);
        primaryStage.show(); 
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application. main() serves only as fallback in case the
     * application can not be launched through deployment artifacts, e.g., in IDEs with limited FX support. NetBeans
     * ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
