package gui;

import State.GameState;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import results.GameResult;
import results.ResultRepository;
import util.javafx.ControllerHelper;

import javax.inject.Inject;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;


public class GameController {

    @FXML
    public Label actPlayer;
    @FXML
    public Label winnerLabel;

    @FXML
    private GridPane chessTable;


    @Getter
    @Setter
    private String foxNickName;

    @Setter
    @Getter
    private String dogsNickName;

    private static final Logger logger = LogManager.getLogger(OpeningController.class);

   ImageView[] pawns;

   @Getter
   private static GameState position = new GameState();

   private String winner;

   @Getter
   @Setter
   private static int steps;

   @FXML
   private Button resetButton;

    @FXML
    private Button FinishButton;

    @Inject
    private FXMLLoader fxmlLoader;


   private BooleanProperty isGoal = new SimpleBooleanProperty();


    @FXML
   public void initialize(){

       pawns = Stream.of("black-pawn.png","black-pawn.png","black-pawn.png","black-pawn.png", "white-pawn.png")
               .map(s -> "/images/" + s)
               .peek(s -> logger.debug("Loading image resource {}", s))
               .map(Image::new)
               .map(ImageView::new)
               .toArray(ImageView[]::new);
       actPlayer.setText("Indulhat a játék! A kutya kezd");
       resetGame();
       chessTableInitializer();
       showState();
       isGoal.addListener(this::isSolvedPosition);

   }

   public void chessTableInitializer(){
       for (int i = 0; i < chessTable.getRowCount(); i++) {
           for (int j = 0; j < chessTable.getColumnCount(); j++) {
               final var square = new StackPane();
               square.getStyleClass().add("square");
               square.getStyleClass().add((i + j) % 2 == 0 ? "light" : "dark");
               square.setOnMouseClicked(this::handleMouseClick);
               chessTable.add(square, j, i);
           }
       }
   }

    private void handleMouseClick(
            @NonNull final MouseEvent event) {

        final var source = (Node) event.getSource();
        final var row = GridPane.getRowIndex(source);
        final var col = GridPane.getColumnIndex(source);
        position.validChoose(row, col, position);
        position = position.performMove(row, col, position);
        showState();
        logger.info("Click on square ({},{})", row, col);

    }


    private GameResult createGameResult() {
        return GameResult.builder()
                .player1(foxNickName)
                .player2(dogsNickName)
                .winner(winner)
                .steps(getSteps())
                .created(LocalDateTime.now())
                .build();
    }


    private void clearState() {
        for (int row = 0; row < chessTable.getRowCount(); row++) {
            for (int col = 0; col < chessTable.getColumnCount(); col++) {
                int finalCol = col;
                int finalRow = row;
                (chessTable.getChildren().stream()
                        .filter(child -> GridPane.getRowIndex(child) == finalRow && GridPane.getColumnIndex(child) == finalCol)
                        .findFirst())
                        .ifPresent(node -> ((StackPane) node).getChildren().clear());
            }
        }
    }

    private void showState() {
       clearState();
        for (int i = 0; i < 5; i++) {
            final var actPosition = position.getPosition(i);
            logger.info(actPosition);
            final var pieceView = pawns[i];
            pieceView.setFitHeight(58);
            pieceView.setFitWidth(58);
            StackPane pane = new StackPane();
            pane.getChildren().add(pieceView);
            chessTable.getChildren().stream().
                    filter(child -> GridPane.getRowIndex(child) == actPosition.getRow() && GridPane.getColumnIndex(child) == actPosition.getCol()).findFirst().
                    ifPresent(node -> ((StackPane) node).getChildren().add(pieceView));
        }

        logger.info("this is a goal for dogs?({})",position.isGoalForDogs(position));
        logger.info("this is a goal for fox?({})",position.isGoalForFox(position));
        isWinnerPosition();
        nextPlayer();
    }

    private void nextPlayer(){
        if (GameState.getActPlayerId() == 1 && foxNickName != null){
            actPlayer.setText("A lépés az övé: " + foxNickName);
        }else if (GameState.getActPlayerId() ==  0 && dogsNickName != null){
            actPlayer.setText("A lépés az övé: " + dogsNickName);
        }
    }

    private void isWinnerPosition(){
        if (position.isGoalForFox(position)) {
            winner = foxNickName;
            isGoal.set(true);
        } else if (position.isGoalForDogs(position)) {
            winner = dogsNickName;
            isGoal.set(true);
        }
    }

    private void isSolvedPosition(ObservableValue<? extends Boolean> observableValue, boolean oldValue, boolean newValue){
        if (newValue) {
            logger.info("Player {} has won the game in {} steps", winner, steps);
            resetButton.setDisable(true);
            FinishButton.setVisible(true);
            FinishButton.setDisable(false);
            actPlayer.setVisible(false);
            winnerLabel.setVisible(true);
            chessTable.setDisable(true);
            winnerLabel.setText("A játéknak vége! A nyertes:"+winner+" Gratulálok!");
        }
    }


    public void handleResetButton(ActionEvent actionEvent) {
        logger.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        logger.info("Resetting game");
        resetGame();
    }

    public void handleFinishButton(
            @NonNull final ActionEvent actionEvent) throws IOException {

        final var buttonText = ((Button) actionEvent.getSource()).getText();
        logger.debug("{} is pressed", buttonText);

        logger.debug("Saving result");
        ResultRepository.addElement(createGameResult());

        logger.debug("Loading HighScoreController");
        ControllerHelper.loadAndShowFXML(
                "/fxml/highScoreTableUI.fxml",
                (Stage) ((Node) actionEvent.getSource()).getScene().getWindow()
        );
    }

    private void resetGame() {

        position = new GameState();
        GameState.setActPlayerId(0);
        clearState();
        showState();

    }


}
