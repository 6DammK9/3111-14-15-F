package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStoreToMemory;
import hkust.cse.calendar.apptstorage.LocationStorage;
import hkust.cse.calendar.apptstorage.UserStorage;
import hkust.cse.calendar.gui.UserDialog;
import hkust.cse.calendar.unit.User;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginDialog extends JFrame implements ActionListener {
	
	/**
	 * Added by Darren: Stuffs added in Phase 2
	 * Don't know why serialVersionUID is needed
	 */
	private static final long serialVersionUID = 1L;
	public UserStorage _controller = new UserStorage();
	//public HashMap<String, User> mUsers= new HashMap<String, User>(); //Use for better access time
	
	private JTextField userName;
	private JPasswordField password;
	private JButton button;
	private JButton closeButton;
	private JButton signupButton;

	public LoginDialog() // Create a dialog to log in
	{
		setTitle("Log in");
		UserDialog.set_parent(this);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		Container contentPane;
		contentPane = getContentPane();

		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

		JPanel messPanel = new JPanel();
		messPanel.add(new JLabel(
				"Please input your user name and password to log in."));
		top.add(messPanel);

		JPanel namePanel = new JPanel();
		namePanel.add(new JLabel("User Name:"));
		userName = new JTextField(15);
		namePanel.add(userName);
		top.add(namePanel);

		JPanel pwPanel = new JPanel();
		pwPanel.add(new JLabel("Password:  "));
		password = new JPasswordField(15);
		pwPanel.add(password);
		top.add(pwPanel);

		JPanel signupPanel = new JPanel();
		signupPanel.add(new JLabel(
				"If you don't have an account, please sign up:"));
		signupButton = new JButton("Sign up now");
		signupButton.addActionListener(this);
		signupPanel.add(signupButton);
		top.add(signupPanel);

		contentPane.add("North", top);

		JPanel butPanel = new JPanel();
		butPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		button = new JButton("Log in");
		button.addActionListener(this);
		butPanel.add(button);

		closeButton = new JButton("Close program");
		closeButton.addActionListener(this);
		butPanel.add(closeButton);

		contentPane.add("South", butPanel);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		getRootPane().setDefaultButton(button);
		
		// After creating Dialog
		_controller.loadUserFromXml();
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == button) {
			// When the button is clicked, check the user name and password, and
			// try to log the user in

			User user = login(); // Check if ID and password are both present and correct 
			//User user = new User("noname", "nopass", "Regular User");
			if (user != null) {
				setVisible(false);
				CalGrid grid = new CalGrid(new ApptStoreToMemory(user, _controller,LocationStorage.locationsFactory()), this);
				//setVisible(true);
			}
			//else {JOptionPane.showMessageDialog(null, "Login failed.");}
			
		} else if (e.getSource() == signupButton) {
			UserDialogFactory userDialogFactory= new UserDialogFactory();
			userDialogFactory.createDialog("SignUp");
		} else if (e.getSource() == closeButton) {
			int n = JOptionPane.showConfirmDialog(null, "Exit Program ?",
					"Confirm", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION)
				System.exit(0);
		}
	}

	// This method checks whether a string is a valid user name or password, as
	// they can contains only letters and numbers
	public static boolean isValidString(String s) {
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

	
	// ADDED by Darren: Return corresponding User if ID and password are both present and correct
	private User login() {
		User temp = _controller.UserFromString(userName.getText());
		if (temp != null) {
			if (temp.getPassword().equals(password.getText())) {
				return temp;
			} else {
				//System.out.println(password.getText() + " " + temp.getPassword());
				JOptionPane.showMessageDialog(null, "Password incorrect!");
				return null;
			}
		} else {
			JOptionPane.showMessageDialog(null, "Username not found!");
			return null;
		}
	}
}
