import java.io.IOException;
import java.time.LocalTime;

public class SignOut {
    private String timeOut;

    public SignOut() {

    }

    public void addSignOut(LogWriter w, String date, int id) throws IOException {
        timeOut = (LocalTime.now().toString()).substring(0, 8);
        w.addSignOut(id, timeOut, date);
    }
}