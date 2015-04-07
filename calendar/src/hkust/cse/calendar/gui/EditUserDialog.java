package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.User;

public class EditUserDialog extends UserDialog {
	private User user;
	private boolean editingUserIsAdmin;

	EditUserDialog(User user, boolean editingUserIsAdmin) {
		this.user = user;
		this.editingUserIsAdmin = editingUserIsAdmin;
		init();
	}

	public void init() {
		save = new Save("Save");
		if (editingUserIsAdmin) {
			userType = new UserType(true);
		} else {
			userType = new UserType(false);
		}
		setupCommonGUI();
		setTitle("Edit");
		admin = editingUserIsAdmin;
		if (editingUserIsAdmin) {
			instructions.setText("Hi admin :)");
		} else {
			instructions.setText("Hi normal :)");
		}
		if (user == null) {
			makingNewUser(true, null);
		} else {
			makingNewUser(false, user);
		}
		pack();
		setLocationRelativeTo(null);
		setVisible(true);

	}

}
