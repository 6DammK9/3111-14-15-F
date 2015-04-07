package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.UserStorage;
import hkust.cse.calendar.unit.User;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.AbstractAction;
import javax.swing.Action;

public abstract class UserDialog extends JFrame  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static LoginDialog _parent;
	protected JLabel instructions;
	private JTextField userName;
	private JPasswordField password;
	private JButton button;
	private JButton closeButton;
	private JButton changeButton;
	private JTextField email;
	private JTextField lastName;
	private JTextField firstName;
	private boolean isNew;
	protected boolean admin;
	protected Action userType =null;
	protected Action save = null;
	private final Action cancel = new Cancel();
	private static int email_length_required = 3;

	public UserDialog () {
	}
	public void setupCommonGUI() // Create a dialog to edit user info
	{

		Container contentPane;
		contentPane = getContentPane();
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		
		//message panel
		JPanel messPanel = new JPanel();
		instructions=new JLabel(".");
		messPanel.add(instructions);
		top.add(messPanel);
		//first name
		JPanel pnlFisrtName = new JPanel();
		top.add(pnlFisrtName);
		
		JLabel lblFirstName = new JLabel("First name :");
		pnlFisrtName.add(lblFirstName);
		
		firstName = new JTextField(15);
		pnlFisrtName.add(firstName);
		//last name
		JPanel pnlLastName = new JPanel();
		top.add(pnlLastName);
		
		JLabel lblLastName = new JLabel("Last Name :");
		pnlLastName.add(lblLastName);
		
		lastName = new JTextField(15);
		pnlLastName.add(lastName);
		
		//user name
		JPanel namePanel = new JPanel();
		namePanel.add(new JLabel("User Name:"));
		userName = new JTextField(15);
		namePanel.add(userName);
		top.add(namePanel);
		
		//password
		JPanel pwPanel = new JPanel();
		pwPanel.add(new JLabel("Password:  "));
		password = new JPasswordField(15);
		pwPanel.add(password);
		top.add(pwPanel);
		
		//user type
		JPanel signupPanel = new JPanel();
		signupPanel.add(new JLabel(
				"User type (Regular/ Administrator):"));
		changeButton = new JButton("Regular User"); // Initially Regular user
		changeButton.setAction(userType);
		
		//email
		JPanel pnlEmail = new JPanel();
		top.add(pnlEmail);
		
		JLabel lblEmail = new JLabel("E-mail :");
		pnlEmail.add(lblEmail);
		
		email = new JTextField(15);
		pnlEmail.add(email);
		//signupButton.setEnabled(false); // Disable button when needed
		signupPanel.add(changeButton);
		top.add(signupPanel);

		contentPane.add("North", top);
		
		//button
		JPanel butPanel = new JPanel();
		butPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		button = new JButton();
		button.setAction(save);
		butPanel.add(button);

		closeButton = new JButton("Cancel");
		closeButton.setAction(cancel);
		butPanel.add(closeButton);

		contentPane.add("South", butPanel);

	}
	protected void makingNewUser (boolean isNew,User user) {
		//to identify whether the user is a new user or not
		//yes: for sign up		no: for edit
		this.isNew=isNew;
		if (isNew) {
			userName.setEnabled(true);
			changeButton.setText("Regular User");
		} else {
			userName.setEnabled(false);
			userName.setText(user.getID());
			firstName.setText(user.getFirstName());
			lastName.setText(user.getLastName());
			email.setText(user.getEmail());
			if (user.getType().equals("Regular User")) {changeButton.setText("Regular User");} else {changeButton.setText("Administrator");}
		}
	}
	// This method checks whether a string is a valid user name or password, as
	// they can contains only letters and numbers
	// At least 3 characters long!
	public static boolean isValidString(String s) {
		if (s.length() < UserStorage.USERNAME_AND_PASSWORD_LENGTH_REQUIRED) {return false;}
		
		char[] sChar = s.toCharArray();
		for (int i = 0; i < sChar.length; i++) {
			int sInt = (int) sChar[i];
			if (sInt < 48 || sInt > 122)
				return false;
			if (sInt > 57 && sInt < 65)
				return false;
			if (sInt > 90 && sInt < 97)
				return false;
		}
		return true;
	}
	//check email input
	//if the input email has length less than 3, or without "@" & ".", this input is invalid
	public static boolean isValidEmail(String input) {
		if (input.length() < email_length_required || !input.contains("@") || !input.contains(".")){
			return false;
		}
		return true;
	}

	public static LoginDialog get_parent() {
		return _parent;
	}

	public static void set_parent(LoginDialog _parent) {
		UserDialog._parent = _parent;
	}
	protected class UserType extends AbstractAction {
		public UserType(boolean enabled) {
			putValue(NAME, "Regular User"); //Darren: Regular User instead of User Type?
			setEnabled(enabled);
		}
		public void actionPerformed(ActionEvent e) {
			if (changeButton.getText().equals("Regular User")) {changeButton.setText("Administrator");}
			else {changeButton.setText("Regular User");}
		}
	}
	protected class Save extends AbstractAction {
		public Save(String displayText) {
			putValue(NAME, displayText);
		}
		public void actionPerformed(ActionEvent e) {
			if( isValidString(userName.getText()) && isValidString(password.getText()) ) {
				if (!isValidEmail(email.getText())){
					JOptionPane.showMessageDialog(null, "Email is not vaild.");
					return;
				}
				User user = get_parent()._controller.UserFromString(userName.getText());
				if (isNew) {
					if (user != null) {
						JOptionPane.showMessageDialog(null, "Username exists. Choose another one.");
						return;//added to prevent the case that modify the existing user's info
					}
				} else {
					if (!admin && user == null) {
						JOptionPane.showMessageDialog(null, "Creating a new user is not allowed.");
						return;
					}
				}
				if (user != null) {
					user.setPassword(password.getText());
					user.setType(changeButton.getText());
					user.setFirstName(firstName.getText());
					user.setLastName(lastName.getText());
					user.setEmail(email.getText());
					JOptionPane.showMessageDialog(null, "User information updated.");
				} else {
					user = new User(userName.getText(), password.getText(), changeButton.getText(),firstName.getText(),lastName.getText(),email.getText());
					JOptionPane.showMessageDialog(null, "User information created.");
				}
				get_parent()._controller.SaveUser(user);
				get_parent()._controller.saveUserToXml();
				setVisible(false);
				//reset
				firstName.setText("");
				lastName.setText("");
				userName.setText("");
				password.setText("");
				email.setText("");
				//System.out.println("Username: " + userName.getText());
				//System.out.println("Password: " + password.getText());
				//System.out.println("UserType: " + changeButton.getText());
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "User information is not vaild.");
			}
		}
	}
	private class Cancel extends AbstractAction {
		public Cancel() {
			putValue(NAME, "Cancel");
		}
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
}
