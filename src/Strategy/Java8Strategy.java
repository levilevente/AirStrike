package Strategy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Java8Strategy implements Strategy{

    @Override
    public List<Players> processFile(String fileName){
        List <Players> list;
        list = new ArrayList<>();
        try{
            BufferedReader fin = new BufferedReader(new FileReader(fileName));
            list = list = fin.lines().map(e-> {
                String[] tokens = e.split(" ");
                return new Players(tokens[0], Integer.parseInt(tokens[1]));
            }).sorted(Comparator.comparing(Players::getScore).reversed()).collect(Collectors.toList());
        }catch (Exception e){
            e.getMessage();
        }

        return list;
    }

    @Override
    public String printStatistics(List<Players> players) {
        StringBuilder stringInfo = new StringBuilder();
        System.out.print("Mean score: ");
        double avg = players.stream()
                .flatMapToInt(e -> IntStream.of(e.getScore()))
                .average()
                .orElse(0);
        System.out.println(avg);
        stringInfo.append("Mean Score: ").append(avg).append('\n');

        System.out.print("Max score: ");
        OptionalInt maxScore = players.stream()
                .flatMapToInt(e -> IntStream.of(e.getScore()))
                .max();
        System.out.println(maxScore.getAsInt());
        stringInfo.append("Max Score: ").append(maxScore.getAsInt()).append('\n');


        System.out.println("Players with score " + maxScore.getAsInt() + ": ");
        List<String> maxScorers = players.stream()
                .filter(player -> player.getScore() == maxScore.getAsInt())
                .map(name -> name.getName())
                .toList();
        stringInfo.append("Max Scorers: ").append('\n');
        for (String m : maxScorers){
            System.out.println(m);
            stringInfo.append(m).append('\n');
        }

        return stringInfo.toString();
    }
}
