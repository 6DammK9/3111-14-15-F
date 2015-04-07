package hkust.cse.calendar.unit;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {

	private String mPassword; // User password
	private String mID; // User id
	private String mType; // ADDED by Darren: User Type (Regular User/ Administrator)
	private String mFirstName;// Added by Kathy
	private String mLastName;// Added by Kathy
	private String mEmail;// Added by Kathy
	// Getter of the user id
	public String getID() {
		return mID;
	}

	// Constructor of class 'User' which set up the user id and password
	public User(String id, String pass, String type, String firstName, String lastName ,String email) {
		mID = id;
		mPassword = pass;
		mType = type;
		mFirstName=firstName;
		mLastName=lastName;
		mEmail=email;
	}

	// Another getter of the user id
	public String toString() {
		return getID();
	}

	// Getter of the user password
	public String getPassword() {
		return mPassword;
	}

	// Setter of the user password
	public void setPassword(String pass) {
		mPassword = pass;
	}
	
	// ADDED by Darren: Getter of user Type
	public String getType() {
		return mType;
	}
	
	// ADDED by Darren: Setter of the user Type
	public void setType(String type) {
		mType = type;
	}

	public String getFirstName() {
		return mFirstName;
	}

	public void setFirstName(String mFirstName) {
		this.mFirstName = mFirstName;
	}

	public String getLastName() {
		return mLastName;
	}

	public void setLastName(String mLastName) {
		this.mLastName = mLastName;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String mEmail) {
		this.mEmail = mEmail;
	}
	public boolean equals(Object other) {
		if (other == null) {return false;}
		if (other == this) {return true;}
		if (!(other instanceof User)) {return false;}
		return mID.equals(((User)other).mID);
	}
	public int hashCode() {
		return mID.hashCode();
	}
}
