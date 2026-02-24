import java.util.ArrayList;

public class StockAnalysis {

    public static void main(String[] args) {

        // Sample data
        int[] priceArray = {100, 120, 130, 120, 150};

        ArrayList<Integer> priceList = new ArrayList<>();
        priceList.add(100);
        priceList.add(120);
        priceList.add(130);
        priceList.add(120);
        priceList.add(150);

        // Calculate average
        double averageArray = calculateAveragePrice(priceArray);
        double averageList = calculateAveragePrice(priceList);

        // Find maximum
        int maxArray = findMaximumPrice(priceArray);
        int maxList = findMaximumPrice(priceList);

        // Count occurrences of 120
        int count = countOccurrences(priceArray, 120);

        // Compute cumulative sum
        ArrayList<Integer> cumulativeSum = computeCumulativeSum(priceList);

        // Print results
        System.out.println("Average (Array): " + averageArray);
        System.out.println("Average (ArrayList): " + averageList);

        System.out.println("Maximum (Array): " + maxArray);
        System.out.println("Maximum (ArrayList): " + maxList);

        System.out.println("Occurrences of 120: " + count);

        System.out.println("Cumulative Sum (ArrayList): " + cumulativeSum);
    }

    // Calculate average (Array)
    public static double calculateAveragePrice(int[] prices) {
        double sum = 0;

        for (int i = 0; i < prices.length; i++) {
            sum += prices[i];
        }

        return sum / prices.length;
    }

    // Calculate average (ArrayList)
    public static double calculateAveragePrice(ArrayList<Integer> prices) {
        double sum = 0;

        for (int i = 0; i < prices.size(); i++) {
            sum += prices.get(i);
        }

        return sum / prices.size();
    }

    // Find maximum (Array)
    public static int findMaximumPrice(int[] prices) {
        int max = prices[0];

        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > max) {
                max = prices[i];
            }
        }

        return max;
    }

    // Find maximum (ArrayList)
    public static int findMaximumPrice(ArrayList<Integer> prices) {
        int max = prices.get(0);

        for (int i = 1; i < prices.size(); i++) {
            if (prices.get(i) > max) {
                max = prices.get(i);
            }
        }

        return max;
    }

    // Count occurrences (Array)
    public static int countOccurrences(int[] prices, int target) {
        int count = 0;

        for (int i = 0; i < prices.length; i++) {
            if (prices[i] == target) {
                count++;
            }
        }

        return count;
    }

    // Compute cumulative sum (ArrayList)
    public static ArrayList<Integer> computeCumulativeSum(ArrayList<Integer> prices) {
        ArrayList<Integer> cumulativeList = new ArrayList<>();
        int runningSum = 0;

        for (int i = 0; i < prices.size(); i++) {
            runningSum += prices.get(i);
            cumulativeList.add(runningSum);
        }

        return cumulativeList;
    }
}