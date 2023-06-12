package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import results.ResultRepository;
import util.javafx.FileChooserHelper;

import javax.inject.Inject;

public final class OpeningController {
    @FXML
    private Label nickNameErrorLabel;

    @FXML
    private TextField foxNickName;

    @FXML
    private TextField dogsNickName;

    @Inject
    private FXMLLoader fxmlLoader;


    private static Logger logger = LogManager.getLogger(OpeningController.class);

    public void loadScoreBoard(ActionEvent actionEvent) throws IOException{
        FileChooserHelper.show(
                    true,
                    (Stage) ((Node) actionEvent.getSource()).getScene().getWindow()
            )
            .ifPresent(file -> {
                try {
                    ResultRepository.loadFromFile(file);
                } catch (IOException ex) {
                    logger.error(ex.getMessage());
                }
            });
    }

    public void startGame(ActionEvent actionEvent) throws IOException {
        if (foxNickName.getText().isEmpty() || dogsNickName.getText().isEmpty() || foxNickName.getText().length() > 14 || dogsNickName.getText().length() > 14 || foxNickName.getText().equals(dogsNickName.getText())) {
            nickNameErrorLabel.setText("Hiba! A nevek nem lehetnek üresek, azonosak, vagy 14 karakternél hosszabbak!");
        } else {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/gameUI.fxml"));
            Parent root = fxmlLoader.load();
            fxmlLoader.<GameController>getController().setFoxNickName(foxNickName.getText());
            fxmlLoader.<GameController>getController().setDogsNickName(dogsNickName.getText());
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
            logger.info("The usernames are set to {} (fox) and {} (dogs), loading game scene", foxNickName.getText(), dogsNickName.getText());
        }
    }

}

