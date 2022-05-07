package oose.vcs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Config {

	void configStart(final Controller controller) {
		String label = "start";
		controller.buttons[0] = new JButton(label);
		controller.buttons[0].setBackground(Color.lightGray);
		controller.buttons[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(controller.vehicle == null) {
					int selectedIndex = controller.combobox.getSelectedIndex();
					String vehicleName = controller.vehicles[selectedIndex];
					controller.vehicle = controller.init.initialiseVehicle(vehicleName);
					controller.speedlabel.setText(controller.vehicle.printSpeed());
				}
				if(controller.simulationPane !=null) {
					controller.frame.remove(controller.simulationPane);
				}
				controller.accelerate = false;
				controller.decelerate = false;
				controller.cruise = false;
				controller.stop = false;
				
				setBackgrounds(label, controller);
	
				controller.simulationPane = new Simulator(controller.maximumvelocity, controller.currentvelocity, controller.vehicle);
				controller.frame.add(controller.simulationPane,BorderLayout.CENTER);
				controller.frame.revalidate();
				controller.frame.repaint();
			}
	
		});
	}

	void configAccelerate(final Controller controller) {
		String label = "accelerate";
		controller.buttons[1] = new JButton(label);
		controller.buttons[1].setBackground(Color.lightGray);
		controller.buttons[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.accelerate = true;
				controller.decelerate = false;
				controller.cruise = false;
				controller.stop = false;
	
				setBackgrounds(label, controller);
	
				Thread thread = new Thread(){
					public void run(){
						try {
							while(controller.accelerate) {
								Thread.sleep(2 * 1000);
	
								if(controller.currentvelocity <= controller.maximumvelocity) {
									controller.currentvelocity = controller.currentvelocity + 1;
									controller.vehicle.setCurrentSpeed(controller.currentvelocity);
									controller.speedlabel.setText(controller.vehicle.printSpeed());
									controller.simulationPane.updateTimer(controller.maximumvelocity, controller.currentvelocity);
								}									    
							}
						}
						catch (InterruptedException e) {
							e.printStackTrace();
						} 
					}
				};
	
				thread.start();
			}				    	
		});
	
	}

	void configCruise(final Controller controller) {
		String label = "cruise";
		controller.buttons[2] = new JButton(label);
		controller.buttons[2].setBackground(Color.lightGray);
		controller.buttons[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.accelerate = false;
				controller.decelerate = false;
				controller.cruise = true;
				controller.stop = false;
	
				setBackgrounds(label, controller);
	
			}				    	
		});
	}
	
	void configDecelerate(final Controller controller) {
		String label = "decelerate";
		controller.buttons[3] = new JButton(label);
		controller.buttons[3].setBackground(Color.lightGray);
		controller.buttons[3].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.accelerate = false;
				controller.decelerate = true;
				controller.cruise = false;
				controller.stop = false;
	
				setBackgrounds(label, controller);
				
				Thread thread = new Thread(){
					public void run(){
						try {
							while(controller.decelerate) {
								Thread.sleep(2 * 1000);
	
								if(controller.currentvelocity >1) {
									controller.currentvelocity = controller.currentvelocity - 1;
									controller.vehicle.setCurrentSpeed(controller.currentvelocity);
									controller.speedlabel.setText(controller.vehicle.printSpeed());
									controller.simulationPane.updateTimer(controller.maximumvelocity, controller.currentvelocity);
								}									    
							}
						}
						catch (InterruptedException e) {
							e.printStackTrace();
						} 
					}
				};
	
				thread.start();
			}				    	
		});
	}

	void configStop(final Controller controller) {
		String label = "stop";
		controller.buttons[4] = new JButton(label);
		controller.buttons[4].setBackground(Color.lightGray);
		controller.buttons[4].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.accelerate = false;
				controller.decelerate = false;
				controller.cruise = false;
				controller.stop = true;
	
				setBackgrounds(label, controller);
				
				controller.currentvelocity = 1;
				controller.vehicle.setCurrentSpeed(controller.currentvelocity);
				controller.speedlabel.setText(controller.vehicle.printSpeed());
				controller.simulationPane.updateTimer(controller.maximumvelocity, controller.currentvelocity);
			}				    	
		});
	}

	private void setBackgrounds(String green, final Controller controller) {
		for(JButton button : controller.buttons) {
			if(button.getText().equals(green)) {
				button.setBackground(Color.green);
			} else {
				button.setBackground(Color.lightGray);
			}
		}
	}
	

}
