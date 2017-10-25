import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static Random rand = new Random(1);

    public static void main (String[] args) {
//        List<Good> goods = new ArrayList<>();
//        goods.add(new Good("Elixir", 60, 3));
//        goods.add(new Good("Dart", 72, 4));
//        goods.add(new Good("Perl", 80, 5));
//        goods.add(new Good("Python", 90, 6));
//        goods.add(new Good("Ruby", 48, 4));
//        int maxWeight = 10;
        List<Good> goods = new ArrayList<>();
        for (int i = 0; i < rand.nextInt(11) + 10; i++) {
            int weight = rand.nextInt(16) + 1; // [1 - 15]
            int profit = rand.nextInt(51) + 50;// [50 - 100]
            goods.add(new Good(Integer.toString(i + 1), profit, weight));
        }
        int maxWeight = (2 * goods.stream().mapToInt(Good::getWeight)
                .sum()) / 3; // On average take only 2/3rds of my goods.

        System.out.println("Packing these goods into a knapsack. Can only " +
                "carry " + maxWeight + " weight.");
        for (Good good : goods) {
            System.out.println(good.toString());
        }

        List<Good> packedGoods = knapsack(goods, maxWeight);

        System.out.println("\nThese goods fit with the best Total Profit, " +
                totalProfit(packedGoods) + ".");
        for (Good packedGood : packedGoods) {
            System.out.println(packedGood.toString());
        }

    }

    private static List<Good> knapsack (List<Good> goods, int maxWeight) {
        MemoMatrix<List<Good>> memo = new MemoMatrix<>(maxWeight + 1,
                                                       goods.size());

        List<Good> solution = knapsackHelper(goods, maxWeight, memo);
        memo.printMatrix(e -> Integer.toString(totalProfit(e)));
        return solution;
    }

    private static List<Good> knapsackHelper (List<Good> goods, int maxWeight,
            MemoMatrix<List<Good>> memo) {
        int lastGoodIdx = goods.size() - 1;
        if (memo.isMemoized(maxWeight, lastGoodIdx)) {
            return memo.recall(maxWeight, lastGoodIdx);
        }

        // A base case: only one item in goods
        if (goods.size() == 1) {
            if (goods.get(0).getWeight() > maxWeight) {
                List<Good> emptyGoods = new ArrayList<>();
                memo.memoize(maxWeight, lastGoodIdx, emptyGoods);
                return emptyGoods;
            } else {
                List<Good> newGoods = new ArrayList<>();
                newGoods.add(goods.get(0));
                memo.memoize(maxWeight, lastGoodIdx, newGoods);
                return newGoods;
            }
        }

        List<Good> goodsExceptLast = new ArrayList<>(goods.subList(0,
                lastGoodIdx));

        List<Good> solutionNotUsingLast = knapsackHelper(goodsExceptLast,
                maxWeight, memo);

        List<Good> solutionUsingLast;
        int newMaxWeight = maxWeight - goods.get(lastGoodIdx).getWeight();
        if (newMaxWeight < 0) {
            // If the last good is heavier than the capacity we can't use it
            solutionUsingLast = new ArrayList<>();
        } else {
            solutionUsingLast = new ArrayList<>(knapsackHelper(goodsExceptLast,
                    newMaxWeight, memo));
            solutionUsingLast.add(goods.get(lastGoodIdx));
        }

        if (totalProfit(solutionUsingLast) >
                totalProfit(solutionNotUsingLast)) {
            memo.memoize(maxWeight, lastGoodIdx, solutionUsingLast);
            return solutionUsingLast;
        } else {
            memo.memoize(maxWeight, lastGoodIdx, solutionNotUsingLast);
            return solutionNotUsingLast;
        }
    }

    private static int totalProfit (List<Good> goods) {
        return goods.stream().mapToInt(Good::getProfit).sum();
    }
}
