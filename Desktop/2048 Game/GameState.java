/**********************************************************
 * Assignment: (GameState)
 *
 * Author: (Nafis Rahman)
 *
 * Description: (It sets the status, score and 4x4 grid)
 *
 * Academic Integrity: I pledge that this program represents my own work. I
 * received help from (Nathan) in designing and debugging
 * my program.
 **********************************************************/
package twosCP2;
import java.util.ArrayList;

/* Represents the game state, including status, score, and the 4x4 grid 
 * of values. Knows how to shift the values around. Notifies all 
 * registered IChangeListeners if anything happens. 
 */
public class GameState
{
	public final static int LEFT = 0;
	public final static int UP = 1;
	public final static int RIGHT = 2;
	public final static int DOWN = 3;

	private int[][] values;
	private String status;
	private int score;

	private ArrayList<IChangeListener> listeners;

	public GameState()
	{
		// initialize listeners, values, status, and score
		
		listeners = new ArrayList<IChangeListener>();
		values = new int[4][4];
		status = "";
		score = 0;
	}
	
	// For use with unit tests
	public GameState(int[][] initialValues)
	{
		listeners = new ArrayList<IChangeListener>();
		values = initialValues;
		status = "";
		score = 0;
	}

	public String getStatus()
	{
		return status;
	}

	public int getScore()
	{
		return score;
	}

	public void addListener(IChangeListener listener)
	{
		// add the listener to the arrayList of listeners
		listeners.add(listener);
	}

	public void newGame()
	{
		// clear the board
		for (int r = 0; r < 4; r++)
		{
			for (int c = 0; c < 4; c++)
			{
				values[r][c] = 0;
			}
		}

		// spawn two new tiles
		spawn();
		spawn();

		// change status, and update listeners
		score = 0;
		status = "New game started";
		this.updateListeners();
	}

	// Spawns a new tile to an empty spot on the board
	// 90% chance of getting a 2, 10% a 4
	private void spawn()
	{
		int r = (int) (Math.random() * 4);
		int c = (int) (Math.random() * 4);
		while (values[r][c] != 0)
		{
			r = (int) (Math.random() * 4);
			c = (int) (Math.random() * 4);
		}
		int rand = (int) (Math.random() * 10);
		values[r][c] = (rand != 0) ? 2 : 4;
		if(gameOver() == true)
		{
			status = "Game over!";
		}
	}

	public void shift(int direction)
	{
		// change status and update listeners
		int[][] oldValues = saveValues();
		if (direction == LEFT)
		{
			status = "Shifted tiles left";
		}
		if (direction == UP)
		{
			status = "Shifted tiles up";
		}
		if (direction == RIGHT)
		{
			status = "Shifted tiles right";
		}
		if (direction == DOWN)
		{
			status = "Shifted tiles down";
		}
		shifter(direction);
		if (boardHasChanged(oldValues))
		{
			spawn();
		}
		updateListeners();
	}

	// Returns a copy of the values array
	private int[][] saveValues()
	{
		int[][] newValues = new int [4][4];
		for(int r = 0; r < values.length; r++)
		{
			for(int c = 0; c < values[r].length; c++)
			{
				newValues[r][c] = values[r][c];
			}
		}
		return newValues;
		// TODO complete me
	}

	// Returns true when there is a difference between oldValues and values
	private boolean boardHasChanged(int[][] oldValues)
	{
		for(int r = 0; r < values.length; r++)
		{
			for(int c = 0; c < values[r].length; c++)
			{
				if(oldValues[r][c] != values[r][c])
				{
					return true;
				}
			}
		}
		return false; 
		// TODO complete me
	}

	// Shifts tiles on the board according to the direction
	private void shifter(int direction)
	{
		ArrayList<Integer> tray;
		for (int i = 0; i < 4; i++)
		{
			tray = loadTiles(i, direction);
			trayShifter(tray);
			placeOnBoard(tray, i, direction);
		}
	}

	// Returns an arrayList of values from a row or a column, according to direction.
	// The first tiles loaded are from the direction toward which we're shifting
	// (RIGHT and DOWN are loaded in reverse order)
	private ArrayList<Integer> loadTiles(int i, int direction)
	{
		ArrayList<Integer> tray = new ArrayList<Integer>();
		if (direction == RIGHT || direction == LEFT)
		{
			for (int c = 0; c < 4; c++)
			{
				tray.add((direction == LEFT) ? values[i][c] : values[i][3 - c]);
			}
		}
		else
		{
			for (int r = 0; r < 4; r++)
			{
				tray.add((direction == UP) ? values[r][i] : values[3 - r][i]);
			}
		}
		return tray;
	}

	// Copies the contents of the tray back onto the game board
	// i is the row or column on the board we are copying
	private void placeOnBoard(ArrayList<Integer> tray, int i, int direction)
	{
		// TODO complete me
		if(direction == LEFT)
		{
			int index = 0;
			for(int c = 0; c < values[i].length; c++)
			{
				values[i][c] = tray.get(index);
				index++;
			}
		}
		if(direction == RIGHT)
		{
			int index = 0;
			for(int c = values[i].length - 1; c >= 0; c--)
			{
				values[i][c] = tray.get(index);
				index++;
			}
		}
		if(direction == UP)
		{
			int index = 0;
			for(int r = 0; r < values[i].length; r++)
			{
				values[r][i] = tray.get(index);
				index++;
			}
		}
		if(direction == DOWN)
		{
			int index = 0;
			for(int r = values[i].length - 1; r >= 0; r--)
			{
				values[r][i] = tray.get(index);
				index++;
			}
		}
	}

	// Does the shifting of values in the tray. Values are always shifted
	// toward the beginning of the tray. The tray size is 4 when finished.
	private void trayShifter(ArrayList<Integer> tray)
	{
		removeZeros(tray);
		merge(tray);
		padUntil4(tray);
	}

	// Removes all zeros from the tray
	private void removeZeros(ArrayList<Integer> tray)
	{
		// TODO complete me
		int index = 0;
		while(index < tray.size())
		{
			if(tray.get(index) == 0)
			{
				tray.remove(index);
			}
			else
			{
				index++;
			}
		}
	}

	// Starting from the beginning of the tray this merges adjacent values
	// when they are equal
	// Any one value is never merged with more than one other value
	private void merge(ArrayList<Integer> tray)
	{
		// traverse tray until one short of length
		// when current value = next value, replace with double and remove next
		int pos = 0;
		int set = 0;
		while (pos < tray.size() - 1)
		{
			Integer current = tray.get(pos);
			Integer next = tray.get(pos + 1);
			if (current.equals(next))
			{
				tray.set(pos, current * 2);
				set = current * 2;
				tray.remove(pos + 1);
				score = score + set;
			}
			pos++;
		}
		
	}

	// Pad the tray with zeros until its length is 4
	private void padUntil4(ArrayList<Integer> tray)
	{
		// complete me
		while(tray.size() != 4)
		{
			tray.add(0);
		}
	}

	public int getValue(int r, int c)
	{
		// return the appropriate value
		return values[r][c];
	}

	private void updateListeners()
	{
		// for each item in the listeners list calls its redraw method
		for (IChangeListener listener : listeners)
		{
			listener.redraw();
		}
	}
	
	private boolean gameOver()
	{
		int count = 0;
		for(int i = 0; i < values.length; i++)
		{
			for(int j = 0; j < values[i].length; j++)
			{
				if(values[i][j] != 0)
				{
					count++;
				}
			}
		}
		if(count == 16)
		{
			for(int r = 0; r < values.length - 1; r++)
			{
				for(int c = 0; c < values[r].length - 1; c++)
				{
					if(values[r][c] == values[r][c + 1])
					{
						return false;
					}
					if(values[c][r] == values[c][r + 1])
					{
						return false; 
					}
				}
			}
			return true;
		}
		return false;
	}
}