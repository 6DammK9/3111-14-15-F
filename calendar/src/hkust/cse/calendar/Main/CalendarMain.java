//main method, code starts here
package hkust.cse.calendar.Main;

import hkust.cse.calendar.unit.Time;
import hkust.cse.calendar.gui.LoginDialog;

public class CalendarMain {

	// REPLACED by Jimmy: removed irrelevant code and placed time machine code
	public static void main(String[] args) {
		 // Time machine starter
		Time.startTimeMachine();
		new LoginDialog();
	}
}
// commented out since the only thing required is up there.
// don't need a logout boolean as when the grid is closed, the program quits
/*
 * public class CalendarMain { public static boolean logOut = false;
 * 
 * public static void main(String[] args) { Time.startTimeMachine(); // Time
 * machine starter while(true){ logOut = false; try{ //
 * UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
 * }catch(Exception e){
 * 
 * } LoginDialog loginDialog = new LoginDialog(); while(logOut == false){ try {
 * Thread.sleep(300); } catch (InterruptedException e) { // TODO Auto-generated
 * catch block e.printStackTrace(); } } } } }
 */