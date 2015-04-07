/**
 *  BIG MOD: Change single Date into start and end date
 */
package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class AppScheduler extends JDialog implements ActionListener,
		ComponentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel syearL;
	private JTextField syearF;
	private JLabel smonthL;
	private JTextField smonthF;
	private JLabel sdayL;
	private JTextField sdayF;
	private JLabel sTimeHL;
	private JTextField sTimeH;
	private JLabel sTimeML;
	private JTextField sTimeM;
	private JLabel eyearL;
	private JTextField eyearF;
	private JLabel emonthL;
	private JTextField emonthF;
	private JLabel edayL;
	private JTextField edayF;
	private JLabel eTimeHL;
	private JTextField eTimeH;
	private JLabel eTimeML;
	private JTextField eTimeM;

	private DefaultListModel model;
	private JTextField titleField;

	private JCheckBox haveReminder;
	private JLabel rdayL;
	private JTextField rdayF;
	private JLabel rTimeHL;
	private JTextField rTimeH;
	private JLabel rTimeML;
	private JTextField rTimeM;
	private JLabel rTimeSL;
	private JTextField rTimeS;

	private JTextArea reminderDetailArea;

	private JSplitPane pReminderDes;
	private JPanel reminderDetailPanel;

	private JButton saveBut;
	private JButton CancelBut;
	private JButton rejectBut;

	private Appt NewAppt;
	private CalGrid parent;
	//private boolean isNew = true;
	//private boolean isChanged = true;
	//private boolean isJoint = false;

	private JTextArea detailArea;

	private JSplitPane pDes;
	private JPanel detailPanel;

	private int selectedApptId = -1;
	
	//ADDED by Darren
	private boolean inviteD_ViewOnly;
	private boolean joint_scheduled;

	private JComboBox<Location> locField;
	private JComboBox<String> freqField;
	
	private JButton AvilBut;
	private JButton AutoAvilBut;
	
	//TODO
	private JCheckBox publicity;
	private JButton inviteBut;
	private InviteDialog inviteD;
	ArrayList<String> involved; //For Invite Dialog: no need to re-select all people
	//private JTextField attendField;
	//private JTextField rejectField;
	//private JTextField waitingField;
	private AvaliableTimeSlots AvilTSD;
	
	private void commonConstructor(String title, CalGrid cal) {
		parent = cal;
		//this.setAlwaysOnTop(true);
		setTitle(title);
		setModal(false);

		Container contentPane;
		contentPane = getContentPane();
		//ADDED by Kathy:improve the interface of event adding
		JPanel psDate = new JPanel();
		Border dateBorder = new TitledBorder(null, "START DATE");
		psDate.setBorder(dateBorder);

		syearL = new JLabel("YEAR: ");
		psDate.add(syearL);
		syearF = new JTextField(6);
		psDate.add(syearF);
		smonthL = new JLabel("MONTH: ");
		psDate.add(smonthL);
		smonthF = new JTextField(4);
		psDate.add(smonthF);
		sdayL = new JLabel("DAY: ");
		psDate.add(sdayL);
		sdayF = new JTextField(4);
		psDate.add(sdayF);

		JPanel peDate = new JPanel();
		Border dateBorder2 = new TitledBorder(null, "END DATE");
		peDate.setBorder(dateBorder2);

		eyearL = new JLabel("YEAR: ");
		peDate.add(eyearL);
		eyearF = new JTextField(6);
		peDate.add(eyearF);
		emonthL = new JLabel("MONTH: ");
		peDate.add(emonthL);
		emonthF = new JTextField(4);
		peDate.add(emonthF);
		edayL = new JLabel("DAY: ");
		peDate.add(edayL);
		edayF = new JTextField(4);
		peDate.add(edayF);

		JPanel psTime = new JPanel();
		Border stimeBorder = new TitledBorder(null, "START TIME");
		psTime.setBorder(stimeBorder);
		sTimeHL = new JLabel("Hour");
		psTime.add(sTimeHL);
		sTimeH = new JTextField(4);
		psTime.add(sTimeH);
		sTimeML = new JLabel("Minute");
		psTime.add(sTimeML);
		sTimeM = new JTextField(4);
		psTime.add(sTimeM);

		JPanel peTime = new JPanel();
		Border etimeBorder = new TitledBorder(null, "END TIME");
		peTime.setBorder(etimeBorder);
		eTimeHL = new JLabel("Hour");
		peTime.add(eTimeHL);
		eTimeH = new JTextField(4);
		peTime.add(eTimeH);
		eTimeML = new JLabel("Minute");
		peTime.add(eTimeML);
		eTimeM = new JTextField(4);
		peTime.add(eTimeM);

		//ADDED by Kathy: frequency part
		JPanel freqPanel = new JPanel();
		Border freqBorder = new TitledBorder(null, "FREQUENCY");
		freqPanel.setBorder(freqBorder);
		String[] frequencyStr = { "Once", "Daily", "Weekly", "Monthly" };
		freqField = new JComboBox<String>(frequencyStr);
		freqPanel.add(freqField);

		JPanel pTime = new JPanel();
		pTime.setLayout(new BorderLayout());
		pTime.add("West", psTime);
		pTime.add("Center", freqPanel);
		pTime.add("East", peTime);

		JPanel pDate = new JPanel();
		pDate.setLayout(new BorderLayout());
		pDate.add("West", psDate);
		pDate.add("East", peDate);

		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		top.setBorder(new BevelBorder(BevelBorder.RAISED));
		top.add(pDate, BorderLayout.NORTH);
		top.add(pTime, BorderLayout.CENTER);

		contentPane.add("North", top);

		JPanel titleAndTextPanel = new JPanel();
		JLabel titleL = new JLabel("TITLE");
		titleField = new JTextField(15);
		titleAndTextPanel.add(titleL);
		titleAndTextPanel.add(titleField);
		
		//Modified by Jimmy: Add location into GUI
		JLabel locationL = new JLabel("LOCATION");
		locField = new JComboBox<Location>(parent.locationStorage.getLocations());
		//locField.setSelectedIndex(0);
		titleAndTextPanel.add(locationL);
		titleAndTextPanel.add(locField);
		
		//Added by Darren: Publicity and Invite button
		//TODO
		publicity = new JCheckBox("Publicity");
		inviteBut = new JButton("Invite");
		inviteBut.addActionListener(this);
		AvilBut = new JButton("Find Slots");
		AvilBut.addActionListener(this);
		titleAndTextPanel.add(publicity);
		titleAndTextPanel.add(inviteBut);
		titleAndTextPanel.add(AvilBut);

		detailPanel = new JPanel();
		detailPanel.setLayout(new BorderLayout());
		Border detailBorder = new TitledBorder(null, "Appointment Description");
		detailPanel.setBorder(detailBorder);
		detailArea = new JTextArea(10, 30);

		detailArea.setEditable(true);
		JScrollPane detailScroll = new JScrollPane(detailArea);
		detailPanel.add(detailScroll);

		pDes = new JSplitPane(JSplitPane.VERTICAL_SPLIT, titleAndTextPanel,
				detailPanel);

		top.add(pDes, BorderLayout.SOUTH);

		if (NewAppt != null) {
			detailArea.setText(NewAppt.getInfo());

		}

		JPanel pReminder = new JPanel();
		Border reminderBorder = new TitledBorder(null, "Reminder");
		pReminder.setBorder(reminderBorder);

		haveReminder = new JCheckBox("Activated");
		pReminder.add(haveReminder);
		rdayL = new JLabel("DAYS: ");
		pReminder.add(rdayL);
		rdayF = new JTextField(4);
		pReminder.add(rdayF);
		rTimeHL = new JLabel("HOURS: ");
		pReminder.add(rTimeHL);
		rTimeH = new JTextField(6);
		pReminder.add(rTimeH);
		rTimeML = new JLabel("MINUTES: ");
		pReminder.add(rTimeML);
		rTimeM = new JTextField(6);
		pReminder.add(rTimeM);
		rTimeSL = new JLabel("SECONDS: ");
		pReminder.add(rTimeSL);
		rTimeS = new JTextField(6);
		pReminder.add(rTimeS);

		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());
		bottom.setBorder(new BevelBorder(BevelBorder.RAISED));
		// bottom.add(pReminder, BorderLayout.NORTH);

		reminderDetailPanel = new JPanel();
		reminderDetailPanel.setLayout(new BorderLayout());
		Border reminderDetailBorder = new TitledBorder(null,
				"Reminder Description");
		reminderDetailPanel.setBorder(reminderDetailBorder);
		reminderDetailArea = new JTextArea(10, 30);

		reminderDetailArea.setEditable(true);
		JScrollPane reminderDetailScroll = new JScrollPane(reminderDetailArea);
		reminderDetailPanel.add(reminderDetailScroll);

		pReminderDes = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pReminder,
				reminderDetailPanel);

		bottom.add(pReminderDes, BorderLayout.NORTH);

		contentPane.add("Center", bottom);

		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		// inviteBut = new JButton("Invite");
		// inviteBut.addActionListener(this);
		// panel2.add(inviteBut);

		AutoAvilBut = new JButton("Auto");
		AutoAvilBut.addActionListener(this);
		panel2.add(AutoAvilBut);
		
		saveBut = new JButton("Save");
		saveBut.addActionListener(this);
		panel2.add(saveBut);

		rejectBut = new JButton("Reject");
		rejectBut.addActionListener(this);
		panel2.add(rejectBut);
		rejectBut.show(false);

		CancelBut = new JButton("Cancel");
		CancelBut.addActionListener(this);
		panel2.add(CancelBut);

		contentPane.add("South", panel2);
		NewAppt = new Appt(parent.controller.defaultUser);
		
		//ADDED By Darren: Load DefaultUser
		//TODO
		involved = new ArrayList <String> ();
		involved.add(getCurrentUser());		
		joint_scheduled = false; // Maybe changed in UpdateSetApp 

		if (this.getTitle().equals("Join Appointment Invitation/ Content Change")) {
			//inviteBut.show(false);
			rejectBut.show(true);
			CancelBut.setText("Consider Later");
			saveBut.setText("Accept");
			AutoAvilBut.show(false);
		}
		if (this.getTitle().equals("Join Appointment Being scheudled")){
			rejectBut.show(false);
			saveBut.show(false);
			CancelBut.setText("OK");
			AutoAvilBut.show(false);
		}
		
		if (this.getTitle().equals(
				"Someone has responded to your Joint Appointment invitation")) {
			rejectBut.show(true);
			rejectBut.setText("Discard");
			CancelBut.setText("OK");
			saveBut.setText("Schedule");
		}
		
		if (this.getTitle().equals("Join Appointment Invitation/ Content Change")
				|| this.getTitle()
						.equals("Someone has responded to your Joint Appointment invitation")
				|| this.getTitle().equals("Join Appointment Being scheudled")
				|| this.getTitle().equals("Modify")) {
			
			if (!this.getTitle().equals("Modify")) {
				disableEditing();
			}
			inviteBut.setText("View");
			inviteD_ViewOnly = true;
		} else {
			inviteD_ViewOnly = false;
		}
		
		inviteD = new InviteDialog(parent, this, inviteD_ViewOnly);
		inviteD.setVisible(false);
		//locField.setSelectedIndex(0);
		this.setResizable(false);
		pack();

	}

	AppScheduler(String title, CalGrid cal, int selectedApptId) {
		this.selectedApptId = selectedApptId;
		commonConstructor(title, cal);
	}

	AppScheduler(String title, CalGrid cal) {
		commonConstructor(title, cal);
	}
	
	public void actionPerformed(ActionEvent e) {

		// distinguish which button is clicked and continue with require
		// function
		// TODO
		if (e.getSource() == CancelBut) {
			setVisible(false);
			dispose();
		} else if (e.getSource() == saveBut) {
			PrepareSave();
		} else if (e.getSource() == rejectBut) {
			if (this.getTitle().equals(
				"Someone has responded to your Joint Appointment invitation")){
				//Discard this event
				if (JOptionPane.showConfirmDialog(this,
						"Discard this joint appointment?", "Confirmation",
						JOptionPane.YES_NO_OPTION) == 0) {
					// Make the key, blow that Appt up
					NewAppt.setAttendList(new String[] {getCurrentUser()});
					NewAppt.setID(this.selectedApptId);
					//System.out.println(this.selectedApptId);
					parent.controller.removeAppt(NewAppt);
					this.setVisible(false);
					dispose();
				}
			}
			else if (JOptionPane.showConfirmDialog(this,
					"Reject this joint appointment?", "Confirmation",
					JOptionPane.YES_NO_OPTION) == 0) {
				// Not a safe method to get the initiator... will improve this
				String key = new String(involved.get(0) + "-" + this.selectedApptId);
				//System.out.println(key);
				Appt temp = parent.controller.getAppt(key);
				if (temp != null) {
					//System.out.println(temp.getAttendList());
					//System.out.println(temp.getRejectList());
					//System.out.println(temp.getWaitingList());
				temp.addReject(getCurrentUser());
				temp.getAttendList().remove(getCurrentUser());
				temp.getWaitingList().remove(getCurrentUser());
				parent.controller.saveAppt(temp);
				} else {System.out.println("ERROR: HashMap not match!");}
				this.setVisible(false);
				dispose();
			}
		} else if (e.getSource() == inviteBut){
			if (!inviteD_ViewOnly) {
				inviteD.RebuildChoice(involved);
			}
			inviteD.setVisible(true);
			inviteD.InviteF.setVisible(true);
		} else if (e.getSource() == AvilBut){
			//TODO
			//System.out.println("BEEP");
			AvilTSD = new AvaliableTimeSlots(this);
		} else if (e.getSource() == AutoAvilBut){
			 // Do something
			 AvilTSD = new AvaliableTimeSlots(this);
			 UpdateTimeFields (AvilTSD.StartTimes.get(0), AvilTSD.EndTimes.get(0));
			 AvilTSD.DisposeFrame();
			 PrepareSave();
		}
		parent.getAppList().clear();
		parent.getAppList().setTodayAppt(parent.getTodayAppt());
		parent.repaint();
	}

	// Somebody make use of it?
	private JPanel createPartOperaPane() {
		JPanel POperaPane = new JPanel();
		JPanel browsePane = new JPanel();
		JPanel controPane = new JPanel();

		POperaPane.setLayout(new BorderLayout());
		TitledBorder titledBorder1 = new TitledBorder(
				BorderFactory.createEtchedBorder(Color.white, new Color(178,
						178, 178)), "Add Participant:");
		browsePane.setBorder(titledBorder1);

		POperaPane.add(controPane, BorderLayout.SOUTH);
		POperaPane.add(browsePane, BorderLayout.CENTER);
		POperaPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
		return POperaPane;

	}

	// Modified by Kathy
	private int[] getValidDate(JTextField year, JTextField month, JTextField day, String input) {
		int[] date = new int[3];
		date[0] = Utility.getNumber(year.getText());
		date[1] = Utility.getNumber(month.getText());
		if (date[0] < 1980 || date[0] > 2100) {
			JOptionPane.showMessageDialog(this,
					"Please input proper " + input + " year", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (date[1] <= 0 || date[1] > 12) {
			JOptionPane.showMessageDialog(this,
					"Please input proper " + input + " month", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		date[2] = Utility.getNumber(day.getText());
		int monthDay = CalGrid.monthDays[date[1] - 1];
		if (date[1] == 2) {
			GregorianCalendar c = new GregorianCalendar();
			if (c.isLeapYear(date[0]))
				monthDay = 29;
		}
		if (date[2] <= 0 || date[2] > monthDay) {
			JOptionPane.showMessageDialog(this,
					"Please input proper " + input + " month day", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return date;
	}

	// Modified by Kathy
	private int getTime(JTextField h, JTextField min) {

		int hour = Utility.getNumber(h.getText());
		if ((hour == -1) || (hour < 0) || (hour >= 24))
			return -1;
		int minute = Utility.getNumber(min.getText());
		if ((minute == -1) || (minute < 0) || (minute >= 60))
			return -1;

		return (hour * 60 + minute);

	}

	// From base code
	private int[] getValidTimeInterval() {

		int[] result = new int[2];
		result[0] = getTime(sTimeH, sTimeM);
		result[1] = getTime(eTimeH, eTimeM);

		if (result[1] == -1 || result[0] == -1) {
			JOptionPane.showMessageDialog(this, "Please check time",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		if ((result[0] % 15) != 0 || (result[1] % 15) != 0) {
			JOptionPane.showMessageDialog(this,
					"Minute Must be 0, 15, 30, or 45 !", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		if (!sTimeM.getText().equals("0") && !sTimeM.getText().equals("15")
				&& !sTimeM.getText().equals("30")
				&& !sTimeM.getText().equals("45")
				|| !eTimeM.getText().equals("0")
				&& !eTimeM.getText().equals("15")
				&& !eTimeM.getText().equals("30")
				&& !eTimeM.getText().equals("45")) {
			JOptionPane.showMessageDialog(this,
					"Minute Must be 0, 15, 30, or 45 !", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		// if (result[1] <= result[0]) {
		// JOptionPane.showMessageDialog(this,
		// "End time should be bigger than \nstart time",
		// "Input Error", JOptionPane.ERROR_MESSAGE);
		// return null;
		// }

		if ((result[0] < AppList.OFFSET * 60)
				|| (result[1] > (AppList.OFFSET * 60 + AppList.ROWNUM * 2 * 15))) {
			JOptionPane.showMessageDialog(this, "Out of Appointment Range !",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return result;
	}

	private void saveButtonResponse(boolean Joint_Confirmed) {
		// Fix Me!
		// Save the appointment to the hard disk
		// DENY IF INVALID INPUT
		boolean isNewEvent;
		boolean pass1 = true; // getValidDate1()
		boolean pass2 = true; // You can't create started events!
		boolean pass3 = true; // Start time must before end time!
		boolean pass4 = true; // Collision of other events!
		boolean pass5 = true; // Collision of other events in this frequent!
		boolean pass6 = true; // Event length must be between 15 minutes to 24
		boolean pass7 = true; // Location is Full/ being delected
								// hours!
		//int controller_action;
		// System.out.println("START");

		// TIME_START_VALID
		if (getValidDate(syearF, smonthF, sdayF, "start") == null || getValidDate(eyearF, emonthF, edayF, "end") == null
				|| getValidTimeInterval() == null) {
			// System.out.println("DENY");
			pass1 = false;
		}

		// TIME
		int[] pro_date = new int[10];
		pro_date[0] = Utility.getNumber(syearF.getText()) - 1900;
		pro_date[1] = Utility.getNumber(smonthF.getText()) - 1;
		pro_date[2] = Utility.getNumber(sdayF.getText());
		pro_date[3] = Utility.getNumber(sTimeH.getText());
		pro_date[4] = Utility.getNumber(sTimeM.getText());

		pro_date[5] = Utility.getNumber(eyearF.getText()) - 1900;
		pro_date[6] = Utility.getNumber(emonthF.getText()) - 1;
		pro_date[7] = Utility.getNumber(edayF.getText());
		pro_date[8] = Utility.getNumber(eTimeH.getText());
		pro_date[9] = Utility.getNumber(eTimeM.getText());
		// TIMESTAMP
		Timestamp TStart = new Timestamp(pro_date[0], pro_date[1], pro_date[2],	pro_date[3], pro_date[4], 0, 0);
		Timestamp TEnd = new Timestamp(pro_date[5], pro_date[6], pro_date[7], pro_date[8], pro_date[9], 0, 0);
		
		// LOCATION
		if (locField.getSelectedIndex() < 0) {
			locField.setSelectedIndex(0);
		}
		Location location = (Location) locField.getSelectedItem();
		// Some check.
		// System.out.println(TStart);
		
		// Added by Darren
		if (TStart.getTime() <= parent.getCurrentTime().getTime()) {
			JOptionPane.showMessageDialog(this,
					"You can't create events in the past!", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			pass2 = false;
		}
		
		// Added by Darren
		if (TStart.getTime() > TEnd.getTime()) {
			JOptionPane.showMessageDialog(this,
					"The starting time must be before the ending time!", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			pass3 = false;
		}

		// Added by Darren : Changed in PHASE2 - check all involved users
		boolean create_mode = true;
		int pass4int = 0;
		int temp4int = 0; //found bug
		if (involved.size() > 0) {
			//System.out.println(involved.size());
			for (int i0 = 0; i0 < involved.size(); i0++) {
				temp4int = this.parent.controller.retrieveUserApptsDuring(
						this.parent.controller2.UserFromString(involved.get(i0)), this.selectedApptId,
						new TimeSpan(new Timestamp(TStart.getTime() + 1000 * 60),
								new Timestamp(TEnd.getTime() - 1000 * 60)), create_mode, (Location)locField.getSelectedItem()).length;
				pass4int += temp4int;
				//System.out.println(involved.get(i0) + " - " + pass4int + " - " + temp4int);
				if ((this.selectedApptId != 0) && (joint_scheduled) && (temp4int > 0)) {
					//System.out.println("HEHE");
					//pass4int--;
				} // Exclude itself if Modify
			}
		}
		if ((this.selectedApptId != 0) && (!joint_scheduled)) {
			//System.out.println("SHESHE");
			//pass4int--;
		}
		//System.out.println(pass4int);
		
		//System.out.print(this.selectedApptId);
		
		// System.out.println(TStart + " " + new
		// Timestamp(TStart.getTime()+1000*60));
		//System.out.println(pass4int);
		if (pass4int > 0) {
			JOptionPane.showMessageDialog(this, "Collision with " + pass4int
					+ " event(s)!", "Input Error", JOptionPane.ERROR_MESSAGE);
			pass4 = false;
		}

		// Added by Darren
		int pass5int = 0;
		int temp5int = 0;
		if (involved.size() > 0) {
			//System.out.println(involved.size());
			for (int i0 = 0; i0 < involved.size(); i0++) {
				temp5int = this.parent.controller.create_check(
						this.parent.controller2.UserFromString(involved.get(i0)), this.selectedApptId,
						new TimeSpan(new Timestamp(TStart.getTime() + 1000 * 60),
								new Timestamp(TEnd.getTime() - 1000 * 60)),
								(String) freqField.getSelectedItem(), (Location) locField.getSelectedItem());
				pass5int += temp5int;
				//System.out.println(involved.get(i0) + " - " + pass5int + " - " + temp5int);
				if (this.selectedApptId != 0) {
					//pass5int--;
				} // Exclude itself if Modify
			}
		}
		if (pass5int > 0) {
			JOptionPane.showMessageDialog(this, "Collision with " + pass5int
					+ " event(s) if you create it in this frequent!",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			pass5 = false;
		}

		// Added by Darren
		if ((TEnd.getTime() - TStart.getTime() < 1000 * 60 * 15)
				|| (TEnd.getTime() - TStart.getTime() > 1000 * 60 * 60 * 24)) {
			JOptionPane.showMessageDialog(this,
					"Event length must be between 15 minutes to 24 hours!",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			pass6 = false;
		}
		
		// Added by Darren
		if ((involved.size() > location.getCapacity())) {
			JOptionPane.showMessageDialog(this,
					"Location is Full",
					"Location capacity error", JOptionPane.ERROR_MESSAGE);
			pass7 = false;
		}
		if (location.isToBeDeleted()) {
			JOptionPane.showMessageDialog(this,
					"Location is to be deleted",
					"Location error", JOptionPane.ERROR_MESSAGE);
			pass7 = false;
		}

		if (pass1 && pass2 && pass3 && pass4 && pass5 && pass6 && pass7) {
			// If joint confirmed, only time, space, frequency will be checked
			
			//System.out.println(Joint_Confirmed);
			
			if (!Joint_Confirmed) {
			// TITLE
			NewAppt.setTitle(titleField.getText());
			// INFO
			NewAppt.setInfo(detailArea.getText());
			}
			
			// TIMESPAN
			TimeSpan NewTimeSpan = new TimeSpan(TStart, TEnd);
			NewAppt.setTimeSpan(NewTimeSpan);
			// ID (Need to specify later)
			int silly_ID = this.selectedApptId;
			// System.out.println("ID: " + silly_ID);
			if (silly_ID == 0) {
				boolean joint = false;
				silly_ID = parent.controller.FindNextID(joint);
				isNewEvent=true;
			} else {
				isNewEvent=false;
			}
			NewAppt.setID(silly_ID);
			
			if ((!Joint_Confirmed) && (!joint_scheduled)) {
			// Joint ID (Need to specify later)
			if (involved.size() > 1) {
				boolean joint = true;
				NewAppt.setJoint(true);
				NewAppt.setJoinID(parent.controller.FindNextID(joint));
			} else {
				NewAppt.setJoint(false);
				NewAppt.setJoinID(0);
			}
			}
			
			// Exclude confirmed Joint events (either New or Modify)
			if ((!Joint_Confirmed) && (!joint_scheduled)) {
				// addAttendant
				if (!NewAppt.getAttendList().contains(getCurrentUser())) 
				{NewAppt.addAttendant(getCurrentUser());} // user
			else {
				System.out.println("ERROR: New Appt contains Current User");
				} // shouldn't occur
			// addReject
			NewAppt.setRejectList(new LinkedList<String>()); // clear
			// addWaiting
			if (involved.size() > 1) {
				//System.out.println(involved);
				involved.remove(getCurrentUser());
				//TODO
				NewAppt.setWaitingList(new LinkedList<String>(involved));
				JOptionPane.showMessageDialog(this, "Invition has been sent to: " + (String) NewAppt.getWaitingList().toString());
				//System.out.println(NewAppt.getWaitingList());
			}
			}

			//Modified by Jimmy: Location
			//if (location == null) {
			//	location=parent.locationStorage.getLocation("Empty Venue");
			//}
			//if (NewAppt.getAllPeople().size()>location.getCapacity()) {
			//Darren: corrected here at r109
			NewAppt.setLocation(location);
			String frequency = (String) freqField.getSelectedItem();
			NewAppt.setFrequency(frequency);

			if (publicity.isSelected()){
				NewAppt.setPublic(true);
			} else {NewAppt.setPublic(false);}
			
			// ADDED by Jimmy: Reminder checks & save

			if (haveReminder.isSelected()) {

				// reminder sanity check begin
				// no negative times possible
				long reminderSeconds = Utility.getNumber(rTimeS.getText());
				long reminderMinutes = Utility.getNumber(rTimeM.getText());
				long reminderHours = Utility.getNumber(rTimeH.getText());
				long reminderDays = Utility.getNumber(rdayF.getText());

				if (reminderSeconds < 0 || reminderMinutes < 0
						|| reminderHours < 0 || reminderDays < 0) {
					JOptionPane.showMessageDialog(this,
							"Reminder time is invalid", "Reminder Input error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				NewAppt.setReminder(
						(reminderSeconds + reminderMinutes * 60 + reminderHours
								* 3600 + reminderDays * 24 * 3600) * 1000,
						reminderDetailArea.getText());
			} else {
				NewAppt.unSetReminder();
			}

			// reminder checks & save end

			// ADD
			if (isNewEvent) {
				parent.controller.saveAppt(NewAppt);
			} else {
				//System.out.println(NewAppt.getAttendList());
				parent.controller.updateSavedAppt(NewAppt);	
			}
			
			// If it is from "Schedule" button, it is first time to make it JointScheduled
			if (Joint_Confirmed) {
				NewAppt.setJointScheduled(true);
			}

			parent.updateCal();
			parent.updateAppList();
			setVisible(false);
			dispose();
		}
		return;
	}

	private Timestamp createTimeStamp(int[] date, int time) {
		Timestamp stamp = new Timestamp(0);
		stamp.setYear(date[0]);
		stamp.setMonth(date[1] - 1);
		stamp.setDate(date[2]);
		stamp.setHours(time / 60);
		stamp.setMinutes(time % 60);
		return stamp;
	}

	public void updateSetApp(Appt appt) {
		// Fix Me!
		// parent.controller.UpdateAppt(appt);
		titleField.setText(appt.getTitle());
		detailArea.setText(appt.getInfo());
		syearF.setText(Integer
				.toString(appt.getTimeSpan().getStartTime().getYear() + 1900));
		smonthF.setText(Integer
				.toString(appt.getTimeSpan().getStartTime().getMonth() + 1));
		sdayF.setText(Integer.toString(appt.getTimeSpan().getStartTime().getDate()));
		eyearF.setText(Integer
				.toString(appt.getTimeSpan().getEndTime().getYear() + 1900));
		emonthF.setText(Integer
				.toString(appt.getTimeSpan().getEndTime().getMonth() + 1));
		edayF.setText(Integer.toString(appt.getTimeSpan().getEndTime().getDate()));
		sTimeH.setText(Integer.toString(appt.getTimeSpan().getStartTime().getHours()));
		sTimeM.setText(Integer.toString(appt.getTimeSpan().getStartTime()
				.getMinutes()));
		eTimeH.setText(Integer.toString(appt.getTimeSpan().getEndTime().getHours()));
		eTimeM.setText(Integer.toString(appt.getTimeSpan().getEndTime().getMinutes()));
		locField.setSelectedItem(appt.getLocation());
		freqField.setSelectedItem(appt.getFrequency());

		if (appt.hasReminder()) {
			rdayF.setText(Long.toString(appt.getReminderTriggerAheadDays()));
			rTimeH.setText(Long.toString(appt.getReminderTriggerAheadHours()));
			rTimeM.setText(Long.toString(appt.getReminderTriggerAheadMinutes()));
			rTimeS.setText(Long.toString(appt.getReminderTriggerAheadSeconds()));
			reminderDetailArea.setText(appt.getReminderInfo());
			haveReminder.setSelected(true);
		}

		if (appt.isPublic()){
			publicity.setSelected(true);
		}
			
		ArrayList<String> list1 = Utility.purify(new ArrayList<String>(appt.getAttendList()));
		ArrayList<String> list2 = Utility.purify(new ArrayList<String>(appt.getRejectList()));
		ArrayList<String> list3 = Utility.purify(new ArrayList<String>(appt.getWaitingList()));
		
		involved = new ArrayList<String>();
		involved.addAll(list1);
		involved.addAll(list2);
		involved.addAll(list3);
		if ((list1.size() > 1) && (list2.size() == 0) && (list3.size() == 0)) {
			joint_scheduled = true; // Scheduled joint event
		}
		//System.out.println(list1.size() +"-"+ list2.size() +"-"+ list3.size());
		if (inviteD_ViewOnly) {
			inviteD.view_all_involved(list1, list2, list3);
			NewAppt.setAttendList(appt.getAttendList());
			NewAppt.setRejectList(appt.getRejectList());
			NewAppt.setWaitingList(appt.getWaitingList());
			if (!appt.IsAllAttended()) {
				saveBut.setEnabled(false);
				AutoAvilBut.setEnabled(false);
				//reminderDetailArea.setEditable(true);
				//detailArea.setEditable(true);
				
				if (this.getTitle().equals("Join Appointment Invitation/ Content Change")) {
					// Eeeyep, saveBut is messed up
					saveBut.setEnabled(true);
					//AutoAvilBut.setEnabled(true);
				}
			}
		} else { 
			inviteD.RebuildChoice(involved);
			if (joint_scheduled) {
				// If it is modifying a joint event, rebuild the AttendList
				NewAppt.setAttendList(appt.getAttendList());
				NewAppt.setRejectList(appt.getRejectList());
				NewAppt.setWaitingList(appt.getWaitingList());
				NewAppt.setJointScheduled(true);
				//System.out.println("BEEP");
			}
		}
		this.selectedApptId = appt.getID(); // GOT YOU

	}

	public void componentHidden(ComponentEvent e) {

	}

	public void componentMoved(ComponentEvent e) {

	}

	public void componentResized(ComponentEvent e) {

		Dimension dm = pDes.getSize();
		double width = dm.width * 0.93;
		double height = dm.getHeight() * 0.6;
		detailPanel.setSize((int) width, (int) height);
	}

	public void componentShown(ComponentEvent e) {

	}

	public String getCurrentUser() // get the id of the current user
	{
		return this.parent.mCurrUser.getID();
	}

	private void disableEditing() {
		// Lock all the editable elements ! 
		syearF.setEditable(false);
		smonthF.setEditable(false);
		sdayF.setEditable(false);
		eyearF.setEditable(false);
		emonthF.setEditable(false);
		edayF.setEditable(false);
		sTimeH.setEditable(false);
		sTimeM.setEditable(false);
		eTimeH.setEditable(false);
		eTimeM.setEditable(false);
		titleField.setEditable(false);
		detailArea.setEditable(false);
		locField.setEnabled(false);
		freqField.setEnabled(false);
		rdayF.setEditable(false);
		rTimeH.setEditable(false);
		rTimeM.setEditable(false);
		rTimeS.setEditable(false);
		reminderDetailArea.setEditable(false);
		haveReminder.setEnabled(false);
		publicity.setEnabled(false);
		//inviteBut.setEnabled(false);
		AvilBut.setEnabled(false);
		//AutoAvilBut.setEnabled(false);
	}
	
	// ADDED By Darren: Low level code reuse
	private void PrepareSave(){
		boolean Joint_Confirmed = false;
		// System.out.println("CALLING");
		if (this.getTitle().equals("Join Appointment Invitation/ Content Change")) {
			// Not a safe method to get the initiator... will improve this
			String key = new String(involved.get(0) + "-" + this.selectedApptId);
			//System.out.println(key);
			Appt temp = parent.controller.getAppt(key);
			if (temp != null) {
				//System.out.println(temp.getAttendList());
				//System.out.println(temp.getRejectList());
				//System.out.println(temp.getWaitingList());
			temp.addAttendant(getCurrentUser());
			temp.getRejectList().remove(getCurrentUser());
			temp.getWaitingList().remove(getCurrentUser());
			parent.controller.saveAppt(temp);
			} else {
				System.out.println("HashMap not match!");
				}
			this.setVisible(false);
			dispose();
		}
		else if (this.getTitle().equals(
				"Someone has responded to your Joint Appointment invitation")){
			String key = new String(getCurrentUser() + "-" + this.selectedApptId);
			//System.out.println(key);
			NewAppt = parent.controller.getAppt(key);
			Joint_Confirmed = true;
			saveButtonResponse(Joint_Confirmed);
		}
		else {saveButtonResponse(false);}
	}
	
	public CalGrid GetParent() {
		return parent;
	}
	
	public int GetselectedApptId(){
		return this.selectedApptId;
	}
	
	public Location GetSelectedLocation(){
		return (Location) locField.getSelectedItem();
	}
	
	public String GetFrequency(){
		return (String) freqField.getSelectedItem();
	}
	
	public void UpdateTimeFields(Timestamp start, Timestamp end) {
		syearF.setText(Integer.toString(start.getYear() + 1900));
		smonthF.setText(Integer.toString(start.getMonth() + 1));
		sdayF.setText(Integer.toString(start.getDate()));
		eyearF.setText(Integer.toString(end.getYear() + 1900));
		emonthF.setText(Integer.toString(end.getMonth() + 1));
		edayF.setText(Integer.toString(end.getDate()));
		sTimeH.setText(Integer.toString(start.getHours()));
		sTimeM.setText(Integer.toString(start.getMinutes()));
		eTimeH.setText(Integer.toString(end.getHours()));
		eTimeM.setText(Integer.toString(end.getMinutes()));
	}
}
