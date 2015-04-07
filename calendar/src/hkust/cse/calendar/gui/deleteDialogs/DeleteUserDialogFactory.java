package hkust.cse.calendar.gui.deleteDialogs;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;

import hkust.cse.calendar.apptstorage.ApptStorage;
import hkust.cse.calendar.apptstorage.NotificationStorage;
import hkust.cse.calendar.unit.User;

public class DeleteUserDialogFactory implements DeleteDialogAbstractFactory {
	private User user;
	private NotificationStorage notificationStorage;
	private ApptStorage apptStorage;
	private DeleteDialog<User> deleteUserDialog;
	public DeleteUserDialogFactory(User user, NotificationStorage notificationStorage, ApptStorage apptStorage) {
		this.user=user;
		this.notificationStorage=notificationStorage;
		this.apptStorage=apptStorage;
	}

	@Override
	public DeleteDialog<User> createGUI() {
		DefaultListModel<User> listModel=new DefaultListModel<User>();
		listModel.addElement(user);
		ArrayList<AbstractAction> actions=new ArrayList<AbstractAction>();
		actions.add(new NotifyAction());
		//actions.add(new ReplaceAction());
		actions.add(new ForceAction());
		deleteUserDialog =new DeleteDialog<User>("Deleting User", "User :"+user.getLastName()+", "+user.getFirstName()+" ("+user.getID()+")" , listModel,actions,"Cancel");
		return deleteUserDialog;
	}

	@SuppressWarnings("serial")
	private class NotifyAction extends AbstractAction {
		public NotifyAction() {
			putValue(NAME, "Notify");
		}
		public void actionPerformed(ActionEvent e) {
			notificationStorage.addUser(user);
			notificationStorage.saveNotificationsToXml();
			deleteUserDialog.dispose();
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
			apptStorage.deleteUser(user);
			deleteUserDialog.dispose();
		}
	}
}
