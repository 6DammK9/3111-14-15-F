package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Time;

public abstract interface TimeDialog {
	public abstract void changeDisplayTime(String time,Time.TimeMachineKey key);
	public abstract void changeDisplayFlowSpeed(double flowSpeed,Time.TimeMachineKey key);
}
