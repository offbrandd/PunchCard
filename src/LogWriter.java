import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class LogWriter {
	private BufferedReader reader;
	private BufferedWriter writer;
	private File log;
	private String[][] list;
	private int maxRows, maxCols;

	public LogWriter() {
		log = new File("output/log.csv");
		try {
			//InputStream in = new FileInputStream(log);
			//reader = new BufferedReader(new InputStreamReader(in));
			//OutputStream out = new FileOutputStream(log, true);
			//writer = new BufferedWriter(new OutputStreamWriter(out));
			//writer = new BufferedWriter(new FileWriter(log, true));
			reader = new BufferedReader(new FileReader(log));
	        writer = new BufferedWriter(new FileWriter(log, true));
		} catch (IOException e) {
			e.printStackTrace();
		}
		maxRows = 100;
		maxCols = 40;
		list = new String[maxRows][maxCols];
		list[0][0] = "Attendance";
	}

	public void toArray() {
		try {
			String line = reader.readLine();
			String temp = null;
			int items = 0;
			for (int i = 0; line != null && i < 100; i++) {
				temp = line;
				items = countChar(temp, ",");
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static int countChar(String str, String c) {
		String s = str;
		int count = 0;
		for(int i = 0; i < str.length(); i++) {
			if(str.substring(i, i + 1).equals(c)) {
				count++;
			}
		}
		return count;
	}

	public String[][] getArray() {
		toArray();
		return list;
	}

	public String get(int row, int column) {
		return list[row][column];
	}

	public void addSignIn(int id, String timeIn, String date) {
		int column = findID(id);
		int row = findDate(date).get(0);
		list[row][column] = timeIn;
		writeToCSV();
	}

	public int findID(int id) {
		toArray();
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
		toArray();
		for (int j = 1; j < list[0].length; j++) {
			if (list[0][j] != null && list[0][j].equals(Integer.toString(id))) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<Integer> findDate(String date) {
		ArrayList<Integer> rows = new ArrayList<Integer>();
		for (int i = list.length - 1; i >= 0; i--) {
			if (list[i][0] != null && list[i][0].equals(date)) {
				rows.add(i);
			}
		}
		return rows;
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
			if (list[i][0] != null && !list[i][0].equals("")) {
				row = i + 2;
				break;
			}
		}
		try {
			list[row][0] = date;
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "E");

		}
	}

	public boolean isSignInPresent(String date, int id) {
		int column = findID(id);
		for (Integer i : findDate(date)) {
			int row = i;
			if (list[row][column] != null && !list[row][column].equals("")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Attemps to add an extra sign in time for a given date to the next blank slot
	 * that matches that date. Returns true if successful.
	 * 
	 * @param date   String representing date to add entry to
	 * @param id     int representing id to add entry to
	 * @param timeIn String respresenting time to enter
	 * @return boolean represents whether method successfully added the entry or not
	 */
	public boolean addExtraSignIn(String date, int id, String timeIn) {
		int column = findID(id);
		int row = -1;
		for (Integer i : findDate(date)) {
			if (list[i][column] != null && list[i][column].equals("") || list[i][column] == null) {
				row = i;
			}
		}
		if (row != -1) {
			list[row][column] = timeIn;
			writeToCSV();
			return true;
		} else {
			return false;
		}
	}

	public int getSignInRow(String date, int id) {
		int column = findID(id);
		for (Integer i : findDate(date)) {
			int row = i;
			if (list[row][column] != null && !list[row][column].equals("")) {
				return i;
			}
		}
		return -1;
	}

	public boolean isSignOutPresent(int signInRow, int id) {
		int column = findID(id);
		int row = signInRow + 1;
		if (list[row][column] != null && !list[row][column].equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public void addSignOut(int id, String timeOut, String date) {
		int column = findID(id);
		int row = 0;
		boolean ready = false;
		int i = 0;
		while(!ready) {
			row = findDate(date).get(i);
			if(list[row][column] == null || list[row][column].equals("")) {
				i++;
			} else {
				ready = true;
			}
		}
		
		list[row + 1][column] = timeOut;
		writeToCSV();
	}

	public void addID(int id) {
		int column = -1;
		for (int j = 0; j < list[0].length; j++) {
			if ((list[0][j] != null && list[0][j].equals("")) || list[0][j] == null) {
				column = j;
				break;
			}
		}
		list[0][column] = Integer.toString(id);
		writeToCSV();

	}

	public void writeToCSV() {
		try {
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
						writer.write(",");
					}
				}
				writer.write("\n");
			}
			writer.flush();
			toArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString()
	{
		String[][] a = getArray();
		String s = "{";
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < a[0].length; j++) {
				s += a[i][j] + "";
			}
			s += "\n";
		}
		return s;
	}
}