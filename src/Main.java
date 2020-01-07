import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;

import javax.swing.JOptionPane;

public class Main {
	public static LogWriter logWriter;
	public static TotalWriter totalWriter;
	private static Total total;
	private static Frame frame;
	private static MainMenu mainMenu;
	private static Create create;
	private static SignIn signIn;
	private static SignOut signOut;
	public static int frameWidth, frameHeight;
	public static String currentBarcode;
	private static Thread barcode;
	public static AppState state;

	public static void main(String[] args) throws Exception {
		System.out.println(new File(".").getAbsolutePath());
		frameWidth = 1024;
		frameHeight = 768;
		currentBarcode = null;
		frame = new Frame(1024, 768);
		create = new Create(frame);
		signIn = new SignIn(frame);
		signOut = new SignOut(frame);
		logWriter = new LogWriter();
		totalWriter = new TotalWriter();
		total = new Total(logWriter);
		mainMenu = new MainMenu(frame);
		state = AppState.MAIN;

		barcode = new Thread(){
			String code = null;
			boolean hasScanned = false;
			public void run() {
				while(true) {
					//TODO: ask morton why thread would on run again (after going from one of the menus to the main) if there was a print statement after the while loop in the thread.
					if(state == AppState.MAIN) {
						if(!hasScanned) {
							try {
								code = BarcodeScanner.searchForBarcode();
							} catch (NullPointerException e) {
								mainMenu.camNotFound.setVisible(true);
							}
							if(code != null) {
								currentBarcode = code;
								automaticPunch();
								hasScanned = true;
							}
						} else {
							code = BarcodeScanner.searchForBarcode();
							if(code != null) {
								hasScanned = false;
							}
						}
					} else {
						//barcode.interrupt();
					}
					try  {

						Thread.sleep(500);
					} catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}
		};
		barcode.start();
	}

	public static void automaticPunch() {
		if(mainMenu.isCreated(currentBarcode, logWriter)) {
			boolean isSignedIn = mainMenu.isSignedIn(currentBarcode);
			if(isSignedIn) {
				signOut.confirmAuto(Integer.parseInt(currentBarcode));
				JOptionPane.showMessageDialog(null, "Sign Out time successfully entered. Remove ID then click 'OK'");
				mainMenu.refresh();
				frame.repaint();
			} else if(!isSignedIn) {
				if(signIn.confirmAuto(Integer.parseInt(currentBarcode))) {
					JOptionPane.showMessageDialog(null, "Sign In time successfully entered. Remove ID then click 'OK'");
				}
				mainMenu.refresh();
				frame.repaint();
			}
		} else {
			mainMenu.toggleVisible();
			frame.repaint();
			create.setIDText(currentBarcode);
			showCreate();
		}
	}
	private static String getDateTime() {
		return LocalDate.now().toString();

	}
	public static void mainMenu() {
		resetBarcode();
		state = AppState.MAIN;
		mainMenu.toggleVisible();
	}

	public static void showSignIn() {
		resetBarcode();
		state = AppState.SIGNIN;
		signIn.toggleVisible();
	}
	public static void signIn(String date, int id) {
		logWriter.toArray();
		signIn.toLogWriter(logWriter, date, id);

	}
	public static void showSignOut() {
		resetBarcode();
		state = AppState.SIGNOUT;
		signOut.toggleVisible();
	}
	public static void signOut(String date, int id) {
		logWriter.toArray();
		signOut.addSignOut(logWriter, date, id);
	}

	public static void showCreate() {
		resetBarcode();
		state = AppState.CREATE;
		create.toggleVisible();

	}
	public static void create(String name, int id) {
		logWriter.toArray();
		create.registerID(logWriter, totalWriter, name, id);
	}

	public static void total() {
		total.getTotals(totalWriter);
	}
	private static void resetBarcode() {
		currentBarcode = null;
	}
}