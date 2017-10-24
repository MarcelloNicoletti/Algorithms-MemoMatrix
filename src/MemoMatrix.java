import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MemoMatrix<T> {
    private List<List<T>> memo;

    public MemoMatrix (int initialX, int initialY) {
        memo = new ArrayList<>();
        for (int i = 0; i < initialX; i++) {
            ArrayList<T> temp = new ArrayList<>();
            for (int j = 0; j < initialY; j++) {
                temp.add(null);
            }
            memo.add(temp);
        }
    }

    public void memoize (int x, int y, T element) {
        // todo: index checks
        memo.get(x).set(y, element);
    }

    public boolean isMemoized (int x, int y) {
        // todo: index checks
        return memo.get(x).get(y) != null;
    }

    public T recall (int x, int y) {
        // todo: index checks
        return memo.get(x).get(y);
    }

    public void printMatrix () {
        printMatrix(T::toString);
    }

    public void printMatrix (Function<T, String> stringFunction) {
        // example call printMatrix((e) -> (Integer.toString(profit(e))));

        int maxCellWidth = 0;
        List<List<String>> lists = new ArrayList<>();
        for (int i = 0; i < memo.size(); i++) {
            List<String> strings = new ArrayList<>();
            for (int j = 0; j < memo.get(i).size(); j++) {
                if (isMemoized(i, j)) {
                    String stringForm = stringFunction.apply(recall(i, j));
                    maxCellWidth = Math.max(maxCellWidth, stringForm.length());
                    strings.add(stringForm);
                } else {
                    strings.add("—");
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
                for (int k = 0; k < (maxCellWidth - (padding / 2) - string.length());
                     k++) {
                    System.out.print(" ");
                }
                System.out.print("|");
            }
            horizontalRule(maxCellWidth);
        }
    }

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
