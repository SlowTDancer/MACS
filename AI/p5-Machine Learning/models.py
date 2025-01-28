import nn

class PerceptronModel(object):
    def __init__(self, dimensions):
        """
        Initialize a new Perceptron instance.

        A perceptron classifies data points as either belonging to a particular
        class (+1) or not (-1). `dimensions` is the dimensionality of the data.
        For example, dimensions=2 would mean that the perceptron must classify
        2D points.
        """
        self.w = nn.Parameter(1, dimensions)

    def get_weights(self):
        """
        Return a Parameter instance with the current weights of the perceptron.
        """
        return self.w

    def run(self, x):
        """
        Calculates the score assigned by the perceptron to a data point x.

        Inputs:
            x: a node with shape (1 x dimensions)
        Returns: a node containing a single number (the score)
        """
        return nn.DotProduct(self.get_weights(), x)

    def get_prediction(self, x):
        """
        Calculates the predicted class for a single data point `x`.

        Returns: 1 or -1
        """
        return 1.0 if nn.as_scalar(self.run(x)) >= 0 else -1.0

    def train(self, dataset):
        """
        Train the perceptron until convergence.
        """
        done = False
        while not done:
            done = True
            for x, y in dataset.iterate_once(1):
                predicted = self.get_prediction(x)
                y = nn.as_scalar(y)
                if predicted != y:
                    done = False
                    self.w.update(x, y)

class RegressionModel(object):
    """
    A neural network model for approximating a function that maps from real
    numbers to real numbers. The network should be sufficiently large to be able
    to approximate sin(x) on the interval [-2pi, 2pi] to reasonable precision.
    """
    def __init__(self):
        # Initialize your model parameters here
        self.lr = -0.08
        self.layer_sizes = [10, 15, 1]
        self.layer_num = len(self.layer_sizes)
        self.network_weights = []
        self.network_biases = []

        for i in range(self.layer_num):
            fn = 1 if i == 0 else self.layer_sizes[i - 1]
            layer_size = self.layer_sizes[i]
            layer_weights = nn.Parameter(fn, layer_size)
            layer_biases = nn.Parameter(1, layer_size)
            self.network_weights.append(layer_weights)
            self.network_biases.append(layer_biases)

    def run(self, x):
        """
        Runs the model for a batch of examples.

        Inputs:
            x: a node with shape (batch_size x 1)
        Returns:
            A node with shape (batch_size x 1) containing predicted y-values
        """
        predictions, i = None, 0

        while i < self.layer_num:
            xw = nn.Linear(x,  self.network_weights[i])
            predictions = nn.AddBias(xw, self.network_biases[i])
            x = nn.ReLU(predictions)
            i += 1

        return predictions

    def get_loss(self, x, y):
        """
        Computes the loss for a batch of examples.

        Inputs:
            x: a node with shape (batch_size x 1)
            y: a node with shape (batch_size x 1), containing the true y-values
                to be used for training
        Returns: a loss node
        """
        return nn.SquareLoss(self.run(x), y)

    def train(self, dataset):
        """
        Trains the model.
        """
        loss = 0.03
        while loss > 0.02:
            for x, y in dataset.iterate_once(20):
                curr = self.get_loss(x, y)
                parameters = []
                    
                for i in range(self.layer_num):
                    parameters.append(self.network_weights[i])
                    parameters.append(self.network_biases[i])

                grad = nn.gradients(curr, parameters)

                for i in range(self.layer_num):
                    self.network_weights[i].update(grad[2 * i], self.lr)
                    self.network_biases[i].update(grad[2 * i + 1], self.lr)

            loss = nn.as_scalar(self.get_loss(nn.Constant(dataset.x), nn.Constant(dataset.y)))

class DigitClassificationModel(object):
    """
    A model for handwritten digit classification using the MNIST dataset.

    Each handwritten digit is a 28x28 pixel grayscale image, which is flattened
    into a 784-dimensional vector for the purposes of this model. Each entry in
    the vector is a floating point number between 0 and 1.

    The goal is to sort each digit into one of 10 classes (number 0 through 9).

    (See RegressionModel for more information about the APIs of different
    methods here. We recommend that you implement the RegressionModel before
    working on this part of the project.)
    """
    def __init__(self):
        # Initialize your model parameters here
        self.lr = -0.525
        self.layer_sizes = [220, 150, 10]

        self.ln = 3
        self.network_weights = []
        self.network_biases = []

        for i in range(self.ln):
            fn = 784 if i == 0 else self.layer_sizes[i - 1]
            layer_size = self.layer_sizes[i]
            layer_weights = nn.Parameter(fn, layer_size)
            layer_biases = nn.Parameter(1, layer_size)
            self.network_weights.append(layer_weights)
            self.network_biases.append(layer_biases)

    def run(self, x):
        """
        Runs the model for a batch of examples.

        Your model should predict a node with shape (batch_size x 10),
        containing scores. Higher scores correspond to greater probability of
        the image belonging to a particular class.

        Inputs:
            x: a node with shape (batch_size x 784)
        Output:
            A node with shape (batch_size x 10) containing predicted scores
                (also called logits)
        """
        predictions, i = None, 0

        while i < self.ln:
            xw = nn.Linear(x,  self.network_weights[i])
            predictions = nn.AddBias(xw, self.network_biases[i])
            x = nn.ReLU(predictions)
            i += 1

        return predictions


    def get_loss(self, x, y):
        """
        Computes the loss for a batch of examples.

        The correct labels `y` are represented as a node with shape
        (batch_size x 10). Each row is a one-hot vector encoding the correct
        digit class (0-9).

        Inputs:
            x: a node with shape (batch_size x 784)
            y: a node with shape (batch_size x 10)
        Returns: a loss node
        """
        return nn.SquareLoss(self.run(x), y)

    def train(self, dataset):
        """
        Trains the model.
        """
        va = 0
        while va < 975 * (10 ** -3):
            for x, y in dataset.iterate_once(200):
                curr_loss = self.get_loss(x, y)
                parameters = []

                for i in range(self.ln):
                    parameters.append(self.network_weights[i])
                    parameters.append(self.network_biases[i])

                grad = nn.gradients(curr_loss, parameters)

                for i in range(self.ln):
                    self.network_weights[i].update(grad[2 * i], self.lr)
                    self.network_biases[i].update(grad[2 * i + 1], self.lr)

            va = dataset.get_validation_accuracy()

class LanguageIDModel(object):
    """
    A model for language identification at a single-word granularity.

    (See RegressionModel for more information about the APIs of different
    methods here. We recommend that you implement the RegressionModel before
    working on this part of the project.)
    """

    def __init__(self):
        # Our dataset contains words from five different languages, and the
        # combined alphabets of the five languages contain a total of 47 unique
        # characters.
        # You can refer to self.num_chars or len(self.languages) in your code
        self.num_chars = 47
        self.languages = ["English", "Spanish", "Finnish", "Dutch", "Polish"]

        # Initialize your model parameters here
        self.lr = -1 * 5 * (10 ** -2)
        self.layer_sizes = [250, 250, len(self.languages)]
        self.layer_num = len(self.layer_sizes)
        self.network_weights = []
        self.network_biases = []
        
        for i in range(self.layer_num):
            fn = self.num_chars if i == 0 else self.layer_sizes[i - 1]
            layer_size = self.layer_sizes[i]
            layer_weights = nn.Parameter(fn, layer_size)
            layer_biases = nn.Parameter(1, layer_size)
            self.network_weights.append(layer_weights)
            self.network_biases.append(layer_biases)

    def getInitialPrediction(self, x):
        return nn.AddBias(nn.Linear(x, self.network_weights[0]),  self.network_biases[0])

    def run(self, xs):
        """
        Runs the model for a batch of examples.

        Although words have different lengths, our data processing guarantees
        that within a single batch, all words will be of the same length (L).

        Here `xs` will be a list of length L. Each element of `xs` will be a
        node with shape (batch_size x self.num_chars), where every row in the
        array is a one-hot vector encoding of a character. For example, if we
        have a batch of 8 three-letter words where the last word is "cat", then
        xs[1] will be a node that contains a 1 at position (7, 0). Here the
        index 7 reflects the fact that "cat" is the last word in the batch, and
        the index 0 reflects the fact that the letter "a" is the inital (0th)
        letter of our combined alphabet for this task.

        Your model should use a Recurrent Neural Network to summarize the list
        `xs` into a single node of shape (batch_size x hidden_size), for your
        choice of hidden_size. It should then calculate a node of shape
        (batch_size x 5) containing scores, where higher scores correspond to
        greater probability of the word originating from a particular language.

        Inputs:
            xs: a list with L elements (one per character), where each element
                is a node with shape (batch_size x self.num_chars)
        Returns:
            A node with shape (batch_size x 5) containing predicted scores
                (also called logits)
        """
        lp = nn.ReLU(self.getInitialPrediction(xs[0]))

        for x in xs[1:]:
            ip = self.getInitialPrediction(x)
            lp = nn.ReLU(nn.Add(ip, nn.Linear(lp, self.network_weights[1])))

        fp = nn.Linear(lp, self.network_weights[2])
        return fp

    def get_loss(self, xs, y):
        """
        Computes the loss for a batch of examples.

        The correct labels `y` are represented as a node with shape
        (batch_size x 5). Each row is a one-hot vector encoding the correct
        language.

        Inputs:
            xs: a list with L elements (one per character), where each element
                is a node with shape (batch_size x self.num_chars)
            y: a node with shape (batch_size x 5)
        Returns: a loss node
        """
        return nn.SoftmaxLoss(self.run(xs), y)

    def train(self, dataset):
        """
        Trains the model.
        """
        va = 0
        while va < 87 * (10 ** -2):
            for x, y in dataset.iterate_once(30):
                curr_loss = self.get_loss(x, y)
                parameters = []

                for i in range(self.layer_num):
                    parameters.append(self.network_weights[i])

                grad = nn.gradients(curr_loss, parameters)

                for i in range(self.layer_num):
                    self.network_weights[i].update(grad[i], self.lr)

            va= dataset.get_validation_accuracy()
