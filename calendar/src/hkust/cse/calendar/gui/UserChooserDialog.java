package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorage;
import hkust.cse.calendar.apptstorage.NotificationStorage;
import hkust.cse.calendar.apptstorage.UserStorage;
import hkust.cse.calendar.gui.deleteDialogs.DeleteDialogAbstractFactory;
import hkust.cse.calendar.gui.deleteDialogs.DeleteUserDialogFactory;
import hkust.cse.calendar.unit.User;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.GridBagLayout;

import javax.swing.JList;

import java.awt.GridBagConstraints;

import javax.swing.JButton;

import java.awt.Insets;

import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;

import javax.swing.Action;

public class UserChooserDialog extends JFrame implements ListSelectionListener {
	private DefaultListModel<String> listModel;
	private UserStorage userStorage;
	private final Action actBtnOk = new ActBtnPressedOk();
	private final Action actBtnMyself = new ActBtnPressedMyself();
	private final Action actBtnCancel = new ActBtnPressedCancel();
	private JList list;
	private JButton btnOk;
	private User myself;
	private final Action actBtnNew = new ActBtnNew();
	private UserDialogFactory userDialogFactory= new UserDialogFactory();
	private final Action actBtnDelete = new ActBtnDelete();
	private NotificationStorage notificationStorage;
	public ApptStorage apptStorage;
	
	public UserChooserDialog(UserStorage userStorage, User myself, NotificationStorage notificationStorage,ApptStorage apptStorage) {
		setTitle("Pick user");
		this.userStorage=userStorage;
		this.notificationStorage=notificationStorage;
		this.myself=myself;
		this.apptStorage=apptStorage;
		listModel = new DefaultListModel<String>();
		for ( Object user : userStorage.ExportHashMap()) {
			listModel.addElement(((User) user).getID()+"|"+((User) user).getLastName()+", "+((User) user).getFirstName());			
		}
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0};
		getContentPane().setLayout(gridBagLayout);
		
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);
		list.setVisibleRowCount(5);
		list.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		btnOk = new JButton("");
		btnOk.setAction(actBtnOk);
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.insets = new Insets(0, 0, 0, 5);
		gbc_btnOk.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnOk.gridx = 0;
		gbc_btnOk.gridy = 1;
		getContentPane().add(btnOk, gbc_btnOk);
		
		JButton button = new JButton("");
		button.setAction(actBtnDelete);
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(0, 0, 0, 5);
		gbc_button.gridx = 1;
		gbc_button.gridy = 1;
		getContentPane().add(button, gbc_button);
		
		JButton btnMyself = new JButton("");
		btnMyself.setAction(actBtnMyself);
		GridBagConstraints gbc_btnMyself = new GridBagConstraints();
		gbc_btnMyself.insets = new Insets(0, 0, 0, 5);
		gbc_btnMyself.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnMyself.gridx = 3;
		gbc_btnMyself.gridy = 1;
		getContentPane().add(btnMyself, gbc_btnMyself);
		
		JButton btnCancel = new JButton("");
		btnCancel.setAction(actBtnCancel);
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnCancel.gridx = 4;
		gbc_btnCancel.gridy = 1;
		getContentPane().add(btnCancel, gbc_btnCancel);
		
		JScrollPane scrollPane = new JScrollPane(list);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.weighty = 1.0;
		gbc_scrollPane.gridwidth = 5;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		getContentPane().add(scrollPane, gbc_scrollPane);
		
		JButton btnNew = new JButton("");
		btnNew.setAction(actBtnNew);
		GridBagConstraints gbc_btnNew = new GridBagConstraints();
		gbc_btnNew.insets = new Insets(0, 0, 0, 5);
		gbc_btnNew.gridx = 2;
		gbc_btnNew.gridy = 1;
		getContentPane().add(btnNew, gbc_btnNew);
		pack();
		setVisible(true);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {

			if (list.getSelectedIndex() == -1) {
				btnOk.setEnabled(false);

			} else {
				btnOk.setEnabled(true);
			}
		}
	}
	
	private class ActBtnPressedOk extends AbstractAction {
		public ActBtnPressedOk() {
			putValue(NAME, "Edit");
		}
		public void actionPerformed(ActionEvent e) {
			//UserDialogFactory userDialogFactory= new UserDialogFactory();
			userDialogFactory.createDialog("Edit",(User) userStorage.ExportHashMap()[list.getSelectedIndex()], true);
	
			
			//UserDialogFactory editUserDialogFactory= new EditUserDialog((User) userStorage.ExportHashMap()[list.getSelectedIndex()], true);
			//editUserDialogFactory.createDialog();
			dispose();
		}
	}
	private class ActBtnPressedMyself extends AbstractAction {
		public ActBtnPressedMyself() {
			putValue(NAME, "Myself");
		}
		public void actionPerformed(ActionEvent e) {
			//UserDialogFactory userDialogFactory= new UserDialogFactory();
			userDialogFactory.createDialog("Edit",myself, true);
	
			//UserDialogFactory editUserDialogFactory= new EditUserDialog(myself, true);
			//editUserDialogFactory.createDialog();
			dispose();
		}
	}
	private class ActBtnPressedCancel extends AbstractAction {
		public ActBtnPressedCancel() {
			putValue(NAME, "Cancel");
		}
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
	private class ActBtnNew extends AbstractAction {
		public ActBtnNew() {
			putValue(NAME, "New");
		}
		public void actionPerformed(ActionEvent e) {
			//UserDialogFactory userDialogFactory= new UserDialogFactory();
			userDialogFactory.createDialog("Edit",null, true);
	
			//UserDialogFactory editUserDialogFactory= new EditUserDialog(null, true);
			//editUserDialogFactory.createDialog();
			dispose();
		}
	}
	private class ActBtnDelete extends AbstractAction {
		public ActBtnDelete() {
			putValue(NAME, "Delete");
		}
		public void actionPerformed(ActionEvent e) {
			DeleteDialogAbstractFactory deleteUserDialogFactory= new DeleteUserDialogFactory((User) userStorage.ExportHashMap()[list.getSelectedIndex()], notificationStorage,apptStorage);
			deleteUserDialogFactory.createGUI();
			dispose();
		}
	}
}
