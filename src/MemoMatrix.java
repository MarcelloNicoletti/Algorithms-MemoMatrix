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

    public void printMatrix (Function<T, String> stringFunction) {
        // TODO: Ensure this works as expected
        // example call printMatrix((e) -> (Integer.toString(profit(e))));

        int maxLen = 0;
        List<List<String>> lists = new ArrayList<>();
        for (int i = 0; i < memo.size(); i++) {
            List<String> strings = new ArrayList<>();
            for (int j = 0; j < memo.get(i).size(); j++) {
                if (isMemoized(i, j)) {
                    String stringForm = stringFunction.apply(recall(i, j));
                    maxLen = Math.max(maxLen, stringForm.length());
                    strings.add(stringForm);
                } else {
                    strings.add("—");
                }
            }
            lists.add(strings);
        }
        maxLen++;

        for (List<String> strings : lists) {
            for (String string : strings) {
                int padding = maxLen - string.length();
                for (int k = 0; k < padding / 2; k++) {
                    System.out.print(" ");
                }
                System.out.print(string);
                for (int k = 0; k < (maxLen - (padding / 2) - string.length());
                     k++) {
                    System.out.print(" ");
                }
            }
        }
    }
}
