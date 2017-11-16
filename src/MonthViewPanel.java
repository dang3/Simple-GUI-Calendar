/**
 * Displays the month view on the left side of the calendar. Contains all of the components needed
 * to advance the Calendar forward.
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class MonthViewPanel extends JPanel implements ChangeListener {
	/**
	 * Width of the panel
	 */
	private int width;
	/**
	 * Height of the panel
	 */
	private int height;
	/**
	 * Displays the current month and year at the top of the Calendar
	 */
	private JLabel monthYearLab;
	/**
	 * When clicked, advances the Calendar forward by 1 day
	 */
	private JButton nextBut;
	/**
	 * When clicked, advances the Calendar back by 1 day
	 */
	private JButton prevBut;
	/**
	 * Table that contains the days of the month and the days of the week for the Calendar
	 */
	private JTable calTable;
	/**
	 * Contains the information to be placed on the JTable
	 */
	private DefaultTableModel tableModel;
	/**
	 * Contains the JTable
	 */
	private JScrollPane sPane;
	/**
	 * Reference to the Model
	 */
	private Model model;

	/**
	 * 
	 * @param width
	 * @param height
	 * @param theModel
	 */
	public MonthViewPanel(int width, int height, Model theModel) {
		initPanel(width, height, theModel);
		initComponents();
		initCalendar();
		updateCal(model.getCurMonth(), model.getCurYear());
		addListeners();
	}
	
	/**
	 * Initializes the panel
	 * @param width Width of the panel
	 * @param height Height of the panel
	 * @param theModel Reference to the Model
	 */
	private void initPanel(int width, int height, Model theModel) {
		model = theModel;
		this.width = width;
		this.height = height;
		setLayout(null);
		this.setBorder(BorderFactory.createTitledBorder("Month View"));
	}
	
	/**
	 * Initializes the JComponents
	 */
	private void initComponents() {
		monthYearLab = new JLabel("December 2015");
		monthYearLab.setFont(new Font("Arial", Font.BOLD, 15));
		nextBut = new JButton(">");
		prevBut = new JButton("<");
		tableModel = new DefaultTableModel() { // contains the data for the
												// table
			public boolean isCellEditable(int row, int col) {
				return false; // prevents the cell from being edited
			}
		};
		calTable = new JTable(tableModel); // put the data in the table
		sPane = new JScrollPane(calTable); // put the table in the scroll pane
		placeComponents();
	}
	
	/**
	 * Adds components to the panel and sets their locations
	 */
	private void placeComponents() {
		// Add components
		add(monthYearLab);
		add(nextBut);
		add(prevBut);
		add(sPane);

		// Set locations
		prevBut.setBounds(10, 20, 50, 20);
		nextBut.setBounds(width-60, 20, 50, 20);
		monthYearLab.setBounds(width / 2 - monthYearLab.getPreferredSize().width / 2, 20,
				monthYearLab.getPreferredSize().width, 20);

		sPane.setBounds(10, 50, width-20, height-90);
	}
	
	/**
	 * Initialize the properties of the JTable containing the calendar and how 
	 * it will look
	 */
	private void initCalendar() {
		// Prevent user from resizing/reorder columns
		calTable.getTableHeader().setResizingAllowed(false);
		calTable.getTableHeader().setReorderingAllowed(false);

		// Allows selection of cells and only 1 cell at a time
		calTable.setColumnSelectionAllowed(true);
		calTable.setRowSelectionAllowed(true);
		calTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		prepCal();
	}
	
	/**
	 * Adds the days of the week to the first row, sets the size dimensions of each panel
	 */
	private void prepCal() {
		String[] daysOfWeek = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

		for (int i = 0; i < daysOfWeek.length; i++) {
			tableModel.addColumn(daysOfWeek[i]);
		}
		calTable.setRowHeight(48);
		tableModel.setColumnCount(7);
		tableModel.setRowCount(6);
	}
	
	/**
	 * Updates the Calendar, the month/year label. Called whenever there is a change in the Model
	 * @param month
	 * @param year
	 */
	public void updateCal(int month, int year) {
		String[] months = { "January", "Feburary", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };

		// Update the month/year label
		monthYearLab.setText(months[month] + " " + year);
		monthYearLab.setBounds(width / 2 - monthYearLab.getPreferredSize().width / 2, 20,
				monthYearLab.getPreferredSize().width, 20);

		// Clear the table
		tableModel.setRowCount(0);
		tableModel.setRowCount(6);

		// Add calendar numbers
		model.setCal(year, month, 1); // Set calendar to first day of the month
		int maxDays = model.getCal().getActualMaximum(Calendar.DAY_OF_MONTH);
		int colCounter = model.getCal().get(Calendar.DAY_OF_WEEK) - 1;
		int rowCounter = 0;

		for (int i = 1; i <= maxDays; i++) {
			tableModel.setValueAt(i, rowCounter, colCounter);
			colCounter++;
			if (colCounter > 6) {
				colCounter = 0;
				rowCounter++;
			}
		}

		model.setCal(model.getCurYear(), model.getCurMonth(), model.getCurDay());
		calTable.setDefaultRenderer(calTable.getColumnClass(0), new MyRenderer());
	}

	/**
	 * Adds Listeners to the JButtons. Also adds a listener to the JTable itself
	 * to allow the user to click on a day and sets the Calendar to that day
	 */
	private void addListeners() {
		nextBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.moveDayForward();
			}
		});

		prevBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.moveDayBack();
			}
		});
		
		calTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = calTable.rowAtPoint(e.getPoint());
				int col = calTable.columnAtPoint(e.getPoint());
				
				Object obj = calTable.getValueAt(row, col);
				
				if(obj == null)
					return;
				else {
					model.setNewDay((int)obj);
				}
			}
		});
	}

	/**
	 * Defines the properties of the JTable, determines how the cells in the JTable will look
	 * @author Dennis
	 *
	 */
	private class MyRenderer extends DefaultTableCellRenderer {
		/**
		 * Overriding method to implement custom renderer. Weekend columns are colored light blue, current day is colored
		 * green and any day with an event is colored yellow.
		 */
		public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused,
				int row, int column) {
			Component cell = super.getTableCellRendererComponent(table, value, selected, focused, row, column);
			setHorizontalAlignment(JLabel.CENTER);
			if (column == 0 || column == 6) // if end of week, set to light blue
				cell.setBackground(new Color(180, 225, 247));
			else
				cell.setBackground(new Color(255, 255, 255)); // else set to default color
			
			if (value != null) {
				// check if curDay has an event
				for(Event event : model.getEventsList()) {
					if( Integer.parseInt(value.toString()) == event.getDay() && event.getMonth() == model.getCurMonth() && event.getYear() == model.getCurYear()) {
						cell.setBackground(new Color(248, 243, 155));
						break;
					}
				}
				
				// check if curDay is the selected day
				if(Integer.parseInt(value.toString()) == model.getCurDay()) {
					cell.setBackground(new Color(163, 245, 156));
				}
			}
		
			return cell;
		}
	}

	/**
	 * Called by the Model when there is a change in the Model
	 */
	public void stateChanged(ChangeEvent e) {
		updateCal(model.getCurMonth(), model.getCurYear());
	}
}
