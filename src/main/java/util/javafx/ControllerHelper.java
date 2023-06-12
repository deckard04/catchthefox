package util.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Class that provides option to make changing stages easies.
 */
public final class ControllerHelper {

    /**
     * The logger helps us to get more detail about the loading process.
     */
    private static final Logger logger = LogManager.getLogger(ControllerHelper.class);

    /**
     * loads in the given FXML file and adding to the given the stage.
     * @param resourceName tells the location and the name of the FXML
     * @param stage the stage we want to add to the file
     * @throws IOException in every cases if problem occurs in the IO
     */
    public static void loadAndShowFXML(String resourceName, Stage stage) throws IOException {
        logger.trace("Loading FXML resource {}", resourceName);
        Parent root = FXMLLoader.load(ControllerHelper.class.getResource(resourceName));
        stage.setScene(new Scene(root));
        stage.show();
    }

}
