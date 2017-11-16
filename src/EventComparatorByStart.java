import java.util.Comparator;

/**
 * Implemens the Comparator interface for Event objects. Used to compare events for sorting
 * 
 * @author Dennis
 *
 */

public class EventComparatorByStart implements Comparator<Event> {

	/**	
	 * Compares two event objects based on their dates
	 * @param e1 An Event object
	 * @param e2 An Event object
	 * @return An int based on how the two event objects compare
	 */
	public int compare(Event e1, Event e2) {
		// First look at year
		if(e1.getYear() < e2.getYear()) {
			return -1;
		}
		else if(e1.getYear() > e2.getYear())
			return 1;
		else {
			// if years are equal, look at months
			if(e1.getMonth() < e2.getMonth())
				return -1;
			else if(e1.getMonth() > e2.getMonth())
				return 1;
			else {
				// if months are equal, look at days
				if(e1.getDay() < e2.getDay())
					return -1;
				else if(e1.getDay() > e2.getDay())
					return 1;
				else {
					// if days are equal, look at starting times
					return compareStartTime(e1,e2);
				}
			}
		}	
}
	/**
	 * Compares two Event objects based on their starting times
	 * @param e1 an Event object
	 * @param e2 an Event object
	 * @return an int based on how e1 and e2 compare based on the starting time
	 */
	private int compareStartTime(Event e1, Event e2) {
		String e1Tokens[], e2Tokens[];
		e1Tokens = e1.getStartTime().split("[:]");
		e2Tokens = e2.getStartTime().split("[:]");
		
		// Compare starting hour
		if( Integer.parseInt(e1Tokens[0]) < Integer.parseInt(e2Tokens[0]) )
			return -1;
		else if(Integer.parseInt(e1Tokens[0]) > Integer.parseInt(e2Tokens[0]) )
			return 1;
		else {
			// if starting hours are equal, look at starting minute
			if( Integer.parseInt(e1Tokens[1]) < Integer.parseInt(e2Tokens[1]) )
				return -1;
			else if(Integer.parseInt(e1Tokens[1]) > Integer.parseInt(e2Tokens[1]) )
				return 1;
			else
				return 0;	// shouldn't ever get here or else there is a time conflict with event
		}
	}
}
