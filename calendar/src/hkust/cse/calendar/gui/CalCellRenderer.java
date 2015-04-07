/**
 *  Unless specified, it's added by Darren or from the base code
 */

package hkust.cse.calendar.gui;

import java.awt.Color;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

class CalCellRenderer extends DefaultTableCellRenderer

{

	private int r;

	private int c;

	public CalCellRenderer(Object value, int colored) {
		if (value != null) {
			setForeground(Color.red);
		} else
			setForeground(Color.black);

		//ADDED by Darren: Color (hash) function for indicating if Appt exists in that specified day
		//If no Appt in that day, it is shown as white as Color(255,255,255)
		//Lower bound of Color is (51,51,51)
		setBackground(new Color(255 - (colored * 17) % 200,
				255 - (colored * 37) % 200, 255 - (colored * 97) % 200));
		// if (colored == true) {
		// setBackground(new Color(180,180,240));
		// } else
		// setBackground(Color.white);
		setHorizontalAlignment(SwingConstants.RIGHT);
		setVerticalAlignment(SwingConstants.TOP);
		// settext
	}

	public int row() {
		return r;
	}

	public int col() {
		return c;
	}

}
