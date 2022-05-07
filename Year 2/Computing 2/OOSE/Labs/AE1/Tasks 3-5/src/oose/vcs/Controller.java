package oose.vcs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import vehicle.types.Vehicle;


public class Controller {

	Vehicle vehicle;
	String[] vehicles = { "Boat", "Ship", "Truck", "Motorcycle", "Bus", "Car", "Bicycle", "Helicopter", "Airplane", "Tram", "Train"};
	Simulator simulationPane;
	Initialise init;
	Config config;
	JLabel speedlabel;
	JButton[] buttons = new JButton[5];
	JComboBox<String> combobox;
	JFrame frame;

	boolean accelerate;
	boolean decelerate;
	boolean cruise;
	boolean stop;
	int currentvelocity = 1;
	int maximumvelocity = 300;

	public static void main(String args[]) {
		new Controller();
	}

	public Controller() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
				} catch (Exception e) {
					e.printStackTrace();
				}
				frame = new JFrame("Vehicle Control System");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLayout(new BorderLayout());

				init = new Initialise();
				config = new Config();
				
				combobox = new JComboBox<String>(vehicles);
				combobox.setSelectedIndex(6);
				combobox.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						int selectedIndex = combobox.getSelectedIndex();
						String vehicleName = vehicles[selectedIndex];
						vehicle = init.initialiseVehicle(vehicleName);							
					}
				});

				speedlabel = new JLabel("          ");
				
				config.configStart(Controller.this);
				config.configAccelerate(Controller.this);
				config.configDecelerate(Controller.this);
				config.configCruise(Controller.this);
				config.configStop(Controller.this);
				
				JToolBar toolBar = new JToolBar();
				toolBar.setRollover(true);

				toolBar.add(combobox);
				toolBar.add(speedlabel);
				for(JButton button : buttons) {
					toolBar.add(button);
				}

				frame.add(toolBar,BorderLayout.NORTH);
				frame.setPreferredSize(new Dimension(800,200));
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}

		});
	}

}
