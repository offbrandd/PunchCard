import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

public class MainMenu {
    private Frame frame;
    private JPanel panel;
    private JButton signIn, signOut, create, total;
    private ArrayList<JButton> buttons;
    private final int buttonWidth, buttonHeight;
    private boolean isVisible;

    public MainMenu(Frame frame) throws IOException {
        this.frame = frame;
        isVisible = true;
        panel = new JPanel();
        signIn = new JButton("Sign In");
        signOut = new JButton("Sign Out");
        create = new JButton("Create Profile");
        total = new JButton("Output Totals");
        buttonWidth = 150;
        buttonHeight = 50;
        buttons = new ArrayList<JButton>();
        buttons.add(signIn);
        buttons.add(signOut);
        buttons.add(create);
        buttons.add(total);
        createButtons();
        addButtons();
    }

    public void addButtons() {
        for (JButton b : buttons) {
            b.setVisible(true);
            panel.add(b);
        }
        frame.addComponent(panel);
    }

    public void createButtons() throws IOException {
        panel.setBounds(0, 0, 1024, 768);
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

    public void toggleVisible() {
        isVisible = !isVisible;
        //panel.setVisible(isVisible);

        if(isVisible) {
            frame.addComponent(panel);
        } else {
            frame.removeComponent(panel);
        }
        frame.repaint();
    }

}