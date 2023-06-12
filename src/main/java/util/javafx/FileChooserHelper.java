package util.javafx;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

/**
 * Class that provides option for browse after files on the computer that we would like to read in. This class can accept only json file format.
 */

public final class FileChooserHelper {

    /**
     * show method provides option to browse file on the computer.
     * @param isOpen tells if the page is open.
     * @param stage stage we want to add to.
     * @return returns the dialog page that lets us read in files.
     */
    public static Optional<File> show(boolean isOpen, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("json documents", "*.json"));
        if (isOpen) {
            fileChooser.setTitle("Browse a JSON file to load the scoreboard");
            return Optional.ofNullable(fileChooser.showOpenDialog(stage));
        } else {
            fileChooser.setTitle("Browse a JSON file to save the scoreboard");
            return Optional.ofNullable(fileChooser.showSaveDialog(stage));
        }
    }
}
