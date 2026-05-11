import java.util.Random;

public class FashionMNISTCNN {

    // Input image size
    static int imageSize = 28;

    // Simple sample image
    static double[][] image =
            new double[28][28];

    // Labels
    static String[] labels = {
            "T-shirt",
            "Trouser",
            "Pullover",
            "Dress",
            "Coat",
            "Sandal",
            "Shirt",
            "Sneaker",
            "Bag",
            "Ankle Boot"
    };

    // Convolution Kernel
    static double[][] kernel = {
            {1, 0, -1},
            {1, 0, -1},
            {1, 0, -1}
    };

    // Feature Map
    static double[][] featureMap =
            new double[26][26];

    // Initialize Image
    static void initializeImage() {

        Random rand = new Random();

        for (int i = 0; i < 28; i++) {

            for (int j = 0; j < 28; j++) {

                image[i][j] =
                        rand.nextDouble();
            }
        }
    }

    // Convolution Operation
    static void convolution() {

        for (int i = 0; i < 26; i++) {

            for (int j = 0; j < 26; j++) {

                double sum = 0;

                for (int ki = 0; ki < 3; ki++) {

                    for (int kj = 0; kj < 3; kj++) {

                        sum += image[i + ki][j + kj]
                                * kernel[ki][kj];
                    }
                }

                featureMap[i][j] = relu(sum);
            }
        }
    }

    // ReLU Activation
    static double relu(double x) {

        return Math.max(0, x);
    }

    // Flatten Layer
    static double[] flatten() {

        double[] flat =
                new double[26 * 26];

        int index = 0;

        for (int i = 0; i < 26; i++) {

            for (int j = 0; j < 26; j++) {

                flat[index++] =
                        featureMap[i][j];
            }
        }

        return flat;
    }

    // Fully Connected Layer
    static int classify(double[] flat) {

        Random rand = new Random();

        double maxScore = -1;

        int predictedClass = 0;

        for (int i = 0; i < 10; i++) {

            double score =
                    rand.nextDouble();

            if (score > maxScore) {

                maxScore = score;
                predictedClass = i;
            }
        }

        return predictedClass;
    }

    // Main Method
    public static void main(String[] args) {

        initializeImage();

        convolution();

        double[] flat = flatten();

        int prediction =
                classify(flat);

        System.out.println(
                "Predicted Fashion Category:");

        System.out.println(
                labels[prediction]);
    }
}
