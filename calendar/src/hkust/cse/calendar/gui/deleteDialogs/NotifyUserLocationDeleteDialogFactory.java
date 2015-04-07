package hkust.cse.calendar.gui.deleteDialogs;

import hkust.cse.calendar.apptstorage.ApptStorage;
import hkust.cse.calendar.apptstorage.NotificationStorage;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.User;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;

public class NotifyUserLocationDeleteDialogFactory implements DeleteDialogAbstractFactory {
	private User user;
	private Location location;
	private ApptStorage apptStorage;
	public NotifyUserLocationDeleteDialogFactory(User user,Location location, ApptStorage apptStorage) {
		this.user = user;
		this.location=location;
		this.apptStorage=apptStorage;
	}

	@Override
	public DeleteDialog<Appt> createGUI() {
		List <Appt> affectedAppts = apptStorage.getApptsBelongingToUserWithLocation(user, location);
		DefaultListModel<Appt> listModel=new DefaultListModel<Appt>();
		for (Iterator<Appt> iterator = affectedAppts.iterator(); iterator.hasNext();) {
			listModel.addElement(iterator.next());
		}
		ArrayList<AbstractAction> actions=new ArrayList<AbstractAction>();
		DeleteDialog<Appt> deleteLocationDialog =new DeleteDialog<Appt>("Deleting Location", "Location :"+location.toString()+" Affected Appts:" , listModel,actions, "Ok");
		return deleteLocationDialog;
	}
}
