package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javax.inject.Inject;

public class GameApplication extends Application {

    private static Logger logger = LogManager.getLogger(GameApplication.class);

    @Inject
    private FXMLLoader fxmlLoader;

    @Override
    public void start(Stage stage) throws Exception {

        logger.info("Game started");
        fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/fxml/gameOpening.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
}
