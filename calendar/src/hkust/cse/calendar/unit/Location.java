package hkust.cse.calendar.unit;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Location implements Serializable{

	private String name;
	private long capacity;
	private boolean toBeDeleted=false;
	public boolean isToBeDeleted() {
		return toBeDeleted;
	}
	public void setToBeDeleted (boolean bool) {
		toBeDeleted=bool;
	}
	public Location(String name, long capacity) {
		this.name=name;
		this.capacity=capacity;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public long getCapacity() {
		return capacity;
	}
	
	public void setCapacity(long capacity) {
		this.capacity=capacity;
	}
	
	public String toString() {
		return name+" ("+capacity+")";
	}
	
	public boolean equals(Object other) {
		if (other == null) {return false;}
		if (other == this) {return true;}
		if (!(other instanceof Location)) {return false;}
		return this.name.equals(((Location) other).name);
	}
	public int hashCode(){
		return name.hashCode();
	}
}
