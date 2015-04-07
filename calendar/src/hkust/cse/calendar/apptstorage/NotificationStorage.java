package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.gui.CalGrid;
import hkust.cse.calendar.gui.deleteDialogs.DeleteDialogAbstractFactory;
import hkust.cse.calendar.gui.deleteDialogs.NotifyAdminDeleteLocationDialogFactory;
import hkust.cse.calendar.gui.deleteDialogs.NotifyUserLocationDeleteDialogFactory;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import com.thoughtworks.xstream.XStream;

public class NotificationStorage {
	private List<User> usersToBeDeleted=new ArrayList<User>();
	private Map<Location,List<User>> locationsToBeDeleted=new HashMap<Location,List<User>>();;
	private List<User> usersToBeDeleted_NOTIFIED=new ArrayList<User>();
	//private Map<Location,List<User>> locationsToBeDeleted_NOTIFIED=new HashMap<Location,List<User>>();;
	public void addUser(User user) {
		if (!usersToBeDeleted.contains(user)) { 
			usersToBeDeleted.add(user);
		}
	}
	public void addLocation(Location location,ApptStorage apptStorage) {
		List<User> users;
		if (locationsToBeDeleted.containsKey(location)) {
			users=locationsToBeDeleted.get(location);
		} else {
			users=new ArrayList<User>();
		}
		for (User user : apptStorage.getUsersWithLocation(location)) {
			if (!users.contains(user)) { 
				users.add(user);
			}
		}
		locationsToBeDeleted.put(location,users);
	}
	private NotificationStorage() {
	}
	public List<User> getUsersToBeDeleted() {
		return usersToBeDeleted;
	}
	public Map<Location, List<User>> getLocationsToBeDeleted() {
		return locationsToBeDeleted;
	}
	public void saveNotificationsToXml() {
		XStream xstream = new XStream();
		xstream.alias("Notifications", NotificationStorage.class);
		xstream.alias("Location", Location.class);
		xstream.alias("User", User.class);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream("Notifications.xml");
			xstream.toXML(this, out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static NotificationStorage notificationsFactory() {
		XStream xstream = new XStream();
		xstream.alias("Notifications", NotificationStorage.class);
		xstream.alias("Location", Location.class);
		xstream.alias("User", User.class);
		try {
			return (NotificationStorage) xstream.fromXML(new File("Notifications.xml"));
		} catch (Exception e) {
			NotificationStorage notificationStorage =new NotificationStorage();
			notificationStorage.saveNotificationsToXml();
			return notificationStorage;
		}
	}
	public void notifyUserLocationsBeingDeleted(User user,ApptStorage apptStorage) {
		for (Location location : locationsToBeDeleted.keySet()) {
			for (Iterator<User> iterator = locationsToBeDeleted.get(location).iterator(); iterator.hasNext();) {
				User affectedUser = iterator.next();
				if (affectedUser.getID().equals(user.getID())) {
					DeleteDialogAbstractFactory notifyUserLocationDeleteDialog= new NotifyUserLocationDeleteDialogFactory(user, location, apptStorage);
					notifyUserLocationDeleteDialog.createGUI();
					iterator.remove();
					break;
				}
			}
		}
		saveNotificationsToXml();
	}
	public void notifyAdminLocationsBeingDeleted(CalGrid parent, ApptStorage apptStorage) {
		for (Iterator<Location> iterator = locationsToBeDeleted.keySet().iterator(); iterator
				.hasNext();) {
			Location location = iterator.next();
			if (locationsToBeDeleted.get(location).isEmpty()) {
				JOptionPane.showMessageDialog(parent, "Deleted location :"+location.toString(),
						"Comfirm",JOptionPane.INFORMATION_MESSAGE);
				apptStorage.replaceAllLocationsWith(location, apptStorage.locationStorage.getLocation("Empty Venue"));
				parent.locationStorage.removeLocation(location);
				iterator.remove();
			} else {
				NotifyAdminDeleteLocationDialogFactory notifyAdminDeleteLocationDeleteDialog= new NotifyAdminDeleteLocationDialogFactory(location, locationsToBeDeleted.get(location), this, apptStorage);
				notifyAdminDeleteLocationDeleteDialog.createGUI();
			}
		}
		saveNotificationsToXml();
	}
	public void notifyUserBeingDeleted(CalGrid parent,User user) {
		for (Iterator<User> iterator = usersToBeDeleted.iterator(); iterator
				.hasNext();) {
			User target = (User) iterator.next();
			if (user.getID().equals(target.getID())) {
				JOptionPane.showMessageDialog(parent,"You are being Deleted", "You are being Deleted",JOptionPane.ERROR_MESSAGE);
				iterator.remove();
				usersToBeDeleted_NOTIFIED.add(user);
			}
		}
	}
	public void notifyAdminUserBeingDeleted(CalGrid calGrid, ApptStorage con) {
		for (User user : usersToBeDeleted_NOTIFIED) {
			if (JOptionPane.showConfirmDialog(calGrid,
					"Delete user :"+user.getID()+" "+user.getLastName()+", "+user.getFirstName()+"?", "Delete user",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			{
				con.deleteUser(user);
				con.dumpStorageToFile();
			}
		}
		usersToBeDeleted_NOTIFIED.clear();
		saveNotificationsToXml();
	}
	public void removeLocation(Location location) {
		locationsToBeDeleted.remove(location);
		saveNotificationsToXml();
	}
	public void removeUserFromAllNotifications(User user) {
		usersToBeDeleted.remove(user);
		usersToBeDeleted_NOTIFIED.remove(user);
		for (Iterator<Entry<Location, List<User>>> iterator = locationsToBeDeleted.entrySet().iterator(); iterator
				.hasNext();) {
			Entry<Location, List<User>> entry = iterator.next();
			for (Iterator<User> iterator2 = entry.getValue().iterator();iterator2.hasNext();) {
				if (iterator2.next().equals(user)) {
					iterator2.remove();
				}
			}
			
		}
	}
}
