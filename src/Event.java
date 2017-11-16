import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Encapsulates all relevant information pertaining to an event in one class. This includes
 * event name, start time and end time.
 * @author Dennis
 *
 */
public class Event {
	private String name, startTime, endTime;
	private SimpleDateFormat myFormat;
	private Calendar cal;
	public boolean hasEndTime;
	
	/**
	 * Initializes all the fields to default values
	 */
	public Event() {
		cal = new GregorianCalendar();
		myFormat = new SimpleDateFormat( "MM/dd/yyyy" );
		hasEndTime = true;
	}
	
	/**
	 * Configures the Calendar object
	 * @param year Year of the event
	 * @param month Month of the event
	 * @param date Day of the event
	 */
	public void setCal(int year, int month, int date) {
		cal.set(year, month, date);
	}
	
	/**
	 * Mutator, sets the name of the event
	 * @param name Event name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Parses input string and sets event date using string
	 * @param date a date for the event in the format MM/DD/YYYY
	 */
	public void setDate(String date) {
		try {
			cal.setTime(myFormat.parse(date));
		} catch(ParseException e) {
			System.out.println("Could not parse");
		}
	}
	
	/**
	 * Sets input time
	 * @param str A string for the startTime field
	 */
	public void setStartTime(String str) {	// Append a 0 to get time in the format HH:MM
		if(str.length() < 5)
			str = "0" + str;
		startTime = str;
	}
	
	/**
	 * Sets end time 
	 * @param str A string for the endTime field
	 */
	public void setEndTime(String str) {
		if(str.equals("")) {
			setHasEndTime(false);
		}
		else if(str.length() < 5) {
			str = "0" + str;

		}
		endTime = str;

	}
	/**
	 * Sets whether the event has an end time. If the event does not have an end time, "23:59" is used to 
	 * facilitate event sorting.
	 * @param bool a boolean for the hasEndTime field
	 */
	public void setHasEndTime(boolean bool) {
		hasEndTime = bool;
		if(bool == false)
			endTime = "23:59";
	}
	
	/**
	 * hasEndTime getter
	 * @return endTime field
	 */
	public boolean hasEndTime() {
		return hasEndTime;
	}
	
	/**
	 * month getter
	 * @return the month of the event
	 */
	public int getMonth() {
		return cal.get(Calendar.MONTH);
	}
	
	/**
	 * day getter
	 * @return the day of the event
	 */
	public int getDay() {
		return cal.get(Calendar.DATE);
	}
	
	/**
	 * year getter
	 * @return the year of the event
	 */
	public int getYear() {
		return cal.get(Calendar.YEAR);
	}
	
	/**
	 * Accesses the events Calendar object to get the day of the week
	 * @return the day of the week of the event i.e. Monday, Tuesday, Wednesday etc.
	 */
	public int getDayOfYear() {
		return cal.get(Calendar.DAY_OF_YEAR);
	}
	
	/**
	 * Event name getter
	 * @return the name of the event
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * startTime getter
	 * @return the startTime of the event
	 */
	public String getStartTime() {
		return startTime;
	}
	
	/**
	 * endTime getter
	 * @return the endTime of the vent
	 */
	public String getEndTime() {
		return endTime;
	}
	
	/**
	 * Accesses the Calendar object of the event to obtain a Date in the format MM/DD/YYYY
	 * @return Formatted string of the event's Date
	 */
	public String getDate() {
		return myFormat.format(cal.getTime());
	}
	
	/**
	 * Overrides the toString method, called by the DayView when printing events in the 
	 * JTextArea
	 */
	public String toString() {
		String str = name + ": " + startTime;
		
		if(hasEndTime) {
			str += " - " + endTime;
		}
		return str;
	}
	
}

	
