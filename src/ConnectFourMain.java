// Extra imports required for GUI code
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * The CorrectFourMain class - creates the JFrame for Connect Four Plays a
 * simple game of Connect 4 using ConnectFourBoard class
 * 
 * @author Derrick Thai
 * @version May 14, 2014
 */
public class ConnectFourMain extends JFrame implements ActionListener
{
	// Program variables for the Menu items and the game board
	private JMenuItem newOption, exitOption, rulesMenuItem, aboutMenuItem;
	private ConnectFourBoard gameBoard;

	/**
	 * Constructs a new ConnectFourMain frame (sets up the Game)
	 */
	public ConnectFourMain()
	{
		// Sets up the frame for the game
		super("Connect Four");
		setResizable(false);

		// Load up the icon image by Derrick Thai based off Angry Birds
		setIconImage(Toolkit.getDefaultToolkit().getImage("cornerIcon.png"));

		// Sets up the Connect Four board that plays most of the game
		// and add it to the centre of this frame
		gameBoard = new ConnectFourBoard();
		getContentPane().add(gameBoard, BorderLayout.CENTER);

		// Centre the frame in the middle (almost) of the screen
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - gameBoard.BOARD_SIZE.width) / 2,
				(screen.height - gameBoard.BOARD_SIZE.height) / 2 - 100);

		// Adds the menu and menu items to the frame (see below for code)
		// Set up the Game MenuItems
		newOption = new JMenuItem("New");
		newOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_MASK));
		newOption.addActionListener(this);

		exitOption = new JMenuItem("Exit");
		exitOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				InputEvent.CTRL_MASK));
		exitOption.addActionListener(this);

		// Set up the Help Menu
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');
		rulesMenuItem = new JMenuItem("Rules...", 'R');
		rulesMenuItem.addActionListener(this);
		helpMenu.add(rulesMenuItem);
		aboutMenuItem = new JMenuItem("About...", 'A');
		aboutMenuItem.addActionListener(this);
		helpMenu.add(aboutMenuItem);

		// Add each MenuItem to the Game Menu (with a separator)
		JMenu gameMenu = new JMenu("Game");
		gameMenu.add(newOption);
		gameMenu.addSeparator();
		gameMenu.add(exitOption);
		JMenuBar mainMenu = new JMenuBar();
		mainMenu.add(gameMenu);
		mainMenu.add(helpMenu);
		// Set the menu bar for this frame to mainMenu
		setJMenuBar(mainMenu);
	} // Constructor

	/**
	 * Responds to a Menu Event. This method is needed since our Connect Four
	 * frame implements ActionListener
	 * 
	 * @param event the event that triggered this method
	 */
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == newOption) // Selected "New"
		{
			gameBoard.newGame();
		}
		else if (event.getSource() == exitOption) // Selected "Exit"
		{
			hide();
			System.exit(0);
		}
		else if (event.getSource() == rulesMenuItem) // Selected "Rules"
		{
			JOptionPane
					.showMessageDialog(
							this,
							"Player's turn is depicted by the image at top."
									+ "\n\nPlayer selects a column by clicking the mouse,"
									+ "\nor by pressing the arrow keys and/or Enter key."
									+ "\n\nFirst player to get 4-in-a-row in any direction "
									+ "wins. \n\nGood luck!", "Rules",
							JOptionPane.INFORMATION_MESSAGE);
		}
		else if (event.getSource() == aboutMenuItem) // Selected "About"
		{
			JOptionPane.showMessageDialog(this, "by Derrick Thai"
					+ "\n\u00a9 2014", "About Connect Four",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Starts up the ConnectFourMain frame
	 * 
	 * @param args An array of Strings (ignored)
	 */
	public static void main(String[] args)
	{
		// Starts up the ConnectFourMain frame
		ConnectFourMain frame = new ConnectFourMain();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	} // main method
}
