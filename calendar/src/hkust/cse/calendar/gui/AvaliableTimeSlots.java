package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.TimeSpan;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

//ADDED by Nelson: LocationsDialog Class to deal with location GUI
public class AvaliableTimeSlots extends JPanel implements ListSelectionListener {

	private static final long serialVersionUID = 1L;
	private static final String addString = "Apply";

	private AppScheduler _controller;

	private DefaultListModel<String> listModel;
	ArrayList<Timestamp> StartTimes;
	ArrayList<Timestamp> EndTimes;
	private JList<String> list;

	private JButton btnChoose;
	private JFrame frame;
	
	public AvaliableTimeSlots(AppScheduler controller) {
		super(new BorderLayout());

		_controller = controller;

		listModel = new DefaultListModel<String>();
		StartTimes = new ArrayList<Timestamp>();
		EndTimes = new  ArrayList<Timestamp>();

		MakeAvaliableTimeSlots(10); //At most 10?
		String[] TimeSlots = new String[10];
		for (int i = 0; i < TimeSlots.length; i++) {
			TimeSlots[i] = (String) (StartTimes.get(i).toString() + " , " + EndTimes.get(i).toString());
			listModel.addElement(TimeSlots[i]);
		}

		// Create the list and put it in a scroll pane.
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);
		list.setVisibleRowCount(5);
		JScrollPane listScrollPane = new JScrollPane(list);

		btnChoose = new JButton(addString);
		AddLocationListener ALListener = new AddLocationListener(btnChoose);
		btnChoose.setActionCommand(addString);
		btnChoose.addActionListener(ALListener);
		btnChoose.setEnabled(true);

		// Create a panel that uses BoxLayout.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(btnChoose);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		add(listScrollPane, BorderLayout.CENTER);
		add(buttonPane, BorderLayout.PAGE_END);
		
		// Create and set up the window.
		frame = new JFrame("Avaliable Time Slot");

		// Create and set up the content pane.
		//JComponent newContentPane = new AvaliableTimeSlots(_controller);
		this.setOpaque(true); // content panes must be opaque
		frame.setContentPane(this);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
		frame.setSize(400, 300);
		frame.setResizable(false);
	}

	class AddLocationListener implements ActionListener, DocumentListener {
		private boolean alreadyEnabled = false;
		private JButton button;

		public AddLocationListener(JButton button) {
			this.button = button;
		}

		// Required by ActionListener.
		public void actionPerformed(ActionEvent e) {

			int index = list.getSelectedIndex(); // get selected index
			if (index == -1) { // no selection, so insert at beginning
				index = 0;
			}

			//System.out.println("Start: " + StartTimes.get(index));
			//System.out.println("End: " + EndTimes.get(index));
			_controller.UpdateTimeFields(StartTimes.get(index),EndTimes.get(index));

			// Select the new item and make it visible.
			list.setSelectedIndex(index);
			list.ensureIndexIsVisible(index);
			
			DisposeFrame();
		}

		protected boolean alreadyInList(String name) {
			return listModel.contains(name);
		}

		public void insertUpdate(DocumentEvent e) {
			enableButton();
		}

		public void removeUpdate(DocumentEvent e) {
			handleEmptyTextField(e);
		}

		public void changedUpdate(DocumentEvent e) {
			if (!handleEmptyTextField(e)) {
				enableButton();
			}
		}

		private void enableButton() {
			if (!alreadyEnabled) {
				button.setEnabled(true);
			}
		}

		private boolean handleEmptyTextField(DocumentEvent e) {
			if (e.getDocument().getLength() <= 0) {
				button.setEnabled(false);
				alreadyEnabled = false;
				return true;
			}
			return false;
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {

			if (list.getSelectedIndex() == -1) {
				// No selection, disable fire button.
				//btnRemoveLocation.setEnabled(false);

			} else {
				// Selection, enable the fire button.
				//btnRemoveLocation.setEnabled(true);
			}
		}
	}
	
	private void MakeAvaliableTimeSlots(int target_size){
		int hit = 0;
		int index = 0;
		long increment = 1000*60*AppList.SMALLEST_DURATION; //15 minutes
		long offset = 1000*60; //1 minute
		long limit = increment * 4; //60 minutes + increment
		Timestamp now = new Timestamp(_controller.GetParent().getCurrentTime().getTime());
		if (now.getTime() % increment > 0){
			now.setTime(now.getTime()- (now.getTime() % increment) + increment);//
		}
		Timestamp temp_start = new Timestamp(now.getTime());
		Timestamp temp_end = new Timestamp(now.getTime());
		
		//System.out.println(new Timestamp(now.getTime() + increment));
		//System.out.println(increment + " - " + limit);
		
		//Will find closest 10 Time slots and each time slots' longest duration is 1 hour
		while (index < target_size) {
			temp_end.setTime(temp_end.getTime() + increment); //Push 15 minutes forward
			hit = 0;
			//System.out.println(temp_start+" - "+temp_end);
			for (int i0 = 0; i0 < _controller.involved.size(); i0++) {
				
				//Appt[] retrieveUserApptsDuring(User entity, int ID, TimeSpan time, Boolean create_mode, String location);
				hit += _controller.GetParent().controller.retrieveUserApptsDuring(
					_controller.GetParent().controller2.UserFromString(_controller.involved.get(i0)),
					_controller.GetselectedApptId(),
					new TimeSpan(new Timestamp(temp_start.getTime() + offset), new Timestamp(temp_end.getTime() - offset)),
					true,
					_controller.GetSelectedLocation()).length;
				
				//int create_check(User entity, int ID, TimeSpan time, String frequency, String location)
				hit += _controller.GetParent().controller.create_check(
						_controller.GetParent().controller2.UserFromString(_controller.involved.get(i0)),
						_controller.GetselectedApptId(),
						new TimeSpan(new Timestamp(temp_start.getTime() + offset), new Timestamp(temp_end.getTime() - offset)),
						_controller.GetFrequency(),
						_controller.GetSelectedLocation());				
				
			}
			if (hit > 0) {
				// If collide with events, save valid values
				if ((temp_end.getTime() - temp_start.getTime()) > increment) {
					//System.out.println ((temp_end.getTime() - temp_start.getTime()));
					//Save values
					 StartTimes.add(index, new Timestamp(temp_start.getTime()));
					 EndTimes.add(index, new Timestamp(temp_end.getTime() - increment));
					 index++;
				}
				temp_start.setTime(temp_end.getTime()); //Reset time gap
			} else if ( (temp_end.getTime() - temp_start.getTime()) >= limit) {
				//System.out.println(temp_start+" - "+temp_end);
				//Time gap exceed preferred limit
				StartTimes.add(index, new Timestamp(temp_start.getTime()));
				EndTimes.add(index, new Timestamp(temp_end.getTime()));
				index++;
				temp_start.setTime(temp_end.getTime()); //Reset time gap
			}
		}
		
		//System.out.println(StartTimes);
		//System.out.println(EndTimes);
	}
	
	public void DisposeFrame(){
		frame.dispose();
	}
}

