// JCount.java

/*
 Basic GUI/Threading exercise.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JCount extends JPanel {
	private final JTextField field;
	private final JLabel label;
	private final JButton start;
	private final JButton stop;
	private Worker worker;

	public class Worker extends Thread{
		private int target;
		private int count;
		public Worker(int target) {
			this.target = target;
			count = 0;
		}

		@Override
		public void run() {
			while (count < target) {
				if (isInterrupted()) break;
				if (count % 10000 == 0) {
					int finalCount = count;
					SwingUtilities.invokeLater(() -> label.setText(Integer.toString(finalCount)));
					try {
						sleep(100);
					} catch (InterruptedException e) {
						break;
					}
				}
				count++;
			}
			label.setText("Counting Done");
		}
	}
	public JCount() {
		// Set the JCount to use Box layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		field = new JTextField(15);
		label = new JLabel("waiting for input");
		start = new JButton("Start");
		stop = new JButton("Stop");

		addListeners();

		add(field);
		add(label);
		add(start);
		add(stop);

		add(Box.createRigidArea(new Dimension(0,40)));
	}

	private void addListeners(){
		start.addActionListener(e -> startCounting());
		stop.addActionListener(e -> stopCounting());
	}

	private void startCounting(){
		try{
			int limit = Integer.parseInt(field.getText());
			if(worker != null && worker.isAlive()) worker.interrupt();
			worker = new Worker(limit);
			worker.start();
		}catch(NumberFormatException ex) {
			ex.printStackTrace();
		}
	}

	private void stopCounting(){
		if(worker != null && worker.isAlive()) worker.interrupt();
	}
	private static void createAndShowGUI() {
		// Creates a frame with 4 JCounts in it.
		// (provided)

		JFrame frame = new JFrame("The Count");
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args)  {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}