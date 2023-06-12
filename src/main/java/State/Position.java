package State;


import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Class that contains a row, a col, and a playerID variable. these values used to store characters places on the chess table
 *  playerID can be 0 (dog) or 1 (fox)
 */

@Value
@AllArgsConstructor
public class Position {

    int row;

    int col;

    int playerId;


}
