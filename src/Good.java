public class Good {
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
