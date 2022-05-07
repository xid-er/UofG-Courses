package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import monster.Monster;
import monster.Trainer;

/**
 * The top-level window to present a Trainer, allowing it to be loaded and saved
 * and for Monsters to be added and deleted.
 * 
 * JP2 Lab 6, 2020
 * 
 * @author Mary Ellen Foster <MaryEllen.Foster@glasgow.ac.uk>
 * @author Karlis Siders <2467273S@student.gla.ac.uk>
 * 
 * @see monster#Trainer
 */
// Suppress an annoying and (in this case) irrelevant Eclipse compiler warning
@SuppressWarnings("serial")
public class TrainerFrame extends JFrame implements ActionListener {
	// The buttons
	private JButton addButton, deleteButton, loadButton, saveButton;
	
	// Field to display and edit the monster name
	private JTextField nameField;
	
	// The monster list and its underlying model
	private DefaultListModel<Monster> monsterModel;
	private JList<Monster> monsterList;
	
	/**
	 * Creates a new TrainerFrame.
	 */
	public TrainerFrame() {
		// Basic window features
		super("Trainer manager");
		setLocationByPlatform(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Trainer name
		Box nameBox = Box.createHorizontalBox();
		nameField = new JTextField(20);
		nameBox.add(new JLabel("Trainer name:"));
		nameBox.add(nameField);
		
		// Create the JList and its corresponding list model
		monsterModel = new DefaultListModel<>();
		monsterList = new JList<>(monsterModel);
		
		// Add titled scrolling pane to hold the list, and add it to the layout
		JScrollPane trainerScroll = new JScrollPane(monsterList);
		trainerScroll.setBorder(new TitledBorder("Monsters"));
		JPanel listPanel = new JPanel();
		listPanel.add(trainerScroll);
		
		// Create the buttons and add the current object as a listener to all of them
		addButton = new JButton("Add Monster");
		addButton.addActionListener(this);
		deleteButton = new JButton("Delete Monster");
		deleteButton.addActionListener(this);
		loadButton = new JButton("Load Trainer");
		loadButton.addActionListener(this);
		saveButton = new JButton("Save Trainer");
		saveButton.addActionListener(this);
		
		// Lay out the buttons in a row
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(addButton);
		buttonBox.add(deleteButton);
		buttonBox.add(loadButton);
		buttonBox.add(saveButton);
		
		// Lay out the whole window
		getContentPane().setLayout(new BorderLayout()); 
		getContentPane().add(BorderLayout.NORTH, nameBox);
		getContentPane().add(listPanel);
		getContentPane().add(BorderLayout.SOUTH, buttonBox);
		pack();
	}
	
	/**
	 * Responds to a click on one of the buttons.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == addButton) {
			// Pop up a dialog box to add a monster to the trainer
			new AddMonsterDialog(this, monsterModel).setVisible(true);

		} else if (source == deleteButton) {
			// Remove monster from model
			if (! monsterList.isSelectionEmpty()) {
				int ix = monsterList.getSelectedIndex();
				monsterModel.remove(ix);
			}

		} else if (source == loadButton) {
			// Load a Trainer from "trainer.txt" and set fields to that Trainer's properties
			try {
				Trainer loaded = Trainer.parseTrainer(Files.readAllLines(Paths.get("trainer.txt")));
				nameField.setText(loaded.getName());
				monsterModel.clear();

				int i = 0;
				for(Monster mon : loaded.getMonsters()) {
					monsterModel.add(i, mon);
					i++;
				}
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Couldn't get trainer", JOptionPane.ERROR_MESSAGE);
			}
		} else if (source == saveButton) {
			// Create a Trainer object based on the fields and save it to "trainer.txt"
			try {
				PrintWriter pw = new PrintWriter (Files.newBufferedWriter(Paths.get("trainer.txt")));
				Trainer trainer = new Trainer(nameField.getText());
				for (int i = 0; i < monsterModel.size(); i++) {
					trainer.addMonster(monsterModel.get(i));
				}
				for (String line : trainer.toStringForFile()) {
					pw.println (line);
				}
				pw.close();
			} catch (IOException ex) {
				// This is how to show an error message to the user in a GUI
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Couldn't save to file", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Main method: just displays the toplevel window.
	 * 
	 * @param args Command-line arguments (not used)
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new TrainerFrame().setVisible(true);
			}
		});
	}

}