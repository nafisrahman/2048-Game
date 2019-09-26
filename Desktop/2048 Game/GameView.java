package twosCP2;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameView extends JFrame implements IChangeListener
{
	private JLabel status;
	private JLabel score;
	private GameState grid;
	private JLabel[] tiles;

	public GameView(GameState g)
	{
		// set the value for the instance variable grid
		grid = g;
		// this next line registers the GameView with the GameState
		grid.addListener(this);

		// initialize the instance variables status, score, and tiles
		status = new JLabel("");
		score = new JLabel("");
		tiles = new JLabel[16];

		// initialize all the tiles with a centering property using
		// SwingConstants
		for (int i = 0; i < 16; i++)
		{
			tiles[i] = new JLabel("", SwingConstants.CENTER);
			tiles[i].setOpaque(true);
		}
		// create a new ActionHandler object
		ActionHandler handler = new ActionHandler(grid);
		// set the title and size
		setTitle("2048 Game");
		setSize(500, 500);
		// set the layout to a BorderLayout
		setLayout(new BorderLayout());
		// build a top panel and add it to the NORTH of the BorderLayout
		JPanel top = this.buildTopPanel(handler);
		add(top, BorderLayout.NORTH);
		// build a center panel and add it to the CENTER of the BorderLayout
		JPanel center = this.buildCenterPanel(handler);
		add(center, BorderLayout.CENTER);
		// build a bottom panel and add it to the BOTTOM of the BorderLayout
		JPanel bottom = this.buildBottomPanel();
		add(bottom, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void display()
	{
		setVisible(true);
	}

	@Override
	public void redraw()
	{
		// set the text of these labels: status, score, all the tiles
		status.setText(grid.getStatus());
		score.setText("Score: " + grid.getScore() + " ");
		for (int i = 0; i < 16; i++)
		{
			int num = grid.getValue(i / 4, i % 4);
			if (num != 0)
			{
				tiles[i].setText(num + "");
				
				if(num == 2)
				{
					tiles[i].setBackground(new Color(238, 228, 218));
				}
				else if(num == 4)
				{
					tiles[i].setBackground(new Color(237, 224, 200));
				}
				else if(num == 8)
				{
					tiles[i].setBackground(new Color(242, 177, 121));
				}
				else if(num == 16)
				{
					tiles[i].setBackground(new Color(245, 149, 99));
				}
				else if(num == 32)
				{
					tiles[i].setBackground(new Color(246, 124, 95));
				}
				else if(num == 64)
				{
					tiles[i].setBackground(new Color(246, 77, 38));
				}
				else if(num == 128)
				{
					tiles[i].setBackground(new Color(237, 207, 114));
				}
				else if(num == 256)
				{
					tiles[i].setBackground(new Color(237, 204, 97));
				}
				else if(num == 512)
				{
					tiles[i].setBackground(new Color(237, 200, 80));
				}
				else if(num == 1024)
				{
					tiles[i].setBackground(new Color(237, 197, 63));
				}
				else if(num == 2048)
				{
					tiles[i].setBackground(new Color(237, 194, 46));
				}
			}
			else
			{
				tiles[i].setText("");
				tiles[i].setBackground(Color.WHITE);
			}
		}
	}

	private JPanel buildBottomPanel()
	{
		// create a JPanel and use the instance variable status to add a label
		// to the bottom panel
		JPanel bottom = new JPanel();
		bottom.add(status);
		return bottom;
	}

	private JPanel buildTopPanel(ActionHandler handler)
	{
		// create JPanel and add a JButton to it for "New game"
		// also use the instance variable score to add a label
		// the next line registers the button if it were called "button"
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		JButton button = new JButton("New game");
		topPanel.add(button, BorderLayout.WEST);
		topPanel.add(score, BorderLayout.EAST);
		button.addActionListener(handler);
		return topPanel;
	}

	private JPanel buildCenterPanel(ActionHandler handler)
	{
		// create a JPanel with a GridLayout
		// use the instance variable tiles to fill the grid with labels
		// the next line sets up the arrow keys using the method we gave you
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(4, 4));
		for (int i = 0; i < 16; i++)
		{
			centerPanel.add(tiles[i]);
		}
		bindArrows(handler, centerPanel);
		return centerPanel;
	}

	// might need to give the students this method instead of making them
	// learn about key bindings and anonymous inner classes
	private void bindArrows(ActionHandler handler, JPanel panel)
	{
		String[] commands = { "left arrow", "up arrow", "right arrow", "down arrow" };
		for (int i = 0; i < 4; i++)
		{
			int copy = i;
			KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT + i, 0);
			Action action = new AbstractAction()
			{
				public void actionPerformed(ActionEvent e)
				{
					handler.handleArrowPress(GameState.LEFT + copy);
				}
			};
			panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(key, commands[i]);
			panel.getActionMap().put(commands[i], action);
		}
	}
}
