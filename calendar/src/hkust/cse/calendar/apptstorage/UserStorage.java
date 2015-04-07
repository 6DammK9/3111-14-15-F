package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.User;

import java.io.File;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UserStorage {
	public final static int USERNAME_AND_PASSWORD_LENGTH_REQUIRED = 3; //Wow so long
	
	private HashMap<String, User> mUsers= new HashMap<String, User>(); //Use for better access time

	public UserStorage(){
		//Empty default constructor
	}
	public void removeUser(User user) {
		mUsers.remove(user.getID());
		saveUserToXml();
	}
	// ADDED by Darren: Return User from given ID; return null if not found
	public User UserFromString (String ID) {
		return mUsers.get(ID);
	}
	
	// ADDED by Darren: Save User to hashmap with given user	
	public void SaveUser (User user){
		// Just being careful
		if (user != null) {
			if (user.getID() != null) {
				mUsers.put(user.getID(), user);
			}
		}
	}
	
	// ADDED by Darren: Return whole hashmap as an Array
	public User[] ExportHashMap(){
		return mUsers.values().toArray(new User[mUsers.size()]);
	}
	
	// ADDED by Darren: Load User Data from XML
	// TODO
	public void loadUserFromXml() {
		//the code for xml saving
		try {

			File fXmlFile = new File("user.xml");
			if(!fXmlFile.exists()) {
				return;
			} 
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			//System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

			//xUsers
			Node xUsers = doc.getElementsByTagName("Users").item(0);
			//System.out.println("\nCurrent Element: " + xUsers.getNodeName());	

			//xUser
			NodeList xUser = doc.getElementsByTagName("User");
			for (int i0 = 0; i0 < xUser.getLength(); i0++) {
				//System.out.println(xUser.item(i0).getNodeName() + ": " + xUser.item(i0).getTextContent());
				Element eElement = (Element) xUser.item(i0);
				String id = eElement.getAttribute("ID");
				String pass = eElement.getAttribute("Password");
				String type = eElement.getAttribute("Type");
				String firstName = eElement.getAttribute("FirstName");
				String lastName = eElement.getAttribute("LastName");
				String email = eElement.getAttribute("Email");
				User temp = new User(id,pass,type,firstName,lastName,email);
				mUsers.put(id, temp);
				System.out.println(id+"-"+pass+"-"+type);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ADDED by Darren: Save User Data to XML
	public void saveUserToXml() {
		//TODO
		try {

			// from memory
			User[] Users = mUsers.values().toArray(new User[mUsers.size()]);

			// DocumentBuilder
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// xUsers (ROOT of XML)
			Document doc = docBuilder.newDocument();
			Element xUsers = doc.createElement("Users");
			doc.appendChild(xUsers);

			if (Users != null) {
				if (Users.length > 0) {
					for (int i0 = 0; i0 < Users.length; i0++) {
						// xUser
						Element xUser = doc.createElement("User");
						xUsers.appendChild(xUser);

						// ID
						Attr id = doc.createAttribute("ID");
						id.setValue(Users[i0].getID());
						xUser.setAttributeNode(id);

						// Password
						Attr pass = doc.createAttribute("Password");
						pass.setValue(Users[i0].getPassword());
						xUser.setAttributeNode(pass);

						// Type
						Attr type = doc.createAttribute("Type");
						type.setValue(Users[i0].getType());
						xUser.setAttributeNode(type);

						//FirstName
						Attr firstName= doc.createAttribute("FirstName");
						firstName.setValue(Users[i0].getFirstName());
						xUser.setAttributeNode(firstName);

						//LastName
						Attr lastName= doc.createAttribute("LastName");
						lastName.setValue(Users[i0].getLastName());
						xUser.setAttributeNode(lastName);

						//Email
						Attr email= doc.createAttribute("Email");
						email.setValue(Users[i0].getEmail());
						xUser.setAttributeNode(email);

					}
				}	
			}	

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("user.xml"));

			// Output to console for testing
			StreamResult result2 = new StreamResult(System.out);

			transformer.transform(source, result);
			transformer.transform(source, result2);

			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}
