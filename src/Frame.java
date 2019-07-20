import javax.swing.*;

public class Frame {
    private JFrame frame;
    public final int width, height;

    public Frame() {
        frame = new JFrame("PunchCard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        width = 1024;
        height = 768;
        frame.setSize(width, height);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public void addComponent(JComponent c) {
        frame.add(c);
        frame.repaint();
    }
    public void removeComponent(JComponent c) {
        frame.remove(c);
        frame.repaint();
    }
    public void repaint() {
        frame.repaint();
    }
}