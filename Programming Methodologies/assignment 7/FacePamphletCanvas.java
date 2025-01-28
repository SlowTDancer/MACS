
/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */

import acm.graphics.*;
import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas implements FacePamphletConstants {

	/**
	 * Constructor This method takes care of any initialization needed for the
	 * display
	 */
	public FacePamphletCanvas() {
		// You fill this in
	}

	/**
	 * This method displays a message string near the bottom of the canvas. Every
	 * time this method is called, the previously displayed message (if any) is
	 * replaced by the new message text passed in.
	 */
	public void showMessage(String msg) {
		GLabel message = new GLabel(msg);
		message.setFont(MESSAGE_FONT);
		double x = (getWidth() - message.getWidth()) / 2;
		double y = getHeight() - BOTTOM_MESSAGE_MARGIN;
		add(message, x, y);
	}

	/**
	 * This method displays the given profile on the canvas. The canvas is first
	 * cleared of all existing items (including messages displayed near the bottom
	 * of the screen) and then the given profile is displayed. The profile display
	 * includes the name of the user from the profile, the corresponding image (or
	 * an indication that an image does not exist), the status of the user, and a
	 * list of the user's friends in the social network.
	 */
	public void displayProfile(FacePamphletProfile profile) {
		removeAll();
		if (profile != null) {
			displayName(profile);
			displayPicture(profile);
			displayStatus(profile);
			displayFriends(profile);
		}
	}

	/*
	 * this method displays current profiles name.
	 */
	private void displayName(FacePamphletProfile profile) {
		String name = profile.getName();
		namesLabel = new GLabel(name);
		namesLabel.setColor(Color.BLUE);
		namesLabel.setFont(PROFILE_NAME_FONT);
		double x = LEFT_MARGIN;
		double y = TOP_MARGIN + namesLabel.getAscent();
		add(namesLabel, x, y);
	}

	/*
	 * this method displays current profiles picture.
	 */
	private void displayPicture(FacePamphletProfile profile) {
		GImage profilePicture = profile.getImage();
		double x = LEFT_MARGIN;
		double y = TOP_MARGIN + namesLabel.getHeight() + IMAGE_MARGIN;
		if (profilePicture == null) {
			GRect noImage = new GRect(IMAGE_WIDTH, IMAGE_HEIGHT);
			add(noImage, x, y);
			GLabel imageLabel = new GLabel("No Image");
			imageLabel.setFont(PROFILE_IMAGE_FONT);
			double x0 = LEFT_MARGIN + (IMAGE_WIDTH - imageLabel.getWidth()) / 2;
			double y0 = TOP_MARGIN + namesLabel.getHeight() + (IMAGE_HEIGHT + imageLabel.getHeight()) / 2;
			add(imageLabel, x0, y0);
		} else {
			double xSize = IMAGE_WIDTH / profilePicture.getWidth();
			double ySize = IMAGE_HEIGHT / profilePicture.getHeight();
			profilePicture.scale(xSize, ySize);
			add(profilePicture, x, y);
		}
	}

	/*
	 * this method displays current profiles status.
	 */
	private void displayStatus(FacePamphletProfile profile) {
		String status = "";
		if (profile.getStatus().equals("")) {
			status = "No current status";
		} else {
			status = profile.getName() + " is " + profile.getStatus();
		}
		GLabel statusLabel = new GLabel(status);
		statusLabel.setFont(PROFILE_STATUS_FONT);
		double x = LEFT_MARGIN;
		double y = TOP_MARGIN + namesLabel.getHeight() + IMAGE_MARGIN + IMAGE_HEIGHT + STATUS_MARGIN
				+ statusLabel.getAscent();
		add(statusLabel, x, y);
	}

	/*
	 * this method displays current profiles friends.
	 */
	private void displayFriends(FacePamphletProfile profile) {
		GLabel friendsLabel = new GLabel("Friends:");
		friendsLabel.setFont(PROFILE_FRIEND_LABEL_FONT);
		double x = getWidth() / 2;
		double y0 = TOP_MARGIN + IMAGE_MARGIN + namesLabel.getHeight();
		add(friendsLabel, x, y0);
		Iterator<String> friendsList = profile.getFriends();
		int count = 0;
		while (friendsList.hasNext()) {
			String friend = friendsList.next();
			GLabel friendLabel = new GLabel(friend);
			friendLabel.setFont(PROFILE_FRIEND_FONT);
			double y = TOP_MARGIN + IMAGE_MARGIN + namesLabel.getHeight() + friendsLabel.getHeight()
					+ friendLabel.getHeight() * count;
			add(friendLabel, x, y);
			count++;
		}
	}

	/*
	 * instance variables.
	 */
	private GLabel namesLabel;
}
