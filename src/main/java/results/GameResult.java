package results;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Class that represents all the data the program saves about the played games.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class GameResult {
    @EqualsAndHashCode.Include
    /**
     * The player's ID in the given repository.
     */
    private Long id;

    /**
     * Fox players name stored in the playe1 variable.
     */
    private String player1;

    /**
     * Dog players name stored in the playe1 variable.
     */
    private String player2;

    /**
     * variable to determinate the winner.
     */
    private String winner;

    /**
     * Steps variable show how many step long the game was. During a difficult match this number can be very huge.
     */
    private int steps;

    /**
     * created variable contains the time when the game happened.
     */
    private LocalDateTime created;
}

