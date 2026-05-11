import java.util.*;
import java.util.concurrent.*;

public class ParallelReduction {

    // Parallel Sum
    static class SumTask implements Callable<Integer> {

        int[] arr;
        int start, end;

        SumTask(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        public Integer call() {

            int sum = 0;

            for (int i = start; i < end; i++) {
                sum += arr[i];
            }

            return sum;
        }
    }

    // Parallel Min
    static class MinTask implements Callable<Integer> {

        int[] arr;
        int start, end;

        MinTask(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        public Integer call() {

            int min = arr[start];

            for (int i = start; i < end; i++) {

                if (arr[i] < min) {
                    min = arr[i];
                }
            }

            return min;
        }
    }

    // Parallel Max
    static class MaxTask implements Callable<Integer> {

        int[] arr;
        int start, end;

        MaxTask(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        public Integer call() {

            int max = arr[start];

            for (int i = start; i < end; i++) {

                if (arr[i] > max) {
                    max = arr[i];
                }
            }

            return max;
        }
    }

    public static void main(String[] args) throws Exception {

        int[] arr = {
                10, 20, 30, 40,
                50, 60, 70, 80
        };

        int threads = 4;

        ExecutorService executor =
                Executors.newFixedThreadPool(threads);

        List<Future<Integer>> sumResults =
                new ArrayList<>();

        List<Future<Integer>> minResults =
                new ArrayList<>();

        List<Future<Integer>> maxResults =
                new ArrayList<>();

        int chunk = arr.length / threads;

        // Create Tasks
        for (int i = 0; i < threads; i++) {

            int start = i * chunk;

            int end = (i == threads - 1)
                    ? arr.length
                    : start + chunk;

            sumResults.add(
                    executor.submit(
                            new SumTask(arr, start, end)));

            minResults.add(
                    executor.submit(
                            new MinTask(arr, start, end)));

            maxResults.add(
                    executor.submit(
                            new MaxTask(arr, start, end)));
        }

        // Reduction for Sum
        int totalSum = 0;

        for (Future<Integer> f : sumResults) {
            totalSum += f.get();
        }

        // Reduction for Min
        int minimum = Integer.MAX_VALUE;

        for (Future<Integer> f : minResults) {

            minimum = Math.min(minimum, f.get());
        }

        // Reduction for Max
        int maximum = Integer.MIN_VALUE;

        for (Future<Integer> f : maxResults) {

            maximum = Math.max(maximum, f.get());
        }

        // Average
        double average =
                (double) totalSum / arr.length;

        executor.shutdown();

        // Output
        System.out.println("Array: "
                + Arrays.toString(arr));

        System.out.println("Sum = "
                + totalSum);

        System.out.println("Minimum = "
                + minimum);

        System.out.println("Maximum = "
                + maximum);

        System.out.println("Average = "
                + average);
    }
}
