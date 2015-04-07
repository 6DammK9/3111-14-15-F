/**
 *  Unless specified, it's added by Darren or from the base code
 */

package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorage;
import hkust.cse.calendar.apptstorage.LocationStorage;
import hkust.cse.calendar.apptstorage.NotificationStorage;
import hkust.cse.calendar.apptstorage.UserStorage;
import hkust.cse.calendar.gui.deleteDialogs.NotifyUserLocationDeleteDialogFactory;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Time;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.metal.MetalBorders.Flush3DBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class CalGrid extends JFrame implements ActionListener {

	// private User mNewUser;
	private static final long serialVersionUID = 1L;
	private static final boolean NOT_SELECTED = false; //Added by Darren
	private static final boolean SELECTED = true; //Added by Darren
	public ApptStorage controller;
	public UserStorage controller2;
	public LoginDialog caller;
	public LocationStorage locationStorage;
	public User mCurrUser;
	private String mCurrTitle = "Desktop Calendar - No User - "; //Added by Darren
	private GregorianCalendar today;
	public int currentD;
	public int currentWD;
	public int currentM;
	public int currentY;
	public int previousRow;
	public int previousCol;
	public int currentRow;
	public int currentCol;
	private BasicArrowButton eButton;
	private BasicArrowButton wButton;
	private JLabel year;
	private JComboBox month;

	private final Object[][] data = new Object[6][7];
	// private final Vector[][] apptMarker = new Vector[6][7];
	private final String[] names = { "Sunday", "Monday", "Tuesday",
			"Wednesday", "Thursday", "Friday", "Saturday" };
	private final String[] months = { "January", "Feburary", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December" };
	private JTable tableView;
	private AppList applist;
	public static final int[] monthDays = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
			31, 30, 31 };
	private JTextPane note;

	private JSplitPane upper;
	private JSplitPane whole;
	private JScrollPane scrollpane;
	private StyledDocument mem_doc = null;
	private SimpleAttributeSet sab = null;
	// private boolean isLogin = false;
	private JMenu Appmenu = new JMenu("Extra dialogs");;

	private final String[] holidays = {
			"New Years Day\nSpring Festival\n",
			"President's Day (US)\n",
			"",
			"Ching Ming Festival\nGood Friday\nThe day following Good Friday\nEaster Monday\n",
			"Labour Day\nThe Buddha���s Birthday\nTuen Ng Festival\n",
			"",
			"Hong Kong Special Administrative Region Establishment Day\n",
			"Civic Holiday(CAN)\n",
			"",
			"National Day\nChinese Mid-Autumn Festival\nChung Yeung Festival\nThanksgiving Day\n",
			"Veterans Day(US)\nThanksgiving Day(US)\n", "Christmas\n" };

	private AppScheduler setAppDial;

	public CalGrid(ApptStorage con, LoginDialog called) {
		super();
		locationStorage=con.locationStorage;
		Time.setApptStorage(con);
				
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				controller.dumpStorageToFile();
				controller.clearPendingReminders();
				dispose();
				caller.setVisible(true);
				return;
			}
		});

		controller = con;
		controller2 = con.getUserStorage();
		caller = called;
		mCurrUser = null;

		previousRow = 0;
		previousCol = 0;
		currentRow = 0;
		currentCol = 0;

		applist = new AppList();
		applist.setParent(this);

		setJMenuBar(createMenuBar());

		today = Time.getTime();
		// today = new GregorianCalendar(1997,9,1); //Start from 1 Oct, 1997
		currentY = today.get(Calendar.YEAR);
		currentD = today.get(Calendar.DAY_OF_MONTH);
		currentWD = today.get(Calendar.DAY_OF_WEEK);
		int temp = today.get(Calendar.MONTH) + 1;
		currentM = 12;

		getDateArray(data);

		JPanel leftP = new JPanel();
		leftP.setLayout(new BorderLayout());
		leftP.setPreferredSize(new Dimension(500, 300));

		JLabel textL = new JLabel("Important Days");
		textL.setForeground(Color.red);

		note = new JTextPane();
		note.setEditable(false);
		note.setBorder(new Flush3DBorder());
		mem_doc = note.getStyledDocument();
		sab = new SimpleAttributeSet();
		StyleConstants.setBold(sab, true);
		StyleConstants.setFontSize(sab, 30);

		JPanel noteP = new JPanel();
		noteP.setLayout(new BorderLayout());
		noteP.add(textL, BorderLayout.NORTH);
		noteP.add(note, BorderLayout.CENTER);

		leftP.add(noteP, BorderLayout.CENTER);

		eButton = new BasicArrowButton(SwingConstants.EAST);
		eButton.setEnabled(true);
		eButton.addActionListener(this);
		wButton = new BasicArrowButton(SwingConstants.WEST);
		wButton.setEnabled(true);
		wButton.addActionListener(this);

		year = new JLabel(new Integer(currentY).toString());
		month = new JComboBox();
		month.addActionListener(this);
		month.setPreferredSize(new Dimension(200, 30));
		for (int cnt = 0; cnt < 12; cnt++)
			month.addItem(months[cnt]);
		month.setSelectedIndex(temp - 1);

		JPanel yearGroup = new JPanel();
		yearGroup.setLayout(new FlowLayout());
		yearGroup.setBorder(new Flush3DBorder());
		yearGroup.add(wButton);
		yearGroup.add(year);
		yearGroup.add(eButton);
		yearGroup.add(month);

		leftP.add(yearGroup, BorderLayout.NORTH);

		TableModel dataModel = prepareTableModel();

		// Modified by Darren
		tableView = new JTable(dataModel) {
			public TableCellRenderer getCellRenderer(int row, int col) {
				String tem = (String) data[row][col];
				// Collection<int> selectedRows
				// tableView.getSelectedRows()

				if (tem.equals("") == false) {
					try {
						Timestamp start = new Timestamp(currentY - 1900,
								currentM - 1,
								Integer.parseInt(tem.substring(0)), 0, 0, 0, 0);
						Timestamp end = new Timestamp(currentY - 1900,
								currentM - 1,
								Integer.parseInt(tem.substring(0)), 23, 59, 0,
								0);
						// System.out.println(start); System.out.println(end);
						int get_day_appt = controller.retrieveUserApptsDuring(mCurrUser, 0,
								new TimeSpan(start, end), false, null).length;
						// if (tem.length() > 1) {
						if (today.get(Calendar.YEAR) == currentY
								&& today.get(Calendar.MONTH) + 1 == currentM
								&& today.get(Calendar.DAY_OF_MONTH) == Integer
								// .parseInt(tem.substring(1))) {
										.parseInt(tem.substring(0))) {
							// if (tem.charAt(0) == 'Y')
							// if (((row == previousRow) && (col ==
							// previousCol)) || ((row == currentRow) && (col ==
							// currentCol)))
							// if ((row == currentRow) && (col == currentCol))
							return new CalCellRenderer(today, get_day_appt);
						}
						// else {return new CalCellRenderer(today,
						// NOT_SELECTED);}

						// }
						// if (tem.charAt(0) == 'Y')
						// if (((row == previousRow) && (col == previousCol)) ||
						// ((row == currentRow) && (col == currentCol)))
						// if ((row == currentRow) && (col == currentCol))
						return new CalCellRenderer(null, get_day_appt);
						// else return new CalCellRenderer(null, NOT_SELECTED);
					} catch (Throwable e) {
						System.out.println(row + " " + col + " " + tem);
						System.exit(1);
					}

				}
				return new CalCellRenderer(null, 0);
				// return new CalCellRenderer(null, NOT_SELECTED);
				/**
				 * if (tem.equals("N") == false) { try { if (tem.length() > 1) {
				 * if (today.get(Calendar.YEAR) == currentY &&
				 * today.get(Calendar.MONTH) + 1 == currentM &&
				 * today.get(Calendar.DAY_OF_MONTH) == Integer
				 * .parseInt(tem.substring(1))) { if (tem.charAt(0) == 'Y')
				 * return new CalCellRenderer(today, SELECTED); else {return new
				 * CalCellRenderer(today, NOT_SELECTED);} } } if (tem.charAt(0)
				 * == 'Y') return new CalCellRenderer(null, SELECTED); else
				 * return new CalCellRenderer(null, NOT_SELECTED); } catch
				 * (Throwable e) { System.out.println(row + " " + col + " " +
				 * tem); System.exit(1); }
				 * 
				 * } return new CalCellRenderer(null, NOT_SELECTED);
				 **/
			}
		};

		tableView.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tableView.setRowHeight(40);
		tableView.setRowSelectionAllowed(false);
		tableView.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				mousePressResponse();
			}

			public void mouseReleased(MouseEvent e) {
				mouseResponse();
			}
		});

		JTableHeader head = tableView.getTableHeader();
		head.setReorderingAllowed(false);
		head.setResizingAllowed(true);

		scrollpane = new JScrollPane(tableView);
		scrollpane.setBorder(new BevelBorder(BevelBorder.RAISED));
		scrollpane.setPreferredSize(new Dimension(536, 260));

		upper = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftP, scrollpane);

		whole = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upper, applist);
		getContentPane().add(whole);

		initializeSystem(); // for you to add.
		// mCurrUser = getCurrUser(); // totally meaningless code
		Appmenu.setEnabled(true);

		updateCal();
		pack(); // sized the window to a preferred size
		setVisible(true); // set the window to be visible
		con.notificationStorage = NotificationStorage.notificationsFactory();
		if (con.defaultUser.getType().equals("Administrator")) {
			con.notificationStorage.notifyAdminLocationsBeingDeleted(this,con);
			con.notificationStorage.notifyAdminUserBeingDeleted(this, con);
		}
		con.notificationStorage.notifyUserBeingDeleted(this,con.defaultUser);
		con.notificationStorage.notifyUserLocationsBeingDeleted(con.defaultUser,con);
	}

	public TableModel prepareTableModel() {

		TableModel dataModel = new AbstractTableModel() {

			public int getColumnCount() {
				return names.length;
			}

			public int getRowCount() {
				return 6;
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
				System.out.println("Setting value to: " + aValue);
				data[row][column] = aValue;
			}
		};
		return dataModel;
	}

	public void getDateArray(Object[][] data) {
		GregorianCalendar c = new GregorianCalendar(currentY, currentM - 1, 1);
		int day;
		int date;
		Date d = c.getTime();
		c.setTime(d);
		day = d.getDay();
		date = d.getDate();

		if (c.isLeapYear(currentY)) {

			monthDays[1] = 29;
		} else
			monthDays[1] = 28;

		int temp = day - date % 7;
		if (temp > 0)
			day = temp + 1;
		else if (temp < 0)
			day = temp + 1 + 7;
		else
			day = date % 7;
		day %= 7;
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 7; j++) {
				int temp1 = i * 7 + j - day + 1;
				if (temp1 > 0 && temp1 <= monthDays[currentM - 1]) {
					// data[i][j] = new String("N" + Integer.toString(temp1));
					data[i][j] = new String(Integer.toString(temp1));
				} else
					// data[i][j] = new String("N");
					data[i][j] = new String("");
			}

	}

	JMenuBar createMenuBar() {

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("Manual Scheduling")) {
					AppScheduler a = new AppScheduler("New", CalGrid.this);
					a.updateSetApp(hkust.cse.calendar.gui.Utility
							.createDefaultAppt(currentY, currentM, currentD,
									mCurrUser));
					a.setLocationRelativeTo(null);
					a.show();
					TableModel t = prepareTableModel();
					tableView.setModel(t);
					tableView.repaint();
				}

			}
		};
		JMenuBar menuBar = new JMenuBar();
		menuBar.getAccessibleContext().setAccessibleName("Calendar Choices");
		JMenuItem mi;

		JMenu Access = (JMenu) menuBar.add(new JMenu("Access"));
		Access.setMnemonic('A');
		Access.getAccessibleContext().setAccessibleDescription(
				"Account Access Management");

		mi = (JMenuItem) Access.add(new JMenuItem("Logout")); // adding a Logout
																// menu button
																// for user to
																// logout
		mi.setMnemonic('L');
		mi.getAccessibleContext().setAccessibleDescription("For user logout");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(null, "Logout?",
						"Comfirm", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					controller.dumpStorageToFile();
					controller.clearPendingReminders();
					// System.out.println("closed");
					dispose();
					caller.setVisible(true);
					// CalendarMain.logOut = true;
					return; // return to CalendarMain()
				}
			}
		});

		mi = (JMenuItem) Access.add(new JMenuItem("Exit"));
		mi.setMnemonic('E');
		mi.getAccessibleContext().setAccessibleDescription("Exit Program");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(null, "Exit Program ?",
						"Comfirm", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION)
					System.exit(0);

			}
		});

		menuBar.add(Appmenu);
		Appmenu.setEnabled(false);
		Appmenu.setMnemonic('p');
		Appmenu.getAccessibleContext().setAccessibleDescription(
				"Appointment Management");
		mi = new JMenuItem("Manual Scheduling");
		mi.addActionListener(listener);
		Appmenu.add(mi);

		mi = new JMenuItem("Edit user");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (controller.defaultUser.getType().equals("Administrator")) {
					new UserChooserDialog(controller.getUserStorage(),controller.defaultUser,controller.notificationStorage,controller);
				} else {
					UserDialogFactory userDialogFactory= new UserDialogFactory();
					userDialogFactory.createDialog("Edit",controller.defaultUser, false);
			
					//UserDialogFactory editUserDialogFactory= new EditUserDialog(controller.defaultUser, false);
					//editUserDialogFactory.createDialog();
				}
				TableModel t = prepareTableModel();
				tableView.setModel(t);
				tableView.repaint();
			}
		});
		Appmenu.add(mi);
		
		
		if (controller.defaultUser.getType().equals("Administrator")) {
		//Added by Nelson: Manage Locations
			mi = new JMenuItem("Manage Locations");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					new LocationsDialog(controller);
					// dlg.setLocationRelativeTo(null);
					TableModel t = prepareTableModel();
					tableView.setModel(t);
					tableView.repaint();
				}
			});
			Appmenu.add(mi);
		}

		return menuBar;
	}

	private void initializeSystem() {

		mCurrUser = this.controller.getDefaultUser(); // get User from
														// controller
		controller.loadApptFromXml();
		// Fix Me !
		// Load the saved appointments from disk
		checkUpdateJoinAppt();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == eButton) {
			if (year == null)
				return;
			currentY = currentY + 1;
			year.setText(new Integer(currentY).toString());
			CalGrid.this.setTitle("Desktop Calendar - No User - (" + currentY
					+ "-" + currentM + "-" + currentD + ")");
			getDateArray(data);
			if (tableView != null) {
				TableModel t = prepareTableModel();
				tableView.setModel(t);
				tableView.repaint();

			}
			updateCal();
		} else if (e.getSource() == wButton) {
			if (year == null)
				return;
			currentY = currentY - 1;
			year.setText(new Integer(currentY).toString());
			CalGrid.this.setTitle("Desktop Calendar - No User - (" + currentY
					+ "-" + currentM + "-" + currentD + ")");
			getDateArray(data);
			if (tableView != null) {
				TableModel t = prepareTableModel();
				tableView.setModel(t);
				tableView.repaint();

			}
			updateCal();
		} else if (e.getSource() == month) {
			if (month.getSelectedItem() != null) {
				currentM = month.getSelectedIndex() + 1;
				try {
					mem_doc.remove(0, mem_doc.getLength());
					mem_doc.insertString(0, holidays[currentM - 1], sab);
				} catch (BadLocationException e1) {

					e1.printStackTrace();
				}

				CalGrid.this.setTitle("Desktop Calendar - No User - ("
						+ currentY + "-" + currentM + "-" + currentD + ")");
				getDateArray(data);
				if (tableView != null) {
					TableModel t = prepareTableModel();
					tableView.setModel(t);
					tableView.repaint();
				}
				updateCal();
			}
		}
	}

	// update the appointment list on gui
	public void updateAppList() {
		applist.clear();
		applist.repaint();
		applist.setTodayAppt(getTodayAppt());
	}

	public void updateCal() {
		if (mCurrUser != null) {
			mCurrTitle = "Desktop Calendar - " + mCurrUser.getID() + " - ";
			this.setTitle(mCurrTitle + "(" + currentY + "-" + currentM + "-"
					+ currentD + ")");
			// Appt[] monthAppts = null;
			Appt[] monthAppts = getMonthAppts();

			// for (int i = 0; i < 6; i++)
			// for (int j = 0; j < 7; j++)
			// apptMarker[i][j] = new Vector(10, 1);

			TableModel t = prepareTableModel();
			this.tableView.setModel(t);
			this.tableView.repaint();
			updateAppList();
		}
	}

	// public void clear() {
	// for (int i = 0; i < 6; i++)
	// for (int j = 0; j < 7; j++)
	// apptMarker[i][j] = new Vector(10, 1);
	// TableModel t = prepareTableModel();
	// tableView.setModel(t);
	// tableView.repaint();
	// applist.clear();
	// }

	private Appt[] getMonthAppts() {
		// System.out.println("MONTH");
		Timestamp start = new Timestamp(0);
		start.setYear(currentY);
		start.setMonth(currentM - 1);
		// start.setMonth(currentM);
		start.setDate(1);
		start.setHours(0);
		Timestamp end = new Timestamp(0);
		end.setYear(currentY);
		end.setMonth(currentM - 1);
		// end.setMonth(currentM);
		GregorianCalendar g = new GregorianCalendar(currentY, currentM - 1, 1);
		end.setDate(g.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
		end.setHours(23);
		TimeSpan period = new TimeSpan(start, end);
		return controller.retrieveUserApptsDuring(mCurrUser, 0, period, false, null);
	}

	private void mousePressResponse() {
		previousRow = tableView.getSelectedRow();
		previousCol = tableView.getSelectedColumn();

		// for(int i = 0; i < data.length; i++)
		// for(int j = 0; j < data[i].length; j++)
		// data [i][j] = "N"+((String)data [i][j]).substring(1);

		// data [previousRow][previousCol] = "Y"+((String)data
		// [previousRow][previousCol]).substring(1);
		// tableView.setBackground(Color.blue);
		// System.out.println("Curr");
		currentRow = previousRow;
		currentCol = previousCol;
	}

	private void mouseResponse() {

		int[] selectedRows = tableView.getSelectedRows();
		int[] selectedCols = tableView.getSelectedColumns();
		// for (int i=0;i<selectedRows.length;i++)
		// {System.out.print(selectedRows[i]);}
		// System.out.println();
		// for (int i=0;i<selectedRows.length;i++)
		// {System.out.print(selectedCols[i]);}
		// System.out.println();
		if (tableView.getSelectedRow() == previousRow
				&& tableView.getSelectedColumn() == previousCol) {
			if (selectedRows.length - 1 < 0) {
				currentRow = 0;
				currentCol = 0;
			} else {
				currentRow = selectedRows[selectedRows.length - 1];
				currentCol = selectedCols[selectedCols.length - 1];
			}
			// TableModel t = prepareTableModel();
			// TODO
			// t.setValueAt(aValue, currentRow , currentCol);
			// tableView.setModel(t);
			// tableView.repaint();
			// data[currentRow][currentCol] = "13";

			// System.out.println("Prev");
			// tableView.setBackground(Color.blue);
			// tableView.repaint();
		} else if (tableView.getSelectedRow() != previousRow
				&& tableView.getSelectedColumn() == previousCol) {
			currentRow = tableView.getSelectedRow();
			currentCol = selectedCols[selectedCols.length - 1];
			// System.out.println("F T");
		} else if (tableView.getSelectedRow() == previousRow
				&& tableView.getSelectedColumn() != previousCol) {
			currentRow = selectedRows[selectedRows.length - 1];
			currentCol = tableView.getSelectedColumn();
			// System.out.println("T F");
		} else {
			currentRow = tableView.getSelectedRow();
			currentCol = tableView.getSelectedColumn();
			// System.out.println("F F");
		}

		/**
		 * for(int i = 0; i < data.length; i++) for(int j = 0; j <
		 * data[i].length; j++) data [i][j] = "N"+((String)data
		 * [i][j]).substring(1);
		 * 
		 * data [currentRow][currentCol] = "Y"+((String)data
		 * [currentRow][currentCol]).substring(1);
		 **/
		// previousRow = currentRow; previousCol = currentCol;

		if (currentRow > 5 || currentRow < 0 || currentCol < 0
				|| currentCol > 6)
			return;

		if (!tableView.getModel().getValueAt(currentRow, currentCol).equals(""))
			try {
				currentD = new Integer(((String) tableView.getModel()
				// .getValueAt(currentRow,
				// currentCol)).substring(1)).intValue();
						.getValueAt(currentRow, currentCol)).substring(0))
						.intValue();
				currentWD = currentCol;
			} catch (NumberFormatException n) {
				return;
			}

		CalGrid.this.setTitle(mCurrTitle + "(" + currentY + "-" + currentM
				+ "-" + currentD + ")");
		tableView.repaint();
		updateAppList();
	}

	public boolean isApptToday(Appt appt) {
		if (appt.getTimeSpan().getStartTime().getYear() + 1900 != currentY)
			return false;
		if ((appt.getTimeSpan().getStartTime().getMonth() + 1) != currentM)
			return false;
		if (appt.getTimeSpan().getStartTime().getDate() != currentD)
			return false;
		return true;
	}

	public boolean isApptInThisMonth(Appt appt) {

		if (appt.getTimeSpan().getStartTime().getYear() + 1900 != currentY)
			return false;

		if ((appt.getTimeSpan().getStartTime().getMonth() + 1) != currentM)
			return false;
		return true;
	}

	public Appt[] getTodayAppt() {
		// System.out.println("TODAY");
		Timestamp start = new Timestamp(0);
		start.setYear(currentY);
		start.setMonth(currentM - 1);
		// start.setMonth(currentM);
		start.setDate(currentD);
		// start.setDate(temp);
		start.setHours(0);
		start.setMinutes(0);
		start.setSeconds(0);

		Timestamp end = new Timestamp(0);
		end.setYear(currentY);
		end.setMonth(currentM - 1);
		// end.setMonth(currentM);
		end.setDate(currentD);
		// end.setDate(temp);
		end.setHours(23);
		end.setMinutes(59);
		end.setSeconds(59);

		// System.out.println(start.getYear()+" "+ start.getMonth()+" "+
		// start.getDate());
		// System.out.println(mCurrUser);
		TimeSpan period = new TimeSpan(start, end);
		return controller.retrieveUserApptsDuring(mCurrUser, 0, period, false, null);
	}

	public AppList getAppList() {
		return applist;
	}

	public User getCurrUser() {
		return mCurrUser;
	}

	// check for any invite or update from join appointment
	public void checkUpdateJoinAppt() {
		// Fix Me!
		// Darren: Assume all Appts is stored locally
		Appt[] Appts = controller.ExportHashMap();
		//System.out.println("CHECK");
		for (int i = 0; i < Appts.length; i++) {
			if ((Appts[i].isJoint()) && (!Appts[i].isJointScheduled())) {
				if (Appts[i].getAttendList().contains(mCurrUser.getID())) {
					//System.out.println(Appts[i].getAttendList().indexOf(mCurrUser.getID()));
					AppScheduler setAppDial;
					// Update from somewhere else
					if (Appts[i].getAttendList().getFirst().equals(mCurrUser.getID())){
						setAppDial = new AppScheduler(
								"Someone has responded to your Joint Appointment invitation", this, Appts[i].getID());
					} else {
						setAppDial = new AppScheduler(
								"Join Appointment Being scheudled", this, Appts[i].getID());
					}
					setAppDial.updateSetApp(Appts[i]);
					setAppDial.show();
				} else if (Appts[i].getRejectList().contains(mCurrUser.getID())) {
					// Maybe meaningless here
				} else if (Appts[i].getWaitingList().contains(mCurrUser.getID())) {
					// pop up invite
					//System.out.println("CHECK");
					AppScheduler setAppDial = new AppScheduler("Join Appointment Invitation/ Content Change", this,
							Appts[i].getID());
					setAppDial.updateSetApp(Appts[i]);
					setAppDial.show();
				}
			}
		}
	}

	public Timestamp getCurrentTime() {
		// ADDED by Jimmy: Replaced with time from Time Machine
		return Time.getCurrentTimestamp();
		// Timestamp now = new Timestamp(today.getTimeInMillis());
		// System.out.println("NOW: "+ now);
		// return now;
	}
}
