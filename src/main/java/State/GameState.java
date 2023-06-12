package State;



import gui.GameController;
import gui.OpeningController;
import javafx.beans.property.IntegerProperty;
import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that provides the implementation of the "catch the fox" game business logic. The class use List and integer, boolean data types.
 * The class contains a logger. The logger logging only to the terminal.
 */
@With
@AllArgsConstructor
public class GameState {

    @Setter
    List<Position> positions;

    private static final List<Position> INITIAL_STATE = List.of(
            new Position(7, 1, 0),
            new Position(7, 3, 0),
            new Position(7, 5, 0),
            new Position(7, 7, 0),
            new Position(0, 2, 1));


    private int row;

    private int col;

    private int character;

    boolean isGoal = false;

    @Getter
    @Setter
    static int actPlayerId = 0;

    private static final Logger logger = LogManager.getLogger(GameState.class);

    /**
     * GameState constructor initialise the original states. This is how the characters appear on the chess table when the game starts, or restarting.
     */
    public GameState(){
        this.positions = INITIAL_STATE;
    }

    /**
     * returns the position on the given index.
     * @param n the index of the element we want to get back.
     * @return the desired position.
     */
    public Position getPosition(int n){
        return positions.get(n);
    }

    /**
     * checks if the player choose the character that belongs to its player base.
     * @param row the clicked row.
     * @param col the clicked col.
     * @param position positions on the chess table.
     */
    public void validChoose(int row, int col, GameState position){
        for (int i = 0; i<5; i++) {
            if (row == position.getPosition(i).getRow() && col == position.getPosition(i).getCol() && position.getPosition(i).getPlayerId() == actPlayerId) {
                this.row = row;
                this.col = col;
                this.character = i;
            }
        }
    }

    /**
     *  performs the move if any neighbor cell is corresponding with the clicked cell, and if the move is valid. only diagonal neighbors are good.
     * @param row the clicked row.
     * @param col the cliced col.
     * @param position list of positions to check is the neighbor cells are empty or not.
     * @return returns the new positions to the controller.
     */
    public GameState performMove(int row, int col, GameState position){
        if (this.character<=3 && this.actPlayerId == 0){
            if ((row == this.row-1 && col == this.col-1 ||
                    row == this.row-1 && col == this.col+1) &&
                    (isEmpty(row, col, 0) &&
                            isEmpty(row, col, 1))) {
                this.actPlayerId = 1;
                GameController.setSteps(GameController.getSteps()+1);
                logger.info("One of the dogs moved to ({},{})", row, col);
                return position.move(row, col, character, 0);
            }else if (!(position.getPosition(this.character).getRow()==row && position.getPosition(this.character).getCol()==col)){
                logger.warn("INVALID MOVE");
            }
        } else if(this.character == 4 && this.actPlayerId == 1){
            if ((row == this.row-1 && col == this.col-1 ||
                    row == this.row-1 && col == this.col+1 ||
                    row == this.row+1 && col == this.col+1 ||
                    row == this.row+1 && col == this.col-1) &&
                    (isEmpty(row, col, 0) &&
                    isEmpty(row, col, 1))) {
                this.actPlayerId = 0;
                GameController.setSteps(GameController.getSteps()+1);
                logger.info("fox moved to ({},{})", row, col);
                return position.move(row, col, character, actPlayerId+1);
            }else if (!(position.getPosition(this.character).getRow()==row && position.getPosition(this.character).getCol()==col)){
                logger.warn("INVALID MOVE");
            }
        }else{
            logger.warn("INVALID CHOICE");
        }
        return position;
    }

    /**
     * checks if the clicked cell is empty or not.
     * @param row clicked row.
     * @param col clicked col.
     * @param playerId the player id who clicked 0 (dog) 1 (fox).
     * @return returns true is the cell is empty, and false if the cell is not empty.
     */
    public boolean isEmpty(int row, int col, int playerId){
        Position plannedPosition = new Position(row, col, playerId);
        return !positions.contains(plannedPosition);
    }

    /**
     * moves the given element to the clicked position if the performsMove conditions are true.
     * @param row clicked row where we want to move the character
     * @param col clicked col where we want to move the character
     * @param i index of the item we want to move
     * @param charId id of the charater we want to move. 0 (dog) 1 (fox)
     * @return the new position list with the modified character position
     */
    private GameState move(int row, int col, int i, int charId) {

        final var newPositions = new ArrayList<>(this.positions);
        final var newPosition = new Position(row, col, charId);
        newPositions.set(i, newPosition);
        return withPositions(Collections.unmodifiableList(newPositions));
    }

    /**
     * checks if the positions are goal for fox.
     * @param positions position list.
     * @return returns true if the fox wins, false if the position is not goal for fox.
     */

    public boolean isGoalForFox(GameState positions){
        isGoal = false;
        int matches = 0;
        for (int i = 1; i < 5; i++) {
            if (positions.getPosition(i-1).getRow() < positions.getPosition(4).getRow())
                matches++;
            if (matches == 4){
                isGoal = true;
            }
        }
        return isGoal;
    }

    /**
     * checks if the positions are goal for dogs.
     * @param positions position list.
     * @return returns true if the dogs wins, false if the position is not goal for dogs.
     */
    public boolean isGoalForDogs(GameState positions){
        Position foxPosition = positions.getPosition(4);

        List<Position> catchPosition = new ArrayList<>(List.of(
                new Position(foxPosition.getRow() - 1, foxPosition.getCol() - 1, 0),
                new Position(foxPosition.getRow() - 1, foxPosition.getCol() + 1, 0),
                new Position(foxPosition.getRow() + 1, foxPosition.getCol() - 1, 0),
                new Position(foxPosition.getRow() + 1, foxPosition.getCol() + 1, 0)
        ));
        int matches = 0;
        for (int i = 0; i < catchPosition.size(); i++) {
            for (Position position : catchPosition) {
                if (position.getCol() == positions.getPosition(i).getCol() && position.getRow() == positions.getPosition(i).getRow()) {
                    matches++;
                }
            }
        }
        if (foxPosition.getRow() == 0 && foxPosition.getCol() ==0){
            isGoal = (matches == 1);
            return isGoal;
        } else if ((foxPosition.getRow() > 0 && foxPosition.getCol() > 0) && (foxPosition.getRow() < 7 && foxPosition.getCol() < 7)) {
            isGoal = (matches == 4);
            return isGoal;
        } else if (foxPosition.getRow() == 0 || foxPosition.getCol() == 0 || foxPosition.getCol() == 7) {
            isGoal = (matches == 2);
            return isGoal;
        }
        return isGoal;
    }
}
