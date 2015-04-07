package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorage;
import hkust.cse.calendar.gui.deleteDialogs.DeleteDialogAbstractFactory;
import hkust.cse.calendar.gui.deleteDialogs.DeleteLocationDialogFactory;
import hkust.cse.calendar.unit.Location;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

//ADDED by Nelson: LocationsDialog Class to deal with location GUI
public class LocationsDialog extends JFrame implements ListSelectionListener {

	private static final long serialVersionUID = 1L;
	private static final String addString = "Add";
	private static final String removeString = "Remove";

	private ApptStorage _controller;

	private DefaultListModel<Location> listModel;
	private JList<Location> list;

	private JTextField txtAddLocation;
	private JButton btnRemoveLocation;
	private JButton btnAddLocation;
	private JLabel lblAddLocation;
	private JSpinner spnCapacity;

	public void displayLocations() {
		listModel.clear();
		Location[] Locations = _controller.locationStorage.getLocations();
		for (int i = 0; i < Locations.length; i++) {
			listModel.addElement(Locations[i]);
		}
	}
	public LocationsDialog(ApptStorage controller) {
		setTitle("Edit Locations");
		_controller = controller;
		lblAddLocation = new JLabel("Add Location:");
		listModel = new DefaultListModel<Location>();
		// Create Default Location
		// Location locationD = new Location("DEFAULT");
		// listModel.addElement(locationD.getName());
		// listModel.addElement("DEFAULT");

		// Load Location list with default

		// Create the list and put it in a scroll pane.
		list = new JList<Location>(listModel);
		displayLocations();
		// list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);
		list.setVisibleRowCount(5);
		JScrollPane listScrollPane = new JScrollPane(list);

		btnAddLocation = new JButton(addString);
		AddLocationListener ALListener = new AddLocationListener(btnAddLocation);
		btnAddLocation.setActionCommand(addString);
		btnAddLocation.addActionListener(ALListener);
		btnAddLocation.setEnabled(false);

		btnRemoveLocation = new JButton(removeString);
		btnRemoveLocation.setActionCommand(removeString);
		btnRemoveLocation.addActionListener(new RLListener());

		txtAddLocation = new JTextField(20);
		txtAddLocation.addActionListener(ALListener);
		txtAddLocation.getDocument().addDocumentListener(ALListener);
		
		// Create a panel that uses BoxLayout.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.add(btnRemoveLocation);
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(txtAddLocation);
		
		spnCapacity = new JSpinner();
		spnCapacity.setModel(new SpinnerNumberModel(Long.valueOf(1), Long.valueOf(1), null, Long.valueOf(1)));
		buttonPane.add(spnCapacity);
		buttonPane.add(btnAddLocation);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		add(listScrollPane, BorderLayout.CENTER);
		add(buttonPane, BorderLayout.PAGE_END);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent w) {
				_controller.locationStorage.saveLocationsToXml();
			}
		});

		// Display the window.
		pack();
		setVisible(true);
	}

	class RLListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// This method can be called only if
			// there's a valid selection
			// so go ahead and remove whatever's selected.
			
			// Modified by Jimmy: Accessing data structure
			Location location = list.getSelectedValue();
			if (location.getName().equals("Empty Venue")) {
				JOptionPane.showMessageDialog((Component) e.getSource(), "You may not delete the location \"Empty Venue\". This is used when a venue is force deleted.","Delete error",JOptionPane.ERROR_MESSAGE);
				return;
			}
			DeleteDialogAbstractFactory deleteLocaDeleteDialogAbstractFactory=new DeleteLocationDialogFactory(location, _controller.getApptsWithLocation(location),_controller.notificationStorage,_controller);
			deleteLocaDeleteDialogAbstractFactory.createGUI();
			dispose();
			return;
			/*
			displayLocations();

			int size = listModel.getSize();

			if (size == 1) { // Nobody except the default is left, disable firing.
				btnRemoveLocation.setEnabled(false);
			}
			*/
		}
	}
	
	class AddLocationListener implements ActionListener, DocumentListener {
		private boolean alreadyEnabled = false;
		private JButton button;

		public AddLocationListener(JButton button) {
			this.button = button;
		}

		// Required by ActionListener.
		public void actionPerformed(ActionEvent e) {
			String name = txtAddLocation.getText();

			// User didn't type in a unique name...
			if (name.equals("") || alreadyInList(name)) {
				Toolkit.getDefaultToolkit().beep();
				txtAddLocation.requestFocusInWindow();
				txtAddLocation.selectAll();
				return;
			}

			int index = list.getSelectedIndex(); // get selected index
			if (index == -1) { // no selection, so insert at beginning
				index = 0;
			} else { // add after the selected item
				index++;
			}

			Location location = new Location(txtAddLocation.getText(),(Long) spnCapacity.getValue());
			listModel.insertElementAt(location, index);
			_controller.locationStorage.addLocation(location);

			// If we just wanted to add to the end, we'd do this:
			// listModel.addElement(employeeName.getText());

			// Reset the text field.
			txtAddLocation.requestFocusInWindow();
			txtAddLocation.setText("");
			
			spnCapacity.setValue((long)1);

			// Select the new item and make it visible.
			list.setSelectedIndex(index);
			list.ensureIndexIsVisible(index);
		}

		protected boolean alreadyInList(String name) {
			
			for (Enumeration<Location> e = listModel.elements(); e.hasMoreElements();) {
				Location location = (Location) e.nextElement();
				if (location.getName().equals(name)) {
					return true;
				}
			}
			return false;
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
				btnRemoveLocation.setEnabled(false);

			} else {
				// Selection, enable the fire button.
				btnRemoveLocation.setEnabled(true);
			}
		}
	}
}