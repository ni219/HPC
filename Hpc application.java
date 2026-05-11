import java.util.concurrent.*;

public class ParallelMatrixMultiplication {

    static int SIZE = 4;

    static int[][] A = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 1, 2, 3},
            {4, 5, 6, 7}
    };

    static int[][] B = {
            {1, 0, 2, 1},
            {0, 1, 2, 0},
            {1, 0, 1, 2},
            {2, 1, 0, 1}
    };

    static int[][] result = new int[SIZE][SIZE];

    // Task for Parallel Computation
    static class MultiplyTask implements Runnable {

        int row;
        int col;

        MultiplyTask(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public void run() {

            for (int k = 0; k < SIZE; k++) {

                result[row][col] +=
                        A[row][k] * B[k][col];
            }
        }
    }

    public static void main(String[] args)
            throws InterruptedException {

        ExecutorService executor =
                Executors.newFixedThreadPool(4);

        long start = System.nanoTime();

        // Create Parallel Tasks
        for (int i = 0; i < SIZE; i++) {

            for (int j = 0; j < SIZE; j++) {

                executor.execute(
                        new MultiplyTask(i, j));
            }
        }

        executor.shutdown();

        executor.awaitTermination(
                1,
                TimeUnit.MINUTES);

        long end = System.nanoTime();

        // Print Result Matrix
        System.out.println(
                "Result Matrix:");

        for (int i = 0; i < SIZE; i++) {

            for (int j = 0; j < SIZE; j++) {

                System.out.print(
                        result[i][j] + " ");
            }

            System.out.println();
        }

        System.out.println(
                "\nExecution Time: "
                + (end - start)
                + " ns");
    }
}
