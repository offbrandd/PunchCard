import java.io.*;

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

    public void addTotal(String id, double hours) {
        for (int i = 1; i < list.length; i++) {
            if(list[i][0] != null && list[i][0].equals(id)) {
                list[i][1] = Double.toString(hours);
                break;
            }
            else if ((list[i][0] != null && list[i][0].equals(" ")) || list[i][0] == null) {
                list[i][0] = id;
                list[i][1] = Double.toString(hours);
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

}