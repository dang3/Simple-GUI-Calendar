/**
 * The Model - contains the data that will be referenced by the Views
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Model {
	/**
	 * Stores the events
	 */
	private ArrayList<Event> eventsList;
	/**
	 * Contains the Views that will be notified whenever there is a change in the Model so that the Views know
	 * when to update themselves
	 */
	private ArrayList<ChangeListener> listenersList;
	/**
	 * Calendar that is set to the current day, month and year. 
	 */
	private Calendar cal;
	/**
	 * Used to compare 2 events objects when there is a time conflict
	 */
	private Comparator<Event> comp;
	/**
	 * The current month
	 */
	private int curMonth;
	/**
	 * The current year
	 */
	private int curYear;
	/**
	 * The current day
	 */
	private int curDay;

	/**
	 * Initializes the fields and loads in the events (if one exists) from a text file to store in the ArrayList
	 * for the events
	 */
	public Model() {
		initFields();
		load();
	}
	
	/**
	 * Initializes all components
	 */
	private void initFields() {
		eventsList = new ArrayList<>();
		cal = new GregorianCalendar();
		listenersList = new ArrayList<>();
		comp = new EventComparatorByStart();
		
		curMonth = cal.get(Calendar.MONTH);
		curYear = cal.get(Calendar.YEAR);
		curDay = cal.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * Accessor method to obtain the Calendar object
	 * @return Calendar object
	 */
	public Calendar getCal() {
		return cal;
	}
	
	/**
	 * Accessor method to obtian the events ArrayList. Called when checking for time conflicts and for
	 * printing out event information
	 * @return The events ArrayList
	 */
	public ArrayList<Event> getEventsList() {
		return eventsList;
	}
	
	/**
	 * Sets the Calendar to a new date
	 * @param year The year that the Calendar will be set to
	 * @param month The month that the Calendar will be set to
	 * @param date The day that the Calendar will be set to
	 */
	public void setCal(int year, int month, int date) {
		cal.set(year, month, date);
	}
	
	/**
	 * Changes only the day for the Calendar object, called when the user clicks on a day
	 * to set the Calendar to
	 * @param day
	 */
	public void setNewDay(int day) {
		curDay = day;
		cal.set(curYear, curMonth, day);
		alertListeners();
	}
	
	/**
	 * Adds a new event to the ArrayList, alerts the listeners of the change
	 * @param e
	 */
	public void addEvent(Event e) {
		eventsList.add(e);
		sortEvents();
		alertListeners();
	}
	
	/**
	 * Accessor method that returns the current month
	 * @return Current month
	 */
	public int getCurMonth() {
		return curMonth;
	}
	
	/**
	 * Accessor method that returns the current day
	 * @return Current day
	 */
	public int getCurDay() {
		return curDay;
	}
	
	/**
	 * Accessor Method that returns the current year
	 * @return Current year
	 */
	public int getCurYear() {
		return curYear;
	}
	
	/**
	 * Helper method, determines if the end of the month is reached
	 * @return True of end of the month is reached, false otherwise
	 */
	private boolean endOfMonth() {
		return curDay == cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * Helper method, determines if the end of the year  is reached
	 * @return True of beginning of the year is reached, false otherwise
	 */
	private boolean endOfYear() {
		return curMonth == 11;
	}
	
	/**
	 * Helper method, determines if the beginning of the month is reached
	 * @return True of beginning of the month is reached, false otherwise
	 */
	private boolean startOfMonth() {
		return curDay == 1;
	}
	
	/**
	 * Helper method, determines if the start of the year  is reached
	 * @return True of beginning of the year is reached, false otherwise
	 */
	private boolean startOfYear() {
		return curMonth == 0;
	}
	
	/**
	 * Moves the current day forward by one, then alerts the listeners of the change, called
	 * when the user clicks on the forward arrow button in the MonthViewPanel
	 */
	public void moveDayForward() {
		if(endOfMonth()) {
			if(endOfYear()) {
				curYear++;
				curMonth = 0;
			}
			else
				curMonth++;
			curDay = 1;
		}
		else
			curDay++;
		alertListeners();
	}
	
	/**
	 * Moves the current day back by one, alerts the listeners of the change, called
	 * when the user clicks on the backward arrow button in the MonthViewPanel
	 */
	public void moveDayBack() {
		Calendar temp = Calendar.getInstance();
		if(startOfMonth()) {
			if(startOfYear()) {
				curYear--;
				curMonth = 11;
			}
			else
				curMonth--;
			temp.set(curYear, curMonth, 1);
			curDay = temp.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		else
			curDay--;
		alertListeners();
	}
	
	/**
	 * Adds ChangeListeners to the ArrayList, contains the Views
	 * @param A ChangeListener to be added to the ArrayList
	 */
	public void addListeners(ChangeListener l) {
		listenersList.add(l);
	}
	
	/**
	 * Alerts the Views when there is a change
	 */
	public void alertListeners() {
		for(ChangeListener l : listenersList) {
			l.stateChanged(new ChangeEvent(this));
		}
	}
	
	/**
	 * Attempts to load events from a text file into the application. If no text file exists,
	 * a new one will be created at the end of the program
	 */
	public void load() {
		Scanner reader;
		
		try {
			reader = new Scanner(new File("events.txt"));
		} catch (FileNotFoundException e) {
			// indicates file does not exist, will be created at end of program
			return;
		}
				
		while(reader.hasNextLine()) {
			Event event = new Event();
			event.setName(reader.nextLine());
			event.setDate(reader.nextLine());
			event.setStartTime(reader.nextLine());
			
			String input = reader.nextLine();
			if(input.equals("x"))
				event.setHasEndTime(false);
			else
				event.setEndTime(input);
			
			reader.nextLine();	// discard empty new line
			eventsList.add(event);
		}
		
		sortEvents();
		reader.close();
	}
	
	/**
	 * Saves the events into a text file, called when the user clicks on the quit button in the 
	 * DayViewPanel.
	 */
	public void saveToFile() {
		PrintWriter writer;
		sortEvents();
		Iterator<Event> itr = eventsList.iterator();

		try {
			writer = new PrintWriter("events.txt");
		} catch (IOException e) {
			System.out.println("Cannot create file");
			return;
		}

		while (itr.hasNext()) {
			Event event = itr.next();
			writer.println(event.getName());
			writer.println(event.getDate());
			writer.println(event.getStartTime());
			
			if(event.hasEndTime()) {
				writer.println(event.getEndTime());
			}
			else
				writer.println("x");
			writer.println();
		}
		writer.close();
	}
	
	/**
	 * Sort events using insertion sort
	 */
	private void sortEvents() {
		// Events sorted using selection sort
		for (int i = 0; i < eventsList.size(); i++) {
			int minIndex = i;
			for (int j = i + 1; j < eventsList.size(); j++) {
				if (comp.compare(eventsList.get(j), eventsList.get(minIndex)) < 0) {
					minIndex = j;
				}
			}
			swap(minIndex, i);
		}
	}
	
	/**
	 * Helper method that swaps elements from the ArrayList
	 * @param from Index of the element to be swapped
	 * @param to Index of the element to be swapped
	 */
	private void swap(int from, int to) {
		Event temp = eventsList.get(from);
		eventsList.set(from, eventsList.get(to));
		eventsList.set(to, temp);
	}
	
	/**
	 * Determines if the new event the user is trying to add will conflict with existing events,
	 * compares events using the EventComparatorByStart
	 * @param newEvent New event that the user created and is trying to added to the Model's ArrayList
	 * @return True there is a time conflict, false otherwise
	 */
	public boolean isConflicting(Event newEvent) {
		for (int i = 0; i < eventsList.size(); i++) {
			Event curEvent = eventsList.get(i);
			// Check if both events are on the same day
			if( curEvent.getYear() == newEvent.getYear() && curEvent.getDayOfYear() == newEvent.getDayOfYear() ) {
				if(newEvent.getStartTime().compareTo(curEvent.getEndTime()) < 0 && curEvent.getStartTime().compareTo(newEvent.getEndTime()) < 0)
					return true;
			}
		}
		return false;
	}

}