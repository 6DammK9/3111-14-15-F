package hkust.cse.calendar.unit;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Calendar;
import java.sql.Timestamp;
import java.util.Date;

public class Appt implements Serializable {
	private User owner;
	private TimeSpan timeSpan; // Include day, start time and end time of the
								// appointments

	private String title; // The Title of the appointments

	private String eventInfo; // Store the content of the appointments description

	private int apptID; // The appointment id

	private int joinApptID; // The join appointment id

	private boolean isJoint; // The appointment is a joint appointment
	
	private boolean isJointScheduled; // The appointment is a joint appointment AND it is Scheduled

	private LinkedList<String> attendants; // The Attendant list

	private LinkedList<String> rejectors; // The reject list

	private LinkedList<String> pending; // The waiting list

	private Location location; // Event Location

	private String frequency; // Event frequency

	//ADDED by Jimmy: reminder triggered flag
	private boolean isReminderTriggered; // true= reminder has been triggered, false
									// = reminder has not yet been triggered

	//ADDED by Jimmy: have reminder flag
	private boolean isHaveReminder; // true= have reminder. false= no reminder

	//ADDED by Jimmy: reminder trigger time
	private Calendar reminderTriggerTime; // When the reminder is to be
											// triggered

	//ADDED by Jimmy: reminder description
	private String reminderInfo; // The reminder Info

	//ADDED by Jimmy: reminder time ahead
	private long reminderTriggerAheadAmmount; // The amount of time in milliseconds before
									// the event the reminder is to be
									// triggered.
	
	//ADDED by Darren: can be seen by others
	private boolean publicity; // true = can be seen by others

	public Appt(User owner) { // A default constructor used to set all the attribute to
					// default values
		this.owner=owner;
		apptID = 0;
		timeSpan = null;
		title = "Untitled";
		eventInfo = "";
		isJoint = false;
		attendants = new LinkedList<String>();
		rejectors = new LinkedList<String>();
		pending = new LinkedList<String>();
		joinApptID = -1;
		location = null;
		frequency = "Once";
		isReminderTriggered = true;
		isHaveReminder = false;
		reminderTriggerTime = null;
		reminderInfo = null;
		reminderTriggerAheadAmmount = 0;
		publicity = false;
		isJointScheduled = false;
	}

	// Getter of the mTimeSpan
	public TimeSpan getTimeSpan() {
		return timeSpan;
	}

	// Getter of the appointment title
	public String getTitle() {
		return title;
	}

	// Getter of appointment description
	public String getInfo() {
		return eventInfo;
	}

	// Getter of the appointment id
	public int getID() {
		return apptID;
	}

	// Getter of the join appointment id
	public int getJoinID() {
		return joinApptID;
	}

	public void setJoinID(int joinID) {
		this.joinApptID = joinID;
	}

	// Getter of the attend LinkedList<String>
	public LinkedList<String> getAttendList() {
		return attendants;
	}

	// Getter of the reject LinkedList<String>
	public LinkedList<String> getRejectList() {
		return rejectors;
	}

	// Getter of the waiting LinkedList<String>
	public LinkedList<String> getWaitingList() {
		return pending;
	}

	public LinkedList<String> getAllPeople() {
		LinkedList<String> allList = new LinkedList<String>();
		allList.addAll(attendants);
		allList.addAll(rejectors);
		allList.addAll(pending);
		return allList;
	}

	public void addAttendant(String addID) {
		if (attendants == null)
			attendants = new LinkedList<String>();
		attendants.add(addID);
	}

	public void addReject(String addID) {
		if (rejectors == null)
			rejectors = new LinkedList<String>();
		rejectors.add(addID);
	}

	public void addWaiting(String addID) {
		if (pending == null)
			pending = new LinkedList<String>();
		pending.add(addID);
	}

	public void setWaitingList(LinkedList<String> waitingList) {
		pending = waitingList;
	}

	public void setWaitingList(String[] waitingList) {
		LinkedList<String> tempLinkedList = new LinkedList<String>();
		if (waitingList != null) {
			for (int a = 0; a < waitingList.length; a++) {
				tempLinkedList.add(waitingList[a].trim());
			}
		}
		pending = tempLinkedList;
	}

	public void setRejectList(LinkedList<String> rejectLinkedList) {
		rejectors = rejectLinkedList;
	}

	public void setRejectList(String[] rejectList) {
		LinkedList<String> tempLinkedList = new LinkedList<String>();
		if (rejectList != null) {
			for (int a = 0; a < rejectList.length; a++) {
				tempLinkedList.add(rejectList[a].trim());
			}
		}
		rejectors = tempLinkedList;
	}

	public void setAttendList(LinkedList<String> attendLinkedList) {
		attendants = attendLinkedList;
	}

	public void setAttendList(String[] attendList) {
		LinkedList<String> tempLinkedList = new LinkedList<String>();
		if (attendList != null) {
			for (int a = 0; a < attendList.length; a++) {
				tempLinkedList.add(attendList[a].trim());
			}
		}
		attendants = tempLinkedList;
	}

	// Setter of the appointment title
	public String toString() {
		return title;
	}

	// Setter of the appointment title
	public void setTitle(String t) {
		title = t;
	}

	// Setter of the appointment description
	public void setInfo(String in) {
		eventInfo = in;
	}

	// Setter of the mTimeSpan
	public void setTimeSpan(TimeSpan d) {
		timeSpan = d;
	}

	// Setter of the appointment id
	public void setID(int id) {
		apptID = id;
	}

	// check whether this is a joint appointment
	public boolean isJoint() {
		return isJoint;
	}

	// setter of the isJoint
	public void setJoint(boolean isjoint) {
		this.isJoint = isjoint;
	}
	
	// getter of location
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	//ADDED by Kathy
	// getter of frequency
	public String getFrequency() {
		return frequency;
	}
	//ADDED by Kathy
	// setter of frequency
	public void setFrequency(String newFrequency) {
		frequency = newFrequency;
	}

	// ADDED by Jimmy: Returns if the reminder has been triggered already
	public boolean isReminderDone() {
		return isReminderTriggered;
	}

	// ADDED by Jimmy: Flag to show if there is a reminder for this event
	public boolean hasReminder() {
		return isHaveReminder;
	}

	// ADDED by Jimmy: Return amount of time before to trigger reminder as a
	// Calendar object
	public Calendar getReminderTriggerTime() {
		return reminderTriggerTime;
	}

	// ADDED by Jimmy: Returns reminder description
	public String getReminderInfo() {
		return reminderInfo;
	}

	// ADDED by Jimmy: Return amount of time in milliseconds before to trigger
	// reminder
	public long getReminderTriggerAheadAmmount() {
		return reminderTriggerAheadAmmount;
	}

	// ADDED by Jimmy: Obtain amount of days before to trigger reminder
	public long getReminderTriggerAheadDays() {
		return reminderTriggerAheadAmmount / 1000 / 86400;
	}

	// ADDED by Jimmy: Obtain amount of hours before to trigger reminder
	public long getReminderTriggerAheadHours() {
		return reminderTriggerAheadAmmount / 1000 % 86400 / 3600;
	}

	// ADDED by Jimmy: Obtain amount of minutes before to trigger reminder
	public long getReminderTriggerAheadMinutes() {
		return reminderTriggerAheadAmmount / 1000 % 3600 / 60;
	}

	// ADDED by Jimmy: Obtain amount of seconds before to trigger reminder
	public long getReminderTriggerAheadSeconds() {
		return reminderTriggerAheadAmmount / 1000 % 60;
	}

	// ADDED by Jimmy: Delete reminder
	public void unSetReminder() {
		isReminderTriggered = true;
		isHaveReminder = false;
		reminderTriggerTime = null;
		reminderInfo = null;
		reminderTriggerAheadAmmount = 0;
	}

	// ADDED by Jimmy: Set reminder
	public void setReminder(long timeAhead, String info) {
		isReminderTriggered = false;
		isHaveReminder = true;
		reminderInfo = info;
		reminderTriggerAheadAmmount = timeAhead;
		reminderTriggerTime = new GregorianCalendar();
		reminderTriggerTime.setTimeInMillis(timeSpan.getStartTime().getTime()
				- reminderTriggerAheadAmmount);
	}

	// ADDED by Jimmy: Flag to show if the reminder has been triggered
	public void setReminderDone() {
		if (frequency.equals("Once")) {
			isReminderTriggered = true;
		} else {
			reminderTriggerTime= getNextReminderTriggerTime();
		}
	}

	public Calendar getEventNextTime(Timestamp time, Calendar now) {
		Calendar nextTime=new GregorianCalendar ();
		nextTime.setTime((Date) time);
		if (frequency.equals("Once")) {
			throw new Error("You are at Appt.getEventNextTime(), comparing the frequency, and it returned once. You should not be here.");
		} else if (frequency.equals("Daily")) {
			nextTime.set(Calendar.YEAR, now.get(Calendar.YEAR));
			nextTime.set(Calendar.DAY_OF_YEAR, now.get(Calendar.DAY_OF_YEAR));
			return nextTime;
		} else if (frequency.equals("Weekly")) {
			nextTime.set(Calendar.YEAR, now.get(Calendar.YEAR));
			nextTime.set(Calendar.WEEK_OF_YEAR, now.get(Calendar.WEEK_OF_YEAR));
			if (now.after(nextTime)) {
				nextTime.add(Calendar.WEEK_OF_YEAR, 1);
			}
			return nextTime;
		} else if (frequency.equals("Monthly")) {
			int count = -1;
			int before_value;
			nextTime.set(Calendar.YEAR, now.get(Calendar.YEAR));
			nextTime.set(Calendar.MONTH, now.get(Calendar.MONTH));
			GregorianCalendar day_value = new GregorianCalendar();
			while (true) {
				count++;
				day_value.setTime(now.getTime());
				before_value=nextTime.get(Calendar.DAY_OF_MONTH);
				day_value.add(Calendar.MONTH, count);
				if (day_value.get(Calendar.DAY_OF_MONTH) != before_value) {
					continue;
				}
				nextTime.add(Calendar.MONTH, count);
				return nextTime;
			}
		} else {
			//Should not be here.
			throw new Error("You are at Appt.getEventNextTime(), comparing the frequency, and was not any of the above chocices.You should not be here. Pls add new choices or check your spelling.");
		}
	}

	private Calendar getNextReminderTriggerTime() {
		if (frequency.equals("Once")) {
			//Should not be here.
			throw new Error("You are at Appt.getNextReminderTriggerTime(), comparing the frequency, and it returned once. You should not be here.");
		} else if (frequency.equals("Daily")) {
			reminderTriggerTime.add(Calendar.DAY_OF_YEAR, 1);
			return reminderTriggerTime;
		} else if (frequency.equals("Weekly")) {
			reminderTriggerTime.add(Calendar.WEEK_OF_YEAR, 1);
			return reminderTriggerTime;
		} else if (frequency.equals("Monthly")) {
			int count = 0;
			int before_value;
			GregorianCalendar day_value = new GregorianCalendar();
			while (true) {
				count++;
				day_value.setTime(reminderTriggerTime.getTime());
				before_value=reminderTriggerTime.get(Calendar.DAY_OF_MONTH);
				day_value.add(Calendar.MONTH, count);
				if (day_value.get(Calendar.DAY_OF_MONTH) != before_value) {
					continue;
				}
				reminderTriggerTime.add(Calendar.MONTH, count);
				return reminderTriggerTime;
			}
		} else {
			//Should not be here.
			throw new Error("You are at Appt.getNextReminderTriggerTime(), comparing the frequency, and was not any of the above chocices.You should not be here. Pls add new choices or check your spelling.");
		}
	}

	// ADDED by Jimmy: set reminder description
	public void setReminderInfo(String in) {
		reminderInfo = in;
	}
	
	// ADDED by Darren: check whether this is visible by public
	public boolean isPublic() {
		return publicity;
	}

	// ADDED by Darren: set publicity
	public void setPublic (boolean pub) {
		this.publicity = pub;
	}

	// ADDED by Darren: isJointScheduled
	public boolean isJointScheduled() {
		return isJointScheduled;
	}
	
	// ADDED by Darren: isJointScheduled
	public void setJointScheduled(boolean input) {
		isJointScheduled = input;
	}
	
	public boolean IsAllAttended() {
		if ((rejectors.size() == 0) && (pending.size() == 0)) {
			return true;
		}
		return false;
	}

	public User getOwner() {
		return owner;
	}
}
