package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import results.GameResult;
import results.ResultRepository;
import util.javafx.ControllerHelper;
import util.javafx.FileChooserHelper;

import javax.inject.Inject;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

public final class HighScoreController {

    @Inject
    private FXMLLoader fxmlLoader;


    @FXML
    private TableView<GameResult> highScoreTable;

    @FXML
    private TableColumn<GameResult, String> foxNickName;
    @FXML
    private TableColumn<GameResult, String> dogsNickName;

    @FXML
    private TableColumn<GameResult, String> winner;

    @FXML
    private TableColumn<GameResult, Integer> steps;


    @FXML
    private TableColumn<GameResult, ZonedDateTime> created;

    private static final Logger logger = LogManager.getLogger(HighScoreController.class);

    @FXML
    private void initialize() {
        logger.debug("Loading high scores...");

        List<GameResult> highScoreList = ResultRepository.getGameResults();
        logger.info(highScoreList);

        foxNickName.setCellValueFactory(new PropertyValueFactory<>("player1"));
        dogsNickName.setCellValueFactory(new PropertyValueFactory<>("player2"));
        steps.setCellValueFactory(new PropertyValueFactory<>("steps"));
        winner.setCellValueFactory(new PropertyValueFactory<>("winner"));
        created.setCellValueFactory(new PropertyValueFactory<>("created"));

        ObservableList<GameResult> observableResult = FXCollections.observableArrayList();
        observableResult.addAll(highScoreList);

        highScoreTable.setItems(observableResult);
    }

    public void handleRestartButton(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        logger.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        ControllerHelper.loadAndShowFXML("/fxml/gameOpening.fxml", stage);
    }

    public void handleSaveScoreboard(ActionEvent actionEvent) {
        FileChooserHelper.show(
                        false,
                        (Stage) ((Node) actionEvent.getSource()).getScene().getWindow()
                )
                .ifPresent(file -> {
                    try {
                        ResultRepository.writeResult(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        logger.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
    }
}