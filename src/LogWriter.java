import java.io.*;
import java.util.Scanner;

public class LogWriter {
    private BufferedReader reader;
    private BufferedWriter writer;
    private File log;
    private String[][] list;
    private int maxRows, maxCols;

    public LogWriter() throws FileNotFoundException, IOException {
        log = new File("../output/log.csv");
        reader = new BufferedReader(new FileReader(log));
        writer = new BufferedWriter(new FileWriter(log, true));
        maxRows = 100;
        maxCols = 20;
        list = new String[maxRows][maxCols];
        list[0][0] = "Attendance";
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

    public void addSignIn(int id, String timeIn, String date) throws IOException {
        int column = findID(id);
        int row = findDate(date);
        list[row][column] = timeIn;
        writeToCSV();
    }

    public int findID(int id) {
        int column = -1;
        for (int j = 1; j < list[0].length; j++) {
            if (list[0][j] != null && list[0][j].equals(Integer.toString(id))) {
                column = j;
                break;
            }
        }
        return column;
    }

    public boolean isIDPresent(int id) {
        for (int j = 1; j < list[0].length; j++) {
            if (list[0][j] != null && list[0][j].equals(Integer.toString(id))) {
                return true;
            }
        }
        return false;
    }

    public int findDate(String date) {
        int row = 1;
        for (int i = list.length - 1; i >= 0; i--) {
            if (list[i][0] != null && list[i][0].equals(date)) {
                row = i;
                break;
            }
        }
        return row;
    }

    public boolean isDatePresent(String date) {
        for (int i = list.length - 1; i >= 0; i--) {
            if (list[i][0] != null && list[i][0].equals(date)) {
                return true;
            }
        }
        return false;
    }
    public void addDate(String date) {
        int row = 0;
        for (int i = list.length - 1; i >= 0; i--) {
            if(list[i][0] != null && !list[i][0].equals(" ")) {
                row = i + 2;
                break;
            }
        }
        list[row][0] = date;
    }

    public boolean isSignInPresent(String date, int id) {
        int row = findDate(date);
        int column = findID(id);
        if (list[row][column] != null && !list[row][column].equals(" ")) {
            return true;
        } else {
            return false;
        }
    }
    public boolean isSignOutPresent(String date, int id) {
        int row = findDate(date) + 1;
        int column = findID(id);
        if(list[row][column] != null && !list[row][column].equals(" ")) {
            return true;
        } else {
            return false;
        }
    }

    public void addSignOut(int id, String timeOut, String date) throws IOException {
        int column = findID(id);
        int row = findDate(date) + 1;
        list[row][column] = timeOut;
        writeToCSV();
    }

    public void addID(int id) throws IOException {
        int column = -1;
        for (int j = 0; j < list[0].length; j++) {
            if ((list[0][j] != null && list[0][j].equals(" ")) || list[0][j] == null) {
                column = j;
                break;
            }
        }
        list[0][column] = Integer.toString(id);
        writeToCSV();

    }

    public void writeToCSV() throws IOException {
        reader.close();
        writer.close();
        log.delete();
        log.createNewFile();
        reader = new BufferedReader(new FileReader(log));
        writer = new BufferedWriter(new FileWriter(log, true));

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
}