import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Total {
    private String[][] list;
    private String id;
    private int logRow;

    public Total(LogWriter w) throws IOException {
        list = w.getArray();
        logRow = 0;
        /*
         * -get array from Writer -pick first column -get date get -in and out time
         * convert -to milliseconds for machine math -get difference, add it to int
         * total repeat -for every logged date write ID and total to totals.csv -repeat
         * for every id
         */
    }

    public void getTotals(TotalWriter w) throws ParseException, IOException {
        for (int j = 1; j < list[0].length; j++) {
            if (list[0][j] != null && !list[0][j].equals(" ")) {
                logRow = 0;
                String id = list[0][j];
                double hours = millisToHours(getIDTotal(j));
                w.addTotal(id, hours);
            } else {
                System.out.println("All ID totals calculated");
                break;
            }
        }
        w.writeToCSV();
    }

    public long getIDTotal(int column) throws ParseException {
        String logDate = getNextDate();
        long totalDifference = 0;
        long time1 = 0;
        long time2 = 0;
        String dateTime = "";
        while (logDate != null) {
            String time = list[logRow][column];
            if (time != null && !time.equals(" ")) {
                dateTime = logDate + " " + time;
                time1 = toMillis(dateTime);
            }
            if (time != null && !time.equals(" ")) {
                time = list[logRow + 1][column];
                dateTime = logDate + " " + time;
                time2 = toMillis(dateTime);
            }
            long difference = time2 - time1;
            logDate = getNextDate();
            totalDifference += difference;
        }
        return totalDifference;
    }

    public double millisToHours(long millis) {
        return (double) millis / 3600000; // millis in 1 hour
    }

    public long toMillis(String dateTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(dateTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getTimeInMillis();
    }

    public String getNextDate() {
        for (int i = logRow + 1; i < list.length; i++) {
            if (list[i][0] != null && !list[i][0].equals(" ")) {
                logRow = i;
                return list[i][0];
            }
        }
        return null;
    }

    public long getMillis() {
        return 1;
    }
}