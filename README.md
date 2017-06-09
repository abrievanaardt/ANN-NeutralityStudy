#ANN-NeutralityStudy#
##Characterising Neutrality in Neural Network Error Landscapes##

Neural network error landscapes are known for their long plateaus that make population-based training moderately ineffective. The aim of the project is to identify/propose a good measure of neutrality, or flatness in NNs, and to find a correlation between training algorithm performance and the level of neutrality present.

- The above has to be performed on datasets for which the optimal NN configuration is known.
- As activation function, stick to sigmoid.
- Only quality of solution is considered to measure the performance of the training algorithm

For now the study excludes the following:
- Factors contributing to neutrality (activation functions, # layers, overall network dimensionality).
