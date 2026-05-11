import java.util.concurrent.*;

public class VectorAddition {

    static int SIZE = 1000000;

    static int[] A = new int[SIZE];
    static int[] B = new int[SIZE];
    static int[] C = new int[SIZE];

    // Parallel Task
    static class AddTask implements Runnable {

        int start, end;

        AddTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public void run() {

            for (int i = start; i < end; i++) {

                C[i] = A[i] + B[i];
            }
        }
    }

    public static void main(String[] args)
            throws InterruptedException {

        // Initialize vectors
        for (int i = 0; i < SIZE; i++) {

            A[i] = i;
            B[i] = i * 2;
        }

        int threads = 4;

        ExecutorService executor =
                Executors.newFixedThreadPool(threads);

        int chunk = SIZE / threads;

        long startTime = System.nanoTime();

        // Create Tasks
        for (int i = 0; i < threads; i++) {

            int start = i * chunk;

            int end = (i == threads - 1)
                    ? SIZE
                    : start + chunk;

            executor.execute(
                    new AddTask(start, end));
        }

        executor.shutdown();

        executor.awaitTermination(
                1,
                TimeUnit.MINUTES);

        long endTime = System.nanoTime();

        // Print sample output
        System.out.println(
                "Sample Result:");

        for (int i = 0; i < 10; i++) {

            System.out.print(C[i] + " ");
        }

        System.out.println(
                "\nExecution Time: "
                + (endTime - startTime)
                + " ns");
    }
}


import java.util.concurrent.*;

public class MatrixMultiplication {

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

    static int[][] C = new int[SIZE][SIZE];

    // Parallel Task
    static class MultiplyTask implements Runnable {

        int row, col;

        MultiplyTask(int row, int col) {

            this.row = row;
            this.col = col;
        }

        public void run() {

            for (int k = 0; k < SIZE; k++) {

                C[row][col] +=
                        A[row][k] * B[k][col];
            }
        }
    }

    public static void main(String[] args)
            throws InterruptedException {

        ExecutorService executor =
                Executors.newFixedThreadPool(4);

        long startTime = System.nanoTime();

        // Create Tasks
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

        long endTime = System.nanoTime();

        // Print Result
        System.out.println(
                "Result Matrix:");

        for (int i = 0; i < SIZE; i++) {

            for (int j = 0; j < SIZE; j++) {

                System.out.print(
                        C[i][j] + " ");
            }

            System.out.println();
        }

        System.out.println(
                "\nExecution Time: "
                + (endTime - startTime)
                + " ns");
    }
}
