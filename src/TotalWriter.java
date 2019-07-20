import java.awt.Dimension;
import java.io.*;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class TotalWriter {
    private File totals;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String[][] list;
    private int maxRows, maxCols;

    public TotalWriter() throws IOException {
        totals = new File("../output/totals.csv");
        reader = new BufferedReader(new FileReader(totals));
        writer = new BufferedWriter(new FileWriter(totals, true));
        maxRows = 20;
        maxCols = 3;
        list = new String[maxRows][maxCols];
        list[0][0] = "Totals";
        toArray();
    }

    public void toArray() throws IOException {
        String line = reader.readLine();
        String temp = null;
        int items = 0;
        for (int i = 0; line != null && i < 100; i++) {
            temp = line;
            items = temp.length() - (temp.replace(",", "").length());
            for (int j = 0; j < items + 1; j++) {
                if (line.indexOf(",") != -1) {
                    list[i][j] = line.substring(0, line.indexOf(","));
                    line = line.substring(line.indexOf(",") + 1);
                } else if (!line.equals("")) {
                    list[i][j] = line;
                    line = "";
                }
            }
            line = reader.readLine();
        }
    }

    public String[][] getArray() throws IOException {
        toArray();
        return list;
    }

    public void addID(int id) {
        for (int i = 0; i < list.length; i++) {
            if ((list[i][0] != null && list[i][0].equals(" ")) || list[i][0] == null) {
                list[i][0] = Integer.toString(id);
                return;
            }
        }
    }

    public void addName(int id, String name, Scanner scanner) {
        list[findID(id)][1] = name;
    }

    public boolean isNamePresent(String name) {
        for (int i = 1; i < list.length; i++) {
            if (list[i][1] != null && list[i][1].equals(name)) {
                return true;
            }
        }
        return false;
    }
    public boolean isIDPresent(int id) {
        for (int i = 1; i < list.length; i++) {
            if (list[i][0] != null && list[i][0].equals(Integer.toString(id))) {
                return true;
            }
        }
        return false;
    }

    public boolean requestDuplicateName(Scanner scanner) {
        String response = scanner.next();
        if (response.equals("y")) {
            System.out.println("Duplicate name added.");
            return true;
        } else if (response.equals("n")) {
            System.out.println("Registration cancelled");
            return false;
        } else {
            if (response.equals("close")) {
                System.exit(0);
            }
            System.out.println("Invalid response, please try again (y for yes, n for no)");
            return requestDuplicateName(scanner);
        }
    }

    public int findID(int id) {
        for (int i = 0; i < list.length; i++) {
            if (list[i][0] != null && list[i][0].equals(Integer.toString(id))) {
                return i;
            }
        }
        return -1;
    }

    public void addTotal(String id, double hours) {
        for (int i = 1; i < list.length; i++) {
            if (list[i][0] != null && list[i][0].equals(id)) {
                list[i][2] = Double.toString(hours);
                break;
            } else if ((list[i][0] != null && list[i][0].equals(" ")) || list[i][0] == null) {
                System.out
                        .println("ID " + id + " was not used for proper profile creation. Please notify administrator");
                list[i][0] = id;
                list[i][2] = Double.toString(hours);
                break;
            }
        }
    }

    public void writeToCSV() throws IOException {
        reader.close();
        writer.close();
        totals.delete();
        totals.createNewFile();
        writer = new BufferedWriter(new FileWriter(totals, true));
        reader = new BufferedReader(new FileReader(totals));

        for (int i = 0; i < list.length; i++) {
            for (int j = 0; j < list[0].length; j++) {
                if (list[i][j] != null) {
                    writer.write(list[i][j] + ",");
                } else {
                    writer.write(" ,");
                }
            }
            writer.write("\n");
        }
        writer.flush();
    }
    public void closingMessage() {
        String path = totals.getAbsolutePath();
        String temp = path;
        path = temp.substring(0, temp.indexOf("src"));
        path += temp.substring(temp.indexOf("..") + 3); //removes the "src/../" from the absolute path
        JOptionPane.showMessageDialog(null, "Total hours successfully exported to: " + path);
    }

}