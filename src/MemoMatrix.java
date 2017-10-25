import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * A matrix of solutions of type {@code T}.
 * This is a helper class for a broad class of dynamic programming problems.
 * It generalizes a 2d collection of sub-problem solutions of type {@code T} and
 * provides a method for pretty printing. Dynamically resizes as needed.
 * Note: This is not a sparse matrix, will use {@code maxCols * maxRows} space.
 *
 * @param <T> The type of the solutions to "memoize".
 * @author Marcello Nicoletti
 * @version v0.3.0, 10/25/2017
 */
public class MemoMatrix <T> {
    // memo is a column major list. It is a list of columns where each column
    // is a list of values. A column's ith value is in the ith row.
    private List<List<T>> memo;
    private int maxCols, maxRows;

    /**
     * Creates a matrix with no initial columns or rows.
     */
    public MemoMatrix () {
        this(0, 0);
    }

    /**
     * Creates a matrix with initial columns and rows.
     *
     * @param initialCols The initial number of columns.
     * @param initialRows The initial number of rows.
     */
    public MemoMatrix (int initialCols, int initialRows) {
        maxCols = initialCols;
        maxRows = initialRows;

        memo = new ArrayList<>();
        for (int i = 0; i < maxCols; i++) {
            List<T> column = new ArrayList<>();
            for (int j = 0; j < maxRows; j++) {
                column.add(null);
            }
            memo.add(column);
        }
    }

    /**
     * Records a particular solution at the spot (col, row) in the matrix.
     * Dynamically resizes matrix if either col or row are bigger than current
     * matrix dimensions.
     *
     * @param col     The column coordinate of the solution.
     * @param row     The row coordinate of the solution.
     * @param element The solution to record.
     */
    public void memoize (int col, int row, T element) {
        ensureMatrixSize(col, row);

        memo.get(col).set(row, element);
    }

    /**
     * Checks whether a solution is recorded in the spot (col, row) in the
     * matrix.
     *
     * @param col The column coordinate of the solution.
     * @param row The row coordinate of the solution.
     * @return True if the solution at this location has been recorded.
     */
    public boolean isMemoized (int col, int row) {
        if (col >= maxCols || row >= maxRows) {
            return false;
        }

        return memo.get(col).get(row) != null;
    }

    /**
     * Returns the solution recorded in the spot (col, row) in the matrix.
     *
     * @param col The column coordinate of the solution.
     * @param row The row coordinate of the solution.
     * @return The solution recorded in this spot, or {@code null} if no
     * solution yet recorded.
     */
    public T recall (int col, int row) {
        if (col >= maxCols || row >= maxRows) {
            return null;
        }

        return memo.get(col).get(row);
    }

    /**
     * Pretty prints the matrix to standard out using {@code T.toString()} to
     * fill in the cells.
     */
    public void printMatrix () {
        printMatrix(T::toString);
    }

    /**
     * Pretty prints the matrix to standard out using {@code stringFunction}
     * to transform solutions into the string for cell contents.
     *
     * @param stringFunction A function, or lambda, defining a
     *                       transformation from {@code T} to {@code String}
     */
    public void printMatrix (Function<T, String> stringFunction) {
        // example call printMatrix((e) -> (Integer.toString(profit(e))));

        int maxCellWidth = 1;
        List<List<String>> lists = new ArrayList<>();
        for (int i = 0; i < maxCols; i++) {
            List<String> strings = new ArrayList<>();
            for (int j = 0; j < maxRows; j++) {
                if (isMemoized(i, j)) {
                    String stringForm = stringFunction.apply(recall(i, j));
                    strings.add(stringForm);
                    maxCellWidth = Math.max(maxCellWidth, stringForm.length());
                } else {
                    strings.add(" ");
                }
            }
            lists.add(strings);
        }

        horizontalRule(maxCellWidth);
        for (int j = 0; j < maxRows; j++) {
            System.out.print("|");
            for (int i = 0; i < maxCols; i++) {
                String string = lists.get(i).get(j);
                int padding = maxCellWidth - string.length();
                for (int k = 0; k < padding / 2; k++) {
                    System.out.print(" ");
                }
                System.out.print(string);
                for (int k = 0; k < (padding - (padding / 2)); k++) {
                    System.out.print(" ");
                }
                System.out.print("|");
            }
            horizontalRule(maxCellWidth);
        }
    }

    public int getNumCols () {
        return maxCols;
    }

    public int getNumRows () {
        return maxRows;
    }

    /**
     * Ensures matrix is large enough to contain the spot (col, row),
     * resizing if needed.
     *
     * @param col The column needed.
     * @param row The row needed.
     */
    private void ensureMatrixSize (int col, int row) {
        if (col >= maxCols || row >= maxRows) {
            // Add any extra columns if needed
            int newMaxCols = Math.max(col + 1, maxCols);
            for (int i = 0; i < newMaxCols - maxCols; i++) {
                List<T> column = new ArrayList<>();
                for (int j = 0; j < maxRows; j++) {
                    column.add(null);
                }
                memo.add(column);
            }
            maxCols = newMaxCols;

            // Add any extra rows if needed
            int newMaxRows = Math.max(row + 1, maxRows);
            if (newMaxRows > maxRows) {
                for (int i = 0; i < maxCols; i++) {
                    List<T> column = memo.get(i);
                    for (int j = 0; j < newMaxRows - maxRows; j++) {
                        column.add(null);
                    }
                }
            }
            maxRows = newMaxRows;
        }
    }

    /**
     * Helper method to print a horizontal line between rows in the pretty
     * print.
     *
     * @param cellWidth The width of each cell.
     */
    private void horizontalRule (int cellWidth) {
        cellWidth += 1; // adjust for separator
        System.out.println();
        System.out.print("+");
        int lineWidth = maxCols * cellWidth;
        for (int i = 0; i < lineWidth; i++) {
            if ((i + 1) % cellWidth == 0) {
                System.out.print("+");
            } else {
                System.out.print("-");
            }
        }
        System.out.println();
    }
}
