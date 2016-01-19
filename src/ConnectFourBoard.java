import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

/**
 * The "ConnectFourBoard" class. Handles the board play for a simple game of
 * Connect 4
 * 
 * @author Derrick Thai
 * @version May 14, 2014
 */
public class ConnectFourBoard extends JPanel implements MouseListener,
		KeyListener
{
	// Program constants (declared at the top, these can be used by any method)
	private final int BIRD = -1;
	private final int PIG = 1;
	private final int EMPTY = 0;

	private final int SQUARE_SIZE = 64;
	private final int DROPPING_SPEED = 10;
	private final int NO_OF_ROWS = 6;
	private final int NO_OF_COLUMNS = 7;
	private final boolean ANIMATION_ON = true;

	// Artwork by Derrick Thai based off Angry Birds
	private final String IMAGE_FILENAME_PLAYER1 = "bird.png";
	private final String IMAGE_FILENAME_PLAYER2 = "pig.png";
	public final Dimension BOARD_SIZE = new Dimension(NO_OF_COLUMNS
			* SQUARE_SIZE + 1, (NO_OF_ROWS + 1) * SQUARE_SIZE + 1);

	// Program variables (declared at the top, these can be
	// used or changed by any method)
	private int[][] board;
	private boolean droppingPiece;
	private int xFallingPiece, yFallingPiece;
	private int currentPlayer;
	private int currentColumn;
	private Image firstImage, secondImage;
	private boolean gameOver;
	private int noOfMoves;

	/**
	 * Constructs a new ConnectFourBoard object
	 */
	public ConnectFourBoard()
	{
		// Sets up the board area, loads in piece images and starts a new game
		setPreferredSize(BOARD_SIZE);

		// Set the background colour to light blue
		setBackground(new Color(210, 240, 250));
		// Add mouse listeners and Key Listeners to the game board
		addMouseListener(this);
		setFocusable(true);
		addKeyListener(this);
		requestFocusInWindow();

		// Load up the images for the pieces
		firstImage = new ImageIcon(IMAGE_FILENAME_PLAYER1).getImage();
		secondImage = new ImageIcon(IMAGE_FILENAME_PLAYER2).getImage();

		// Sets up the board array and starts a new game
		board = new int[NO_OF_ROWS + 2][NO_OF_COLUMNS + 2];
		newGame();
	}

	/**
	 * Starts a new game
	 */
	public void newGame()
	{
		// Reset number of moves to 0 for a new game
		noOfMoves = 0;

		currentPlayer = PIG;
		clearBoard();
		gameOver = false;
		currentColumn = NO_OF_COLUMNS / 2 + 1;
		droppingPiece = false;
		repaint();
	}

	/**
	 * Makes all of the squares on the board empty
	 */
	private void clearBoard()
	{
		// Make each square of the visible board to empty
		for (int row = 1; row <= NO_OF_ROWS; row++)
			for (int col = 1; col <= NO_OF_COLUMNS; col++)
				board[row][col] = EMPTY;
	}

	/**
	 * Finds and returns the row to place a piece in, given a column
	 * 
	 * @param column the column where the piece is to be placed
	 * @return the row to place the piece in or 0 if the given column has all of
	 *         its rows filled with pieces
	 */
	private int findRow(int column)
	{
		// Start at the bottom most row of the given column and move upward
		// until an empty square is found. If all of the rows are filled in the
		// given column return 0 to notify the make move method
		int row = NO_OF_ROWS;
		while (board[row][column] != EMPTY)
			row--;
		return row;
	}

	/**
	 * Checks to see if there is a winning combination on the board
	 * 
	 * @param lastRow the row of the last piece placed
	 * @param lastColumn the column of the last piece placed
	 * @return the winner or EMPTY if the there is no winner
	 */
	private int checkForWinner(int lastRow, int lastColumn)
	{
		// Get the type of the last piece placed and initializes a variable to
		// keep track of piece streaks (number of consecutive pieces)
		int lastPiece = board[lastRow][lastColumn];
		int streak = 1;

		// Check vertically under the last piece placed
		for (int change = 1; board[lastRow + change][lastColumn] == lastPiece; change++)
			if (++streak >= 4)
				return lastPiece;

		// Check horizontally beside the last piece placed both ways (E and W)
		streak = 1;
		for (int direction = -1; direction <= 1; direction += 2)
			for (int change = 1; board[lastRow][lastColumn + change * direction] == lastPiece; change++)
				if (++streak >= 4)
					return lastPiece;

		// Check both diagonal possibilities: NW -> SE and NE -> SW (determined
		// by invert variable). Within each of those possibilities, check both
		// ways (back and forth; determined by the direction variable)
		for (int invert = -1; invert <= 1; invert += 2)
		{
			streak = 1;
			for (int direction = -1; direction <= 1; direction += 2)
				for (int change = 1; board[lastRow + change * direction][lastColumn
						+ (change * direction) * invert] == lastPiece; change++)
					if (++streak >= 4)
						return lastPiece;
		}

		return EMPTY;
	}

	/**
	 * Makes a move on the board (if possible)
	 * 
	 * @param selectedColumn the selected column to move in
	 */
	private void makeMove(int selectedColumn)
	{
		if (gameOver)
		{
			JOptionPane.showMessageDialog(this,
					"Please Select Game...New to start a new game",
					"Game Over", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int row = findRow(selectedColumn);
		if (row <= 0)
		{
			JOptionPane.showMessageDialog(this, "Please Select another Column",
					"Column is Full", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// If the move is valid increase the number of moves by one
		noOfMoves++;

		if (ANIMATION_ON)
			animatePiece(currentPlayer, selectedColumn, row);
		board[row][selectedColumn] = currentPlayer;

		int winner = checkForWinner(row, selectedColumn);

		if (winner == BIRD)
		{
			gameOver = true;
			repaint(0);
			JOptionPane.showMessageDialog(this, "Bird Wins!!!", "GAME OVER",
					JOptionPane.INFORMATION_MESSAGE);
		}
		else if (winner == PIG)
		{
			gameOver = true;
			repaint(0);
			JOptionPane.showMessageDialog(this, "Pig Wins!!!", "GAME OVER",
					JOptionPane.INFORMATION_MESSAGE);
		}
		// If the number of moves is equal to the total number of squares on the
		// board and there is no winner, it's a tie
		else if (noOfMoves == NO_OF_ROWS * NO_OF_COLUMNS)
		{
			gameOver = true;
			repaint(0);
			JOptionPane.showMessageDialog(this, "It's a tie!!!", "GAME OVER",
					JOptionPane.INFORMATION_MESSAGE);
		}
		else
			// Switch to the other player
			currentPlayer *= -1;

		// Start piece in centre
		currentColumn = NO_OF_COLUMNS / 2 + 1;

		repaint();
	}

	/**
	 * Animates a falling piece
	 * 
	 * @param player the player whose piece is falling
	 * @param column the column the piece is falling in
	 * @param finalRow the final row the piece will fall to
	 */
	private void animatePiece(int player, int column, int finalRow)
	{
		droppingPiece = true;
		for (double row = 0; row < finalRow; row += 0.20)
		{
			// Find the x and y positions for the falling piece
			xFallingPiece = (column - 1) * SQUARE_SIZE;
			yFallingPiece = (int) (row * SQUARE_SIZE);

			// Update the drawing area
			paintImmediately(0, 0, getWidth(), getHeight());

			delay(DROPPING_SPEED);

		}
		droppingPiece = false;
	}

	/**
	 * Delays the given number of milliseconds
	 * 
	 * @param milliSec The number of milliseconds to delay
	 */
	private void delay(int milliSec)
	{
		try
		{
			Thread.sleep(milliSec);
		}
		catch (InterruptedException e)
		{
		}
	}

	/**
	 * Repaint the board's drawing panel
	 * 
	 * @param g The Graphics context
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		// Redraw the board with current pieces
		for (int row = 1; row <= NO_OF_ROWS; row++)
			for (int column = 1; column <= NO_OF_COLUMNS; column++)
			{
				// Find the x and y positions for each row and column
				int xPos = (column - 1) * SQUARE_SIZE;
				int yPos = row * SQUARE_SIZE;

				// Draw the squares (gray colour)
				g.setColor(Color.GRAY);
				g.drawRect(xPos, yPos, SQUARE_SIZE, SQUARE_SIZE);

				// Draw each piece, depending on the value in board
				if (board[row][column] == BIRD)
					g.drawImage(firstImage, xPos, yPos, this);
				else if (board[row][column] == PIG)
					g.drawImage(secondImage, xPos, yPos, this);
			}
		
		// Draw moving piece if animating
		if (droppingPiece)
		{
			if (currentPlayer == BIRD)
				g.drawImage(firstImage, xFallingPiece, yFallingPiece, this);
			else
				g.drawImage(secondImage, xFallingPiece, yFallingPiece, this);
		}
		else
		// Draw next player
		{
			if (!gameOver)
				if (currentPlayer == BIRD)
					g.drawImage(firstImage, (currentColumn - 1) * SQUARE_SIZE,
							0, this);
				else
					g.drawImage(secondImage, (currentColumn - 1) * SQUARE_SIZE,
							0, this);
		}
	} // paint component method

	// Keyboard events you can listen for since this JPanel is a KeyListener

	/**
	 * Responds to a keyPressed event
	 * 
	 * @param event information about the key pressed event
	 */
	public void keyPressed(KeyEvent event)
	{
		// Change the currentRow and currentColumn of the player
		// based on the key pressed
		if (event.getKeyCode() == KeyEvent.VK_LEFT)
		{
			if (currentColumn > 1)
				currentColumn--;
			else
				// If trying to move left and already at the leftmost column, go
				// to the rightmost column
				currentColumn = NO_OF_COLUMNS;
		}
		else if (event.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			if (currentColumn < NO_OF_COLUMNS)
				currentColumn++;
			else
				// If trying to move right and already at the rightmost column, go
				// to the leftmost column
				currentColumn = 1;
		}
		// These keys indicate player's move
		else if (event.getKeyCode() == KeyEvent.VK_DOWN
				|| event.getKeyCode() == KeyEvent.VK_ENTER
				|| event.getKeyCode() == KeyEvent.VK_SPACE)
		{
			makeMove(currentColumn);
		}

		// Repaint the screen after the change
		repaint();
	}

	// Extra methods needed since this game board is a KeyListener
	public void keyReleased(KeyEvent event)
	{
	}

	public void keyTyped(KeyEvent event)
	{
	}

	// Mouse events you can listen for since this JPanel is a MouseListener

	/**
	 * Responds to a mousePressed event
	 * 
	 * @parameventinformation about the mouse pressed event
	 */
	public void mousePressed(MouseEvent event)
	{
		// Calculate which column was clicked, then make
		// the player's move for that column
		int selectedColumn = event.getX() / SQUARE_SIZE + 1;
		makeMove(selectedColumn);
	}

	// Extra methods needed since this game board is a MouseListener

	public void mouseReleased(MouseEvent event)
	{
	}

	public void mouseClicked(MouseEvent event)
	{
	}

	public void mouseEntered(MouseEvent event)
	{
	}

	public void mouseExited(MouseEvent event)
	{
	}
}
