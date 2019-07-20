import java.io.IOException;
import java.util.Scanner;

public class Create {
    private Scanner scanner;

    public Create() {
        scanner = new Scanner(System.in);
    }

    public void registerID(LogWriter w, TotalWriter t, String name, int id) throws IOException {
        t.addID(id);
        w.addID(id);
        t.addName(id, name, scanner);
        t.writeToCSV();

    }
}