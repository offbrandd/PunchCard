import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.IOException;
import java.time.*;

public class SignIn {
    private Scanner scanner;
    private int id;
    private String date;
    private String timeIn;

    public SignIn() {
        scanner = new Scanner(System.in);
        id = -1;
    }

    public void getID() {
        System.out.println("Please enter ID to sign in");
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

    public void toWriter(Writer w) throws IOException {
        date = LocalDate.now().toString();
        timeIn = (LocalTime.now().toString()).substring(0, 8);
        w.addSignIn(id, timeIn, date, scanner);
    }
}