import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.*;

public class MainMenu {
	private Frame frame;
	private JPanel panel;
	private JLabel directions;
	public JLabel camNotFound;
	private ArrayList<JLabel> nameList;
	private JButton signIn, signOut, create, total;
	private ArrayList<JComponent> components;
	private final int buttonWidth, buttonHeight;
	private Font jFont;
	private boolean isVisible;

	public MainMenu(Frame frame) throws IOException, ParseException {
		this.frame = frame;
		isVisible = true;
		jFont = new Font("Rorasfboto", Font.BOLD, 14);
		panel = new JPanel();
		signIn = new JButton("Sign In");
		signOut = new JButton("Sign Out");
		create = new JButton("Create Profile");
		total = new JButton("Output Totals");
		directions = new JLabel("Please scan your id or use the buttons below to proceed manually");
		camNotFound = new JLabel("Camera not found.");
		nameList = new ArrayList<JLabel>();
		buttonWidth = 300;
		buttonHeight = 100;
		components = new ArrayList<JComponent>();
		components.add(signIn);
		components.add(signOut);
		components.add(create);
		components.add(total);
		components.add(directions);
		components.add(camNotFound);
		getActive();
		createcomponents();
		addcomponents();
		camNotFound.setVisible(false);

	}

	public void addcomponents() {
		panel.setLayout(null);
		panel.setBounds(0, 0, Main.frameWidth, Main.frameHeight);
		for (JComponent c : components) {
			c.setFont(jFont);
			c.setVisible(true);
			panel.add(c);
		}
		frame.addComponent(panel);
	}

	public void createcomponents() {
		panel.setVisible(true);
		int distance = 50;
		int startX = frame.width * 2 / 5;
		int startY = frame.height * 2 / 5;
		int centerX = frame.width / 2;
		int centerY = frame.height / 2;
		int halfWidth = buttonWidth / 2;
		
		directions.setFont(jFont);
		camNotFound.setFont(jFont);
		//camNotFound.setFont(new Font("Openafaf Sans", Font.PLAIN, 14));
		camNotFound.setForeground(Color.RED);
		signIn.setBounds(centerX - buttonWidth - (distance / 2), centerY - buttonHeight - (distance / 2), buttonWidth, buttonHeight);
		signOut.setBounds(centerX - buttonWidth - (distance / 2), centerY + (distance / 2), buttonWidth, buttonHeight);
		create.setBounds(centerX + (distance / 2), centerY - buttonHeight - (distance / 2), buttonWidth, buttonHeight);
		total.setBounds(centerX + (distance / 2), centerY + (distance / 2), buttonWidth, buttonHeight);
		int dirWidth = directions.getFontMetrics(directions.getFont()).stringWidth(directions.getText());
		int noCamWidth = camNotFound.getFontMetrics(camNotFound.getFont()).stringWidth(camNotFound.getText());
		directions.setBounds(centerX - dirWidth / 2, 100, 1000, 100);
		camNotFound.setBounds(centerX - noCamWidth / 2, 150, 1000, 100);

		signIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleVisible();
				frame.repaint();
				Main.showSignIn();
			}
		});
		signOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleVisible();
				frame.repaint();
				Main.showSignOut();
			}
		});
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleVisible();
				frame.repaint();
				Main.showCreate();
			}
		});
		total.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.total();
			}
		});
	}

	private void getActive() {
		Main.totalWriter.toArray();
		int row = 1;
		String namesList = "Currently signed in: \n";
		Main.totalWriter.toArray();
		while (row != -1) {
			String id = Main.totalWriter.getNextID(row);
			if (id != null) {
				if (isSignedIn(id)) {
					namesList += "    " + Main.totalWriter.getName(id);
					namesList += "\n";
				}
				row++;
			} else {
				row = -1;
				break;
			}
		}
		addActive(namesList);
	}

	private void addActive(String namesList) {
		String[] lines = namesList.split("\n");
		int y = 40;
		for (String line : lines) {
			JLabel label = new JLabel(line);
			label.setBounds(25, y, 150, 20);
			label.setFont(jFont);
			panel.add(label);
			label.setVisible(true);
			y += 20;
			nameList.add(label);
		}
	}

	private void removeActiveLabels() {
		for (JLabel label : nameList) {
			panel.remove(label);
		}
		while (nameList.size() > 0) {
			nameList.remove(0);
		}
	}

	public boolean isCreated(String id, LogWriter w) {
		return w.isIDPresent(Integer.parseInt(id));
	}

	public boolean isSignedIn(String id) {
		String date = LocalDate.now().toString();
		if (Main.logWriter.isSignInPresent(date, Integer.parseInt(id))) {
			int signInRow = Main.logWriter.getSignInRow(date, Integer.parseInt(id));
			if (!Main.logWriter.isSignOutPresent(signInRow, Integer.parseInt(id))) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public void toggleVisible() {
		isVisible = !isVisible;
		// panel.setVisible(isVisible);

		if (isVisible) {
			getActive();
			frame.addComponent(panel);
			frame.setDefaultButton(null);
		} else {
			removeActiveLabels();
			frame.removeComponent(panel);
		}
		frame.repaint();
	}
	public void refresh() {
		removeActiveLabels();
		getActive();
	}

}