package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class DetailsDialog extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton exitBut;
	private JTextArea area;

	public DetailsDialog(String msg, String title) {
		paintContent(title);
		display(msg);
		this.setSize(500, 350);
		pack();
	}

	public DetailsDialog(Appt appt, String title ,Calendar now) {
		paintContent(title);
		this.setSize(500, 350);
		display(appt, now);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);

	}

	public void paintContent(String title) {

		Container content = getContentPane();
		setTitle(title);

		JScrollPane panel = new JScrollPane();
		Border border = new TitledBorder(null, "Information");
		panel.setBorder(border);

		area = new JTextArea(25, 40);
		// area.setPreferredSize(new Dimension(400, 300));

		panel.getViewport().add(area);

		exitBut = new JButton("Exit");
		exitBut.addActionListener(this);

		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout(FlowLayout.CENTER));

		p2.add(exitBut);

		content.add("Center", panel);
		content.add("South", p2);

	}

	public void display(String msg) {
		area.setFont(new Font("bold", Font.BOLD, 14));

		if (msg.length() == 0)
			msg = "No Information Inputed";
		area.setText(msg);
		area.setEditable(false);
	}

	// MODIFIED by Jimmy: Reminder time and description
	public void display(Appt appt, Calendar now) {

		Calendar converter = new GregorianCalendar();
		String startTime;
		String endTime;
		if (appt.getFrequency().equals("Once")) {
			converter.setTime((Date) appt.getTimeSpan().getStartTime());
			startTime=converter.getTime().toString();
			converter.setTime((Date) appt.getTimeSpan().getEndTime());
			endTime=converter.getTime().toString();
		} else {
			if (appt.hasReminder()) {
				converter.setTimeInMillis(appt.getReminderTriggerTime().getTimeInMillis()+ appt.getReminderTriggerAheadAmmount());
				startTime=converter.getTime().toString();
				converter.setTimeInMillis(appt.getReminderTriggerTime().getTimeInMillis()+ appt.getReminderTriggerAheadAmmount()+appt.getTimeSpan().getEndTime().getTime()-appt.getTimeSpan().getStartTime().getTime());
				endTime=converter.getTime().toString();
			} else {
				converter.setTime((Date) appt.getEventNextTime(appt.getTimeSpan().getStartTime(),now).getTime());
				startTime=converter.getTime().toString();
				converter.setTime((Date) appt.getEventNextTime(appt.getTimeSpan().getEndTime(),now).getTime());
				endTime=converter.getTime().toString();
			}
		}

		area.setText("Appointment Information \n");
		area.append("Title: " + appt.getTitle() + "\n");
		area.append("Start time: " + startTime + "\n");
		area.append("End time: " + endTime + "\n");
		area.append("Frequency: " + appt.getFrequency() + "\n");
		area.append("Location: " + appt.getLocation() + "\n");
		area.append("Publicity: " + appt.isPublic() + "\n");
		area.append("IsJoint: " + appt.isJoint() + "\n");
		area.append("IsJointScheduled: " + appt.isJointScheduled() + "\n");
		area.append("\nParticipants:\n");
		area.append("  Attend:");
		LinkedList<String> attendList = appt.getAttendList();
		if (attendList != null) {
			for (int i = 0; i < attendList.size(); i++) {
				area.append("  " + attendList.get(i));
			}
		}
		area.append("\n\n  Reject:");
		LinkedList<String> rejectList = appt.getRejectList();
		if (rejectList != null) {
			for (int i = 0; i < rejectList.size(); i++) {
				area.append("  " + rejectList.get(i));
			}
		}
		area.append("\n\n  Waiting:");
		LinkedList<String> waitingList = appt.getWaitingList();
		if (waitingList != null) {
			for (int i = 0; i < waitingList.size(); i++) {
				area.append("  " + waitingList.get(i));
			}
		}

		area.append("\n\nDescription: \n" + appt.getInfo());
		if (appt.hasReminder()) {
			area.append("\n\nReminder Time:\n"
					+ appt.getReminderTriggerTime().getTime().toString());
			area.append("\n\nReminder:\n" + appt.getReminderInfo());
		} else {
			area.append("\n\nNo Reminders.");
		}
		area.setEditable(false);
	}

	public void display(Vector<TimeSpan>[] vs, User[] entities) {
		if (vs == null || entities == null)
			return;
		String temp = vs[0].elementAt(0).getStartTime().toString();
		area.setText("Available Time For Selected participants and room ("
				+ temp.substring(0, temp.lastIndexOf(" ")) + ")\n\n\n");
		String temp1 = null;
		String temp2 = null;

		for (int i = 0; i < entities.length; i++) {
			area.append((i + 1) + ". " + entities[i].getID() + " :\n\n");
			for (int j = 0; j < vs[i].size(); j++) {
				temp1 = vs[i].elementAt(j).getStartTime().toString();
				temp2 = vs[i].elementAt(j).getEndTime().toString();
				area.append("   > From: "
						+ temp1.substring(0, temp1.lastIndexOf(':')) + "  To: "
						+ temp2.substring(0, temp2.lastIndexOf(':')) + "\n");

			}
			area.append("\n");

		}
		area.setEditable(false);

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == exitBut) {
			dispose();
		}
	}

}
