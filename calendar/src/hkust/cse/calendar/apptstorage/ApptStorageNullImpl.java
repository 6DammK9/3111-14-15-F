package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.unit.Location;

public class ApptStorageNullImpl extends ApptStorage {

	private User defaultUser = null;

	public ApptStorageNullImpl(User user) {
		defaultUser = user;
	}

	@Override
	public void saveAppt(Appt appt) {
		// TODO Auto-generated method stub

	}

	@Override
	public Appt[] retrieveApptsDuring(TimeSpan d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Appt[] retrieveUserApptsDuring(User entity, int ID, TimeSpan time, Boolean create_mode, Location location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Appt retrieveJointAppts(int joinApptID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateSavedAppt(Appt appt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAppt(Appt appt) {
		// TODO Auto-generated method stub

	}

	@Override
	public User getDefaultUser() {
		// TODO Auto-generated method stub
		return defaultUser;
	}

	@Override
	public void loadApptFromXml() {
		// TODO Auto-generated method stub
	}

	@Override
	public int getStorageSize() {
		return 0;
	}

	@Override
	public void dumpStorageToFile() {
		// Do nothing
	}

	//ADDED by Darren : For checking collision of frequent events
	@Override
	public int create_check(User entity, int ID, TimeSpan time, String frequency, Location location) {
		return 0;
	}

	//ADDED by Jimmy: Abstract function for reminder
	@Override
	public void setApptReminderTriggered(Appt appt) {
		// TODO Auto-generated method stub
		
	}

	//ADDED by Darren : For checking next available ID
	@Override
	public int FindNextID(boolean joint){
		return 0;
	}
	
	//ADDED by Darren : Export whole HashMap
	public Appt[] ExportHashMap(){
		return null;
	}
	
	//ADDED by Darren : Get that specific Appt
	@Override
	public Appt getAppt (String key) {
		return null;
	}
}
