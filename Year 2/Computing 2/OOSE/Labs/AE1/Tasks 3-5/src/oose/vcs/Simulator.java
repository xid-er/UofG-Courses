package oose.vcs;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import vehicle.types.Airplane;
import vehicle.types.Bicycle;
import vehicle.types.Boat;
import vehicle.types.Bus;
import vehicle.types.Car;
import vehicle.types.Helicopter;
import vehicle.types.Motorcycle;
import vehicle.types.Ship;
import vehicle.types.Train;
import vehicle.types.Tram;
import vehicle.types.Truck;
import vehicle.types.Vehicle;

public class Simulator extends JPanel {

	private BufferedImage boat;
	private int xPos = 0;
	private int direction = 1;
	private File file; 
	private Timer timer;
	
	private int maximumvelocity;
	private int currentvelocity;
	private Vehicle vehicle;
	
	public Simulator(int maximumvelocity, int currentvelocity, Vehicle vehicle) {
		this.maximumvelocity = maximumvelocity;
		this.currentvelocity = currentvelocity;
		this.vehicle = vehicle;
		setDisplayObject();
		try {	
			boat = ImageIO.read(file);
			timer = new Timer(maximumvelocity/currentvelocity, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					xPos += direction;
					if (xPos + boat.getWidth() > getWidth()) {
						xPos = 0;
						direction *= -1;

					} else if (xPos < 0) { 
						xPos = 0;
						direction *= -1;
					}
					repaint();
				}

			});
			timer.setRepeats(true);
			timer.setCoalesce(true);
			timer.start();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void updateTimer(int maximumvelocity, int currentvelocity) {
		timer.setDelay(maximumvelocity/currentvelocity);
	}

	private void setDisplayObject() {
		if(vehicle instanceof Boat) {
			file = new File(System.getProperty("user.dir")+"/img/boat.png");
		}
		else if(vehicle instanceof Ship) {
			file = new File(System.getProperty("user.dir")+"/img/ship.png");
		}
		else if(vehicle instanceof Truck) {
			file = new File(System.getProperty("user.dir")+"/img/truck.png");
		}
		else if(vehicle instanceof Motorcycle) {
			file = new File(System.getProperty("user.dir")+"/img/motorcycle.png");
		}
		else if(vehicle instanceof Bus) {
			file = new File(System.getProperty("user.dir")+"/img/bus.png");
		}
		else if(vehicle instanceof Car) {
			file = new File(System.getProperty("user.dir")+"/img/car.png");
		}
		else if(vehicle instanceof Bicycle) {
			file = new File(System.getProperty("user.dir")+"/img/bicycle.png");
		}
		else if(vehicle instanceof Helicopter) {
			file = new File(System.getProperty("user.dir")+"/img/helicopter.png");
		}
		else if(vehicle instanceof Airplane) {
			file = new File(System.getProperty("user.dir")+"/img/airplane.png");
		}
		else if(vehicle instanceof Tram) {
			file = new File(System.getProperty("user.dir")+"/img/tram.png");
		}
		else if(vehicle instanceof Train) {
			file = new File(System.getProperty("user.dir")+"/img/train.png");
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return boat == null ? super.getPreferredSize() : new Dimension(boat.getWidth() * 4, boat.getHeight());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int y = getHeight() - boat.getHeight();
		g.drawImage(boat, xPos, y, this);

	}

}
