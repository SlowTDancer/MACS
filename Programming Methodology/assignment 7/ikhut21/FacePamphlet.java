
/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import javax.swing.*;

public class FacePamphlet extends Program implements FacePamphletConstants {

	/**
	 * This method has the responsibility for initializing the interactors in the
	 * application, and taking care of any other initialization that needs to be
	 * performed.
	 */
	public void init() {
		createDisplay();
		createDatabase();
		addListeners();
	}

	/*
	 * this method creates display.
	 */
	private void createDisplay() {
		this.setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		name = new JLabel("Name");
		add(name, NORTH);
		header = new JTextField(TEXT_FIELD_SIZE);
		add(header, NORTH);
		add = new JButton("Add");
		add(add, NORTH);
		delete = new JButton("Delete");
		add(delete, NORTH);
		lookUp = new JButton("Lookup");
		add(lookUp, NORTH);
		cStatus = new JTextField(TEXT_FIELD_SIZE);
		add(cStatus, WEST);
		changeStatus = new JButton("Change Status");
		add(changeStatus, WEST);
		empty1 = new JLabel(EMPTY_LABEL_TEXT);
		add(empty1, WEST);
		cPicture = new JTextField(TEXT_FIELD_SIZE);
		add(cPicture, WEST);
		changePicture = new JButton("Change Picture");
		add(changePicture, WEST);
		empty2 = new JLabel(EMPTY_LABEL_TEXT);
		add(empty2, WEST);
		aFriend = new JTextField(TEXT_FIELD_SIZE);
		add(aFriend, WEST);
		addFriend = new JButton("Add Freind");
		add(addFriend, WEST);
		canvas = new FacePamphletCanvas();
		add(canvas);
	}

	/*
	 * this method creates database.
	 */
	private void createDatabase() {
		database = new FacePamphletDatabase();
	}

	/*
	 * this method add action listeners.
	 */
	private void addListeners() {
		addActionListeners();
		header.addActionListener(this);
		add.addActionListener(this);
		delete.addActionListener(this);
		lookUp.addActionListener(this);
		cStatus.addActionListener(this);
		changeStatus.addActionListener(this);
		cPicture.addActionListener(this);
		changePicture.addActionListener(this);
		aFriend.addActionListener(this);
		addFriend.addActionListener(this);

	}

	/**
	 * This class is responsible for detecting when the buttons are clicked or
	 * interactors are used, so you will have to add code to respond to these
	 * actions.
	 */
	public void actionPerformed(ActionEvent e) {
		addPeople(e);
		deletePeople(e);
		lookUpPeople(e);
		changeStatus(e);
		changePicture(e);
		addFriend(e);
	}

	/*
	 * this method adds people.
	 */
	private void addPeople(ActionEvent e) {
		if (e.getSource() == add && !header.getText().equals("")) {
			String x = header.getText();
			if (database.containsProfile(x)) {
				String message = "A profile with the name " + x + " already exists";
				currentProfile = database.getProfile(x);
				canvas.displayProfile(currentProfile);
				canvas.showMessage(message);
			} else {
				FacePamphletProfile newProfile = new FacePamphletProfile(x);
				database.addProfile(newProfile);
				currentProfile = newProfile;
				String message = "New profile created";
				canvas.displayProfile(currentProfile);
				canvas.showMessage(message);
			}
			header.setText("");
		}
	}

	/*
	 * this method deletes people.
	 */
	private void deletePeople(ActionEvent e) {
		if (e.getSource() == delete && !header.getText().equals("")) {
			String x = header.getText();
			if (database.containsProfile(x)) {
				String message = "Profile of " + x + " deleted";
				database.deleteProfile(x);
				currentProfile = null;
				canvas.displayProfile(currentProfile);
				canvas.showMessage(message);
			} else {
				String message = "A profile with the name " + x + " doesn't exists";
				currentProfile = null;
				canvas.displayProfile(currentProfile);
				canvas.showMessage(message);
			}
			header.setText("");
		}
	}
	/*
	 * this method searches people.
	 */

	private void lookUpPeople(ActionEvent e) {
		if (e.getSource() == lookUp && !header.getText().equals("")) {
			String x = header.getText();
			if (database.containsProfile(x)) {
				FacePamphletProfile lookedUpProfile = database.getProfile(x);
				String message = "Displaying " + x;
				currentProfile = lookedUpProfile;
				canvas.displayProfile(currentProfile);
				canvas.showMessage(message);
			} else {
				String message = "A profile with the name " + x + " doesn't exists";
				currentProfile = null;
				canvas.displayProfile(currentProfile);
				canvas.showMessage(message);
			}
			header.setText("");
		}
	}

	/*
	 * this method changes current profiles status.
	 */
	private void changeStatus(ActionEvent e) {
		if (e.getSource() == cStatus || e.getSource() == changeStatus && !cStatus.getText().equals("")) {
			String x = cStatus.getText();
			if (currentProfile != null) {
				currentProfile.setStatus(x);
				canvas.displayProfile(currentProfile);
				String message = "Status updated to " + x;
				canvas.showMessage(message);
			} else {
				String message = "Please select a profile to change status";
				canvas.displayProfile(currentProfile);
				canvas.showMessage(message);
			}
			cStatus.setText("");
		}
	}

	/*
	 * this method changes current profiles picture.
	 */
	private void changePicture(ActionEvent e) {
		if (e.getSource() == cPicture || e.getSource() == changePicture && !cPicture.getText().equals("")) {
			String filename = cPicture.getText();
			if (currentProfile != null) {
				GImage image = null;
				try {
					image = new GImage(filename);
				} catch (ErrorException ex) {
					// Code that is executed if the filename cannot be opened.
				}
				if (image != null) {
					currentProfile.setImage(image);
					String message = "Picture updated";
					canvas.displayProfile(currentProfile);
					canvas.showMessage(message);
				} else {
					String message = "Unable to open image file: " + filename;
					canvas.displayProfile(currentProfile);
					canvas.showMessage(message);
				}
			} else {
				String message = "Please select a profile to change picture";
				canvas.displayProfile(currentProfile);
				canvas.showMessage(message);

			}
			cPicture.setText("");
		}
	}

	/*
	 * this method adds friends to current profile and vice-versa.
	 */
	private void addFriend(ActionEvent e) {
		if (e.getSource() == aFriend || e.getSource() == addFriend && !aFriend.getText().equals("")) {
			String x = aFriend.getText();
			if (currentProfile != null) {
				if (database.containsProfile(x)) {
					if (database.getProfile(x) == currentProfile) {
						String message = "sorry, but you can't be friends with yourself :(";
						canvas.displayProfile(currentProfile);
						canvas.showMessage(message);
					} else {
						if (currentProfile.addFriend(x) == true) {
							database.getProfile(x).addFriend(currentProfile.getName());
							String message = x + " added as a friend";
							canvas.displayProfile(currentProfile);
							canvas.showMessage(message);
						} else {
							String message = currentProfile.getName() + " already has " + x + " as a friend";
							canvas.displayProfile(currentProfile);
							canvas.showMessage(message);
						}

					}
				} else {
					String message = x + " does not exist";
					canvas.displayProfile(currentProfile);
					canvas.showMessage(message);
				}
			} else {
				String message = "Please select a profile to add friend";
				canvas.displayProfile(currentProfile);
				canvas.showMessage(message);
			}
		}
		aFriend.setText("");
	}

	/*
	 * instance variables.
	 */
	private JLabel name;
	private JTextField header;
	private JButton add;
	private JButton delete;
	private JButton lookUp;
	private JTextField cStatus;
	private JButton changeStatus;
	private JLabel empty1;
	private JLabel empty2;
	private JTextField cPicture;
	private JButton changePicture;
	private JTextField aFriend;
	private JButton addFriend;
	private FacePamphletDatabase database;
	private FacePamphletProfile currentProfile;
	private FacePamphletCanvas canvas;
}
