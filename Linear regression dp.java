import java.util.Random;

public class BostonHousingDNN {

    // Dataset
    static double[][] X = {
            {0.1, 0.2},
            {0.2, 0.1},
            {0.3, 0.4},
            {0.4, 0.3},
            {0.5, 0.7},
            {0.6, 0.5}
    };

    // House Prices
    static double[] Y = {
            10,
            12,
            18,
            20,
            28,
            30
    };

    // Neural Network Parameters
    static int inputSize = 2;
    static int hiddenSize = 4;
    static int outputSize = 1;

    static double[][] weightsInputHidden =
            new double[inputSize][hiddenSize];

    static double[] weightsHiddenOutput =
            new double[hiddenSize];

    static double learningRate = 0.01;

    // Initialize Weights
    static void initializeWeights() {

        Random rand = new Random();

        for (int i = 0; i < inputSize; i++) {

            for (int j = 0; j < hiddenSize; j++) {

                weightsInputHidden[i][j] =
                        rand.nextDouble();
            }
        }

        for (int i = 0; i < hiddenSize; i++) {

            weightsHiddenOutput[i] =
                    rand.nextDouble();
        }
    }

    // Sigmoid Activation
    static double sigmoid(double x) {

        return 1.0 /
                (1.0 + Math.exp(-x));
    }

    // Training
    static void train(int epochs) {

        for (int epoch = 0; epoch < epochs; epoch++) {

            double totalError = 0;

            for (int sample = 0;
                 sample < X.length;
                 sample++) {

                // Forward Propagation

                double[] hidden =
                        new double[hiddenSize];

                for (int j = 0;
                     j < hiddenSize;
                     j++) {

                    double sum = 0;

                    for (int i = 0;
                         i < inputSize;
                         i++) {

                        sum += X[sample][i]
                                * weightsInputHidden[i][j];
                    }

                    hidden[j] = sigmoid(sum);
                }

                double output = 0;

                for (int j = 0;
                     j < hiddenSize;
                     j++) {

                    output += hidden[j]
                            * weightsHiddenOutput[j];
                }

                // Error
                double error =
                        Y[sample] - output;

                totalError += error * error;

                // Backpropagation

                for (int j = 0;
                     j < hiddenSize;
                     j++) {

                    weightsHiddenOutput[j] +=
                            learningRate
                            * error
                            * hidden[j];
                }

                for (int i = 0;
                     i < inputSize;
                     i++) {

                    for (int j = 0;
                         j < hiddenSize;
                         j++) {

                        weightsInputHidden[i][j] +=
                                learningRate
                                * error
                                * hidden[j]
                                * (1 - hidden[j])
                                * X[sample][i];
                    }
                }
            }

            if (epoch % 100 == 0) {

                System.out.println(
                        "Epoch "
                        + epoch
                        + " Error: "
                        + totalError);
            }
        }
    }

    // Prediction
    static double predict(double[] input) {

        double[] hidden =
                new double[hiddenSize];

        for (int j = 0;
             j < hiddenSize;
             j++) {

            double sum = 0;

            for (int i = 0;
                 i < inputSize;
                 i++) {

                sum += input[i]
                        * weightsInputHidden[i][j];
            }

            hidden[j] = sigmoid(sum);
        }

        double output = 0;

        for (int j = 0;
             j < hiddenSize;
             j++) {

            output += hidden[j]
                    * weightsHiddenOutput[j];
        }

        return output;
    }

    // Main
    public static void main(String[] args) {

        initializeWeights();

        train(1000);

        double[] testInput = {0.45, 0.55};

        double prediction =
                predict(testInput);

        System.out.println(
                "\nPredicted House Price: "
                + prediction);
    }
}
