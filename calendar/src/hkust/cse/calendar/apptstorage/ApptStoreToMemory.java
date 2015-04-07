package hkust.cse.calendar.apptstorage;

//import hkust.cse.calendar.gui.ReminderPopup;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.File;

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
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/* This class is for managing the Appt Storage according to different actions */
public class ApptStoreToMemory extends ApptStorage {

	/*
	 * Add additional flags which you feel necessary
	 */
	public final static int ONCE = 0;
	public final static int DAILY = 1;
	public final static int WEEKLY = 2;
	public final static int MONTHLY = 3;

	private static final String outputEncoding = "UTF-8";
	/* The Appt storage */

	public ApptStoreToMemory(User user, UserStorage userStorage, LocationStorage locationStorage) {
		defaultUser = user;
		this.setUserStorage(userStorage);
		this.locationStorage=locationStorage;
	}

	/*
	 * Retrieve the Appt's in the storage for a specific user within the
	 * specific time span
	 */
	public Appt[] retrieveUserApptsDuring(User entity, int ID, TimeSpan time, Boolean create_mode, Location location) {
		Appt[] Appts = mAppts.values().toArray(
				new Appt[mAppts.size()]);
		ArrayList<Appt> ApptL = new ArrayList<Appt>();
		int ApptStart;
		int ApptEnd;
		int ApptStartDay;
		int ApptEndDay;
		int ApptStartWeekDay;
		int ApptEndWeekDay;
		int timeStart;
		int timeEnd;
		int timeStartDay;
		int timeEndDay;
		int timeStartWeekDay;
		int timeEndWeekDay;
		boolean location_hit = false;
		timeStart = time.getStartTime().getHours() * 60
				+ time.getStartTime().getMinutes();
		timeEnd = time.getEndTime().getHours() * 60 + time.getEndTime().getMinutes();
		timeStartDay = time.getStartTime().getDate() * 60 * 24;
		timeEndDay = time.getEndTime().getDate() * 60 * 24;
		timeStartWeekDay = time.getStartTime().getDay() * 60 * 24;
		timeEndWeekDay = time.getEndTime().getDay() * 60 * 24;
		for (int i = 0; i < Appts.length; i++) {
			// DEBUG_SHOWTIME(time);
			// System.out.println("Requested: "+ time.StartTime());
			// System.out.println("Requested: "+ time.EndTime());
			ApptStart = Appts[i].getTimeSpan().getStartTime().getHours() * 60
					+ Appts[i].getTimeSpan().getStartTime().getMinutes();
			ApptEnd = Appts[i].getTimeSpan().getEndTime().getHours() * 60
					+ Appts[i].getTimeSpan().getEndTime().getMinutes();
			ApptStartDay = Appts[i].getTimeSpan().getStartTime().getDate() * 60 * 24;
			ApptEndDay = Appts[i].getTimeSpan().getEndTime().getDate() * 60 * 24;
			ApptStartWeekDay = Appts[i].getTimeSpan().getStartTime().getDay() * 60 * 24;
			ApptEndWeekDay = Appts[i].getTimeSpan().getEndTime().getDay() * 60 * 24;

			// Phase2: check location_hit
			location_hit = false;
			if ((location != null) && (Appts[i].getLocation() != null)) {
				if (Appts[i].getLocation().equals(location)) {
					location_hit = true;
				}
			}
			
			// Phase2: if not suitable to schedule (i.e. not all confirmed), skip it!
			//if ((Appts[i].isJoint()) &&
			//		((!Appts[i].IsAllAttended()) || 
			// 	((!Appts[i].isJointScheduled()) && !create_mode) )){
			if ( (Appts[i].isJoint()) && (!Appts[i].isJointScheduled()) ){
				//System.out.println("BEEP");
				continue;
			}
			
			// Assume ID is unique - not sure
			if (Appts[i].getID() == ID) {
				continue;
			}
			
			// First find the event related to target user AND won't show if the
			// "frequent" event is not start yet
			// PHASE2: If publicity is enabled, include also
			if ((Appts[i].getAttendList().contains(entity.getID()) || location_hit) || 
					(Appts[i].isPublic() && !create_mode)) {
				if (Appts[i].getFrequency().equals("Once")) {
					// System.out.println("HERE");
					if ((Appts[i].getTimeSpan().getStartTime().getTime() <= time
							.getEndTime().getTime())
							&& (Appts[i].getTimeSpan().getEndTime().getTime() >= time
									.getStartTime().getTime())) {
						ApptL.add(Appts[i]);
					}
				} else if (Appts[i].getFrequency().equals("Daily")) {
					// System.out.println(ApptStart + " " + timeEnd + " " +
					// ApptEnd + " " + timeStart);
					if ((Appts[i].getTimeSpan().getStartTime().getTime() <= time
							.getEndTime().getTime())) {
						if ((ApptStart <= timeEnd) && (ApptEnd >= timeStart)) {
							ApptL.add(Appts[i]);
						}
					}

				} else if (Appts[i].getFrequency().equals("Weekly")) {
					if ((Appts[i].getTimeSpan().getStartTime().getTime() <= time
							.getEndTime().getTime())) {
						if ((ApptStartWeekDay + ApptStart <= timeEndWeekDay
								+ timeEnd)
								&& (ApptEndWeekDay + ApptEnd >= timeStartWeekDay
										+ timeStart)) {
							ApptL.add(Appts[i]);
						} else if ((ApptStartWeekDay > ApptEndWeekDay)
								&& ((ApptStartWeekDay == timeStartWeekDay) || (ApptEndWeekDay == timeEndWeekDay))) {
							// Not sure if there is logic error
							ApptL.add(Appts[i]);
						}
					}
				} else if (Appts[i].getFrequency().equals("Monthly")) {
					if ((Appts[i].getTimeSpan().getStartTime().getTime() <= time
							.getEndTime().getTime())) {
						if ((ApptStartDay + ApptStart <= timeEndDay + timeEnd)
								& (ApptEndDay + ApptEnd >= timeStartDay
										+ timeStart)) {
							// System.out.print("HERE");
							ApptL.add(Appts[i]);
						} else if ((ApptStartDay > ApptEndDay)
								&& ((ApptStartDay == timeStartDay) || (ApptEndDay == timeEndDay))) {
							// Not sure if there is logic error
							ApptL.add(Appts[i]);
						}
					}
				}
			}
		}
		// System.out.println(mApptStorage.mAppts.size()+" "+ApptL.size());
		
		//System.out.println(ApptL.size());
		
		// Put the Appts which user is attended to the last
		if (ApptL.size() > 0) {
			ArrayList<Appt> attending = new ArrayList<Appt>();
			for (int i0 = 0; i0 < ApptL.size(); i0++) {
				if (ApptL.get(i0).getAttendList().contains(entity.getID())){
					attending.add(ApptL.get(i0));
				}
			}
			ApptL.removeAll(attending);
			ApptL.addAll(ApptL.size(),attending);
		}
		//System.out.println(ApptL.size());
		
		return ApptL.toArray(new Appt[ApptL.size()]);
		// return mApptStorage.RetrieveAppts(entity, time);
	}

	// overload method to retrieve appointment with the given joint appointment
	// id
	public Appt retrieveJointAppts(int joinApptID) {
		Appt[] Appts = mAppts.values().toArray(
				new Appt[mAppts.size()]);
		for (int i = 0; i < Appts.length; i++) {
			if (Appts[i].getJoinID() == joinApptID) {
				return Appts[i];
			}
		}
		return null;
		// return mApptStorage.RetrieveAppts(joinApptID);
	}


	// Suggested in base code: method used to load appointment from xml record into ApptStorage
	// TODO
	public void loadApptFromXml() {
		//the code for xml saving
		try {

			File fXmlFile = new File("file.xml");
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

			/*
			//xLocations
			Node xLocations = doc.getElementsByTagName("xLocations").item(0);
			System.out.println("\nCurrent Element: " + xLocations.getNodeName());	

			//xLocation
			NodeList xLocation = doc.getElementsByTagName("xLocation");
			for (int i0 = 0; i0 < xLocation.getLength(); i0++) {
				//System.out.println(xLocation.item(i0).getNodeName() + ": " + xLocation.item(i0).getTextContent());
				mLocations.add(xLocation.item(i0).getTextContent());
			}
			 */
			//xAppts
			Node xAppts = doc.getElementsByTagName("xAppts").item(0);
			//System.out.println("\nCurrent Element :" + xAppts.getNodeName());

			//Appt
			NodeList Appt = doc.getElementsByTagName("Appt");
			for (int i0 = 0; i0 < Appt.getLength(); i0++) {
				Element eElement = (Element) Appt.item(i0);
				//System.out.println(eElement.getNodeName() + ": " + eElement.getTextContent());
				Appt newAppt = new Appt(userStorage.UserFromString(eElement.getAttribute("owner")));
				//System.out.println(eElement.getAttribute("apptID"));
				newAppt.setID(Integer.parseInt(eElement.getAttribute("apptID")));
				//System.out.println(eElement.getAttribute("joinApptID"));
				newAppt.setJoinID(Integer.parseInt(eElement.getAttribute("joinApptID")));
				//System.out.println(eElement.getAttribute("title"));
				newAppt.setTitle(eElement.getAttribute("title"));
				//System.out.println(eElement.getAttribute("isJoint"));
				newAppt.setJoint(Boolean.parseBoolean(eElement.getAttribute("isJoint")));
				newAppt.setJointScheduled(Boolean.parseBoolean(eElement.getAttribute("isJointScheduled")));
				//System.out.println(eElement.getAttribute("location"));
				newAppt.setLocation(locationStorage.getLocation(eElement.getAttribute("location")));
				//System.out.println(eElement.getAttribute("frequency"));
				newAppt.setFrequency(eElement.getAttribute("frequency"));
				//System.out.println(eElement.getAttribute("eventInfo"));
				newAppt.setInfo(eElement.getAttribute("eventInfo"));
				newAppt.setPublic(Boolean.parseBoolean(eElement.getAttribute("publicity")));

				// TimeSpan
				newAppt.setTimeSpan(new TimeSpan(new Timestamp(Long.parseLong(eElement.getAttribute("startTime"))),
						new Timestamp(Long.parseLong(eElement.getAttribute("endTime")))));
				//System.out.println(eElement.getAttribute("startTime") + " - " + eElement.getAttribute("endTime"));

				NodeList attendants = eElement.getElementsByTagName("attendant");
				//System.out.println(attendants.getLength());
				LinkedList<String> attendants_str = new LinkedList<String>(); //String[attendants.getLength()];
				for (int i1 = 0; i1 < attendants.getLength(); i1++) {
					//System.out.println("BEBE");
					Element attendant = (Element) attendants.item(i1);
					//System.out.println(attendant.getNodeName() + ": " + attendant.getTextContent());
					attendants_str.add(attendant.getAttribute("name")); 
					//System.out.println(attendant);
				}
				newAppt.setAttendList(attendants_str);

				NodeList rejectors = eElement.getElementsByTagName("rejector");
				LinkedList<String> rejectors_str = new LinkedList<String>();
				for (int i1 = 0; i1 < rejectors.getLength(); i1++) {
					Element rejector = (Element) rejectors.item(i1);
					//System.out.println(rejector.getNodeName() + ": " + rejector.getTextContent());
					rejectors_str.add(rejector.getAttribute("name"));
				}
				newAppt.setRejectList(rejectors_str);

				NodeList pending = eElement.getElementsByTagName("pending");
				LinkedList<String> pending_str = new LinkedList<String>();
				for (int i1 = 0; i1 < pending.getLength(); i1++) {
					Element wait_for = (Element) pending.item(i1);
					//System.out.println(wait_for.getNodeName() + ": " + wait_for.getTextContent());
					pending_str.add(wait_for.getAttribute("name"));
				}
				newAppt.setWaitingList(pending_str);

				// TODO
				// SETTING REMINDER MISSING 
				//System.out.println(eElement.getAttribute("isReminderTriggered"));
				if (eElement.getAttribute("isHaveReminder").equals("false")) {
					newAppt.unSetReminder();
				} else if (eElement.getAttribute("isHaveReminder").equals("true")) {
					newAppt.setReminder(Long.parseLong(eElement.getAttribute("reminderTriggerAheadAmmount")), 
							eElement.getAttribute("reminderInfo"));
				} else {System.out.println("READ ERROR: " + eElement.getAttribute("isReminderTriggered"));}
				//System.out.println(eElement.getAttribute("isHaveReminder"));
				//System.out.println(eElement.getAttribute("reminderTriggerAheadAmmount"));
				//System.out.println(eElement.getAttribute("reminderInfo"));				
				//System.out.println("----------------------------");

				//String key = appt.getAttendList().getFirst() + "-" + Integer.toString(appt.getID());
				mAppts.put(attendants_str.getFirst() + "-" + Integer.toString(newAppt.getID()), newAppt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		generatePendingReminderList();
	}

	// ADDED by Darren: method used to load the size of hash map
	public int getStorageSize() {
		return mAppts.size();
	}

	//REMOVED by Jimmy: You have 2 functions doing the same stuff 
//	// ADDED by Darren: method used to load the number of joint events (under
//	// construction)
//	public int getJStorageSize() {
//		return mAppts.size();
//	}

	// ADDED by Darren: put the Appt into the ApptStorage
	public void saveAppt(Appt appt) {
		// System.out.println("Start: "+appt.TimeSpan().StartTime());
		// System.out.println("End: "+appt.TimeSpan().EndTime());
		String key = appt.getAttendList().getFirst() + "-"
				+ Integer.toString(appt.getID());
		mAppts.put(key, appt);
		generatePendingReminderList();
		// System.out.println("HEHE "+mApptStorage.mAppts.size());
		// WILL PUT IT INTO XML
	}

	// ADDED by Darren: remove the Appt into the ApptStorage
	public void removeAppt(Appt appt) {
		String key = appt.getAttendList().getFirst() + "-"
				+ Integer.toString(appt.getID());
		mAppts.remove(key);
	}

	// ADDED by Darren: get and put the Appt into the ApptStorage
	public void updateSavedAppt(Appt appt) {
		// GET
		String key = appt.getAttendList().getFirst() + "-"
				+ Integer.toString(appt.getID());
		Appt before = mAppts.get(key);
		// Change values if not null
		if (before != null) {
			before.setTitle(appt.getTitle());
			before.setInfo(appt.getInfo());
			before.setTimeSpan(appt.getTimeSpan());
			before.setAttendList(appt.getAttendList());
			before.setRejectList(appt.getRejectList());
			before.setWaitingList(appt.getWaitingList());
			before.setLocation(appt.getLocation());
			before.setFrequency(appt.getFrequency());
			//ADDED by Jimmy: Saving the reminders
			if (appt.hasReminder()) {
				before.setReminder(appt.getReminderTriggerAheadAmmount(),
						appt.getReminderInfo());
				if (appt.isReminderDone()) {
					before.setReminderDone();
				}
			} else {
				before.unSetReminder();
			}
			before.setPublic(appt.isPublic());
		}
		mAppts.put(key, before);
		generatePendingReminderList();
	}

	// ADDED by Jimmy: disable reminder for specific appt
	public void setApptReminderTriggered(Appt appt) {
		// GET
		String key = appt.getAttendList().getFirst() + "-"
				+ Integer.toString(appt.getID());
		Appt before = mAppts.get(key);
		// Change values if not null
		if (before != null) {
			before.setReminderDone();
		}
		mAppts.put(key, before);
	}


	// ADDED by Darren: Retrieve Appts within TimeSpan d
	public Appt[] retrieveApptsDuring(TimeSpan time) {
		Appt[] Appts = mAppts.values().toArray(
				new Appt[mAppts.size()]);
		ArrayList<Appt> ApptL = new ArrayList<Appt>();
		int ApptStart;
		int ApptEnd;
		int ApptStartDay;
		int ApptEndDay;
		int ApptStartWeekDay;
		int ApptEndWeekDay;
		int timeStart;
		int timeEnd;
		int timeStartDay;
		int timeEndDay;
		int timeStartWeekDay;
		int timeEndWeekDay;
		timeStart = time.getStartTime().getHours() * 60
				+ time.getStartTime().getMinutes();
		timeEnd = time.getEndTime().getHours() * 60 + time.getEndTime().getMinutes();
		timeStartDay = time.getStartTime().getDate() * 60 * 24;
		timeEndDay = time.getEndTime().getDate() * 60 * 24;
		timeStartWeekDay = time.getStartTime().getDay() * 60 * 24;
		timeEndWeekDay = time.getEndTime().getDay() * 60 * 24;
		for (int i = 0; i < Appts.length; i++) {
			/**
			 * if ((Appts[i].TimeSpan().StartTime().getTime() <=
			 * d.EndTime().getTime()) &
			 * //(Appts[i].TimeSpan().EndTime().getTime() >=
			 * d.EndTime().getTime())) {
			 * (Appts[i].TimeSpan().EndTime().getTime() >=
			 * d.StartTime().getTime())) { ApptL.add(Appts[i]); }
			 **/
			// DEBUG_SHOWTIME(time);
			// System.out.println("Requested: "+ time.StartTime());
			// System.out.println("Requested: "+ time.EndTime());
			ApptStart = Appts[i].getTimeSpan().getStartTime().getHours() * 60
					+ Appts[i].getTimeSpan().getStartTime().getMinutes();
			ApptEnd = Appts[i].getTimeSpan().getEndTime().getHours() * 60
					+ Appts[i].getTimeSpan().getEndTime().getMinutes();
			ApptStartDay = Appts[i].getTimeSpan().getStartTime().getDate() * 60 * 24;
			ApptEndDay = Appts[i].getTimeSpan().getEndTime().getDate() * 60 * 24;
			ApptStartWeekDay = Appts[i].getTimeSpan().getStartTime().getDay() * 60 * 24;
			ApptEndWeekDay = Appts[i].getTimeSpan().getEndTime().getDay() * 60 * 24;

			// won't show if the "frequent" event is not start yet

			if (Appts[i].getFrequency().equals("Once")) {
				// System.out.println("HERE");
				if ((Appts[i].getTimeSpan().getStartTime().getTime() <= time
						.getEndTime().getTime())
						&& (Appts[i].getTimeSpan().getEndTime().getTime() >= time
								.getStartTime().getTime())) {
					ApptL.add(Appts[i]);
				}
			} else if (Appts[i].getFrequency().equals("Daily")) {
				// System.out.println(ApptStart + " " + timeEnd + " " + ApptEnd
				// + " " + timeStart);
				if ((Appts[i].getTimeSpan().getStartTime().getTime() <= time
						.getEndTime().getTime())) {
					if ((ApptStart <= timeEnd) && (ApptEnd >= timeStart)) {
						ApptL.add(Appts[i]);
					}
				}

			} else if (Appts[i].getFrequency().equals("Weekly")) {
				if ((Appts[i].getTimeSpan().getStartTime().getTime() <= time
						.getEndTime().getTime())) {
					if ((ApptStartWeekDay + ApptStart <= timeEndWeekDay
							+ timeEnd)
							&& (ApptEndWeekDay + ApptEnd >= timeStartWeekDay
									+ timeStart)) {
						ApptL.add(Appts[i]);
					} else if ((ApptStartWeekDay > ApptEndWeekDay)
							&& ((ApptStartWeekDay == timeStartWeekDay) || (ApptEndWeekDay == timeEndWeekDay))) {
						// Not sure if there is logic error
						ApptL.add(Appts[i]);
					}
				}
			} else if (Appts[i].getFrequency().equals("Monthly")) {
				if ((Appts[i].getTimeSpan().getStartTime().getTime() <= time
						.getEndTime().getTime())) {
					if ((ApptStartDay + ApptStart <= timeEndDay + timeEnd)
							&& (ApptEndDay + ApptEnd >= timeStartDay + timeStart)) {
						// System.out.print("HERE");
						ApptL.add(Appts[i]);
					} else if ((ApptStartDay > ApptEndDay)
							&& ((ApptStartDay == timeStartDay) || (ApptEndDay == timeEndDay))) {
						// Not sure if there is logic error
						ApptL.add(Appts[i]);
					}
				}
			}

		}
		// System.out.println(ApptL.size());
		return ApptL.toArray(new Appt[ApptL.size()]);
	}

	// ADDED by Darren: Save all Appts to XML file, called from CalGrid
	// BASE CODE: Only triggered when Logout
	public void dumpStorageToFile() {
		//TODO
		try {
			/**Why initial "m" was used in base-code:
			 * xApptStorage <-> mApptStorage
			 * xAppts <-> mAppts
			 * xLocations <-> mLocations
			 * But the private variables are still fine
			 * Maybe better to create new class - do it when have time to analysis
			 **/

			// from memory
			Appt[] Appts = mAppts.values().toArray(
					new Appt[mAppts.size()]);

			// DocumentBuilder
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// xApptStorage (ROOT of XML)
			Document doc = docBuilder.newDocument();
			Element xApptStorage = doc.createElement("xApptStorage");
			doc.appendChild(xApptStorage);


			// xAppts 
			Element xAppts = doc.createElement("xAppts");
			xApptStorage.appendChild(xAppts);
			if (Appts != null) {
				if (Appts.length > 0) {
					for (int i0 = 0; i0 < Appts.length; i0++) {
						// xAppt
						Element xAppt = doc.createElement("Appt");
						xAppts.appendChild(xAppt);

						// owner
						Attr owner = doc.createAttribute("owner");
						owner.setValue("");
						if (Appts[i0].getOwner() != null) {
							owner.setValue(Appts[i0].getOwner().toString());
							}
						xAppt.setAttributeNode(owner);

						// location
						Attr location = doc.createAttribute("location");
						location.setValue(Appts[i0].getLocation().getName());
						xAppt.setAttributeNode(location);

						// apptID
						Attr ApptID = doc.createAttribute("apptID");
						ApptID.setValue(Integer.toString(Appts[i0].getID()));
						xAppt.setAttributeNode(ApptID);

						// joinApptID
						Attr joinApptID = doc.createAttribute("joinApptID");
						joinApptID.setValue(Integer.toString(Appts[i0].getJoinID()));
						xAppt.setAttributeNode(joinApptID);

						// title
						Attr title = doc.createAttribute("title");
						title.setValue(Appts[i0].getTitle());
						xAppt.setAttributeNode(title);

						// isJoint
						Attr isJoint = doc.createAttribute("isJoint");
						isJoint.setValue(Boolean.toString(Appts[i0].isJoint()));
						xAppt.setAttributeNode(isJoint);			
						
						// isJoint
						Attr isJointScheduled = doc.createAttribute("isJointScheduled");
						isJointScheduled.setValue(Boolean.toString(Appts[i0].isJointScheduled()));
						xAppt.setAttributeNode(isJointScheduled);			

						// publicity
						Attr publicity = doc.createAttribute("publicity");
						publicity.setValue(Boolean.toString(Appts[i0].isPublic()));
						xAppt.setAttributeNode(publicity);
						//System.out.println("BEEP");
						
						// frequency
						Attr frequency = doc.createAttribute("frequency");
						frequency.setValue(Appts[i0].getFrequency());
						xAppt.setAttributeNode(frequency);

						// eventInfo
						Attr eventInfo = doc.createAttribute("eventInfo");
						eventInfo.setValue(Appts[i0].getInfo());
						xAppt.setAttributeNode(eventInfo);

						// TimeSpan
						Attr startTime = doc.createAttribute("startTime");
						startTime.setValue(Long.toString(Appts[i0].getTimeSpan().getStartTime().getTime()));
						xAppt.setAttributeNode(startTime);
						Attr endTime = doc.createAttribute("endTime");
						endTime.setValue(Long.toString(Appts[i0].getTimeSpan().getEndTime().getTime()));
						xAppt.setAttributeNode(endTime);								
						
						// attendants		
						if (Appts[i0].getAttendList().size() > 0) {
							for (int i1 = 0; i1 < Appts[i0].getAttendList().size(); i1++){
								// attendant
								Element attendant = doc.createElement("attendant");
								Attr name = doc.createAttribute("name");
								name.setValue(Appts[i0].getAttendList().get(i1));
								attendant.setAttributeNode(name);
								xAppt.appendChild(attendant);
							}
						}

						// rejectors		
						if (Appts[i0].getRejectList().size() > 0) {
							for (int i1 = 0; i1 < Appts[i0].getRejectList().size(); i1++){
								// rejector
								Element rejector = doc.createElement("rejector");
								Attr name = doc.createAttribute("name");
								name.setValue(Appts[i0].getRejectList().get(i1));
								rejector.setAttributeNode(name);
								xAppt.appendChild(rejector);
							}
						}

						// pending;				
						if (Appts[i0].getWaitingList().size() > 0) {
							for (int i1 = 0; i1 < Appts[i0].getWaitingList().size(); i1++){
								// pending
								Element pending = doc.createElement("pending");
								Attr name = doc.createAttribute("name");
								name.setValue(Appts[i0].getWaitingList().get(i1));
								pending.setAttributeNode(name);
								xAppt.appendChild(pending);
							}
						}

						// isReminderTriggered
						Attr isReminderTriggered = doc.createAttribute("isReminderTriggered");
						isReminderTriggered.setValue(Boolean.toString(Appts[i0].isReminderDone()));
						xAppt.setAttributeNode(isReminderTriggered);	

						// isHaveReminder
						Attr isHaveReminder = doc.createAttribute("isHaveReminder");
						isHaveReminder.setValue(Boolean.toString(Appts[i0].hasReminder()));
						xAppt.setAttributeNode(isHaveReminder);	

						// reminderTriggerAheadAmmount
						Attr reminderTriggerAheadAmmount = doc.createAttribute("reminderTriggerAheadAmmount");
						reminderTriggerAheadAmmount.setValue(Long.toString(Appts[i0].getReminderTriggerAheadAmmount()));
						xAppt.setAttributeNode(reminderTriggerAheadAmmount);

						// reminderInfo
						Attr reminderInfo = doc.createAttribute("reminderInfo");
						reminderInfo.setValue(Appts[i0].getReminderInfo());
						xAppt.setAttributeNode(reminderInfo);
					}
				}	
			}	


	 
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("file.xml"));
	 
			// Output to console for testing
			//StreamResult result2 = new StreamResult(System.out);
	 
			transformer.transform(source, result);
			//transformer.transform(source, result2);
	 
			//System.out.println("File saved!");
			
		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
	 }

	//ADDED by Darren : For checking collision of frequent events
	//TODO
	public int create_check(User entity, int ID, TimeSpan time, String frequency, Location location) {
		if (frequency.equals("Once")) {
			return 0;
		} // Won't do the things repeated

		Appt[] Appts = mAppts.values().toArray(
				new Appt[mAppts.size()]);
		int check = 0;
		int ApptStart;
		int ApptEnd;
		int ApptStartDay;
		int ApptEndDay;
		int ApptStartWeekDay;
		int ApptEndWeekDay;
		int timeStart;
		int timeEnd;
		int timeStartDay;
		int timeEndDay;
		int timeStartWeekDay;
		int timeEndWeekDay;
		int flag_Appt;
		int flag_in;
		boolean location_hit = false;

		timeStart = time.getStartTime().getHours() * 60
				+ time.getStartTime().getMinutes();
		timeEnd = time.getEndTime().getHours() * 60 + time.getEndTime().getMinutes();
		timeStartDay = time.getStartTime().getDate() * 60 * 24;
		timeEndDay = time.getEndTime().getDate() * 60 * 24;
		timeStartWeekDay = time.getStartTime().getDay() * 60 * 24;
		timeEndWeekDay = time.getEndTime().getDay() * 60 * 24;

		if (timeStart > timeEnd) {
			timeStart -= 60 * 24;
		} // Overnight events comparing with overnight events
		if (timeStartWeekDay < timeEndWeekDay) {
			timeStartWeekDay = timeEndWeekDay;
		} // Overnight events comparing with overnight events
		if (timeStartWeekDay > timeEndWeekDay) {
			timeStartWeekDay = 0;
		} // Over-week events comparing with over-week events
		if (timeStartDay < timeEndDay) {
			timeStartDay = timeEndDay;
		} // Overnight events comparing with overnight events
		if (timeStartDay > timeEndDay) {
			timeStartDay = 1;
		} // Over-month events comparing with over-week events

		if (frequency.equals("Once")) {
			flag_in = ONCE;
		} else if (frequency.equals("Daily")) {
			flag_in = DAILY;
		} else if (frequency.equals("Weekly")) {
			flag_in = WEEKLY;
		} else if (frequency.equals("Monthly")) {
			flag_in = MONTHLY;
		} else
			flag_in = -1; // Error

		for (int i = 0; i < Appts.length; i++) {
			// Phase2: check location_hit
			location_hit = false;
			if ((location != null) && (Appts[i].getLocation() != null)) {
				if (Appts[i].getLocation().equals(location)) {
					location_hit = true;
				}
			}

			if (Appts[i].getTimeSpan().getEndTime().getTime() <= time.getStartTime()
					.getTime()) {
				continue;
			} // Won't process ended events

			// Phase2: if not suitable to schedule (i.e. not all confirmed), skip it!
			//if ((Appts[i].isJoint()) &&
			//		((!Appts[i].IsAllAttended()) || 
			// 	((!Appts[i].isJointScheduled()) && !create_mode) )){
			if ( (Appts[i].isJoint()) && (!Appts[i].isJointScheduled()) ){
				//System.out.println("BEEP");
				continue;
			}

			// Assume ID is unique - not sure
			if (Appts[i].getID() == ID) {
				continue;
			}

			if (!(Appts[i].getAttendList().contains(entity.getID()) || location_hit)) {
				continue;
			} // Won't process the events NOT attended/ created or potentially hit in location

			// DEBUG_SHOWTIME(time);
			// System.out.println("Requested: "+ time.StartTime());
			// System.out.println("Requested: "+ time.EndTime());
			ApptStart = Appts[i].getTimeSpan().getStartTime().getHours() * 60
					+ Appts[i].getTimeSpan().getStartTime().getMinutes();
			ApptEnd = Appts[i].getTimeSpan().getEndTime().getHours() * 60
					+ Appts[i].getTimeSpan().getEndTime().getMinutes();
			ApptStartDay = Appts[i].getTimeSpan().getStartTime().getDate() * 60 * 24;
			ApptEndDay = Appts[i].getTimeSpan().getEndTime().getDate() * 60 * 24;
			ApptStartWeekDay = Appts[i].getTimeSpan().getStartTime().getDay() * 60 * 24;
			ApptEndWeekDay = Appts[i].getTimeSpan().getEndTime().getDay() * 60 * 24;

			if (ApptStart > ApptEnd) {
				ApptStart -= 60 * 24;
			} // Overnight events comparing with overnight events
			if (ApptStartWeekDay < ApptEndWeekDay) {
				ApptStartWeekDay = ApptEndWeekDay;
			} // Overnight events comparing with overnight events
			if (ApptStartWeekDay > ApptEndWeekDay) {
				ApptStartWeekDay = 0;
			} // Over-week events comparing with over-week events
			if (ApptStartDay < ApptEndDay) {
				ApptStartDay = ApptEndDay;
			} // Overnight events comparing with overnight events
			if (ApptStartDay > ApptEndDay) {
				ApptStartDay = 1;
			} // Over-month events comparing with over-week events

			if (Appts[i].getFrequency().equals("Once")) {
				flag_Appt = ONCE;
			} else if (Appts[i].getFrequency().equals("Daily")) {
				flag_Appt = DAILY;
			} else if (Appts[i].getFrequency().equals("Weekly")) {
				flag_Appt = WEEKLY;
			} else if (Appts[i].getFrequency().equals("Monthly")) {
				flag_Appt = MONTHLY;
			} else
				flag_Appt = -1; // Error

			// Only for frequent events
			// For checking the existence of collision of weekly and monthly
			// events, since it must collides some day
			// So that I consider them as daily events
			// For repeating until a certain time, it's another story - will
			// implement it later, since GUI & data structure need to extent!
			switch (flag_in) {
			case DAILY: {
				if ((ApptStart <= timeEnd) & (ApptEnd >= timeStart)) {
					check++;
					// System.out.println("Collison (Daily - *): " +
					// Appts[i].TimeSpan().StartTime() + " , " +
					// Appts[i].TimeSpan().EndTime());
				}
				break;
			}
			case WEEKLY: {
				switch (flag_Appt) {
				case ONCE: {
					if ((ApptStartWeekDay + ApptStart <= timeEndWeekDay
							+ timeEnd)
							& (ApptEndWeekDay + ApptEnd >= timeStartWeekDay
									+ timeStart)) {
						check++;
						// System.out.println("Collison (Weekly - Once): " +
						// Appts[i].TimeSpan().StartTime() + " , " +
						// Appts[i].TimeSpan().EndTime());
					}
					break;
				}
				case DAILY: {
					if ((ApptStart <= timeEnd) & (ApptEnd >= timeStart)) {
						check++;
						// System.out.println("Collison (Weekly - Daily): " +
						// Appts[i].TimeSpan().StartTime() + " , " +
						// Appts[i].TimeSpan().EndTime());
					}
					break;
				}
				case WEEKLY: {
					if ((ApptStartWeekDay + ApptStart <= timeEndWeekDay
							+ timeEnd)
							& (ApptEndWeekDay + ApptEnd >= timeStartWeekDay
									+ timeStart)) {
						check++;
						// System.out.println("Collison (Weekly - Weekly): " +
						// Appts[i].TimeSpan().StartTime() + " , " +
						// Appts[i].TimeSpan().EndTime());
					}
					break;
				}
				case MONTHLY: {
					if ((ApptStart <= timeEnd) & (ApptEnd >= timeStart)) {
						check++;
						// System.out.println("Collison (Weekly - Monthly): " +
						// Appts[i].TimeSpan().StartTime() + " , " +
						// Appts[i].TimeSpan().EndTime());
					}
					break;
				}
				// default: System.out.println("Error in flag_Appt");
				}
				break;
			}
			case MONTHLY: {
				switch (flag_Appt) {
				case ONCE: {
					if ((ApptStartDay + ApptStart <= timeEndDay + timeEnd)
							& (ApptEndDay + ApptEnd >= timeStartDay + timeStart)) {
						check++;
						// System.out.println("Collison (Monthly - Once): " +
						// Appts[i].TimeSpan().StartTime() + " , " +
						// Appts[i].TimeSpan().EndTime());
					}
					break;
				}
				case DAILY: {
					if ((ApptStart <= timeEnd) && (ApptEnd >= timeStart)) {
						check++;
						// System.out.println("Collison (Monthly - Daily): " +
						// Appts[i].TimeSpan().StartTime() + " , " +
						// Appts[i].TimeSpan().EndTime());
					}
					break;
				}
				case WEEKLY: {
					if ((ApptStart <= timeEnd) & (ApptEnd >= timeStart)) {
						check++;
						// System.out.println("Collison (Monthly - Weekly): " +
						// Appts[i].TimeSpan().StartTime() + " , " +
						// Appts[i].TimeSpan().EndTime());
					}
					break;
				}
				case MONTHLY: {
					if ((ApptStartDay + ApptStart <= timeEndDay + timeEnd)
							& (ApptEndDay + ApptEnd >= timeStartDay + timeStart)) {
						check++;
						// System.out.println("Collison (Monthly - Monthly): " +
						// Appts[i].TimeSpan().StartTime() + " , " +
						// Appts[i].TimeSpan().EndTime());
					}
					break;
				}
				default: // System.out.println("Error in flag_Appt");
				}
				break;
			}
			default: // System.out.println("Error in flag_in");
			}
		}

		return check;
		// return mApptStorage.create_check(entity, time, frequency);
	}

	//ADDED by Darren : For checking next available ID
	public int FindNextID(boolean joint){
		Appt[] Appts = mAppts.values().toArray(new Appt[mAppts.size()]);
		ArrayList<Integer> IDs = new ArrayList<Integer>();
		int result = 1;
		if (Appts != null) {
			if (Appts.length > 0) {
				for (int i0=0;i0<Appts.length;i0++){
					if (joint) {IDs.add(Appts[i0].getJoinID());}
					else  {IDs.add(Appts[i0].getID());}
				}
			}
		}
		while (IDs.contains(result)) {
			result++;
		}
		return result;
	}
	
	//ADDED by Darren : Export whole HashMap
	public Appt[] ExportHashMap(){
		return mAppts.values().toArray(new Appt[mAppts.size()]);
	} 
	
	//ADDED by Darren : Get that specific Appt
	public Appt getAppt (String key) {
		return mAppts.get(key);
	}
}
