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
    private ArrayList<JLabel> nameList;
    private JButton signIn, signOut, create, total;
    private ArrayList<JComponent> components;
    private final int buttonWidth, buttonHeight;
    private boolean isVisible;

    public MainMenu(Frame frame) throws IOException, ParseException {
        this.frame = frame;
        isVisible = true;
        panel = new JPanel();
        signIn = new JButton("Sign In");
        signOut = new JButton("Sign Out");
        create = new JButton("Create Profile");
        total = new JButton("Output Totals");
        nameList = new ArrayList<JLabel>();
        buttonWidth = 150;
        buttonHeight = 50;
        components = new ArrayList<JComponent>();
        components.add(signIn);
        components.add(signOut);
        components.add(create);
        components.add(total);
        getActive();
        createcomponents();
        addcomponents();

    }

    public void addcomponents() {
        panel.setLayout(null);
        panel.setBounds(0, 0, Main.frameWidth, Main.frameHeight);
        for (JComponent c : components) {
            c.setVisible(true);
            panel.add(c);
        }
        frame.addComponent(panel);
    }

    public void createcomponents() throws IOException {
        panel.setVisible(true);
        int posInterval = frame.width / 5;
        int halfWidth = buttonWidth / 2;
        signIn.setBounds((posInterval) - halfWidth, frame.height - 200, buttonWidth, buttonHeight);
        signOut.setBounds((posInterval) * 2 - halfWidth, frame.height - 200, buttonWidth, buttonHeight);
        create.setBounds((posInterval) * 3 - halfWidth, frame.height - 200, buttonWidth, buttonHeight);
        total.setBounds((posInterval) * 4 - halfWidth, frame.height - 200, buttonWidth, buttonHeight);

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

    private void getActive() throws IOException, ParseException {
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
            label.setBounds(50, y, 150, 20);
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

    private boolean isSignedIn(String id) throws ParseException {
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
            try {
                getActive();
            } catch (IOException | ParseException ex) {
            }
            frame.addComponent(panel);
        } else {
            removeActiveLabels();
            frame.removeComponent(panel);
        }
        frame.repaint();
    }

}