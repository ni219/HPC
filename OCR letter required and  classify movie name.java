import java.util.Random;

public class IMDBBinaryClassification {

    // Sample Features
    // 1 = positive words count
    // 2 = negative words count

    static double[][] X = {
            {8, 1},
            {7, 2},
            {1, 8},
            {2, 7},
            {9, 1},
            {1, 9}
    };

    // Labels
    // 1 = Positive
    // 0 = Negative

    static double[] Y = {
            1,
            1,
            0,
            0,
            1,
            0
    };

    // Neural Network Parameters
    static int inputSize = 2;
    static int hiddenSize = 4;

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

    // Sigmoid Function
    static double sigmoid(double x) {

        return 1.0 /
                (1.0 + Math.exp(-x));
    }

    // Train Model
    static void train(int epochs) {

        for (int epoch = 0;
             epoch < epochs;
             epoch++) {

            double totalError = 0;

            for (int sample = 0;
                 sample < X.length;
                 sample++) {

                // Hidden Layer
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

                // Output Layer
                double output = 0;

                for (int j = 0;
                     j < hiddenSize;
                     j++) {

                    output += hidden[j]
                            * weightsHiddenOutput[j];
                }

                output = sigmoid(output);

                // Error
                double error =
                        Y[sample] - output;

                totalError += error * error;

                // Update Output Weights
                for (int j = 0;
                     j < hiddenSize;
                     j++) {

                    weightsHiddenOutput[j] +=
                            learningRate
                            * error
                            * hidden[j];
                }

                // Update Hidden Weights
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

        return sigmoid(output);
    }

    // Main Method
    public static void main(String[] args) {

        initializeWeights();

        train(1000);

        // Test Review
        double[] review = {8, 1};

        double prediction =
                predict(review);

        System.out.println(
                "\nPrediction Value: "
                + prediction);

        if (prediction >= 0.5) {

            System.out.println(
                    "Positive Review");
        } else {

            System.out.println(
                    "Negative Review");
        }
    }
}
