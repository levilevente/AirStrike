package Strategy;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class Java8Strategy implements Strategy{

    // reads "name score" lines, sorted by score (highest first); bad lines are skipped
    @Override
    public List<Players> processFile(String fileName){
        try (Stream<String> lines = Files.lines(Path.of(fileName))) {
            return lines.map(Java8Strategy::parseLine)
                    .flatMap(Optional::stream)
                    .sorted(Comparator.comparingInt(Players::getScore).reversed())
                    .toList();
        } catch (NoSuchFileException e) {
            return new ArrayList<>();       // no game was saved yet
        } catch (IOException | UncheckedIOException e) {
            System.err.println("Error reading the leaderboard: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static Optional<Players> parseLine(String line) {
        String[] tokens = line.trim().split("\\s+");
        if (tokens.length != 2) {
            return Optional.empty();
        }
        try {
            return Optional.of(new Players(tokens[0], Integer.parseInt(tokens[1])));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    // adds the player, or keeps the higher score if the player is already in the file
    @Override
    public void saveScore(String fileName, Players player) {
        Path file = Path.of(fileName);
        try {
            List<String> lines = Files.exists(file) ? Files.readAllLines(file) : List.of();
            List<String> updatedLines = new ArrayList<>();
            boolean playerExists = false;

            for (String line : lines) {
                Optional<Players> existing = parseLine(line);
                if (existing.isPresent() && existing.get().getName().equals(player.getName())) {
                    playerExists = true;
                    int bestScore = Math.max(existing.get().getScore(), player.getScore());
                    updatedLines.add(player.getName() + " " + bestScore);
                } else if (!line.isBlank()) {
                    updatedLines.add(line);
                }
            }
            if (!playerExists) {
                updatedLines.add(player.toString());
            }

            Files.write(file, updatedLines);
        } catch (IOException e) {
            System.err.println("Error updating the leaderboard: " + e.getMessage());
        }
    }

    @Override
    public String printStatistics(List<Players> players) {
        if (players.isEmpty()) {
            System.out.println("No scores saved yet.");
            return "No scores saved yet.\n";
        }
        StringBuilder stringInfo = new StringBuilder();

        double avg = players.stream()
                .mapToInt(Players::getScore)
                .average()
                .orElse(0);
        System.out.println("Mean score: " + avg);
        stringInfo.append("Mean Score: ").append(String.format("%.2f", avg)).append('\n');

        int maxScore = players.stream()
                .mapToInt(Players::getScore)
                .max()
                .getAsInt();
        System.out.println("Max score: " + maxScore);
        stringInfo.append("Max Score: ").append(maxScore).append('\n');

        System.out.println("Players with score " + maxScore + ": ");
        List<String> maxScorers = players.stream()
                .filter(player -> player.getScore() == maxScore)
                .map(Players::getName)
                .toList();
        stringInfo.append("Max Scorers: ").append('\n');
        for (String m : maxScorers){
            System.out.println(m);
            stringInfo.append(m).append('\n');
        }

        return stringInfo.toString();
    }
}
