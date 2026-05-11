%%writefile ParallelGraphTraversal.java
  import java.util.*;
import java.util.concurrent.*;

public class ParallelGraphTraversal {

    private int vertices;
    private LinkedList<Integer>[] adj;

    // Constructor
    ParallelGraphTraversal(int v) {
        vertices = v;
        adj = new LinkedList[v];

        for (int i = 0; i < v; i++) {
            adj[i] = new LinkedList<>();
        }
    }

    // Add edge
    void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v); // Undirected graph
    }

    // Parallel BFS
    void parallelBFS(int start) {

        boolean visited[] = new boolean[vertices];

        Queue<Integer> queue = new LinkedList<>();

        visited[start] = true;
        queue.add(start);

        System.out.println("Parallel BFS Traversal:");

        ExecutorService executor = Executors.newFixedThreadPool(4);

        while (!queue.isEmpty()) {

            int node = queue.poll();

            System.out.print(node + " ");

            List<Callable<Void>> tasks = new ArrayList<>();

            for (Integer neighbor : adj[node]) {

                tasks.add(() -> {

                    synchronized (visited) {

                        if (!visited[neighbor]) {
                            visited[neighbor] = true;

                            synchronized (queue) {
                                queue.add(neighbor);
                            }
                        }
                    }

                    return null;
                });
            }

            try {
                executor.invokeAll(tasks);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
    }

    // Parallel DFS Utility
    void parallelDFSUtil(int node, boolean[] visited,
                         ExecutorService executor) {

        visited[node] = true;

        System.out.print(node + " ");

        List<Callable<Void>> tasks = new ArrayList<>();

        for (Integer neighbor : adj[node]) {

            if (!visited[neighbor]) {

                tasks.add(() -> {

                    parallelDFSUtil(neighbor, visited, executor);

                    return null;
                });
            }
        }

        try {
            executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Parallel DFS
    void parallelDFS(int start) {

        boolean visited[] = new boolean[vertices];

        ExecutorService executor = Executors.newFixedThreadPool(4);

        System.out.println("\nParallel DFS Traversal:");

        parallelDFSUtil(start, visited, executor);

        executor.shutdown();
    }

    // Main Method
    public static void main(String args[]) {

        ParallelGraphTraversal g = new ParallelGraphTraversal(7);

        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 3);
        g.addEdge(1, 4);
        g.addEdge(2, 5);
        g.addEdge(2, 6);

        g.parallelBFS(0);

        g.parallelDFS(0);
    }
}
