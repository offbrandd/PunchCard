import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Create {
    private Scanner scanner;
    private int id;

    public Create() {
        scanner = new Scanner(System.in);
        id = -1;
    }
    public void getID() {
        System.out.println("Please enter an ID Number to create your profile");
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
    public void registerID(LogWriter w) throws IOException {
        if(w.addID(id)) {
            System.out.println("Profile successfully created. You may now sign in.");
        } else {
            System.out.println("ID already exists, please sign in or try with another ID.");
        }
    }
}