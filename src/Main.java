import java.util.Scanner;

public class Main {
    private static SignIn signIn;
    private static SignOut signOut;
    private static Writer writer;
    private static Create create;
    private static Scanner scanner;

    public static void main(String[] args) throws Exception {
        signIn = new SignIn();
        signOut = new SignOut();
        writer = new Writer();
        create = new Create();
        scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Would you like to sign in, sign out, or create a profile? (in/out/create)");
            String mode = scanner.next();
            if (mode.equals("in")) {
                signIn.getID();
                writer.toArray();
                signIn.toWriter(writer);
            } else if(mode.equals("create")) {
                create.getID();
                writer.toArray();
                create.registerID(writer);
            } else if(mode.equals("out")) {
                signOut.getID();
                writer.toArray();
                signOut.addSignOut(writer);
            } 
            else {
                System.out.println("Invalid response. Please try again.");
            }
        }
    }
}