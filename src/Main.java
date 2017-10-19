import java.util.Arrays;
import java.util.Random;

public class Main {
    private static boolean DO_MEMOIZATION = true;
    private static Random rand = new Random(1);
    public static void main (String[] args) {
//        Good[] goods = new Good[5];
//        goods[0] = new Good("Elixir", 60, 3);
//        goods[1] = new Good("Dart", 72, 4);
//        goods[2] = new Good("Perl", 80, 5);
//        goods[3] = new Good("Python", 90, 6);
//        goods[4] = new Good("Ruby", 48, 4);
        Good[] goods = new Good[rand.nextInt(11) + 10];
        for (int i = 0; i < goods.length; i++) {
            int weight = rand.nextInt(16) + 1; // [1 - 15]
            int profit = rand.nextInt(51) + 50;// [50 - 100]
            goods[i] = new Good(Integer.toString(i + 1), profit, weight);
        }

        // On average take only 2/3rds of my goods.
        int maxWeight = (2 * Arrays.stream(goods).mapToInt(Good::getWeight)
                .sum()) / 3;

        System.out.println("Packing these goods into a knapsack. Can only " +
                "carry " + maxWeight + " weight.");
        for (Good good : goods) {
            System.out.println(good.toString());
        }

        Good[] packedGoods = knapsack(goods, maxWeight);
        System.out.println("\nThese goods fit with the best profit, " +
                profit(packedGoods) + ".");
        for (Good packedGood : packedGoods) {
            System.out.println(packedGood.toString());
        }
    }

    private static Good[] knapsack (Good[] goods, int maxWeight) {
        // Init memo array to nulls
        Good[][][] memo = new Good[maxWeight + 1][goods.length][];
        for (int i = 0; i < memo.length; i++) {
            for (int j = 0; j < memo[0].length; j++) {
                memo[i][j] = null;
            }
        }

        // Call recursive helper
        Good[] temp = knapsackHelper(goods, maxWeight, memo);
        printMemo(memo);
        return temp;
    }

    private static Good[] knapsackHelper (Good[] goods, int maxWeight,
            Good[][][] memo) {
        // If memoized, return that solution
        int lastGoodIdx = goods.length - 1;
        if (DO_MEMOIZATION && memo[maxWeight][lastGoodIdx] != null) {
            return memo[maxWeight][lastGoodIdx];
        }

        // A base case: only one item in goods
        if (goods.length == 1) {
            if (goods[0].getWeight() > maxWeight) {
                Good[] emptyGoods = new Good[0];
                memo[maxWeight][lastGoodIdx] = emptyGoods;
                return emptyGoods;
            } else {
                Good[] newGoods = new Good[1];
                newGoods[0] = goods[0];
                memo[maxWeight][lastGoodIdx] = newGoods;
                return newGoods;
            }
        }

        // Next recursion covers the goods except the last one
        Good[] goodsExceptLast = Arrays.copyOf(goods, lastGoodIdx);

        // Construct array of possible solution not including last item in goods
        Good[] solutionNotUsingLast = knapsackHelper(goodsExceptLast,
                maxWeight, memo);

        // Construct array of possible solution including last item in goods
        Good[] solutionUsingLast;
        int newMaxWeight = maxWeight - goods[lastGoodIdx].getWeight();
        if (newMaxWeight < 0) {
            // If the last good is heavier than the capacity we can't use it
            // Another base case.
            solutionUsingLast = new Good[0];
        } else {
            solutionUsingLast = knapsackHelper(goodsExceptLast,
                    newMaxWeight, memo);
            solutionUsingLast = Arrays.copyOf(solutionUsingLast,
                    solutionUsingLast.length + 1);
            solutionUsingLast[solutionUsingLast.length - 1] = goods[
                    lastGoodIdx];
        }

        // Compare profits and return best
        if (profit(solutionUsingLast) > profit(solutionNotUsingLast)) {
            memo[maxWeight][lastGoodIdx] = solutionUsingLast;
            return solutionUsingLast;
        } else {
            memo[maxWeight][lastGoodIdx] = solutionNotUsingLast;
            return solutionNotUsingLast;
        }
    }

    private static int profit (Good[] goods) {
       return Arrays.stream(goods).mapToInt(Good::getProfit).sum();
    }

    private static void printMemo (Good[][][] memo) {
        int max = profit(memo[memo.length - 1][memo[0].length - 1]);
        int width = Integer.toString(max).length();
        System.out.println();
        for (int j = 0; j < memo[0].length; j++) {
            for (int i = 0; i < memo.length; i++) {
                if (memo[i][j] == null) {
                    for (int k = 0; k < width; k++) {
                        if (k == (width / 2) + 0) {
                            System.out.print("—");
                        } else {
                            System.out.print(" ");
                        }
                    }
                    System.out.print(" ");
                } else {
                    System.out.printf("%" + width + "d ", profit(memo[i][j]));
                }
            }
            System.out.println();
        }
    }
}

class Good {
    private final String name;
    private final int profit;
    private final int weight;

    Good (String name, int profit, int weight) {
        this.name = name;
        this.profit = profit;
        this.weight = weight;
    }

    public String getName () {
        return name;
    }

    public int getProfit () {
        return profit;
    }

    public int getWeight () {
        return weight;
    }

    @Override
    public String toString () {
        return String.format("(%s, p=%d, w=%d)", this.name, this.profit, this
                .weight);
    }
}