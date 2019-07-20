import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static SignIn signIn;
    private static SignOut signOut;
    public static LogWriter logWriter;
    public static TotalWriter totalWriter;
    private static Create create;
    private static Total total;
    private static Scanner scanner;
    private static File file;
    private static Frame frame;
    private static MainMenu mainMenu;
    private static CreateMenu createMenu;
    private static SignInMenu signInMenu;

    public static void main(String[] args) throws Exception {
        frame = new Frame();
        mainMenu = new MainMenu(frame);
        createMenu = new CreateMenu(frame);
        signInMenu = new SignInMenu(frame);
        signIn = new SignIn();
        signOut = new SignOut();
        logWriter = new LogWriter();
        totalWriter = new TotalWriter();
        create = new Create();
        total = new Total(logWriter);
        scanner = new Scanner(System.in);
    }

    public static void mainMenu() {
        mainMenu.toggleVisible();
    }

    public static void showSignIn() {
        signInMenu.toggleVisible();
    }
    public static void signIn(String date, int id) {
        try {
            logWriter.toArray();
            signIn.toLogWriter(logWriter, date, id);
        } catch (IOException e) {
        }

    }

    public static void signOut() {
        try {
            signOut.getID();
            logWriter.toArray();
            signOut.addSignOut(logWriter);
        } catch (IOException e) {
        }
    }

    public static void showCreate() {
        createMenu.toggleVisible();
 
    }
    public static void create(String name, int id) {
        try {
            logWriter.toArray();
            create.registerID(logWriter, totalWriter, name, id);
        } catch (IOException e) {
            System.out.println("IOException @ Main.java, 82");
        }
    }

    public static void total() {
        try {
            total.getTotals(totalWriter);
        } catch (Exception e) {
        }
    }
}