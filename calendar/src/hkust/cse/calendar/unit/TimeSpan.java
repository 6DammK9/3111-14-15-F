package hkust.cse.calendar.unit;

import java.io.Serializable;
import java.sql.Timestamp;

/* This class represents the time span between two points of time */
@SuppressWarnings("serial")
public class TimeSpan implements Serializable {
	/* The starting time of the time span */
	private Timestamp startTime;
	/* The ending time of the time span */
	private Timestamp endTime;

	/*
	 * Create a new TimeSpan object with the specific starting time and ending
	 * time
	 */
	public TimeSpan(Timestamp start, Timestamp end) {

		if (start.getYear() >= 1900) {
			start.setYear(start.getYear() - 1900);
		}
		if (end.getYear() >= 1900) {
			end.setYear(end.getYear() - 1900);
		}
		startTime = start;
		endTime = end;
	}

	/* Get the starting time */
	public Timestamp getStartTime() {
		return startTime;
	}

	/* Get the ending time */
	public Timestamp getEndTime() {
		return endTime;
	}

	/* Check whether a time span overlaps with this time span */
	public boolean overlaps(TimeSpan d) {
		if (d.getEndTime().before(startTime) || d.getEndTime().equals(startTime)) // If
																				// the
																				// time
																				// span
																				// ends
																				// before
																				// or
																				// at
																				// the
																				// starting
																				// time
																				// of
																				// this
																				// time
																				// span
																				// then
																				// these
																				// two
																				// time
																				// spans
																				// do
																				// not
																				// overlap
			return false;
		if (d.getStartTime().equals(endTime) || endTime.before(d.getStartTime())) // If
																				// the
																				// time
																				// span
																				// starts
																				// after
																				// or
																				// at
																				// the
																				// ending
																				// time
																				// of
																				// this
																				// time
																				// span
																				// then
																				// these
																				// two
																				// time
																				// spans
																				// do
																				// not
																				// overlap
			return false;
		return true; // Else, the time span overlaps with this time span

	}

	/*
	 * Calculate the length of the time span if the starting time and ending
	 * time are within the same day
	 */
	public int getTimeLength() {

		/*
		 * return -1 if the starting time and ending time are not in the same
		 * day
		 */
		if (startTime.getYear() != endTime.getYear())
			return -1;
		if (startTime.getMonth() != endTime.getMonth())
			return -1;
		if (startTime.getDay() != endTime.getDay())
			return -1;

		/* Calculate the number of minutes within the time span */
		int result = startTime.getHours() * 60 + startTime.getMinutes()
				- endTime.getHours() * 60 - endTime.getMinutes();
		if (result < 0)
			return -1;
		else
			return result;
	}

	/* Set the starting time */
	public void setStartTime(Timestamp s) {
		startTime = s;
	}

	/* Set the ending time */
	public void setEndTime(Timestamp e) {
		endTime = e;
	}
}
