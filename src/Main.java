import java.io.File;
import java.util.Scanner;

public class Main {
    private static SignIn signIn;
    private static SignOut signOut;
    private static LogWriter logWriter;
    private static TotalWriter totalWriter;
    private static Create create;
    private static Total total;
    private static Scanner scanner;
    private static File file;

    public static void main(String[] args) throws Exception {
        signIn = new SignIn();
        signOut = new SignOut();
        logWriter = new LogWriter();
        totalWriter = new TotalWriter();
        create = new Create();
        total = new Total(logWriter);
        scanner = new Scanner(System.in);
        while (true) {
            System.out.println("At any time, type 'close' and hit enter to close the application");
            System.out.println(
                    "Would you like to sign in, sign out, create a profile, or output total hours? (in/out/create/total)");
            String mode = scanner.next();
            if (mode.equals("in")) {
                signIn.getID();
                logWriter.toArray();
                signIn.toLogWriter(logWriter);
            } else if (mode.equals("create")) {
                create.getID();
                create.getName();
                logWriter.toArray();
                create.registerID(logWriter, totalWriter);
            } else if (mode.equals("out")) {
                signOut.getID();
                logWriter.toArray();
                signOut.addSignOut(logWriter);
            } else if (mode.equals("total")) {
                total.getTotals(totalWriter);
            } else if (mode.equals("close")) {
                System.exit(0);
            } else {
                System.out.println("Invalid response. Please try again.");
            }
        }
    }
}