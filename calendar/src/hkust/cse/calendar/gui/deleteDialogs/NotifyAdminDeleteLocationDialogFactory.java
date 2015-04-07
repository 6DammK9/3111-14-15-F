package hkust.cse.calendar.gui.deleteDialogs;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;

import hkust.cse.calendar.apptstorage.ApptStorage;
import hkust.cse.calendar.apptstorage.NotificationStorage;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.User;

public class NotifyAdminDeleteLocationDialogFactory implements DeleteDialogAbstractFactory {
	private Location location;
	private ApptStorage apptStorage;
	private DeleteDialog<User> deleteLocationDialog;
	private List<User> affectedUsers;
	public NotifyAdminDeleteLocationDialogFactory(Location location, List<User> affectedUsers, NotificationStorage notificationStorage,ApptStorage apptStorage) {
		this.location=location;
		this.apptStorage=apptStorage;
		this.affectedUsers=affectedUsers;
	}

	@Override
	public DeleteDialog<User> createGUI() {
		DefaultListModel<User> listModel=new DefaultListModel<User>();
		for (User user : affectedUsers) {
			listModel.addElement(user);
		}
		ArrayList<AbstractAction> actions=new ArrayList<AbstractAction>();
		//actions.add(new ReplaceAction());
		actions.add(new ForceAction());
		deleteLocationDialog =new DeleteDialog<User>("Un notified people", "Location :"+location.toString() , listModel,actions,"Cancel");
		return deleteLocationDialog;
	}

	@SuppressWarnings("serial")
	private class ReplaceAction extends AbstractAction {
		public ReplaceAction() {
			putValue(NAME, "Replace");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	@SuppressWarnings("serial")
	private class ForceAction extends AbstractAction {
		public ForceAction() {
			putValue(NAME, "Force");
		}
		public void actionPerformed(ActionEvent e) {
			apptStorage.replaceAllLocationsWith(location, apptStorage.locationStorage.getLocation("Empty Venue"));
			apptStorage.locationStorage.removeLocation(location);
			apptStorage.notificationStorage.removeLocation(location);
			deleteLocationDialog.dispose();
		}
	}
}
