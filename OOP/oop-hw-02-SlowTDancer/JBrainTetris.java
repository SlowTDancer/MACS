import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class JBrainTetris extends JTetris{
    private Brain brain;
    private int pieceCounter;
    private Brain.Move move;
    private Random adversaryRandom;
    private JCheckBox brainActive;
    private JCheckBox animateFalling;
    private JSlider adversarySlider;
    private JPanel little;
    private JLabel txt;
    /**
     * Creates a new JTetris where each tetris square
     * is drawn with the given number of pixels.
     *
     * @param pixels
     */
    JBrainTetris(int pixels) {
        super(pixels);
        brain = new DefaultBrain();
        adversaryRandom = new Random();
    }

    @Override
    public Piece pickNextPiece() {
        if(adversaryRandom.nextInt(0, 100) >= adversarySlider.getValue()){
            txt.setText("ok");
            return super.pickNextPiece();
        }
        int index = 0;
        double score = 0;
        for(int i = 0; i < pieces.length; i++){
            board.undo();
            Brain.Move currentMove = brain.bestMove(board, pieces[i], HEIGHT, move);
            if(currentMove != null){
                if(currentMove.score > score){
                    score = currentMove.score;
                    index = i;
                }
            }
        }
        txt.setText("*ok*");
        return pieces[index];
    }


    @Override
    public void tick(int verb) {
        if(!gameOn) return;
        if(!brainActive.isSelected()) {
            super.tick(verb);
            return;
        }
        if(pieceCounter < super.count) {
            board.undo();
            move = brain.bestMove(board, currentPiece, HEIGHT, move);
            pieceCounter = super.count;
        }
        if(verb == DOWN){
            if(move == null){
                super.tick(verb);
                return;
            }
            int currX = move.x;
            int currY = move.y;
            Piece currP = move.piece;
            if(!currP.equals(currentPiece)){
                super.tick(ROTATE);
            }else if(currX > currentX){
                super.tick(RIGHT);
            }else if(currX < currentX){
                super.tick(LEFT);
            } else if(currY < currentY && !animateFalling.isSelected()) {
                super.tick(DROP);
            }
        }
        super.tick(verb);
    }

    @Override
    public void startGame() {
        super.startGame();
        pieceCounter = 0;
    }

    @Override
    public JComponent createControlPanel() {
        JComponent panel = super.createControlPanel();
        panel.add(new JLabel("Brain:"));
        brainActive = new JCheckBox("Brain active:");
        panel.add(brainActive);
        animateFalling = new JCheckBox("animate Falling:");
        animateFalling.setSelected(true);
        panel.add(animateFalling);
        little = new JPanel();
        little.add(new JLabel("Adversary:"));
        adversarySlider = new JSlider(0,100,0);
        adversarySlider.setPreferredSize(new Dimension(100,15));
        little.add(adversarySlider);
        txt = new JLabel("ok");
        little.add(txt);
        panel.add(little);
        return panel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        JBrainTetris BTetris = new JBrainTetris(16);
        JFrame frame = JBrainTetris.createFrame(BTetris);
        frame.setVisible(true);
    }
}
