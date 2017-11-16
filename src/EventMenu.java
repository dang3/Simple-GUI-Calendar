/**
 * Activated when the user clicks on the create event button from the DayView panel
 */
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EventMenu {
	/**
	 * Frame of the class
	 */
	private JFrame frame;
	/**
	 * Contains the JComponents
	 */
	private JPanel panel;
	/**
	 * Retrieves the information entered in the textfields and uses them to create a new event
	 */
	private JButton saveBut;
	/**
	 * Label that indicates to the user where to enter the name of the event
	 */
	private JLabel eventNameLab;
	/**
	 * Label that indicates to the user the date of the event
	 */
	private JLabel dateLab;
	/**
	 * Label that indicates to the user where to enter the start time of the event
	 */
	private JLabel startLab;
	/**
	 * Label that indicates to the user where to enter the end time of the event
	 */
	private JLabel endLab;
	/**
	 * Where the user enters the name of the event
	 */
	private JTextField eventNameTF;
	/**
	 * Contains the date of the event, cannot be editted 
	 */
	private JTextField dateTF;
	/**
	 * Where the user enters the start time of the event
	 */
	private JTextField startTF;
	/**
	 * Where the user enters the end time of the event
	 */
	private JTextField endTF;
	/**
	 * Used to place components
	 */
	private GridBagConstraints c;	
	/**
	 * Instance of the Model
	 */
	private Model model;

	/**
	 * Initializes the panel, calls the methods necessary for this panel to function
	 * @param model
	 */
	public EventMenu(Model model) {
		this.model = model;
		initFrame();
		initComponents();
		addComponents();
		addListener();
	}
	
	/**
	 * Initialize the panel and the frame
	 */
	private void initFrame() {
		frame = new JFrame();
		frame.setSize(300, 150);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Create Event");
		panel = new JPanel(new GridBagLayout());
		frame.add(panel);
		c = new GridBagConstraints();
	}
	
	/**
	 * Initialize the components
	 */
	private void initComponents() {
		// Labels
		saveBut = new JButton("Save");
		eventNameLab = new JLabel("Event Name:      ");
		dateLab = new JLabel("Date:      ");
		startLab = new JLabel("Start:      ");
		endLab = new JLabel("End:      ");

		// Textfields
		eventNameTF = new JTextField(10);
		dateTF = new JTextField((model.getCurMonth() + 1) + "/" + model.getCurDay() + "/" + model.getCurYear());
		dateTF.setColumns(6);
		dateTF.setEditable(false);
		startTF = new JTextField(5);
		endTF = new JTextField(5);
	}
	
	/**
	 * Places the components using the GridBagLayout manager
	 */
	private void addComponents() {
		// Add labels
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		panel.add(eventNameLab, c);
		c.gridy++;
		panel.add(dateLab, c);
		c.gridy++;
		panel.add(startLab, c);
		c.gridy++;
		panel.add(endLab, c);

		// add textfields
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(eventNameTF, c);
		c.gridy++;
		panel.add(dateTF, c);
		c.gridy++;
		panel.add(startTF, c);
		c.gridy++;
		panel.add(endTF, c);

		c.gridy++;
		panel.add(saveBut, c);
	}

	/**
	 * Adds listeners to the buttons. When clicking on the save button, the text in the textfields are retrieved and used to 
	 * instantiate a new Event object. Before adding the event to the Model's data structure containing events, the application
	 * checks if the new event conflicts with any existing event. If no conflict, add event. Else let the user know using a new
	 * frame.
	 */
	private void addListener() {
		saveBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String endTime = endTF.getText();
				Event event = new Event();
				event.setName(eventNameTF.getText());
				event.setStartTime(startTF.getText());
				event.setCal(model.getCurYear(), model.getCurMonth(), model.getCurDay());
			
				if(endTime.equals(""))
					event.setHasEndTime(false);
				else
					event.setEndTime(endTime);
				
				if(!model.isConflicting(event)) {
					model.addEvent(event);
					frame.dispose();
				}
				else {
					// if conflicts, create new frame to let user know
					JFrame errorFrame = new JFrame();
					errorFrame.setTitle("Error - Conflicting Event Times");
					errorFrame.setLocationRelativeTo(frame);
					errorFrame.setLayout(null);
					JLabel errorLabel = new JLabel("The event you are trying to create\n conflicts with another event");
					JButton okBut = new JButton("Ok");
					errorFrame.setSize(400, 120);
					errorFrame.setVisible(true);
					errorFrame.setResizable(false);
					errorFrame.add(errorLabel);
					errorFrame.add(okBut);
					
					errorLabel.setBounds(20, errorFrame.getWidth()/2 - errorLabel.getPreferredSize().width/2, errorLabel.getPreferredSize().width, 20);
					okBut.setBounds(errorFrame.getWidth()/2 - okBut.getPreferredSize().width/2, 60, 50, 20  );
					okBut.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							errorFrame.dispose();
							frame.dispose();
						}
					});
				}
			}
		});
	}
}
