import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SignOut {
	private Scanner scanner;
    private int id;
    private String date;
    private String timeOut;

    public SignOut() {
        scanner = new Scanner(System.in);
        id = -1;
    }

    public void getID() {
        System.out.println("Please enter ID to sign out");
        boolean done = false;
        while (!done) {
            try {
                id = scanner.nextInt();
                done = true;
            } catch (InputMismatchException e) {
                if (scanner.nextLine().equals("close")) {
                    System.exit(0);
                } else {
                    System.out.println("Invalid input, please enter a number id");
                }
            }
        }
    }

    public void addSignOut(LogWriter w) throws IOException {
        date = LocalDate.now().toString();
        timeOut = (LocalTime.now().toString()).substring(0, 8);
        w.addSignOut(id, timeOut, date, scanner);
    }
}