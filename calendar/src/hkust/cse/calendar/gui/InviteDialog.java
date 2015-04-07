package hkust.cse.calendar.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import hkust.cse.calendar.unit.User;

public class InviteDialog extends JPanel {
	/**
	 *  ADDED by Darren: Invitation Dialog
	 */
	private static final long serialVersionUID = 1L;

	private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);

	private static final String ADD_BUTTON_LABEL = "Add >>";

	private static final String REMOVE_BUTTON_LABEL = "<< Remove";
	
	private static final String CONFIRM_BUTTON_LABEL = "OK";

	private static final String DEFAULT_SOURCE_CHOICE_LABEL = "Available Choices";

	private static final String DEFAULT_DEST_CHOICE_LABEL = "Your Choices";
	
	private static final String[] VIEW_ONLY_LABELS = {"Attended", "Rejected", "Considering"};

	private JLabel sourceLabel;

	private JList sourceList;

	private SortedListModel sourceListModel;

	private JList destList;

	private SortedListModel destListModel;

	private JLabel destLabel;

	private JList thirdList;

	private SortedListModel thirdListModel;

	private JLabel thirdLabel;
	
	private JButton addButton;

	private JButton removeButton;
	
	private JButton confirmButton;
	
	public JFrame InviteF;
	
	private CalGrid father;
	
	private AppScheduler sister;
	
	private boolean view_only;

	//TODO
	public InviteDialog(CalGrid hehe, AppScheduler sheshe, boolean input) {
		father = hehe;
		sister = sheshe;
		view_only = input;
		if (view_only) {
			InviteF = new JFrame("People Invited");
		} else {
			InviteF = new JFrame("Inviting people");
		}
		//InviteF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		InviteF.getContentPane().add(this, BorderLayout.CENTER);
		InviteF.setSize(400, 300);
		InviteF.setVisible(false);
		InviteF.setResizable(false);
		this.setVisible(true);
		initScreen(view_only);
	}

	public String getSourceChoicesTitle() {
		return sourceLabel.getText();
	}

	public void setSourceChoicesTitle(String newValue) {
		sourceLabel.setText(newValue);
	}

	public String getDestinationChoicesTitle() {
		return destLabel.getText();
	}

	public void setDestinationChoicesTitle(String newValue) {
		destLabel.setText(newValue);
	}
	
	public void clearSourceListModel() {
		sourceListModel.clear();
	}

	public void clearDestinationListModel() {
		destListModel.clear();
	}

	public void addSourceElements(ListModel newValue) {
		fillListModel(sourceListModel, newValue);
	}

	public void setSourceElements(ListModel newValue) {
		clearSourceListModel();
		addSourceElements(newValue);
	}

	public void addDestinationElements(ListModel newValue) {
		fillListModel(destListModel, newValue);
	}

	private void fillListModel(SortedListModel model, ListModel newValues) {
		int size = newValues.getSize();
		for (int i = 0; i < size; i++) {
			model.add(newValues.getElementAt(i));
		}
	}

	public void addSourceElements(Object newValue[]) {
		fillListModel(sourceListModel, newValue);
	}

	public void setSourceElements(Object newValue[]) {
		clearSourceListModel();
		addSourceElements(newValue);
	}

	public void addDestinationElements(Object newValue[]) {
		fillListModel(destListModel, newValue);
	}

	private void fillListModel(SortedListModel model, Object newValues[]) {
		model.addAll(newValues);
	}

	public Iterator sourceIterator() {
		return sourceListModel.iterator();
	}

	public Iterator destinationIterator() {
		return destListModel.iterator();
	}

	public void setSourceCellRenderer(ListCellRenderer newValue) {
		sourceList.setCellRenderer(newValue);
	}

	public ListCellRenderer getSourceCellRenderer() {
		return sourceList.getCellRenderer();
	}

	public void setDestinationCellRenderer(ListCellRenderer newValue) {
		destList.setCellRenderer(newValue);
	}

	public ListCellRenderer getDestinationCellRenderer() {
		return destList.getCellRenderer();
	}

	public void setVisibleRowCount(int newValue) {
		sourceList.setVisibleRowCount(newValue);
		destList.setVisibleRowCount(newValue);
		thirdList.setVisibleRowCount(newValue);
	}

	public int getVisibleRowCount() {
		return sourceList.getVisibleRowCount();
	}

	public void setSelectionBackground(Color newValue) {
		sourceList.setSelectionBackground(newValue);
		destList.setSelectionBackground(newValue);
		thirdList.setSelectionBackground(newValue);
	}

	public Color getSelectionBackground() {
		return sourceList.getSelectionBackground();
	}

	public void setSelectionForeground(Color newValue) {
		sourceList.setSelectionForeground(newValue);
		destList.setSelectionForeground(newValue);
		thirdList.setSelectionForeground(newValue);
	}

	public Color getSelectionForeground() {
		return sourceList.getSelectionForeground();
	}

	private void clearSourceSelected() {
		Object selected[] = sourceList.getSelectedValues();
		for (int i = selected.length - 1; i >= 0; --i) {
			sourceListModel.removeElement(selected[i]);
		}
		sourceList.getSelectionModel().clearSelection();
	}

	private void clearDestinationSelected() {
		Object selected[] = destList.getSelectedValues();
		for (int i = selected.length - 1; i >= 0; --i) {
			destListModel.removeElement(selected[i]);
		}
		destList.getSelectionModel().clearSelection();
	}
	
	// thirdList Stuffs
	public String getThirdListTitle() {
		return thirdLabel.getText();
	}

	public void ThirdList(String newValue) {
		thirdLabel.setText(newValue);
	}
		public void clearThirdListModel() {
		thirdListModel.clear();
	}
		public void addThirdElements(ListModel newValue) {
		fillListModel(thirdListModel, newValue);
	}
		public void setThirdElements(ListModel newValue) {
		clearThirdListModel();
		addThirdElements(newValue);
	}
		public void addThirdElements(Object newValue[]) {
		fillListModel(thirdListModel, newValue);
	}

	public void setThirdElements(Object newValue[]) {
		clearThirdListModel();
		addThirdElements(newValue);
	}
	public Iterator thirdIterator() {
		return thirdListModel.iterator();
	}
		public void setThirdCellRenderer(ListCellRenderer newValue) {
		thirdList.setCellRenderer(newValue);
	}
		public ListCellRenderer getThirdCellRenderer() {
		return thirdList.getCellRenderer();
	}
		private void clearThirdSelected() {
		Object selected[] = thirdList.getSelectedValues();
		for (int i = selected.length - 1; i >= 0; --i) {
			thirdListModel.removeElement(selected[i]);
		}
		thirdList.getSelectionModel().clearSelection();
	}
	
	
	private void initScreen(boolean view_only) {
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(new GridBagLayout());
		if (view_only) {
			sourceLabel = new JLabel(VIEW_ONLY_LABELS[0]);
		} else {
			sourceLabel = new JLabel(DEFAULT_SOURCE_CHOICE_LABEL);
		}
		sourceListModel = new SortedListModel();
		sourceList = new JList(sourceListModel);
		add(sourceLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				EMPTY_INSETS, 0, 0));
		add(new JScrollPane(sourceList), new GridBagConstraints(0, 1, 1, 5, .5,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				EMPTY_INSETS, 0, 0));
		
		if (view_only) {
			thirdLabel = new JLabel(VIEW_ONLY_LABELS[2]);
			thirdListModel = new SortedListModel();
			thirdList = new JList(thirdListModel);
			add(thirdLabel, new GridBagConstraints(3, 0, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE,
					EMPTY_INSETS, 0, 0));
			add(new JScrollPane(thirdList), new GridBagConstraints(3, 1, 1, 5, 0.5,
					1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					EMPTY_INSETS, 0, 0));		
		}
		else {
			addButton = new JButton(ADD_BUTTON_LABEL);
			add(addButton, new GridBagConstraints(1, 2, 1, 2, 0, .25,
					GridBagConstraints.CENTER, GridBagConstraints.NONE,
					EMPTY_INSETS, 0, 0));
			addButton.addActionListener(new AddListener());
			removeButton = new JButton(REMOVE_BUTTON_LABEL);
			add(removeButton, new GridBagConstraints(1, 4, 1, 2, 0, .25,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
							0, 5, 0, 5), 0, 0));
			removeButton.addActionListener(new RemoveListener()); 
		}
		
		confirmButton = new JButton(CONFIRM_BUTTON_LABEL);
		if (view_only){
		add(confirmButton, new GridBagConstraints(1, 6, 1, 2, 0, .25,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						0, 5, 0, 5), 0, 0));
		} else {
			add(confirmButton, new GridBagConstraints(1, 6, 1, 2, 0, .25,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
							0, 5, 0, 5), 0, 0));	
		}
		confirmButton.addActionListener(new ConfirmListener());

		if (view_only){
			destLabel = new JLabel(VIEW_ONLY_LABELS[1]);	
		} else {
			destLabel = new JLabel(DEFAULT_DEST_CHOICE_LABEL);			
		}

		destListModel = new SortedListModel();
		destList = new JList(destListModel);
		add(destLabel, new GridBagConstraints(2, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				EMPTY_INSETS, 0, 0));
		add(new JScrollPane(destList), new GridBagConstraints(2, 1, 1, 5, .5,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				EMPTY_INSETS, 0, 0));		
	}

	private class AddListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object selected[] = sourceList.getSelectedValues();
			addDestinationElements(selected);
			clearSourceSelected();
		}
	}

	private class RemoveListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object selected[] = destList.getSelectedValues();
			addSourceElements(selected);
			clearDestinationSelected();
		}
	}
	
	private class ConfirmListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//ADDED by Darren: When "OK" Button is clicked, put all users into the invite list and close the dialog
			//TODO

			//Rebuild sister (AppScheduler)'s involve
			if(!view_only){
				sister.involved.clear();
				sister.involved.add(sister.getCurrentUser()); //Current User

				if (destList.getModel().getSize() > 0){
					String[] temp = new String[destList.getModel().getSize()];
					for (int i0 = 0; i0 < destList.getModel().getSize(); i0++) {
						temp[i0] = destList.getModel().getElementAt(i0).toString();
						sister.involved.add(temp[i0]);
					}
				}
			}
			//Close Dialog
			InviteF.setVisible(false);
		}
	}
	
	public void RebuildChoice(ArrayList<String> involved) {
		clearSourceListModel();
		clearDestinationListModel();

		//Load all users NOT INVOLVED from UserStorage
		User[] users = father.controller2.ExportHashMap();
		ArrayList <String> not_involved = new ArrayList <String>();
		ArrayList <String> selected = new ArrayList <String>(involved);
		selected.remove(father.controller.getDefaultUser().getID());
		if (users != null){
			if (users.length > 0) {
				for (int i0 = 0; i0 < users.length; i0++) {
					if (involved.contains(users[i0].getID()) == false) {
						not_involved.add(users[i0].getID());
					}
				}
				//System.out.println(user_names.size());
			}
		}

		//Put all involved user to the selected list
		addSourceElements(not_involved.toArray());
		addDestinationElements(selected.toArray());
	}
	
	public void view_all_involved (ArrayList<String> list1, ArrayList<String> list2, ArrayList<String> list3){
		clearSourceListModel();
		clearDestinationListModel();
		
		addSourceElements(list1.toArray());
		addDestinationElements(list2.toArray());
		addThirdElements(list3.toArray());
	}
}

class SortedListModel extends AbstractListModel {

	SortedSet model;

	public SortedListModel() {
		model = new TreeSet();
	}

	public int getSize() {
		return model.size();
	}

	public Object getElementAt(int index) {
		return model.toArray()[index];
	}

	public void add(Object element) {
		if (model.add(element)) {
			fireContentsChanged(this, 0, getSize());
		}
	}

	public void addAll(Object elements[]) {
		Collection c = Arrays.asList(elements);
		model.addAll(c);
		fireContentsChanged(this, 0, getSize());
	}

	public void clear() {
		model.clear();
		fireContentsChanged(this, 0, getSize());
	}

	public boolean contains(Object element) {
		return model.contains(element);
	}

	public Object firstElement() {
		return model.first();
	}

	public Iterator iterator() {
		return model.iterator();
	}

	public Object lastElement() {
		return model.last();
	}

	public boolean removeElement(Object element) {
		boolean removed = model.remove(element);
		if (removed) {
			fireContentsChanged(this, 0, getSize());
		}
		return removed;
	}
}
