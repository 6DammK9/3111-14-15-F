package hkust.cse.calendar.gui.deleteDialogs;

import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import javax.swing.JScrollPane;

import java.awt.Insets;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ListModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

@SuppressWarnings("serial")
public class DeleteDialog<E> extends JFrame {
	public DeleteDialog(String title, String instructions, ListModel<E> listModel, List<AbstractAction> actions, String nullActionName) {
		setTitle(title);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel(instructions);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		getContentPane().add(lblNewLabel, gbc_lblNewLabel);

		JList<E> list = new JList<E>(listModel);
		
		JScrollPane scrollPane = new JScrollPane(list);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		getContentPane().add(scrollPane, gbc_scrollPane);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 2;
		getContentPane().add(panel, gbc_panel);
		for (Iterator<AbstractAction> iterator = actions.iterator(); iterator.hasNext();) {
			JButton button = new JButton();
			button.setAction(iterator.next());
			panel.add(button);
			
		}
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setAction(new NullAction(nullActionName));
		panel.add(btnCancel);
		pack();
		setVisible(true);
	}
	private class NullAction extends AbstractAction {
		public NullAction(String name) {
			putValue(NAME, name);
		}
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
}
