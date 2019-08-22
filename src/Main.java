import java.io.IOException;
import java.text.ParseException;

public class Main {
	private static SignIn signIn;
	private static SignOut signOut;
	public static LogWriter logWriter;
	public static TotalWriter totalWriter;
	private static Create create;
	private static Total total;
	private static Frame frame;
	private static MainMenu mainMenu;
	private static CreateMenu createMenu;
	private static SignInMenu signInMenu;
	private static SignOutMenu signOutMenu;
	public static int frameWidth, frameHeight;
	public static String currentBarcode;
	private static Thread barcode;
	public static State state;

	public static void main(String[] args) throws Exception {
		frameWidth = 1024;
		frameHeight = 768;
		currentBarcode = null;
		frame = new Frame(1024, 768);
		createMenu = new CreateMenu(frame);
		signInMenu = new SignInMenu(frame);
		signOutMenu = new SignOutMenu(frame);
		signIn = new SignIn();
		signOut = new SignOut();
		logWriter = new LogWriter();
		totalWriter = new TotalWriter();
		create = new Create();
		total = new Total(logWriter);
		mainMenu = new MainMenu(frame);
		state = State.MAIN;

		barcode = new Thread(){
			String code = null;
			public void run() {
				while(true) {
					try{
						code = BarcodeScanner.searchForBarcode();
						if(code != null) {
							currentBarcode = code;
							setIDBarcode(currentBarcode);
						}
						Thread.sleep(500);
					} catch(InterruptedException v){System.out.println(v);
					}
				}
			}
		};
		barcode.start();
	}

	public static void mainMenu() {
		resetBarcode();
		state = State.MAIN;
		mainMenu.toggleVisible();
	}

	public static void showSignIn() {
		resetBarcode();
		state = State.SIGNIN;
		signInMenu.toggleVisible();
	}
	public static void signIn(String date, int id) {
		try {
			logWriter.toArray();
			signIn.toLogWriter(logWriter, date, id);
		} catch (IOException e) {
		}

	}
	public static void showSignOut() {
		resetBarcode();
		state = State.SIGNOUT;
		signOutMenu.toggleVisible();
	}
	public static void signOut(String date, int id) {
		try {
			logWriter.toArray();
			signOut.addSignOut(logWriter, date, id);
		} catch (IOException e) {
		}
	}

	public static void showCreate() {
		resetBarcode();
		state = State.CREATE;
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
		} catch (ParseException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	private static void resetBarcode() {
		currentBarcode = null;
	}
	private static void setIDBarcode(String id) {
		if(state == State.CREATE) {
			createMenu.setIDText(id);
		} else if(state == State.SIGNIN) {
			signInMenu.setIDText(id);
		}else if(state == State.SIGNOUT) {
			signOutMenu.setIDText(id);
		}
	}
}