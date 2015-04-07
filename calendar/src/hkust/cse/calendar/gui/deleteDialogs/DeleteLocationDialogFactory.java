package hkust.cse.calendar.gui.deleteDialogs;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;

import hkust.cse.calendar.apptstorage.ApptStorage;
import hkust.cse.calendar.apptstorage.NotificationStorage;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;

public class DeleteLocationDialogFactory implements DeleteDialogAbstractFactory {
	private Location location;
	private List<Appt> affectedAppts;
	private NotificationStorage notificationStorage;
	private ApptStorage apptStorage;
	private DeleteDialog<Appt> deleteLocationDialog;
	public DeleteLocationDialogFactory(Location location, List<Appt> affectedAppts, NotificationStorage notificationStorage,ApptStorage apptStorage) {
		this.location=location;
		this.affectedAppts=affectedAppts;
		this.notificationStorage=notificationStorage;
		this.apptStorage=apptStorage;
	}

	@Override
	public DeleteDialog<Appt> createGUI() {
		DefaultListModel<Appt> listModel=new DefaultListModel<Appt>();
		for (Iterator<Appt> iterator = affectedAppts.iterator(); iterator.hasNext();) {
			listModel.addElement(iterator.next());
		}
		ArrayList<AbstractAction> actions=new ArrayList<AbstractAction>();
		actions.add(new NotifyAction());
		//actions.add(new ReplaceAction());
		actions.add(new ForceAction());
		deleteLocationDialog =new DeleteDialog<Appt>("Deleting Location", "Location :"+location.toString() , listModel,actions,"Cancel");
		return deleteLocationDialog;
	}

	@SuppressWarnings("serial")
	private class NotifyAction extends AbstractAction {
		public NotifyAction() {
			putValue(NAME, "Notify");
		}
		public void actionPerformed(ActionEvent e) {
				notificationStorage.addLocation(location,apptStorage);
				location.setToBeDeleted(true);
				notificationStorage.saveNotificationsToXml();
				apptStorage.locationStorage.saveLocationsToXml();
				deleteLocationDialog.dispose();
		}
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
			deleteLocationDialog.dispose();
		}
	}
}
