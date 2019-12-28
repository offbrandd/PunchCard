import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SignOut {
	private Frame frame;
	private JPanel panel;
	private JButton confirm, home, allOut;
	private JTextField idField;
	private JLabel idLabel;
	private ArrayList<JComponent> components;
	private boolean isVisible;

	public SignOut(Frame frame) {
		this.frame = frame;
		panel = new JPanel();
		confirm = new JButton("Confirm");
		home = new JButton("Home");
		allOut = new JButton("Sign Out All Users");
		idField = new JTextField();
		idLabel = new JLabel("Enter ID");
		components = new ArrayList<JComponent>();
		components.add(confirm);
		components.add(home);
		components.add(allOut);
		components.add(idField);
		components.add(idLabel);
		isVisible = false;
		createComponents();
	}

	public void addComponents() {
		for (JComponent jc : components) {
			jc.setVisible(true);
			panel.add(jc);
		}
	}

	public void toggleVisible() {
		isVisible = !isVisible;
		if (isVisible) {
			frame.addComponent(panel);
		} else {
			frame.removeComponent(panel);
		}
		frame.repaint();
	}

	public void createComponents() {
		panel.setBounds(0, 0, 1024, 768);
		panel.setVisible(true);
		confirm.setBounds(500, 600, 100, 50);
		allOut.setBounds(500, 650, 100, 50);
		home.setBounds(100, 100, 100, 50);
		idField.setBounds(500, 500, 200, 25);
		idLabel.setBounds(500, 470, 100, 30);
		addComponents();
		addActions();
	}

	private void addActions() {
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					confirm();
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "ID may only contain numbers.");
				}

			}
		});
		home.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleVisible();
				Main.mainMenu();
			}
		});
		allOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					signAllOut();
				} catch (IOException ex) {
				}
			}
		});
	}
	public void addSignOut(LogWriter w, String date, int id) throws IOException {
		String timeOut = (LocalTime.now().toString()).substring(0, 8);
		w.addSignOut(id, timeOut, date);
	}

	private void confirm() {
		String date = LocalDate.now().toString();
		int id = Integer.parseInt(idField.getText());
		boolean idPresent = true;
		boolean dateReady = true;
		if (!Main.logWriter.isIDPresent(id)) {
			JOptionPane.showMessageDialog(null,
					"ID is not registered. Please create a profile or try again with another ID");
			idPresent = false;
		} else {
			dateReady = true;
			if (Main.logWriter.isDatePresent(date)) {
				int signInRow = Main.logWriter.getSignInRow(date, id);
				if (!Main.logWriter.isSignInPresent(date, id)) {
					JOptionPane.showMessageDialog(null,
							"This ID was not signed in for today. Sign out will be logged. Please see administrator with sign in time for manual entry.");
				} else if (Main.logWriter.isSignOutPresent(signInRow, id)) {
					if (!requestOverwrite()) {
						dateReady = false;
					}
				}
			} else {
				Main.logWriter.addDate(date);
			}
		}
		if (idPresent && dateReady) {
			Main.signOut(date, id);
			finishSignOut();
		}

	}  public void confirmAuto(int id) {
		String date = LocalDate.now().toString();
		boolean idPresent = true;
		boolean dateReady = true;
		dateReady = true;
		if (Main.logWriter.isDatePresent(date)) {
			int signInRow = Main.logWriter.getSignInRow(date, id);
			if (!Main.logWriter.isSignInPresent(date, id)) {
				JOptionPane.showMessageDialog(null,
						"This ID was not signed in for today. Sign out will be logged. Please see administrator with sign in time for manual entry.");
			} else if (Main.logWriter.isSignOutPresent(signInRow, id)) {
				if (!requestOverwrite()) {
					dateReady = false;
				}
			}
		} else {
			Main.logWriter.addDate(date);
		}
		if (idPresent && dateReady) {
			Main.signOut(date, id);
		}

	}

	private void signAllOut() throws IOException {
		int j = 1;
		String id = Main.logWriter.get(0, j);
		String date = LocalDate.now().toString();
		String timeOut = (LocalTime.now().toString()).substring(0, 8);
		while (id != null && !id.equals(" ")) {
			if (Main.logWriter.isSignInPresent(date, Integer.parseInt(id))) {
				int signInRow = Main.logWriter.getSignInRow(date, Integer.parseInt(id));
				if (!Main.logWriter.isSignOutPresent(signInRow, Integer.parseInt(id))) {
					Main.logWriter.addSignOut(Integer.parseInt(id), timeOut, date);
				}
			}
			j++;
			id = Main.logWriter.get(0, j);
		}
	}

	private boolean requestOverwrite() {
		int n = 0;
		n = JOptionPane.showConfirmDialog(null,
				"A sign out time for today has already been entered. Would you like to overwrite?", "Confirmation",
				JOptionPane.YES_NO_OPTION);
		if (n == 0) {
			return true;
		} else {
			return false;
		}
	}

	private void clearTextBoxes() {
		idField.setText("");
	}
	public void setIDText(String id) {
		idField.setText(id);
		confirm.doClick();
	}

	private void finishSignOut() {
		JOptionPane.showMessageDialog(null, "Sign Out time successfully entered.");
		toggleVisible();
		clearTextBoxes();
		Main.mainMenu();

	}

}