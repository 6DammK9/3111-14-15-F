package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.UserStorage;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Utility {
	
	// ADDED By Darren: Don't know where to put this function - maybe here
	public static ArrayList<String> purify(ArrayList<String> input){
		ArrayList<String> junk = new ArrayList<String>();
		if (input.size() > 0){
			for (int i0=0;i0<input.size();i0++) {
				if (input.get(i0).length() < UserStorage.USERNAME_AND_PASSWORD_LENGTH_REQUIRED) {
					junk.add(input.get(i0));
				}
			}
			input.removeAll(junk);
		}
		return input;
	}
	
	public static int getNumber(String s) {
		if (s == null)
			return -1;
		if (s.trim().indexOf(" ") != -1) {
			JOptionPane.showMessageDialog(null,
					"Can't Contain Whitespace In Number !");
			return -1;
		}
		int result = 0;
		try {
			result = Integer.parseInt(s);
		} catch (NumberFormatException n) {
			return -1;
		}
		return result;
	}

	public static Appt createDefaultAppt(int currentY, int currentM,
			int currentD, User me) {
		Appt newAppt = new Appt(me);
		newAppt.setID(0);
		Timestamp start = new Timestamp(0);
		start.setYear(currentY);
		start.setMonth(currentM - 1);
		start.setDate(currentD);
		start.setHours(9);
		start.setMinutes(0);

		Timestamp end = new Timestamp(0);
		end.setYear(currentY);
		end.setMonth(currentM - 1);
		end.setDate(currentD);
		end.setHours(9);
		end.setMinutes(30);

		newAppt.setTimeSpan(new TimeSpan(start, end));
		User[] temp = new User[1];
		temp[0] = me;
		// newAppt.setParticipants(temp);
		// Darren: Add me into Attendlist;
		newAppt.addAttendant(me.getID());

		newAppt.setTitle("Untitled");
		newAppt.setInfo("Input description of this appointment");
		newAppt.setLocation(new Location("Empty Venue", 0));
		return newAppt;
	}

	public static Appt createDefaultAppt(int currentY, int currentM,
			int currentD, User me, int startTime) {
		Appt newAppt = new Appt(me);
		newAppt.setID(0);
		Timestamp start = new Timestamp(0);
		start.setYear(currentY);
		start.setMonth(currentM - 1);
		start.setDate(currentD);
		start.setHours(startTime / 60);
		start.setMinutes(startTime % 60);

		int dur = startTime + 60;
		Timestamp end = new Timestamp(0);
		end.setYear(currentY);
		end.setMonth(currentM - 1);
		end.setDate(currentD);
		end.setHours(dur / 60);
		end.setMinutes(dur % 60);

		newAppt.setTimeSpan(new TimeSpan(start, end));
		User[] temp = new User[1];
		temp[0] = me;
		newAppt.addAttendant(me.getID());

		newAppt.setTitle("Untitled");
		newAppt.setInfo("Input description of this appointment");
		newAppt.setLocation(new Location("Empty Venue", 0));
		return newAppt;
	}
}
