package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JPanel;

import monster.Trainer;

/**
 * Represents a game board, including a battle area, a resting area, and a number
 * of trainers represented as TrainerSprites.
 * 
 * @author Mary Ellen Foster <MaryEllen.Foster@glasgow.ac.uk>
 */
@SuppressWarnings("serial")
public class GameWorld extends JPanel {
	// Dimensions
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    public static final int GRID_SIZE = 4;
	public static final int SQUARE_WIDTH = WIDTH / GRID_SIZE;
	public static final int SQUARE_HEIGHT = HEIGHT / GRID_SIZE;
    
	/** The list of trainer sprites to display */
    private List<TrainerSprite> sprites;
    
    /** The top-level window, with text fields and list boxes */
    private GameWindow window;
    
    // Information relevant to battles
    /** Lock to control access to the battle area */
	private Lock battleLock = new ReentrantLock();
	/** A condition to allow a trainer to wait for another trainer to enter the area */
	private Condition battleCondition = battleLock.newCondition();
	/** The trainer, if any, that is currently waiting */
	private TrainerSprite waitingSprite;
	/** The array of all threads to run*/
	private Thread[] threads;
    
	/**
	 * Creates a new GameWorld, linked to the given window and displaying the given number of trainers.
	 * 
	 * @param window The top-level window
	 * @param numTrainers How many trainers to start with
	 */
    public GameWorld(GameWindow window, int numTrainers) {
    	// GUI properties
        setBackground(Color.DARK_GRAY);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        
        // Store this so that we can update it with battle results
        this.window = window;
        
        // Add the specified number of sprites
        sprites = new ArrayList<>();
		for (int i = 0; i < numTrainers; i++) {
			sprites.add(new TrainerSprite(Utils.getRandomTrainer(), this, (i - numTrainers/2) * 3));
		}

		// Make sure to repaint regularly
		Timer timer = new Timer();
		TimerTask repaintTask = new TimerTask() {
			@Override
			public void run() {
				repaint();
				Toolkit.getDefaultToolkit().sync();
			}
		};
		timer.schedule(repaintTask, 10, 10);
		
		threads = new Thread[numTrainers];
    }
    
    /**
     * Starts all of the sprites moving around.
     */
    public void startSprites() {
    	int n = threads.length;
    	if(n > 0 && (threads[0] == null || ! threads[0].isAlive())) {
    		for (int i = 0; i < n; i++) {
    			threads[i] = new Thread (sprites.get(i));
    			threads[i].start();
    		}
    	}
    }
    
    /**
     * Stops all of the sprites 
     */
    public void stopSprites() {
    	if(threads.length > 0 && threads[0] != null) {
    		for(Thread thread : threads) {
        		if(thread.isAlive()) {
        			thread.interrupt();
        		}
        	}
    	}
    }
    
    /**
     * Checks whether the given location corresponds to the battle area.
     * 
     * @param loc The location to check
     * @return Whether it is in the battle area
     */
    public boolean isBattleArea(Point loc) {
    	return loc.x == 0 && loc.y == 0;
    }
    
    /**
     * Checks whether the given location corresponds to the resting area.
     * 
     * @param loc The location to check
     * @return Whether it is in the resting area
     */
    public boolean isRestingArea(Point loc) {
    	return loc.x == GRID_SIZE - 1 && loc.y == GRID_SIZE - 1;
    }
    
    /**
     * Indicates that the given trainer is entering the battle area. Depending if there is another trainer
     * already there, they will immediately battle; if there is no trainer there, they will wait for another
     * to arrive; if there are already two trainers battling, this trainer will wait until the battle is over
     * before entering.
     * 
     * @param sprite The sprite corresponding to the trainer who wants to battle
     * @throws InterruptedException If the processing is interrupted while waiting for the battle or while battling
     */
	public void enterBattleArea (TrainerSprite sprite) throws InterruptedException {
		// Get the lock first
		battleLock.lockInterruptibly();

		try {
			sprite.setLocation(new Point(0,0));
			if(waitingSprite == null) {
				waitingSprite = sprite;
				window.setTrainer1(sprite.getTrainer());
				battleCondition.await();
			} else {
				window.setTrainer2(sprite.getTrainer());
				Trainer trainer1 = waitingSprite.getTrainer();
				Trainer trainer2 = sprite.getTrainer();
				Trainer winner = Trainer.doBattle(trainer1, trainer2);
				Thread.sleep(1000);
				if(trainer1 == winner) {
					waitingSprite.setBattleWinner();
					sprite.setBattleLoser();
					window.addEliminatedTrainer(trainer2);
				} else {
					sprite.setBattleWinner();
					waitingSprite.setBattleLoser();
					window.addEliminatedTrainer(trainer1);
				}
				
				waitingSprite = null;
				window.setTrainer1(null);
				window.setTrainer2(null);
				battleCondition.signal();
			}
		} finally {
			// Whatever happens above, be sure to release the lock before returning
			battleLock.unlock();
		}
	}
    
    /**
     * Draws the UI on the screen.
     */
    @Override
    protected void paintComponent(Graphics g) {
    	// Draw the background first
    	super.paintComponent(g);
    	
    	// Draw the battle area -- top left corner
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, GameWorld.SQUARE_WIDTH, GameWorld.SQUARE_HEIGHT);

		// Draw the resting area -- bottom right corner
		g.setColor(Color.BLACK);
		int realX = (GameWorld.GRID_SIZE - 1) * GameWorld.SQUARE_WIDTH;
		int realY = (GameWorld.GRID_SIZE - 1) * GameWorld.SQUARE_HEIGHT;
		g.fillRect(realX, realY, realX + GameWorld.SQUARE_WIDTH, realY + GameWorld.SQUARE_HEIGHT);

    	// Draw grid lines (I think this is still buggy but it works on square grids so I'll live with it)
    	g.setColor(Color.LIGHT_GRAY);
    	for (int i = 0; i < (GRID_SIZE-1); i++) {
    		int yPos = (i+1) * SQUARE_WIDTH;
    		int xPos = (i+1) * SQUARE_HEIGHT;
    		g.drawLine(xPos, 0, xPos, WIDTH);
    		g.drawLine(0, yPos, HEIGHT, yPos);
    	}
    	
    	// Draw sprites
    	for (TrainerSprite sprite : sprites) {
    		sprite.paint(g);
    	}
    }

}
