package hkust.cse.calendar.gui;

import hkust.cse.calendar.gui.UserDialog.Save;
import hkust.cse.calendar.gui.UserDialog.UserType;
import hkust.cse.calendar.unit.User;

public class UserDialogFactory {
	// public UserDialog createDialog();
	// private User user;
	// private boolean editingUserIsAdmin;

	UserDialogFactory() {

	}

	public UserDialog createDialog(String input) {
		return createDialog(input, null, false);
	}

	public UserDialog createDialog(String s, User user,
			boolean editingUserIsAdmin) {
		UserDialog userDialog = null;
		if (s.equals("SignUp")) {
			userDialog = new SignUpDialog();

		} else if (s.equals("Edit")) {
			userDialog = new EditUserDialog(user, editingUserIsAdmin);

		}
		return userDialog;
	}
}
