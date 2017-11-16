/**
 * Displays the day view on the right side of the calendar. Contains all of the components needed
 * to create events, show event information and the application quit button.
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DayViewPanel extends JPanel implements ChangeListener {
	/**
	 * When clicked, opens another frame to allow user to enter in information for a new event.
	 */
	private JButton createBut;
	/**
	 * Saves all events into a text file and closes the application
	 */
	private JButton quitBut;
	/**
	 * Displays the date at the top of the JTextArea where event information is displayed
	 */
	private JLabel dateLab;
	/**
	 * Contains any the event information corresponding to the current date
	 */
	private JTextArea textArea;
	/**
	 * Container that contains the textArea
	 */
	private JScrollPane sPane;
	/**
	 * A reference to the Model
	 */
	private Model model;
	/**
	 * Width of the frame
	 */
	private int width;
	/**
	 * Height of the frame
	 */
	private int height;
	/**
	 * Used by the dateLab to print the days of the week
	 */
	private String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	
	/**
	 * Initializes the panel and calls the necessary methods to load the necessary components
	 * @param width Used to position the components
	 * @param height Used to position the components
	 * @param theModel A reference to the model
	 */
	public DayViewPanel(int width, int height, Model theModel) {
		initPanel(width, height, theModel);
		initComponents();
		addListeners();
		updateTextArea();
	}
	
	/**
	 * Initializes the panel
	 * @param width Used to position the components
	 * @param height Used to position the components
	 * @param theModel A reference to the model
	 */
	private void initPanel(int width, int height, Model theModel) {
		model = theModel;
		this.width = width;
		this.height = height;
		setLayout(null);
		this.setBorder(BorderFactory.createTitledBorder("Day View"));
	}
	
	/**
	 * Initialize the JComponents
	 */
	private void initComponents() {
		String curDayOfWeek = daysOfWeek[ model.getCal().get(Calendar.DAY_OF_WEEK)-1 ];
		
		createBut = new JButton("Create");
		createBut.setBackground(Color.RED);
		createBut.setForeground(Color.WHITE);
		quitBut = new JButton("Quit");
		dateLab = new JLabel(curDayOfWeek + " " + (model.getCurMonth()+1) + "/" + model.getCurDay());
		dateLab.setFont(new Font("Arial", Font.BOLD, 15));
		textArea = new JTextArea();
		textArea.setEditable(false);
		sPane = new JScrollPane(textArea);
		placeComponents();
		textArea.setFont(new Font("SANS_SERIF", Font.BOLD, 15));
		textArea.append("Scheduled Events\n\n");
	}
	
	/**
	 * Add the JComponents to the panel and set their locations
	 */
	private void placeComponents() {
		// Add components
		add(createBut);
		add(quitBut);
		add(dateLab);
		add(sPane);
		
		// Set locations
		createBut.setBounds(10, 20, createBut.getPreferredSize().width, 20);
		dateLab.setBounds( width/2 - dateLab.getPreferredSize().width/2 , 20, dateLab.getPreferredSize().width, 20);
		quitBut.setBounds(width-75, 20, quitBut.getPreferredSize().width, 20);
		sPane.setBounds(10, 50, width-25, height-90);
	}
	
	/**
	 * Adds the listeners to each JButton
	 */
	private void addListeners() {
		createBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new EventMenu(model);
			}
		});
		
		quitBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.saveToFile();
				System.exit(0);
			}
		});
	}
	
	/**
	 * Updates the text area. Loops through each event from the Model and adds the events
	 * with the same date and the current date.
	 */
	private void updateTextArea() {
		// clear the textArea
		textArea.setText("");
		textArea.append("Scheduled Events\n\n");
		
		ArrayList<Event> list = model.getEventsList();
		for(Event event : list) {
			boolean sameDay = model.getCurYear() == event.getYear() && model.getCurMonth() == event.getMonth() && model.getCurDay() == event.getDay();
			if(sameDay) 
				textArea.append(event.toString() + "\n");
		}
	}
	
	/**
	 * Called by the Model whenever something in the Model changes from the Controller
	 */
	public void stateChanged(ChangeEvent e) {
		String curDayOfWeek = daysOfWeek[ model.getCal().get(Calendar.DAY_OF_WEEK)-1 ];
		dateLab.setText( curDayOfWeek + " " + (model.getCurMonth()+1) + "/" + model.getCurDay() );
		dateLab.setBounds( width/2 - dateLab.getPreferredSize().width/2 , 20, dateLab.getPreferredSize().width, 20);
		updateTextArea();
	}
}
