/**
 * A container for the monthView and dayView
 */
import javax.swing.JPanel;

public class Displayer extends JPanel {
	/**
	 * Width of the panel
	 */
	private int width;
	/**
	 * Height of the panel
	 */
	private int height;
	/**
	 * MonthView panel, displays the calendar in month view
	 */
	private MonthViewPanel monthView;
	/**
	 * DayView panel, displays the calendar in day view
	 */
	private DayViewPanel dayView;
	
	/**
	 * Initializes the panel with the width, height and its components, passes a reference
	 * of the Model to the other view panels
	 * 
	 * @param width Width of the panel
	 * @param height Height of the panel
	 * @param theModel Reference of the model
	 */
	public Displayer(int width, int height, Model theModel) {
		setLayout(null);
		setSize(width, height);
		monthView = new MonthViewPanel(width/2, height, theModel);
		dayView = new DayViewPanel(width/2, height, theModel);
		
		theModel.addListeners(monthView);
		theModel.addListeners(dayView);
		
		add(monthView);
		add(dayView);
		monthView.setBounds(0, 0, width/2, height-30);
		dayView.setBounds(width/2, 0, width/2-10, height-30);
	}
}

