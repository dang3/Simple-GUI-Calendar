/**
 * Contains the basic layout for the entire application. This is where everything in the application will be 
 * placed on top of.
 */
import javax.swing.JFrame;

public class Frame extends JFrame {
	/**
	 * Width of the frame for the application
	 */
	private final int WIDTH = 600;
	/**
	 * Height of the frame for the application
	 */
	private final int HEIGHT = 400;
	/**
	 * Contains the MonthViewPanel and the DayViewPanel
	 */
	private Displayer panel;
	
	/**
	 * Initializes the Frame and passes the Model to all Views
	 * @param theModel Instance of the Model that will be passed on to all Views
	 */
	public Frame(Model theModel) {
		panel = new Displayer(WIDTH, HEIGHT, theModel);
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("Calendar Application");
		add(panel);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
}
