package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Appt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import java.util.Calendar;
import java.util.GregorianCalendar;

@SuppressWarnings("serial")
class AppCellRenderer extends DefaultTableCellRenderer {
	private int r;
	private int c;

	// public final static int EARLIEST_TIME = 480;
	//
	// public final static int LATEST_TIME = 1050;
	//
	// public final static int[] monthDays = { 31, 28, 31, 30, 31, 30, 31, 31,
	// 30,
	// 31, 30, 31 };

	public AppCellRenderer(Object value, boolean override, int row, int col,
			int colorCMD, Color currColor) {

		Font f1 = new Font("Helvetica", Font.ITALIC, 11);
		if (override) {
			if (row % 2 == 0)
				setBackground(new Color(153, 204, 255));
			else
				setBackground(new Color(204, 204, 255));
			setForeground(Color.black);

		}
		if (col == 2 || col == 5)
			setFont(f1);
		if (col != 0 && col != 3)
			setHorizontalAlignment(SwingConstants.LEFT);
		else
			setHorizontalAlignment(SwingConstants.RIGHT);
		if (col == 1 || col == 4) {
			if (colorCMD == AppList.COLORED_TITLE) {
				setBackground(currColor);
				setForeground(Color.black);
			} else if (colorCMD == AppList.COLORED) {
				setBackground(currColor);
				setForeground(currColor);
			}

		}
		setVerticalAlignment(SwingConstants.TOP);
	}

	public int row() {
		return r;
	}

	public int col() {
		return c;
	}
}

public class AppList extends JPanel implements ActionListener {
	public static int SMALLEST_DURATION = 15;
	private static final long serialVersionUID = 1L;
	// public static int OFFSET = 8;
	public static int OFFSET = 0;
	public static int APPLIST_SCALE = 48; // Added by Darren
	public static int ROWNUM = APPLIST_SCALE;
	public Appt[] todaylist;
	private final String[] names = { "Time", "Appointments", "Status", "Time",
			"Appointments", "Status" };
	private final int[] monthDays = { 31, 28, 31, 30, 31, 30, 31, 30, 31, 30,
			31, 30, 31 };
	private JTable tableView;
	private final Object[][] data = new Object[APPLIST_SCALE][6];
	private JPopupMenu pop;
	private int currentRow;
	private int currentCol;
	private int pressRow;
	private int pressCol;
	private int releaseRow;
	private int releaseCol;
	private CalGrid parent;
	private Color currColor = Color.green;
	private Color currColorForJoint = Color.red;
	public final static int COLORED_TITLE = 1;
	public final static int COLORED = 2;
	public final static int NOT_COLORED = 0;
	private int[][] cellCMD = new int[APPLIST_SCALE][2];
	private Color[][] cellColor = new Color[APPLIST_SCALE][2];
	public Appt selectedAppt = null;

	public AppList() {
		setLayout(new BorderLayout());
		currentRow = 0;
		currentCol = 0;

		TitledBorder b = BorderFactory
				.createTitledBorder("Appointment Contents");
		b.setTitleColor(new Color(102, 0, 51));
		Font f = new Font("Helvetica", Font.BOLD + Font.ITALIC, 11);
		b.setTitleFont(f);
		setBorder(b);
		Font f1 = new Font("Helvetica", Font.ITALIC, 11);

		pop = new JPopupMenu();
		pop.setFont(f1);

		JMenuItem mi;
		mi = (JMenuItem) pop.add(new JMenuItem("New"));

		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newAppt();
			}
		});

		mi = (JMenuItem) pop.add(new JMenuItem("Delete"));

		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		});

		mi = (JMenuItem) pop.add(new JMenuItem("Modify"));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modify();
			}
		});
		pop.add(new JPopupMenu.Separator());
		JMenuItem j = new JMenuItem("Details");
		j.setFont(f);
		pop.add(j);

		j.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getDetail();
			}
		});

		getDataArray(data);
		TableModel dataModel = prepareTableModel();
		tableView = new JTable(dataModel) {
			public TableCellRenderer getCellRenderer(int row, int col) {
				if (col == 0 || col == 3)
					return new AppCellRenderer(new Object(), true, row, col, 1,
							null);
				else if (col == 1) {

					return new AppCellRenderer(new Object(), false, row, col,
							cellCMD[row][0], cellColor[row][0]);

				} else if (col == 4) {
					return new AppCellRenderer(new Object(), false, row, col,
							cellCMD[row][1], cellColor[row][1]);
				} else
					return new AppCellRenderer(new Object(), false, row, col,
							1, null);

			}
		};

		tableView.setAutoResizeMode(tableView.AUTO_RESIZE_ALL_COLUMNS);
		tableView.setRowHeight(20); // Just GUI
		tableView.setRowSelectionAllowed(false);
		TableColumn c = null;
		c = tableView.getColumnModel().getColumn(0);
		c.setPreferredWidth(60);
		c = tableView.getColumnModel().getColumn(3);
		c.setPreferredWidth(60);
		c = tableView.getColumnModel().getColumn(1);
		c.setPreferredWidth(215);
		c = tableView.getColumnModel().getColumn(4);
		c.setPreferredWidth(215);
		c = tableView.getColumnModel().getColumn(2);
		c.setPreferredWidth(60);
		c = tableView.getColumnModel().getColumn(5);
		c.setPreferredWidth(60);
		JTableHeader h = tableView.getTableHeader();
		h.setResizingAllowed(true);
		h.setReorderingAllowed(false);

		tableView.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				pressResponse(e);
			}

//			public void mouseReleased(MouseEvent e) {
//				releaseResponse(e);
//				if (e.getButton() == 1)
//					calculateDrag(e);
//			}
		});

		JScrollPane scrollpane = new JScrollPane(tableView);
		scrollpane.setBorder(new BevelBorder(BevelBorder.LOWERED));
		scrollpane.setPreferredSize(new Dimension(695, 300));
		add(scrollpane, BorderLayout.CENTER);
		setVisible(true);
		setSize(600, 300);
		// this.
	}

	public TableModel prepareTableModel() {

		TableModel dataModel = new AbstractTableModel() {

			public int getColumnCount() {
				return names.length;
			}

			public int getRowCount() {
				return APPLIST_SCALE;
			}

			public Object getValueAt(int row, int col) {
				return data[row][col];
			}

			public String getColumnName(int column) {
				return names[column];
			}

			public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}

			public boolean isCellEditable(int row, int col) {
				return false;
			}

			public void setValueAt(Object aValue, int row, int column) {
				data[row][column] = aValue;
			}
		};
		return dataModel;
	}

	public void getDataArray(Object[][] data) {
		// int tam = 480;
		// int tpm = 60;
		int tam = 0;
		int tpm = 0;
		String s;
		String am = new String("AM");
		String pm = new String("PM");

		int i;
		for (i = 0; i < ROWNUM; i++) {
			if (tam /60 == 0) {
				data[i][0] = "12:";
			} else {
				data[i][0] = (tam / 60) + ":";
			}
			if (tam % 60 == 0)
				data[i][0] = data[i][0] + "00" + am;
			else
				data[i][0]= data[i][0].toString() + (tam % 60) + am;
			tam = tam + SMALLEST_DURATION;
			cellCMD[i][0] = NOT_COLORED;
			cellCMD[i][1] = NOT_COLORED;
			cellColor[i][0] = Color.white;
			cellColor[i][1] = Color.white;
		}
		for (i = 0; i < ROWNUM; i++) {
			if (tpm /60 == 0) {
				data[i][3] = "12:";
			} else {
				data[i][3] = (tpm / 60) + ":";
			}
			if (tpm % 60 == 0)
				data[i][3] = data[i][3] + "00" + pm;
			else
				data[i][3] = data[i][3].toString() + (tpm % 60) + pm;
			tpm = tpm + SMALLEST_DURATION;
		}

	}

	// clear the appointment list on the gui
	public void clear() {
		for (int i = 0; i < APPLIST_SCALE; i++) {
			setTextAt(" ", i, 1);

			setTextAt(" ", i, 4);

			cellCMD[i][0] = NOT_COLORED;
			cellCMD[i][1] = NOT_COLORED;
			cellColor[i][0] = Color.white;
			cellColor[i][1] = Color.white;
		}
	}

	public void setTextAt(String text, int row, int col) {
		TableModel t = tableView.getModel();
		t.setValueAt(text, row, col);
	}

	public String getTextAt(int row, int col) {
		TableModel t = tableView.getModel();
		return (String) t.getValueAt(row, col);
	}

	public String getCurrentText() {
		TableModel t = tableView.getModel();
		return (String) t.getValueAt(currentRow, currentCol);
	}

	public void setTodayAppt(Appt[] list) {
		if (list == null)
			return;
		for (int i = 0; i < list.length; i++)
			addAppt(list[i]);
		repaint();

	}

	// coloring the appointment list
	public void addAppt(Appt appt) {
		if (appt == null)
			return;

		Timestamp start = appt.getTimeSpan().getStartTime();
		Timestamp end = appt.getTimeSpan().getEndTime();
		int startMin = start.getHours() * 60 + start.getMinutes();
		int endMin = end.getHours() * 60 + end.getMinutes();

		// startMin = startMin - OFFSET * 60; endMin = endMin - OFFSET * 60;
		// System.out.println(start + " " + end);

		if (start.getDate() == end.getDate()) {
			color_the_tiles(startMin, endMin, appt);
		} else {
			// Special case for overnight event
			// After some research, no need to compare the date because it has
			// been done before this function
			// Just need to check if they're equal or not
			if ((start.getDay() != parent.currentWD)
					&& (start.getDate() != parent.currentD)) {
				// System.out.println("-" + start.getDay() + " " +
				// parent.currentWD);
				color_the_tiles(0, endMin, appt);
				if ((end.getDay() == parent.currentWD)
						&& (end.getDate() != parent.currentD)
						&& (appt.getFrequency() == "Daily")) {
					color_the_tiles(startMin, 24 * 60, appt); // Special case
																// for daily
																// event
				}
			}
			if ((end.getDay() != parent.currentWD)
					&& (end.getDate() != parent.currentD)) {
				// System.out.println("-" + end.getDay() + " " +
				// parent.currentWD);
				color_the_tiles(startMin, 24 * 60, appt);
				if ((start.getDay() != parent.currentWD)
						&& (start.getDate() != parent.currentD)
						&& (appt.getFrequency() == "Daily")) {
					color_the_tiles(0, endMin, appt); // Special case for daily
														// event
				}
			}
		}

		// if (currColor.equals(Color.yellow))
		// currColor = Color.pink;
		// else
		// currColor = Color.yellow;
	}

	private void color_the_tiles(int start, int end, Appt appt) {
		int[] pos = new int[2];
		Color color;
		currColor = new Color((appt.getTimeSpan().getStartTime().getHours()) * 10,
				240 - (appt.getTimeSpan().getStartTime().getHours()) * 8, 255 - (appt
						.getTimeSpan().getStartTime().getMinutes() * 3));
		currColorForJoint = new Color(
				255 - (appt.getTimeSpan().getStartTime().getHours()) * 8, (appt
						.getTimeSpan().getStartTime().getHours()) * 10, 190 - (appt
						.getTimeSpan().getStartTime().getMinutes() * 3));

		if (!appt.isJoint())
			color = currColor;
		else
			color = currColorForJoint;

		for (int i = start; i < end; i = i + SMALLEST_DURATION) {
			pos = calRowColNum(i);
			// System.out.println(pos[0] + " " + pos[1]);
			if (i == start) {
				tableView.getModel().setValueAt(appt, pos[0], pos[1]);

				if (pos[1] == 1) {
					cellCMD[pos[0]][0] = COLORED_TITLE;
					cellColor[pos[0]][0] = color;
				} else {
					cellCMD[pos[0]][1] = COLORED_TITLE;
					cellColor[pos[0]][1] = color;
				}
			} else {
				tableView.getModel().setValueAt(appt, pos[0], pos[1]);

				if (pos[1] == 1) {
					cellCMD[pos[0]][0] = COLORED;
					cellColor[pos[0]][0] = color;
				} else {
					cellCMD[pos[0]][1] = COLORED;
					cellColor[pos[0]][1] = color;
				}
			}
		}
	}

	public int[] calRowColNum(int total) {
		int[] position = new int[2];
		position[0] = total / SMALLEST_DURATION;

		if (position[0] > (ROWNUM - 1)) {
			position[0] = position[0] - ROWNUM;
			position[1] = 4;
		} else
			position[1] = 1;
		if (position[1] == 4 && position[0] > ROWNUM - 1)
			position[0] = ROWNUM - 1;
		return position;
	}

	private void getDetail() {

		Appt apptTitle = getSelectedAppTitle();
		if (apptTitle == null)
			return;

		Calendar now=new GregorianCalendar();
		now.set(parent.currentY, parent.currentM - 1, parent.currentD, 0, 0, 0);
		DetailsDialog info = new DetailsDialog(apptTitle, "Appointment Details",now);

		info.setVisible(true);

	}

	private void delete() {
		Appt apptTitle = getSelectedAppTitle();
		if (apptTitle == null)
			return;
		
		// ADDED by Darren: You must be administrator or the initiator of the event !
		if ((!parent.getCurrUser().getType().equals("Administrator")) && 
				(!apptTitle.getAttendList().getFirst().equals(parent.getCurrUser().getID()))){
			JOptionPane.showMessageDialog(parent,
					"You must be administrator or the initiator of the event !", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;			
		}
		
		// System.out.println("Requested: "+apptTitle.TimeSpan().StartTime());
		if ((apptTitle.getTimeSpan().getStartTime().getTime() <= parent.getCurrentTime()
				.getTime()) && (apptTitle.getFrequency().equals("Once"))) {
			JOptionPane.showMessageDialog(parent,
					"You cannot modify or delete events that are in the past !", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		parent.controller.removeAppt(apptTitle);
		parent.updateCal();
		parent.updateAppList();
	}

	private void modify() {
		Appt apptTitle = getSelectedAppTitle();
		if (apptTitle == null)
			return;

		// ADDED by Darren: You must be administrator or the initiator of the event !
		if ((!parent.getCurrUser().getType().equals("Administrator")) && 
				(!apptTitle.getAttendList().getFirst().equals(parent.getCurrUser().getID()))){
			JOptionPane.showMessageDialog(parent,
					"You must be administrator or the initiator of the event !", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;			
		}
		// System.out.println("Requested: "+apptTitle.TimeSpan().StartTime());
		if ((apptTitle.getTimeSpan().getStartTime().getTime() <= parent.getCurrentTime()
				.getTime()) && (apptTitle.getFrequency().equals("Once"))) {
			JOptionPane.showMessageDialog(parent,
					"You cannot modify or delete events that are in the past !", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		AppScheduler setAppDial = new AppScheduler("Modify", parent,
				apptTitle.getID());
		setAppDial.updateSetApp(apptTitle);
		setAppDial.show();
		setAppDial.setResizable(false);
		// parent.controller.RemoveAppt(apptTitle);
		parent.updateCal();
		parent.updateAppList();
	}

	public Appt getSelectedAppTitle() {

		Object apptTitle;
		if (currentRow < 0 || currentRow > ROWNUM - 1) {
			JOptionPane.showMessageDialog(parent, "Please Select Again !",
					"Error", JOptionPane.ERROR_MESSAGE);

			selectedAppt = null;
			return selectedAppt;
		}
		if (currentCol < 3) {
			apptTitle = tableView.getModel().getValueAt(currentRow, 1);
		} else
			apptTitle = tableView.getModel().getValueAt(currentRow, 4);

		if (apptTitle instanceof Appt) {
			selectedAppt = (Appt) apptTitle;
			return selectedAppt;
		} else {
			selectedAppt = null;
			return selectedAppt;
		}

	}

	private void newAppt() {

		if (parent.mCurrUser == null)
			return;
		if (currentRow < 0 || currentRow > ROWNUM - 1) {
			JOptionPane.showMessageDialog(parent, "Please Select Again !",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int startTime;

		if (currentCol < 3)
			startTime = currentRow * 15 + OFFSET * 60;
		// startTime = currentRow * 15;
		else
			startTime = (currentRow + APPLIST_SCALE) * 15 + OFFSET * 60;
		// startTime = (currentRow + APPLIST_SCALE) * 15;
		AppScheduler a = new AppScheduler("New", parent);
		a.updateSetApp(hkust.cse.calendar.gui.Utility.createDefaultAppt(
				parent.currentY, parent.currentM, parent.currentD,
				parent.mCurrUser, startTime));
		a.setLocationRelativeTo(null);
		a.show();

	}

	private void pressResponse(MouseEvent e) {
//		tempe = e;
		currentRow=tableView.rowAtPoint(e.getPoint());
		currentCol=tableView.columnAtPoint(e.getPoint());
//		pressRow = tableView.getSelectedRow();
//		pressCol = tableView.getSelectedColumn();
		if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
			pop.show(e.getComponent(), e.getX(), e.getY());
	}

//	private void releaseResponse(MouseEvent e) {
//
//		releaseRow = tableView.getSelectedRow();
//		releaseCol = tableView.getSelectedColumn();
//		if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
//			pop.show(e.getComponent(), e.getX(), e.getY());
//	}
//
//	private void calculateDrag(MouseEvent e) {
//
//		if (releaseRow == pressRow) {
//			currentRow = tableView.getSelectedRow()
//					+ tableView.getSelectedRowCount() - 1;
//		} else {
//			currentRow = releaseRow;
//
//		}
//
//		if (releaseCol == pressCol) {
//			currentCol = tableView.getSelectedColumn()
//					+ tableView.getSelectedColumnCount() - 1;
//		} else {
//			currentCol = releaseCol;
//		}
//
//	}

	public void setParent(CalGrid grid) {
		parent = grid;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == tableView) {
			pop.show(tableView, currentRow * APPLIST_SCALE, currentRow
					* APPLIST_SCALE);

		}

	}

}
