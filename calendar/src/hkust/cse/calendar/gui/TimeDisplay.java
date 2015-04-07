package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Time;
import hkust.cse.calendar.unit.Time.TimeMachineKey;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Font;

@SuppressWarnings("serial")
public class TimeDisplay extends JFrame implements TimeDialog {
	private static JLabel lblCurrentTime;
	private TimeDisplay() {
		setTitle("Time");
		getContentPane().add(lblCurrentTime, BorderLayout.WEST);
		pack();
		setVisible(true);
	}
	public static TimeDialog displayGui(TimeMachineKey timeMachineKey) {
		timeMachineKey.hashCode();
		lblCurrentTime = new JLabel("---------------------------------------------");
		lblCurrentTime.setFont(new Font(lblCurrentTime.getFont().getName(), Font.PLAIN, lblCurrentTime.getFont().getSize()*2));
		return new TimeDisplay();
	}
	public void changeDisplayTime(String time,Time.TimeMachineKey key) {
		lblCurrentTime.setText(time);
	}
	@Override
	public void changeDisplayFlowSpeed(double flowSpeed,Time.TimeMachineKey key) {
	}

}
