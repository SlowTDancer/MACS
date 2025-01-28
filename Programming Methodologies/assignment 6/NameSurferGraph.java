
/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas implements NameSurferConstants, ComponentListener {

	/**
	 * Creates a new NameSurferGraph object that displays the data.
	 */
	public NameSurferGraph() {
		addComponentListener(this);
		update();
	}

	/**
	 * Clears the list of name surfer entries stored inside this class.
	 */
	public void clear() {
		names.clear();
	}

	/* Method: addEntry(entry) */
	/**
	 * Adds a new NameSurferEntry to the list of entries on the display. Note that
	 * this method does not actually draw the graph, but simply stores the entry;
	 * the graph is drawn by calling update.
	 */
	public void addEntry(NameSurferEntry entry) {
		names.add(entry);
	}

	/**
	 * Updates the display image by deleting all the graphical objects from the
	 * canvas and then reassembling the display according to the list of entries.
	 * Your application must call update after calling either clear or addEntry;
	 * update is also called whenever the size of the canvas changes.
	 */
	public void update() {
		removeAll();
		drawGraph(names);
		writeNames(names);
	}

	/*
	 * this method draws graph.
	 */
	public void drawGraph(ArrayList<NameSurferEntry> names) {
		drawBounds();
		drawVerticalLines();
		drawResult(names);
	}

	/*
	 * this method draws horizontal bounds.
	 */
	public void drawBounds() {
		drawUpperBound();
		drawLowerBound();
	}

	/*
	 * this method draws upper horizontal bounds.
	 */
	public void drawUpperBound() {
		int y = GRAPH_MARGIN_SIZE;
		GLine line = new GLine(0, y, getWidth(), y);
		add(line);
	}

	/*
	 * this method draws lower horizontal bounds.
	 */
	public void drawLowerBound() {
		int y = getHeight() - GRAPH_MARGIN_SIZE;
		GLine line = new GLine(0, y, getWidth(), y);
		add(line);
	}

	/*
	 * this method draws vertical lines.
	 */
	public void drawVerticalLines() {
		for (int i = 0; i < NDECADES; i++) {
			int x = getWidth() / NDECADES * i;
			GLine line = new GLine(x, 0, x, getHeight());
			add(line);
		}
	}

	/*
	 * this method draws graphs according to data.
	 */
	public void drawResult(ArrayList<NameSurferEntry> names) {
		for (int j = 0; j < names.size(); j++) {
			NameSurferEntry surfer = names.get(j);
			for (int i = 1; i < NDECADES; i++) {
				int x1 = getWidth() / NDECADES * (i - 1);
				int x2 = getWidth() / NDECADES * (i);
				int y1 = GRAPH_MARGIN_SIZE + (getHeight() - GRAPH_MARGIN_SIZE * 2) * surfer.getRank(i - 1) / MAX_RANK;
				int y2 = GRAPH_MARGIN_SIZE + (getHeight() - GRAPH_MARGIN_SIZE * 2) * surfer.getRank(i) / MAX_RANK;
				if (surfer.getRank(i - 1) == 0) {
					y1 = getHeight() - GRAPH_MARGIN_SIZE;
				}
				if (surfer.getRank(i) == 0) {
					y2 = getHeight() - GRAPH_MARGIN_SIZE;
				}
				GLine line = new GLine(x1, y1, x2, y2);
				Color c = color(j);
				line.setColor(c);
				add(line);
			}
		}
	}

	/*
	 * this method writes text on the screen.
	 */
	public void writeNames(ArrayList<NameSurferEntry> names) {
		writeDecades();
		writeEntries(names);
	}

	/*
	 * this method writes decades.
	 */
	public void writeDecades() {
		for (int i = 0; i < NDECADES; i++) {
			String year = "" + (START_DECADE + 10 * i);
			GLabel decade = new GLabel(year);
			int x = getWidth() / NDECADES * i;
			double y = getHeight() - GRAPH_MARGIN_SIZE + decade.getHeight();
			add(decade, x, y);
		}
	}

	/*
	 * this method writes entries near the point of a graph.
	 */
	public void writeEntries(ArrayList<NameSurferEntry> names) {
		for (int j = 0; j < names.size(); j++) {
			NameSurferEntry surfer = names.get(j);
			for (int i = 0; i < NDECADES; i++) {
				if (surfer.getRank(i) == 0) {
					String a = surfer.getName() + '*';
					GLabel name = new GLabel(a);
					Color c = color(j);
					name.setColor(c);
					int x = getWidth() / NDECADES * (i);
					double y = getHeight() - GRAPH_MARGIN_SIZE;
					add(name, x, y);
					continue;
				}
				String a = surfer.getName() + " " + surfer.getRank(i);
				GLabel name = new GLabel(a);
				Color c = color(j);
				name.setColor(c);
				int x = getWidth() / NDECADES * (i);
				double y = GRAPH_MARGIN_SIZE + (getHeight() - GRAPH_MARGIN_SIZE * 2) * surfer.getRank(i) / MAX_RANK;
				add(name, x, y);
			}
		}
	}

	/*
	 * this method gives color.
	 */
	private Color color(int i) {
		Color c = null;
		if (i % 4 == 0) {
			c = Color.BLACK;
		}
		if (i % 4 == 1) {
			c = Color.RED;
		}
		if (i % 4 == 2) {
			c = Color.BLUE;
		}
		if (i % 4 == 3) {
			c = Color.YELLOW;
		}
		return c;
	}

	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		update();
	}

	public void componentShown(ComponentEvent e) {
	}

	private ArrayList<NameSurferEntry> names = new ArrayList<NameSurferEntry>();
}
