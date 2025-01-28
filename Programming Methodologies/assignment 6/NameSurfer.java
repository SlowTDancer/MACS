
/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class NameSurfer extends Program implements NameSurferConstants {
	/* Method: init() */
	/**
	 * This method has the responsibility for reading in the data base and
	 * initializing the interactors at the bottom of the window.
	 */
	public void init() {
		getData(NAMES_DATA_FILE);
		createBackground();
		addListeners();
	}

	/*
	 * this method gets data.
	 */
	private void getData(String file) {
		data = new NameSurferDataBase(NAMES_DATA_FILE);
	}

	/*
	 * this method creates background.
	 */
	private void createBackground() {
		this.setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		graph = new NameSurferGraph();
		add(graph);
		label = new JLabel("Name");
		add(label, SOUTH);
		name = new JTextField(TEXT_FIELD_SIZE);
		add(name, SOUTH);
		graphs = new JButton("Graph");
		add(graphs, SOUTH);
		clear = new JButton("Clear");
		add(clear, SOUTH);
	}

	/*
	 * this method adds listeners.
	 */
	private void addListeners() {
		addActionListeners();
		name.addActionListener(this);
		graphs.addActionListener(this);
		clear.addActionListener(this);
	}

	/* Method: actionPerformed(e) */
	/**
	 * This class is responsible for detecting when the buttons are clicked, so you
	 * will have to define a method to respond to button actions.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == graphs || e.getSource() == name) {
			String x = name.getText();
			name.setText("");
			String y = oneUpperRestLower(x);
			NameSurferEntry entry = data.findEntry(y);
			if (entry != null) {
				graph.addEntry(entry);
				graph.update();
			}
		}
		if (e.getSource() == clear) {
			graph.clear();
			graph.update();
		}
	}

	/*
	 * this method turns entry into a string,which turns first letter into upper
	 * case letter and turns rest of them into LowerCase letters.
	 */
	private String oneUpperRestLower(String line) {
		String a = line.substring(0, 1).toUpperCase();
		String b = line.substring(1).toLowerCase();
		line = a + b;
		return line;
	}

	/*
	 * instance variables.
	 */
	private NameSurferGraph graph;
	private NameSurferDataBase data;
	private JLabel label;
	private JTextField name;
	private JButton graphs;
	private JButton clear;
}
