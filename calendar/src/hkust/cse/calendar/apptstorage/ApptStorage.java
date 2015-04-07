package hkust.cse.calendar.apptstorage;//

import hkust.cse.calendar.gui.DetailsDialog;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Time;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public abstract class ApptStorage {

	// /public HashMap mAppts; //a hashmap to save every thing to it, write to
	// memory by the memory based storage implementation
	public HashMap<String, Appt> mAppts = new HashMap<String, Appt>();
	public User defaultUser; // a user object, now is single user mode without
								// login
	public int mAssignedApptID; // a global appointment ID for each appointment
								// record
	public LocationStorage locationStorage;
	//ADDED by Jimmy: To store pending reminders after each save/edit/delete so that no need to do it every time increment.
	private List<Appt> pendingReminders = new ArrayList<Appt>();

	public UserStorage userStorage;
	public NotificationStorage notificationStorage;
	public ApptStorage() { // default constructor
	}

	public abstract void saveAppt(Appt appt); // abstract method to save an
												// appointment record

	public abstract Appt[] retrieveApptsDuring(TimeSpan d); // abstract method to
														// retrieve an
														// appointment record by
														// a given timespan

	public abstract Appt[] retrieveUserApptsDuring(User entity, int ID, TimeSpan time, Boolean create_mode, Location location); // overloading
																		// abstract
																		// method
																		// to
																		// retrieve
																		// an
																		// appointment
																		// record
																		// by a
																		// given
																		// user
																		// object
																		// and
																		// timespan

	public abstract Appt retrieveJointAppts(int joinApptID); // overload method to
														// retrieve appointment
														// with the given joint
														// appointment id

	public abstract void updateSavedAppt(Appt appt); // abstract method to update an
												// appointment record

	public abstract void removeAppt(Appt appt); // abstract method to remove an
												// appointment record

	public User getDefaultUser() {
		return defaultUser;
	}

	public abstract void loadApptFromXml(); // abstract method to load
											// appointment from xml reocrd into
											// hash map

	/*
	 * Add other methods if necessary
	 */

	public abstract int getStorageSize();

	public abstract void dumpStorageToFile();

	//ADDED by Darren : For checking collision of frequent events
	public abstract int create_check(User entity, int ID, TimeSpan time,
			String frequency, Location location);
	//ADDED by Jimmy: Abstract functions for reminders 
	public abstract void setApptReminderTriggered(Appt appt);

	// ADDED by Jimmy: Generates reminders which are after the current time and
	// un-triggered reminders
	
	protected void generatePendingReminderList() {
		pendingReminders.clear();
		for (Appt userAppointment : mAppts.values()) {
			if (userAppointment.hasReminder() && ! userAppointment.isReminderDone()) {
				pendingReminders.add(userAppointment);
			}
		}

	}
	public void clearPendingReminders() {
		pendingReminders.clear();
	}
	// ADDED by Jimmy: popup the reminders and set them to be done
	public void popupReminders(Calendar now) {
		for (Appt apptWithReminder : pendingReminders) {
			while (!apptWithReminder.isReminderDone()
					&& Time.getTime().after(
							apptWithReminder.getReminderTriggerTime())) {
				new DetailsDialog(apptWithReminder, "Reminder",now);
				setApptReminderTriggered(apptWithReminder);
			}
		}
	}

	public UserStorage getUserStorage() {
		return userStorage;
	}

	public void setUserStorage(UserStorage userStorage) {
		this.userStorage = userStorage;
	}
	
	//ADDED by Darren : For checking next available ID
	public abstract int FindNextID(boolean Joint);
	
	//ADDED by Darren : Export all Appts to an Array
	public abstract Appt[] ExportHashMap();
	
	//ADDED by Darren : Get that specific Appt
	public abstract Appt getAppt (String key);

	public ArrayList<Appt> getApptsBelongingToUser(User user) {
		ArrayList<Appt> apptsAffected= new ArrayList<Appt>();  
		for (Appt appt : mAppts.values()) {
			if (appt.getOwner().getID().equals(user.getID())) {
				apptsAffected.add(appt);
			}
		}
		return apptsAffected;
	}
	
	//Added by Jimmy : for use with deleting location
	public ArrayList<Appt> getApptsWithLocation(Location location) {
		ArrayList<Appt> apptsAffected= new ArrayList<Appt>();  
		for (Appt appt : mAppts.values()) {
			if (appt.getLocation().equals(location)) {
				apptsAffected.add(appt);
			}
		}
		return apptsAffected;
	}
	public ArrayList<User> getUsersWithLocation(Location location) {
		ArrayList<Appt> apptsAffected= getApptsWithLocation(location);
		ArrayList<String> stringsAffected= new ArrayList<String>();
		for (Appt appt : apptsAffected) {
			if (!stringsAffected.contains(appt.getOwner().getID())) {
				stringsAffected.add(appt.getOwner().getID());
			}
		}
		ArrayList<User> usersAffected= new ArrayList<User>();
		for (String id : stringsAffected) {
			usersAffected.add(userStorage.UserFromString(id));
		}
		return usersAffected;
	}
	public ArrayList<Appt> getApptsBelongingToUserWithLocation(User user,Location location) {
		ArrayList<Appt> apptsAffected= new ArrayList<Appt>();  
		for (Appt appt : mAppts.values()) {
			if (appt.getOwner().getID().equals(user.getID())&&appt.getLocation().equals(location)) {
				apptsAffected.add(appt);
			}
		}
		return apptsAffected;
	}
	public void replaceAllLocationsWith(Location oldLocation,Location newLocation ) {
		for (Appt appt : mAppts.values()) {
			if (appt.getLocation().equals(oldLocation)) {
				appt.setLocation(newLocation);
				saveAppt(appt);
			}
		}
	}
	public void deleteUser (User user) {
		for (Iterator<Entry<String, Appt>> iterator = mAppts.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Appt> entry = iterator.next();
			if (entry.getValue().getOwner().getID().equals(user.getID())) {
				iterator.remove();
			}
		}
		userStorage.removeUser(user);
		notificationStorage.removeUserFromAllNotifications(user);
	}
}
