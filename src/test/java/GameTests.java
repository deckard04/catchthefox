import State.GameState;
import State.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import results.GameResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTests {

    GameState state1 = new GameState();

    @Test
    void isValidState(){
        state1.setPositions(List.of(
                new Position(2, 6, 0),
                new Position(2, 4, 0),
                new Position(4, 2, 0),
                new Position(4, 7, 0),
                new Position(3, 5, 1)));
        assertFalse(state1.isGoalForDogs(state1));
        assertFalse(state1.isGoalForFox(state1));
    }
    @Test
    void isFoxLoserState(){
        state1.setPositions(List.of(
                new Position(2, 6, 0),
                new Position(2, 4, 0),
                new Position(4, 4, 0),
                new Position(4, 6, 0),
                new Position(3, 5, 1)));
        assertTrue(state1.isGoalForDogs(state1));
        assertFalse(state1.isGoalForFox(state1));
    }


    @Test
    void isFoxWinnerState(){
        state1.setPositions(List.of(
                new Position(2, 6, 0),
                new Position(2, 4, 0),
                new Position(4, 2, 0),
                new Position(4, 7, 0),
                new Position(5, 5, 1)));
        assertTrue(state1.isGoalForFox(state1));
    }

    @BeforeEach
    @Test
    void loadUpPositions(){
        state1.setPositions(List.of(
                new Position(2, 6, 0),
                new Position(2, 4, 0),
                new Position(4, 2, 0),
                new Position(4, 7, 0),
                new Position(5, 5, 1)));
    }

    @Test
    void chooseTests(){
        GameState.setActPlayerId(0);
        state1.validChoose(2, 2, state1);
        state1.validChoose(2, 4, state1);
        state1.performMove(2, 4, state1);
        state1.performMove(3, 4, state1);
        state1.performMove(3, 5, state1);
        GameState.setActPlayerId(1);
        state1.validChoose(5, 5, state1);
        state1.performMove(4, 4, state1);
        state1.performMove(5, 5, state1);
    }

    @Test
    void resultTest(){
        GameResult result = GameResult.builder()
                .id(1L)
                .player1("test1")
                .player2("test2")
                .winner("test2")
                .steps(30)
                .build();
        GameResult result1 = new GameResult();
        result1.setId(1L);
        result1.setSteps(30);
        result1.setPlayer1("test1");
        result1.setPlayer2("test2");
        result1.setWinner("test2");

        assertEquals(result, result1);
    }
}
