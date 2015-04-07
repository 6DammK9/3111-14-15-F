package hkust.cse.calendar.unit;

import hkust.cse.calendar.apptstorage.ApptStorage;
import hkust.cse.calendar.gui.TimeDialog;
import hkust.cse.calendar.gui.TimeDisplay;
import hkust.cse.calendar.gui.TimeMachine;

import java.io.File;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.thoughtworks.xstream.XStream;

//ADDED by Jimmy: Time Machine implementation
final public class Time {
	private static boolean activaited = false;
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	private GregorianCalendar currentTime;
	private double flowSpeed;
	private GregorianCalendar previousUpdateTime;
	private static Time time;
	private ApptStorage apptStorage = null;

	public static class TimeMachineKey {private TimeMachineKey() {}} // Key so that only Time is allowed to make TimeMachine GUI
	private static final TimeMachineKey TIME_MACHINE_KEY = new TimeMachineKey();
	private static TimeDialog timeDialog;
	@SuppressWarnings("unchecked")
	private Time() {
		File file =new File("TimeMachine.txt");
		if (file.exists() && file.isFile()) {
			List<Double> standardSpeeds;
			XStream xstream = new XStream();
			xstream.alias("speed", double.class);
			try {
				standardSpeeds =(List<Double>) xstream.fromXML(file);
			} catch (Exception e) {
				standardSpeeds = Arrays.asList(1.0,3.0,3600.0);
				System.out.println(xstream.toXML(standardSpeeds));
			}
			timeDialog=TimeMachine.displayGui(standardSpeeds,TIME_MACHINE_KEY);
		} else {
			timeDialog=TimeDisplay.displayGui(TIME_MACHINE_KEY);
		}
		currentTime = new GregorianCalendar();
		flowSpeed = 1.0;
		timeDialog.changeDisplayFlowSpeed(flowSpeed,TIME_MACHINE_KEY);
		previousUpdateTime = new GregorianCalendar();

		scheduler.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				Time.incrementTime();
			}
		}, 0, 200, TimeUnit.MILLISECONDS);
	}

	// ADDED by Jimmy: Increments time every 200 ms realtime
	public static void startTimeMachine() {
		if (activaited) return;
		time=new Time();
	}

	// ADDED by Jimmy: Increments time by checking time difference with last
	// increment and multiplying with flow speed
	private static void incrementTime() {
		time.currentTime
				.add(Calendar.MILLISECOND, (int) (time.flowSpeed * (System
						.currentTimeMillis() - time.previousUpdateTime
						.getTimeInMillis())));
		time.previousUpdateTime.setTimeInMillis(System.currentTimeMillis());
		timeDialog.changeDisplayTime(time.currentTime.getTime().toString(),TIME_MACHINE_KEY);
		if (time.apptStorage != null) {
			time.apptStorage.popupReminders(time.currentTime);
		}
		// System.out.println("NOW="+currentTime.getTime().toString());
	}

	// ADDED by Jimmy: Change the flow speed
	public static void setFlowSpeed(double in, TimeMachine.TimeController timeController) {
		timeController.hashCode();
		time.flowSpeed = in;
		timeDialog.changeDisplayFlowSpeed(time.flowSpeed,TIME_MACHINE_KEY);
	}

	// ADDED by Jimmy: Set current time using Calendar
	public static void setTime(GregorianCalendar newTime, TimeMachine.TimeController timeController) {
		timeController.hashCode();
		time.currentTime = newTime;
		time.previousUpdateTime.setTimeInMillis(System.currentTimeMillis());
	}

//	// ADDED by Jimmy: Set current time using Date
//	public static void setTime(Date time) {
//		currentTime.setTime(time);
//		previousUpdateTime.setTimeInMillis(System.currentTimeMillis());
//	}

	// ADDED by Jimmy: Set current time to realtime and change speed to +1x
	public static void setToRealTime(TimeMachine.TimeController timeController) {
		timeController.hashCode();
		time.currentTime.setTimeInMillis(System.currentTimeMillis());
		time.flowSpeed = 1;
		timeDialog.changeDisplayFlowSpeed(time.flowSpeed,TIME_MACHINE_KEY);
		time.previousUpdateTime.setTimeInMillis(System.currentTimeMillis());
	}

	// ADDED by Jimmy: Returns current time as Calendar object
	public static GregorianCalendar getTime() {
		return time.currentTime;
	}

	// ADDED by Jimmy: Returns flow speed
	public static double getFlowSpeed() {
		return time.flowSpeed;
	}

	// ADDED by Jimmy: Return current time as Timestamp object
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(time.currentTime.getTimeInMillis());
	}

	// ADDED by Jimmy: set the controller
	public static void setApptStorage(
			ApptStorage con) {
		time.apptStorage = con;

	}
}
