package hkust.cse.calendar.gui;

public class SignUpDialog extends UserDialog {
	SignUpDialog() {
		init();
	}

	public void init() {
		save = new Save("Sign Up");
		userType = new UserType(false);
		setupCommonGUI();
		setTitle("Sign Up");
		instructions
				.setText("Please input your user name and password to sign up.");
		makingNewUser(true, null);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
