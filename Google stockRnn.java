import java.util.Random;

public class GoogleStockRNN {

    // Sample Google Stock Prices
    static double[] stockPrices = {
            100, 102, 101, 105,
            107, 110, 108, 112,
            115, 117
    };

    static int inputSize = 1;
    static int hiddenSize = 5;

    // RNN Weights
    static double[][] Wx =
            new double[inputSize][hiddenSize];

    static double[][] Wh =
            new double[hiddenSize][hiddenSize];

    static double[] Wy =
            new double[hiddenSize];

    static double learningRate = 0.001;

    // Initialize Weights
    static void initializeWeights() {

        Random rand = new Random();

        for (int i = 0; i < inputSize; i++) {

            for (int j = 0; j < hiddenSize; j++) {

                Wx[i][j] =
                        rand.nextDouble();
            }
        }

        for (int i = 0; i < hiddenSize; i++) {

            for (int j = 0; j < hiddenSize; j++) {

                Wh[i][j] =
                        rand.nextDouble();
            }

            Wy[i] =
                    rand.nextDouble();
        }
    }

    // Tanh Activation
    static double tanh(double x) {

        return Math.tanh(x);
    }

    // Train RNN
    static void train(int epochs) {

        for (int epoch = 0;
             epoch < epochs;
             epoch++) {

            double totalError = 0;

            double[] hidden =
                    new double[hiddenSize];

            for (int t = 0;
                 t < stockPrices.length - 1;
                 t++) {

                double input =
                        stockPrices[t];

                double target =
                        stockPrices[t + 1];

                double[] newHidden =
                        new double[hiddenSize];

                // Hidden State
                for (int j = 0;
                     j < hiddenSize;
                     j++) {

                    double sum =
                            input * Wx[0][j];

                    for (int k = 0;
                         k < hiddenSize;
                         k++) {

                        sum += hidden[k]
                                * Wh[k][j];
                    }

                    newHidden[j] =
                            tanh(sum);
                }

                hidden = newHidden;

                // Output
                double output = 0;

                for (int j = 0;
                     j < hiddenSize;
                     j++) {

                    output += hidden[j]
                            * Wy[j];
                }

                // Error
                double error =
                        target - output;

                totalError +=
                        error * error;

                // Update Output Weights
                for (int j = 0;
                     j < hiddenSize;
                     j++) {

                    Wy[j] +=
                            learningRate
                            * error
                            * hidden[j];
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

    // Predict Next Price
    static double predict(double input) {

        double[] hidden =
                new double[hiddenSize];

        for (int j = 0;
             j < hiddenSize;
             j++) {

            hidden[j] =
                    tanh(input * Wx[0][j]);
        }

        double output = 0;

        for (int j = 0;
             j < hiddenSize;
             j++) {

            output += hidden[j]
                    * Wy[j];
        }

        return output;
    }

    // Main Method
    public static void main(String[] args) {

        initializeWeights();

        train(1000);

        double todayPrice = 117;

        double predictedPrice =
                predict(todayPrice);

        System.out.println(
                "\nToday's Price: "
                + todayPrice);

        System.out.println(
                "Predicted Next Price: "
                + predictedPrice);
    }
}
