package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.Location;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.thoughtworks.xstream.XStream;

public class LocationStorage {
	private HashMap<String,Location> locations;
	private LocationStorage() {
		locations=new HashMap<String, Location>();
		addLocation(new Location("Empty Venue", 0));
	}
	public void addLocation(Location location) {
		if (!locations.containsKey(location.getName())) {
			locations.put(location.getName(), location);
		}
	}
	public void removeLocation(Location location) {
		if (locations.containsKey(location.getName())) {
			locations.remove(location.getName());
		}
		saveLocationsToXml();
	}
	public Location getLocation(String name) {
		if (locations.containsKey(name)) {
			return locations.get(name);
		} else {
			return locations.get("Empty Venue");
		}
	}
	public Location[] getLocations() {
		return locations.values().toArray(new Location[locations.size()]);
	}
	public void saveLocationsToXml() {
		XStream xstream = new XStream();
		xstream.alias("Location", Location.class);
		xstream.alias("Locations", LocationStorage.class);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream("Locations.xml");
			xstream.toXML(this, out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static LocationStorage locationsFactory() {
		XStream xstream = new XStream();
		xstream.alias("Location", Location.class);
		xstream.alias("Locations", LocationStorage.class);
		try {
			return (LocationStorage) xstream.fromXML(new File("Locations.xml"));
		} catch (Exception e) {
			LocationStorage locationstorage =new LocationStorage();
			locationstorage.saveLocationsToXml();
			return locationstorage;
		}
	}
}
