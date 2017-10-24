import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * A matrix of solutions of type {@code T}.
 * This is a helper class for a broad class of dynamic programming problems.
 * It generalizes a 2d collection of sub-problem solutions of type {@code T} and
 * provides a method for pretty printing. Dynamically resizes as needed.
 * Note: This is not a sparse matrix, will use {@code maxX * maxY} space.
 *
 * @author Marcello Nicoletti
 * @version v0.1.0, 10/22/2017
 * @param <T> The type of the solutions to "memoize".
 */
public class MemoMatrix<T> {
    private List<List<T>> memo;
    private int maxX, maxY;

    /**
     * Creates a matrix with initial X and Y dimensions.
     *
     * @param initialX The initial number of columns.
     * @param initialY The initial number of rows.
     */
    public MemoMatrix (int initialX, int initialY) {
        memo = new ArrayList<>();
        for (int i = 0; i < initialX; i++) {
            ArrayList<T> temp = new ArrayList<>();
            for (int j = 0; j < initialY; j++) {
                temp.add(null);
            }
            memo.add(temp);
        }
        maxX = initialX;
        maxY = initialY;
    }

    /**
     * Records a particular solution at the spot (x, y) in the matrix.
     * Dynamically resizes matrix if either x or y are bigger than current
     * matrix dimensions.
     *
     * @param x The column coordinate of the solution.
     * @param y The row coordinate of the solution.
     * @param element The solution to record.
     */
    public void memoize (int x, int y, T element) {
        if (x >= maxX || y >= maxY) {
            // resize matrix to new dimensions needed.
        }

        memo.get(x).set(y, element);
    }

    /**
     * Checks whether a solution is recorded in the spot (x, y) in the matrix.
     *
     * @param x The column coordinate of the solution.
     * @param y The row coordinate of the solution.
     * @return True if the solution at this location has been recorded.
     */
    public boolean isMemoized (int x, int y) {
        if (x >= maxX || y >= maxY) {
            return false;
        }

        return memo.get(x).get(y) != null;
    }

    /**
     * Returns the solution recorded in the spot (x, y) in the matrix.
     *
     * @param x The column coordinate of the solution.
     * @param y The row coordinate of the solution.
     * @return The solution recorded in this spot, or {@code null} if no
     * solution yet recorded.
     */
    public T recall (int x, int y) {
        if (x >= maxX || y >= maxY) {
            return null;
        }

        return memo.get(x).get(y);
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
        for (int i = 0; i < memo.size(); i++) {
            List<String> strings = new ArrayList<>();
            for (int j = 0; j < memo.get(i).size(); j++) {
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
        for (int j = 0; j < lists.get(0).size(); j++) {
            System.out.print("|");
            for (int i = 0; i < lists.size(); i++) {
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
        int lineWidth = memo.size() * cellWidth;
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
