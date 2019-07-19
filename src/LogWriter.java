import java.io.*;
import java.util.ArrayList;
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

    public void addSignIn(int id, String timeIn, String date, Scanner scanner) throws IOException {
        int column = findID(id);
        int row = findDate(date);
        if (column == -1) {
            return;
        }
        if (list[row][column] != null && !list[row][column].equals(" ")) {
            System.out.println("A Sign in time for this date and ID has already been logged. Add another? (y/n)");
            if (requestAdditional(scanner)) {
                list[row + 2][column] = timeIn;
                list[row + 2][0] = date;
            }
        } else {
            list[row][column] = timeIn;
        }
        writeToCSV();
    }

    public boolean requestAdditional(Scanner scanner) {
        String response = scanner.next();
        if (response.equals("y")) {
            System.out.println("Additional entry added.");
            return true;
        } else if (response.equals("n")) {
            System.out.println("Entry cancelled");
            return false;
        } else {
            if (response.equals("close")) {
                System.exit(0);
            }
            System.out.println("Invalid response, please try again ( y for yes, n for no)");
            return requestAdditional(scanner);
        }
    }

    public int findID(int id) {
        int column = -1;
        boolean idPresent = false;
        for (int j = 1; j < list[0].length; j++) {
            if (list[0][j] != null && list[0][j].equals(Integer.toString(id))) {
                column = j;
                idPresent = true;
                break;
            } else if ((list[0][j] != null && list[0][j].equals(" ")) || list[0][j] == null) {
                System.out.println("ID not registered. Please create a profile or try another ID");
                break;
            }
        }
        return column;
    }

    public int findDate(String date) {
        int row = 1;
        System.out.println(date);
        for (int i = list.length - 1; i >= 0; i--) {
            if (list[i][0] != null && !list[i][0].equals(" ") && !list[i][0].equals(date)) {
                System.out.println("Found string != date");
                row = i + 2;
                list[row][0] = date;
                break;
            } else if(list[i][0] != null && list[i][0].equals(date)) {
                System.out.println("Found date");
                row = i;
                break;
            }
        }
        //list[row][0] = date;
        return row;
    }

    public void addSignOut(int id, String timeOut, String date, Scanner scanner) throws IOException {
        int column = findID(id);
        int row = findDate(date);
        if (column == -1)
            return;

        if (list[row][column] != null && !list[row][column].equals(" ")) {
            if (list[row + 1][column] != null && !list[row + 1][column].equals(" ")) {
                System.out.println("ID has already been signed out. Would you like to override the entry? (y/n)");
                if (requestAdditional(scanner)) {
                    list[row + 1][column] = timeOut;
                }
            } else {
                list[row + 1][column] = timeOut;
                System.out.println(list[row + 1][column]);
                System.out.println("ID successfully signed out.");
            }
        } else {
            list[row + 1][column] = timeOut;
            System.out.println(
                    "ID was not signed in. Sign out time will be logged. See Administrator with your sign in time for manual entry.");

        }
        writeToCSV();
    }

    public boolean addID(int id) throws IOException {
        int column = -1;
        boolean idPresent = false;
        for (int j = 0; j < list[0].length; j++) {
            if (list[0][j] != null && list[0][j].equals(Integer.toString(id))) {
                idPresent = true;
                break;
            } else if ((list[0][j] != null && list[0][j].equals(" ")) || list[0][j] == null) {
                column = j;
                break;
            }
        }
        if (idPresent) {
            return false;
        } else {
            list[0][column] = Integer.toString(id);
            writeToCSV();
            return true;
        }
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