package Strategy;
import java.util.List;
public interface Strategy {
    public List<Players> processFile(String fileName);
    public String printStatistics(List <Players> players);
    public void saveScore(String fileName, Players player);
}
