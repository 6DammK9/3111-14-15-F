package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Time;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;

//ADDED by Jimmy: Time machine GUI
final public class TimeMachine extends JFrame implements TimeDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final JLabel lblCurrentTime = new JLabel("Loading");
	private static final JLabel lblFlowSpeed = new JLabel("Loading");
	private static final JLabel lblSecondsFactorYears = new JLabel("Loading");
	private static final JLabel lblSecondsFactorDays = new JLabel("Loading");
	private static final JLabel lblSecondsFactorHoursMinutesSeconds = new JLabel("Loading");
	private static final JButton btnApplyTimeChange = new JButton("Apply");
	private static final SpinnerModel spnMdlDateYear = new SpinnerDateModel();
	private static final SpinnerModel spnMdlDateMonth = new SpinnerDateModel();
	private static final SpinnerModel spnMdlDateDay = new SpinnerDateModel();
	private static final SpinnerModel spnMdlDateHour = new SpinnerDateModel();
	private static final SpinnerModel spnMdlDateMinute = new SpinnerDateModel();
	private static final SpinnerModel spnMdlDateSecond = new SpinnerDateModel();
	private static final JSpinner spnDateYear = new JSpinner(spnMdlDateYear);
	private static final JSpinner spnDateMonth = new JSpinner(spnMdlDateMonth);
	private static final JSpinner spnDateDay = new JSpinner(spnMdlDateDay);
	private static final JSpinner spnDateHour = new JSpinner(spnMdlDateHour);
	private static final JSpinner spnDateMinute = new JSpinner(spnMdlDateMinute);
	private static final JSpinner spnDateSecond = new JSpinner(spnMdlDateSecond);
	public static class TimeController {private TimeController() {}} // Key so that only TimeMachine GUI is allowed to control time 
	private static final TimeController TIME_CONTROLLER = new TimeController();
	private TimeMachine(List<JPanel> Panels) {
		Container contentPane;
		contentPane = getContentPane();
		contentPane.setLayout(new GridLayout(0, 1));

		for(JPanel panel: Panels) {
			contentPane.add(panel);
		}

		setTitle("Time Machine");

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public static TimeDialog displayGui(List<Double> standardSpeeds,Time.TimeMachineKey key) {
		key.hashCode();// to prevent GUI to be created from places other than Time
		JButton btnStop = _Helper_NewJButton_To_Set_FlowSpeed("0x", 0);
		JButton btnStart = _Helper_NewJButton_To_Set_FlowSpeed("1x", 1);
		JButton btnRealTime = new JButton("RealTime");
		btnRealTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Time.setToRealTime(TIME_CONTROLLER);

			}
		});
		btnApplyTimeChange.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar converter;
				converter = Calendar.getInstance();
				converter.setTime((Date) spnDateYear.getValue());
				int year = converter.get(Calendar.YEAR);
				converter.setTime((Date) spnDateMonth.getValue());
				int month = converter.get(Calendar.MONTH);
				converter.setTime((Date) spnDateDay.getValue());
				int day = converter.get(Calendar.DAY_OF_MONTH);
				converter.setTime((Date) spnDateHour.getValue());
				int hour = converter.get(Calendar.HOUR_OF_DAY);
				converter.setTime((Date) spnDateMinute.getValue());
				int minute = converter.get(Calendar.MINUTE);
				converter.setTime((Date) spnDateSecond.getValue());
				int second = converter.get(Calendar.SECOND);
				GregorianCalendar newTime = new GregorianCalendar(year, month,
						day, hour, minute, second);
				Time.setTime(newTime, TIME_CONTROLLER);

			}
		});

		List<JPanel> rootPanels=new ArrayList<JPanel>();

		JPanel basicButtonsPanel = new JPanel(new GridLayout(0, 3));
		rootPanels.add(basicButtonsPanel);
		basicButtonsPanel.add(btnStop);
		basicButtonsPanel.add(btnRealTime);
		basicButtonsPanel.add(btnStart);
		basicButtonsPanel.add(lblSecondsFactorYears);
		basicButtonsPanel.add(lblSecondsFactorDays);
		basicButtonsPanel.add(lblSecondsFactorHoursMinutesSeconds);
		
		JPanel extraButtonsPanel = new JPanel(new GridLayout(0, standardSpeeds.size()));
		rootPanels.add(extraButtonsPanel);
		for (double flowSpeed: standardSpeeds) {
			extraButtonsPanel.add(_Helper_NewJButton_For_FlowSpeed_Change("++",flowSpeed));
		}
		for (double flowSpeed: standardSpeeds) {
			extraButtonsPanel.add(_Helper_NewJButton_For_FlowSpeed_Change("--",-flowSpeed));
		}
		
		JPanel displayLabelsPanel = new JPanel(new GridLayout(3, 1));
		rootPanels.add(displayLabelsPanel);
		displayLabelsPanel.add(lblCurrentTime);
		displayLabelsPanel.add(lblFlowSpeed);
		displayLabelsPanel.add(btnApplyTimeChange);

		JPanel dateSpinnersPanel = new JPanel();
		rootPanels.add(dateSpinnersPanel);
		
		dateSpinnersPanel.setLayout(new GridLayout(0, 3));

		spnDateYear.setEditor(new JSpinner.DateEditor(spnDateYear, "yyyy"));
		spnDateMonth.setEditor(new JSpinner.DateEditor(spnDateMonth, "MM"));
		spnDateDay.setEditor(new JSpinner.DateEditor(spnDateDay, "dd"));
		spnDateHour.setEditor(new JSpinner.DateEditor(spnDateHour, "HH"));
		spnDateMinute.setEditor(new JSpinner.DateEditor(spnDateMinute, "mm"));
		spnDateSecond.setEditor(new JSpinner.DateEditor(spnDateSecond, "ss"));

		dateSpinnersPanel.add(new JLabel("Year"));
		dateSpinnersPanel.add(new JLabel("Month"));
		dateSpinnersPanel.add(new JLabel("Day"));

		dateSpinnersPanel.add(spnDateYear);
		dateSpinnersPanel.add(spnDateMonth);
		dateSpinnersPanel.add(spnDateDay);

		dateSpinnersPanel.add(new JLabel("Hour"));
		dateSpinnersPanel.add(new JLabel("Minutes"));
		dateSpinnersPanel.add(new JLabel("Seconds"));

		dateSpinnersPanel.add(spnDateHour);
		dateSpinnersPanel.add(spnDateMinute);
		dateSpinnersPanel.add(spnDateSecond);

		return new TimeMachine(rootPanels);

	}

	private static JButton _Helper_NewJButton_To_Set_FlowSpeed(String name,
			final double flowspeed) {
		JButton NewJButton = new JButton(name);
		NewJButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Time.setFlowSpeed(flowspeed, TIME_CONTROLLER);

			}
		});
		return NewJButton;
	}

	private static JButton _Helper_NewJButton_For_FlowSpeed_Change(String prepend,
			final double flowSpeed) {
		JButton NewJButton = new JButton(prepend + flowSpeed + "x");
		NewJButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Time.setFlowSpeed(Time.getFlowSpeed() + flowSpeed, TIME_CONTROLLER);

			}
		});
		return NewJButton;
	}

	// ADDED by Jimmy: For displaying current time
	public void changeDisplayTime(String time,Time.TimeMachineKey key) {
		lblCurrentTime.setText(time);
	}

	// ADDED by Jimmy: for displaying current flow speed
	public void changeDisplayFlowSpeed(double flowSpeed,Time.TimeMachineKey key) {
		lblFlowSpeed.setText("Flow: " + Double.toString(flowSpeed));
		lblSecondsFactorYears.setText((int) (flowSpeed/31536000)+" Years");
		lblSecondsFactorDays.setText((int) (flowSpeed%31536000/86400)+" Days");
		lblSecondsFactorHoursMinutesSeconds.setText(
				(int) (flowSpeed%86400/3600)+" : "+
				(int) (flowSpeed%3600/60)+" : "+
				(int) (flowSpeed%60)
				);
	}
}
