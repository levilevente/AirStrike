package Strategy;
import java.io.FileNotFoundException;
import java.util.List;
public interface Strategy {
    public List<Players> processFile(String fileName) throws FileNotFoundException;
    public String printStatistics(List <Players> students);
}
