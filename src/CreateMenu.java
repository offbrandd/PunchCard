import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CreateMenu {
    private Frame frame;
    private JPanel panel;
    private JButton confirm;
    private JTextField idField, nameField;
    private JLabel idLabel, nameLabel;
    private ArrayList<JComponent> components;
    private boolean isVisible;

    public CreateMenu(Frame frame) {
        this.frame = frame;
        panel = new JPanel();
        confirm = new JButton("Confirm");
        idField = new JTextField();
        nameField = new JTextField();
        idLabel = new JLabel("Enter ID");
        nameLabel = new JLabel("Enter Name");
        components = new ArrayList<JComponent>();
        components.add(confirm);
        components.add(idField);
        components.add(idLabel);
        components.add(nameField);
        components.add(nameLabel);
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
        idField.setBounds(500, 500, 200, 25);
        idLabel.setBounds(500, 470, 100, 30);
        nameField.setBounds(500, 425, 200, 25);
        nameLabel.setBounds(500, 395, 100, 30);
        addComponents();
        addActions();
    }
    private void addActions() {
        confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText();
                    int id = Integer.parseInt(idField.getText());
                    boolean idPresent = false;
                    if (Main.totalWriter.isIDPresent(id)) {
                        idPresent = true;
                        notifyDuplicateID();
                    }
                    boolean writeName = true;
                    if (Main.totalWriter.isNamePresent(name)) {
                        System.out.println("Name is Present");
                        writeName = requestDuplicateName();
                    }
                    if (writeName && !idPresent) {
                        Main.create(name, id);
                        finishCreation();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "ID may only contain numbers.");
                }

            }
        });
    }

    private void notifyDuplicateID() {
        JOptionPane.showMessageDialog(null, "ID is already registered. Please sign in or try another ID.");
    }

    private boolean requestDuplicateName() {
        int n = 0;
        n = JOptionPane.showConfirmDialog(null, "That name is already registered to another id. Continue anyways?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (n == 0) {
            return true;
        } else {
            return false;
        }
    }
    private void finishCreation() {
        JOptionPane.showMessageDialog(null, "Profile successfully created. You may now sign in");
        toggleVisible();
        Main.mainMenu();


    }

}