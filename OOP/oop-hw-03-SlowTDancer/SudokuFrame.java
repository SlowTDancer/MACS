import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame {
	 private JTextArea result, source;
	 private JButton check;
	 private JCheckBox autoCheck;

	 public SudokuFrame() {
		 super("Sudoku Solver");
		 BorderLayout layout = new BorderLayout(4,4);
		 this.setLayout(layout);
		 source = new JTextArea(15, 20);
		 result = new JTextArea(15, 20);
		 add(source, BorderLayout.CENTER);
		 add(result, BorderLayout.EAST);
		 source.setBorder(new TitledBorder("Puzzle"));
		 result.setBorder(new TitledBorder("Solution"));
		 check = new JButton("Check");
		 autoCheck = new JCheckBox("Auto check");
		 Box box = Box.createHorizontalBox();
		 box.add(check);
		 box.add(autoCheck);
		 add(box, BorderLayout.SOUTH);
		 addListeners();

		 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 pack();
		 setVisible(true);
	 }

	 private void addListeners(){
		 check.addActionListener( new ActionListener(){
									  public void actionPerformed(ActionEvent e){
										  haru();
									  }
								  }
		 );
		 source.getDocument().addDocumentListener(new DocumentListener() {
			 @Override
			 public void insertUpdate(DocumentEvent e) {
				 if(autoCheck.isSelected()) haru();
			 }

			 @Override
			 public void removeUpdate(DocumentEvent e) {
				 if(autoCheck.isSelected()) haru();
			 }

			 @Override
			 public void changedUpdate(DocumentEvent e) {
				 if(autoCheck.isSelected()) haru();
			 }
		 });
	 }

	 private void haru(){
		 try {
			 Sudoku solved = new Sudoku(source.getText());
			 int numSols = solved.solve();
			 String res = solved.getSolutionText() + "\n";
			 res += "solutions: " + numSols + "\n";
			 res += "elapsed: " + solved.getElapsed() + "ms";
			 if(numSols == 0) res = "No Solutions Found";
			 result.setText(res);
		 }catch (RuntimeException exception){
			result.setText("Parsing Problem");
		 }
	 }

	 public static void main(String[] args) {
		 // GUI Look And Feel
		 // Do this incantation at the start of main() to tell Swing
		 // to use the GUI LookAndFeel of the native platform. It's ok
		 // to ignore the exception.
		 try {
			 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		 } catch (Exception ignored) { }

		 SudokuFrame frame = new SudokuFrame();
	 }

}
