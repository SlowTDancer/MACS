import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuTest extends TestCase {

    //sudoku with 6 solutions
    private static final int[][] hardV2Grid = Sudoku.stringsToGrid(
            "3 0 0 0 0 0 0 8 0",
            "0 0 1 0 9 3 0 0 0",
            "0 4 0 7 8 0 0 0 3",
            "0 9 3 8 0 0 0 1 2",
            "0 0 0 0 4 0 0 0 0",
            "5 2 0 0 0 6 7 9 0",
            "6 0 0 0 2 1 0 4 0",
            "0 0 0 5 3 0 9 0 0",
            "0 3 0 0 0 0 0 5 1");

    //sudoku with many solutions
    private static final int[][] WithManySolutionsGrid = Sudoku.stringsToGrid(
            "2 0 0 0 0 0 0 0 0",
            "0 0 0 0 0 0 1 0 0",
            "0 0 0 0 0 0 0 0 0",
            "0 0 0 0 3 0 0 0 0",
            "0 6 0 0 0 0 0 0 0",
            "0 8 0 0 0 0 0 0 4",
            "0 0 0 5 0 0 0 0 0",
            "0 0 0 0 0 0 7 0 0",
            "9 0 0 0 0 0 0 0 0");

    public void testDifferentSudokus(){
        Sudoku easy = new Sudoku(Sudoku.easyGrid);
        assertEquals(1, easy.solve());

        Sudoku medium = new Sudoku(Sudoku.mediumGrid);
        assertEquals(1, medium.solve());

        Sudoku hard = new Sudoku(Sudoku.hardGrid);
        assertEquals(1, hard.solve());

        Sudoku numSolutions = new Sudoku(hardV2Grid);
        assertEquals(6, numSolutions.solve());

        Sudoku withManySolutionsGrid = new Sudoku(WithManySolutionsGrid);
        assertEquals(Sudoku.MAX_SOLUTIONS, withManySolutionsGrid.solve());
    }

    public void testTextToGrid() {
        String badText = """
                123456789
                987654321
                123456789
                987654321
                123456789
                987654321
                123456789
                987654321
                12345678a
                """;
        assertThrows(RuntimeException.class, () -> {
            int[][] grid = Sudoku.textToGrid(badText);
        });
        String text = """
                123456789
                987654321
                123456789
                987654321
                123456789
                987654321
                123456789
                987654321
                123456789
                """;
        assertDoesNotThrow(() -> {
            int[][] grid = Sudoku.textToGrid(text);
            Sudoku sudoku = new Sudoku(text);
        });
    }

    public void testMain(){
        assertDoesNotThrow(() -> Sudoku.main(new String[6]));
    }
}