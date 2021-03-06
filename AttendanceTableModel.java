package gui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import gui.AttendanceTable.EventTableModel;
import model.StudentNameModel;
import model_for_gui.AttendanceModel;

public class AttendanceTableModel extends AbstractTableModel {
	public static final int CLIENT_ID_COLUMN = 0;
	public static final int STUDENT_NAME_COLUMN = 1;
	public static final int STUDENT_AGE_COLUMN = 2;
	public static final int GITHUB_COMMENTS_COLUMN = 3;
	public static final int GITHUB_NAME_COLUMN = 4; // Not actually a column
	private static final int TABLE_NUM_COLUMNS = 5;

	private Object[][] tableObjects;

	// 3rd column represents a sub-table with multiple sub-columns: MAC left-shifts,
	// PC centers title, so added spaces to make it look good on both platforms!
	private final String[] colNames = { " ID ", " Student Name ", " Age ",
			" Class Date        Class Name                    Teacher Name(s)                        "
					+ "Repository Name                         Github Comments " };

	public AttendanceTableModel(ArrayList<AttendanceModel> attendance) {
		initializeTableData(attendance);
	}

	public void setData(ArrayList<AttendanceModel> db) {
		initializeTableData(db);
	}

	private void initializeTableData(ArrayList<AttendanceModel> db) {
		tableObjects = new Object[db.size()][TABLE_NUM_COLUMNS];

		for (int row = 0; row < db.size(); row++) {
			tableObjects[row][CLIENT_ID_COLUMN] = String.valueOf(db.get(row).getClientID());
			StudentNameModel name = new StudentNameModel(db.get(row).getStudentName().getFirstName(), 
					db.get(row).getStudentName().getLastName() + db.get(row).getCurrentLevel(), db.get(row).getStudentName().getIsInMasterDb());
			tableObjects[row][STUDENT_NAME_COLUMN] = name;
			if (db.get(row).getAge() == 0)
				tableObjects[row][STUDENT_AGE_COLUMN] = "";
			else
				tableObjects[row][STUDENT_AGE_COLUMN] = db.get(row).getAge().toString().substring(0, 4);
			tableObjects[row][GITHUB_COMMENTS_COLUMN] = db.get(row).getAttendanceEventList().toArray();

			// Github name is not actually a column in table, just a placeholder
			if (!db.get(row).getStudentName().getIsInMasterDb())
				tableObjects[row][GITHUB_NAME_COLUMN] = "";
			else if (db.get(row).getGithubName() == null || db.get(row).getGithubName().equals(""))
				tableObjects[row][GITHUB_NAME_COLUMN] = null;
			else
				tableObjects[row][GITHUB_NAME_COLUMN] = db.get(row).getGithubName();
		}
	}

	public void removeAll() {
		for (int i = 0; i < tableObjects.length; i++) {
			for (int j = 0; j < TABLE_NUM_COLUMNS; j++) {
				tableObjects[i][j] = null;
			}
		}
		tableObjects = null;
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}

	@Override
	public int getRowCount() {
		if (tableObjects == null)
			return 0;
		else
			return tableObjects.length;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (tableObjects == null || tableObjects.length == 0)
			return Object.class;
		else if (columnIndex == GITHUB_COMMENTS_COLUMN)
			return EventTableModel.class;
		else
			return tableObjects[0][columnIndex].getClass();
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	@Override
	public Object getValueAt(int row, int col) {
		return tableObjects[row][col];
	}

	public String getGithubNameByRow(int row) {
		return (String) tableObjects[row][GITHUB_NAME_COLUMN];
	}
}
