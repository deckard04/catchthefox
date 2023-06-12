package results;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gui.HighScoreController;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.text.SimpleDateFormat;

import java.util.*;

/**
 * Repository class for every {@link GameResult} entity.
 */

public class ResultRepository {

    protected static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    private static final Logger logger = LogManager.getLogger(HighScoreController.class);


    @Getter
    @Setter
    private static List<GameResult> gameResults = new ArrayList<GameResult>();

    public static void loadFromFile(
            @NonNull final File file) throws IOException {
        List<GameResult> gameResults1 =  readResult(new FileInputStream(file));
        gameResults.clear();
        gameResults.addAll(gameResults1);
        logger.info("successful reading");
    }

    public static List<GameResult> readResult(@NonNull final InputStream in) throws IOException {
        return Arrays.asList(MAPPER.readValue(in, GameResult[].class));
    }

    public static void writeResult(@NonNull final File file) throws IOException {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-dd-mm HH:mm:ss");
            List<GameResult> result = gameResults;
            logger.info(result);
            MAPPER.setDateFormat(df);
            MAPPER.writeValue(file, result);
    }


    public static void addElement(@NonNull final GameResult result) {
        if (result.getId() == null) {
            result.setId(getGameResults().stream()
                    .mapToLong(GameResult::getId)
                    .max()
                    .orElse(0L) + 1L);
        }
        gameResults.add(result);
    }
}
