package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import monster.Trainer;

/**
 * An on-screen object corresponding to a trainer in the battle game.
 * 
 * @author Mary Ellen Foster <MaryEllen.Foster@glasgow.ac.uk>
 */
public class TrainerSprite implements Runnable{
	
	/** The trainer itself */
	private Trainer trainer;
	
	/** The world where the trainer exists */
	private GameWorld world;
	
	/** Details of how to draw this sprite on the screen */
	private Color color;
	private int offset;
	private Point location;
	
	/** Whether the trainer can battle, or needs to rest */
	private boolean battleReady;
	
	/**
	 * Creates a new TrainerSprite.
	 * 
	 * @param trainer The underlying trainer
	 * @param world The game world we are added to
	 * @param offset The on-screen offset we should be drawn at
	 */
	public TrainerSprite(Trainer trainer, GameWorld world, int offset) {
		// Set fields
		this.world = world;
		this.trainer = trainer;
		this.offset = offset;
		
		// Trainers start as battle ready
		this.battleReady = true;
		
		// Choose a random colour
		this.color = new Color (Utils.RAND.nextInt(255), Utils.RAND.nextInt(255), Utils.RAND.nextInt(255));
		
		// Choose a random valid starting location
		location = new Point();
		while (world.isBattleArea(location) || world.isRestingArea(location)) {
			location.x = Utils.RAND.nextInt(GameWorld.GRID_SIZE);
			location.y = Utils.RAND.nextInt(GameWorld.GRID_SIZE);
		}
	}
	
	public void run() {
		while(battleReady) {
			try {
				Point next = chooseNextLocation();
				if(! world.isRestingArea(next)) {
					if(world.isBattleArea(next)) {
						world.enterBattleArea(this);
					}
					else {
						this.setLocation(next);
					}
				}
				Thread.sleep(250);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	/**
	 * Returns the underlying trainer. 
	 * @return the trainer
	 */
	public Trainer getTrainer() {
		return trainer;
	}
	
	/**
	 * Chooses and returns a Point representing a new location adjacent
	 * to the current location. Ensures that the Point is not off the screen.
	 * 
	 * @return The new Point
	 */
	public Point chooseNextLocation() {
		Point newLocation = new Point(location);
		// Choose which way to go
		switch (Utils.RAND.nextInt(4)) {
		case 0:
			newLocation.x++;
			break;
			
		case 1:
			newLocation.x--;
			break;
			
		case 2:
			newLocation.y++;
			break;
			
		case 3:
			newLocation.y--;
			break;
		}

		// Make sure it's in range
		if (newLocation.x < 0) newLocation.x = 0;
		if (newLocation.x > GameWorld.GRID_SIZE - 1) newLocation.x = GameWorld.GRID_SIZE - 1;
		if (newLocation.y < 0) newLocation.y = 0;
		if (newLocation.y > GameWorld.GRID_SIZE - 1) newLocation.y = GameWorld.GRID_SIZE - 1;
		
		return newLocation;
	}
	
	/**
	 * Updates this Sprite's position.
	 * 
	 * @param location The new location to use
	 */
	public void setLocation(Point location) {
		this.location = location;
	}

	/**
	 * Called to indicate that this trainer has won a battle.
	 */
	public void setBattleWinner() {
		setLocation (new Point(1, 1));
	}

	/**
	 * Called to indicate that this trainer has lost a battle and must rest.
	 */
	public void setBattleLoser() {
		setLocation (new Point(GameWorld.GRID_SIZE - 1, GameWorld.GRID_SIZE - 1));
		battleReady = false;
	}

	/**
	 * Draws this sprite on the screen at the current location.
	 * 
	 * @param g The graphics context to use
	 */
	public void paint(Graphics g) {
		// Figure out actual (x, y) location
		int realX = location.x * GameWorld.SQUARE_WIDTH + (GameWorld.SQUARE_WIDTH / 2) - 10 + offset;
		int realY = location.y * GameWorld.SQUARE_HEIGHT + (GameWorld.SQUARE_HEIGHT / 2) - 10 + offset;

		// Draw an outlined rectangle with the correct colour
		g.setColor(this.color);
		g.fillRect(realX, realY, 20, 20);
		g.setColor(Color.WHITE);
		g.drawRect(realX, realY, 20, 20);

		// Name on top
		g.drawString(trainer.getName().substring(0, 4), realX - 5, realY - 2);
	}

}
