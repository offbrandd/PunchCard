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

public class SignIn {
	private Frame frame;
	private JPanel panel;
	private JButton confirm, home;
	private JTextField idField;
	private JLabel idLabel;
	private ArrayList<JComponent> components;
	private boolean isVisible;

	public SignIn(Frame frame) {
		this.frame = frame;
		panel = new JPanel();
		confirm = new JButton("Confirm");
		home = new JButton("Home");
		idField = new JTextField();
		idLabel = new JLabel("Enter ID");
		components = new ArrayList<JComponent>();
		components.add(confirm);
		components.add(home);
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
			frame.setDefaultButton(confirm);
		}
		else {
			frame.removeComponent(panel);
		}
		frame.repaint();
	}

	public void createComponents() {
		panel.setBounds(0, 0, 1024, 768);
		panel.setVisible(true);
		confirm.setBounds(500, 600, 100, 50);
		home.setBounds(100, 100, 100, 50);
		idField.setBounds(500, 500, 200, 25);
		idLabel.setBounds(500, 470, 100, 30);
		addComponents();
		addActions();
	}

	private void addActions() {
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirm();
			}
		});
		home.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleVisible();
				Main.mainMenu();
			}
		});
	}
	
	private void confirm() {
		try {
			String date = LocalDate.now().toString();
			int id = Integer.parseInt(idField.getText());
			boolean idPresent = true;
			boolean dateReady = true;
			String timeIn = (LocalTime.now().toString()).substring(0, 8);
			if (Main.logWriter.isIDPresent(id)) {
				if (Main.logWriter.isDatePresent(date)) {
					if (Main.logWriter.isSignInPresent(date, id)) {
						if (requestAdditional()) {
							if (!Main.logWriter.addExtraSignIn(date, id, timeIn)) {
								Main.logWriter.addDate(date);
							} else {
								dateReady = false;
								finishSignIn();
							}
						} else {
							dateReady = false;
							JOptionPane.showMessageDialog(null, "Entry cancelled.");
						}
					}
				} else {
					Main.logWriter.addDate(date);
				}
			} else {
				JOptionPane.showMessageDialog(null,
						"ID is not registered. Please create a profile or try again with another ID");
				idPresent = false;
			}
			if (idPresent && dateReady) {
				Main.logWriter.addSignIn(id, timeIn, date);
				finishSignIn();
			}

		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "ID may only contain numbers.");
		}

	}
	public boolean confirmAuto(int id) {
		try {
			String date = LocalDate.now().toString();
			//check if they want to scan, if not, then use text field
			boolean idPresent = true;
			boolean dateReady = true;
			String timeIn = (LocalTime.now().toString()).substring(0, 8);
				if (Main.logWriter.isDatePresent(date)) {
					if (Main.logWriter.isSignInPresent(date, id)) {
						if (requestAdditional()) {
							if (!Main.logWriter.addExtraSignIn(date, id, timeIn)) {
								Main.logWriter.addDate(date);
							} else {
								dateReady = false;
								finishSignIn();
								return true;
							}
						} else {
							dateReady = false;
							JOptionPane.showMessageDialog(null, "Entry cancelled.");
							return false;
						}
					}
				} else {
					Main.logWriter.addDate(date);
				}
			if (idPresent && dateReady) {
				Main.logWriter.addSignIn(id, timeIn, date);
				return true;
			}

		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "ID may only contain numbers.");
		}
		return false;

	}

	private boolean requestAdditional() {
		int n = 0;
		n = JOptionPane.showConfirmDialog(null,
				"A sign in time for today has already been entered. Would you like to create another entry?",
				"Confirmation", JOptionPane.YES_NO_OPTION);
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
	
    public void toLogWriter(LogWriter w, String date, int id) {
        String timeIn = (LocalTime.now().toString()).substring(0, 8);
        w.addSignIn(id, timeIn, date);
    }

	private void finishSignIn() {
		JOptionPane.showMessageDialog(null, "Sign In time successfully entered.");
		toggleVisible();
		clearTextBoxes();
		Main.mainMenu();

	}

}