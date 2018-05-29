package tsp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.JTextArea;

public class Genetic {

	private int dimension;
    private int[][] costMatrix;
    private JTextArea textArea;
    private int startingPopulation = 200;
    private int generations = 30;
    private int crossovers = 10;
    private int mutations = 5;
    private List<Path> population;
    private List<Double> probabilities;
    private List<Double> cumulativeDistribuation;
    private Path bestPath;
    private int random1;
    private int random2;
    
    
    
	public Genetic() {
	}

	public void GeneticExec(int[][] costMatrix, int dimension, JTextArea textArea, boolean oneRun){
		
		this.dimension = dimension;
		this.costMatrix = costMatrix;
		this.textArea = textArea;
		
		if (oneRun) {
			
			population = generatePopulation();
	        bestPath = generateRandomPath();//new Path(findBestPath().getRoute(), dimension, costMatrix);

	        probabilities = calculateProbabilities();
	        cumulativeDistribuation = calculateCumulativeDistribuation();
	        
	        textArea.append("START: \nKoszt:" + bestPath.getCost() + "\n");
	        
	        for (int i = 0; i < generations; i++) {

	            executeNextGeneration();

	            Path newBestPath = findBestPath();
	            if(newBestPath.getCost() < bestPath.getCost()) {
	                bestPath = new Path(newBestPath.getRoute(), dimension, costMatrix);
	                textArea.append("ITERACJA: " + i + "\nKoszt: " + bestPath.getCost() + "\n");
	            }
	        }
	        
	        textArea.append("\n\n");
		}
	}
	
	private StringBuilder executionMessage = new StringBuilder();
	
    public int justExecute(int [][] costMatrix, int dimension) {

    	population = generatePopulation();
        bestPath = generateRandomPath();//new Path(findBestPath().getRoute(), dimension, costMatrix);

        probabilities = calculateProbabilities();
        cumulativeDistribuation = calculateCumulativeDistribuation();
        
        executionMessage.append("START: \nKoszt:" + bestPath.getCost() + "\n");
        
        for (int i = 0; i < generations; i++) {

            executeNextGeneration();

            Path newBestPath = findBestPath();
            if(newBestPath.getCost() < bestPath.getCost()) {
                bestPath = new Path(newBestPath.getRoute(), dimension, costMatrix);
                executionMessage.append("ITERACJA: " + i + "\nKoszt: " + bestPath.getCost() + "\n");
            }
        }
        
        executionMessage.append("\n\n");
        return bestPath.getCost();
    }
    
    public void justPrint() {
    	
        textArea.append(executionMessage.toString());
    }
	
    private List<Path> generatePopulation() {

        List<Path> population = new ArrayList<>();
        for (int i = 0; i < startingPopulation; i++)
            population.add(generateRandomPath());


        //NearestNeighbourTSP nearestNeighbourTSP = new NearestNeighbourTSP(DataLoader.getCostMatrix(), DataLoader.getDimension());

        /*for(int i = 0; i < population.size() / 2; i++) {

            int index = getRandomNumber(0, population.size() - 1);
            population.set(index, new Path(nearestNeighbourTSP.execute()));
        }*/


        return population;
    }
    
    private Path generateRandomPath() {

        List <Integer> naturalOrderRoute = new ArrayList<>();
        for (int i = 0; i < dimension; i++)
            naturalOrderRoute.add(i);

        List<Integer> route = new ArrayList<>();
        for (int i = 0; i < dimension; i++)
        {
            int randomIndex = getRandomNumber(0, naturalOrderRoute.size() - 1);
            route.add(naturalOrderRoute.get(randomIndex));
            naturalOrderRoute.remove(randomIndex);
        }

        return new Path(route, dimension, costMatrix);
    }
    
    private int getRandomNumber(int min, int max) {

        Random random = new Random();
        return random.ints(min, (max + 1)).findFirst().getAsInt();
    }
    
    private List<Double> calculateProbabilities() {

        double costInversesSum = 0;

        for (Path path : population) {
            costInversesSum += ((double) 1) / path.getCost();
        }

        List<Double> probabilities = new ArrayList<>();

        for (Path path : population) {
            double newProbability = ((double) 1 / path.getCost()) / costInversesSum;
            probabilities.add(newProbability);
        }

        return probabilities;
    }

    private List<Double> calculateCumulativeDistribuation() {

        List<Double> cumulativeDistribuation = new ArrayList<>();
        cumulativeDistribuation.add(probabilities.get(0));

        for (int i = 1; i < probabilities.size(); i++)
            cumulativeDistribuation.add(probabilities.get(i) + cumulativeDistribuation.get(i - 1));

        return cumulativeDistribuation;
    }
    
    public void executeNextGeneration() {

        generateCrossovers(1000000);
        generateMutations();
        removeWorstSpecimen();

        probabilities = calculateProbabilities();
        cumulativeDistribuation = calculateCumulativeDistribuation();

    }
    
    private void generateCrossovers(final int constant) {

        List<Path> children = new ArrayList<>();

        int parent1 = -1, parent2 = -1;

        for (int i = 0; i < crossovers; i++) {

            random1 = getRandomNumber(0, (int) (probabilities.stream().mapToDouble(p -> p.doubleValue()).sum() * constant));
            Double foundValue1 = cumulativeDistribuation.stream()
                    .filter(x -> x > (((double) random1) / constant))
                    .findFirst().get();
            int foundIndex1 = cumulativeDistribuation.indexOf(foundValue1);
            parent1 = foundIndex1 != -1 ? foundIndex1 : population.size() - 1;

            do {
                random2 = getRandomNumber(0, (int) (probabilities.stream().mapToDouble(p -> p.doubleValue()).sum() * constant));
                Double foundValue2 = cumulativeDistribuation.stream()
                        .filter(x -> x > (((double) random2) / constant))
                        .findFirst().get();
                int foundIndex2 = cumulativeDistribuation.indexOf(foundValue2);
                parent2 = foundIndex2 != -1 ? foundIndex2 : population.size() - 1;
            }
            while (parent2 == parent1);

            children.addAll(generateChildren(population.get(parent1), population.get(parent2)));
        }

        population.addAll(children);
    }

    private List<Path> generateChildren(Path parent1, Path parent2) {

        List<Integer> firstParentIndexes = new ArrayList<>();
        List<Integer> secondParentIndexes = new ArrayList<>();

        while (firstParentIndexes.size() < dimension / 2) {

            int index;
            do {
                index = getRandomNumber(0, dimension - 1);
            } while(firstParentIndexes.contains(index));

            firstParentIndexes.add(index);
        }


        while (secondParentIndexes.size() < (dimension % 2 == 0 ?
                dimension / 2 : dimension / 2 + 1)) {

            int randomNumber;
            List<Integer> notTakenVertices;

            do {
                randomNumber = getRandomNumber(0, dimension - 1);

                //Lista wierzcholkow ktorych indeksy wylosowano do firstParentIndexes
                List<Integer> verticesFromFirstParentIndexes = parent1.getRoute()
                        .stream()
                        .filter(x -> firstParentIndexes.contains(parent1.getRoute().indexOf(x)))
                        .collect(Collectors.toList());

                //Lista wierzcholkow z parent2, ktore nie byly jeszcze wziete
                notTakenVertices = parent2.getRoute()
                        .stream()
                        .filter(x -> !verticesFromFirstParentIndexes.contains(x))
                        .collect(Collectors.toList());

                //Sprawdzenie, czy wylosowany nr znajduje sie w tych ktore nie byly jeszcze wziete
                //Jesli tak, to dodanie wylosowanego indeksu do secondParentIndexes

            } while(!notTakenVertices.contains(parent2.getRoute().get(randomNumber)) || secondParentIndexes.contains(randomNumber));

            secondParentIndexes.add(randomNumber);
        }


        //Wierzcholki, ktore w parent1 znajduja sie pod indeksami wylosowanymi w firstParentIndexes
        List<Integer> firstParentGenes = parent1.getRoute()
                .stream()
                .filter(x -> firstParentIndexes.contains(parent1.getRoute().indexOf(x)))
                .collect(Collectors.toList());

        List<Integer> secondParentGenes = parent2.getRoute()
                .stream()
                .filter(x -> secondParentIndexes.contains(parent2.getRoute().indexOf(x)))
                .collect(Collectors.toList());

        List<Path> childrenToBeReturned = new ArrayList<>();
        childrenToBeReturned.add(getChild(firstParentIndexes, firstParentGenes, secondParentGenes));
        childrenToBeReturned.add(getChild(secondParentIndexes, secondParentGenes, firstParentGenes));

        return childrenToBeReturned;
    }

    private Path getChild(List<Integer> mainIndexes, List<Integer> mainGenes, List<Integer> complementaryGenes) {

        List<Integer> indexes = new ArrayList<>(mainIndexes);
        List<Integer> route = new ArrayList<>();
        int complementaryIterator = 0;

        for (int i = 0; i < dimension; i++)
            if (mainIndexes.contains(i))
                route.add(mainGenes.get(indexes.indexOf(i)));
            else {
                route.add(complementaryGenes.get(complementaryIterator++));
            }


        return new Path(route, dimension, costMatrix);
    }

    public void generateMutations() {

        for (int i = 0; i < mutations; i++) {

            int mutatedPathIndex = getRandomNumber(0, population.size() - 1);
            int mutatedGen1 = getRandomNumber(1, dimension - 1);
            int mutatedGen2 = getRandomNumber(1, dimension - 1);
            int mutatedGen3 = getRandomNumber(1, dimension - 1);

            if(mutatedGen1 != mutatedGen3)
                population.get(mutatedPathIndex).swap(costMatrix, mutatedGen1, mutatedGen3);
            if(mutatedGen2 != mutatedGen3)
                population.get(mutatedPathIndex).swap(costMatrix, mutatedGen2, mutatedGen3);
        }
    }

    public void removeWorstSpecimen() {

        population = population
                .stream()
                .sorted(Comparator.comparingInt(Path::getCost))
                .collect(Collectors.toList());

        int index = population.size() - 1;
        while (population.size() > startingPopulation) {
            population.remove(population.get(index--));
        }
    }
    
    private Path findBestPath() {

        return population
                .stream()
                .min(Comparator.comparingInt(Path::getCost))
                .get();
    }
}

class Path {

    private List<Integer> route;
    private int cost;
    private int dimension;
    private int[][] costMatrix;

    Path(List<Integer> route, int dimension, int[][] costMatrix) {

        this.route = route;
        this.dimension = dimension;
        this.costMatrix = costMatrix;
        cost = calculateCost(route);
    }

    private int calculateCost(List<Integer> route) {

        int cost = 0;
        for(int i = 0; i < dimension - 1; i++) {

            cost += costMatrix[route.get(i)][route.get(i + 1)];
        }

        cost += costMatrix[route.get(dimension - 1)][route.get(0)];
        return cost;
    }

    public void swap(int[][] matrix, int i, int j)
    {
        int size = route.size();

        if (i + 1 == j)
        {
            cost = cost
                    - matrix[route.get(i)][route.get(j)]
                    - (i - 1 >= 0 ? matrix[route.get(i - 1)][route.get(i)] : matrix[route.get(route.size() - 1)][route.get(i)])
                    - (j + 1 < size ? matrix[route.get(j)][route.get(j + 1)] : matrix[route.get(j)][route.get(0)])
                    + (i - 1 >= 0 ? matrix[route.get(i - 1)][route.get(j)] : matrix[route.get(route.size() - 1)][route.get(j)])
                    + matrix[route.get(j)][route.get(i)]
                    + (j + 1 < size ? matrix[route.get(i)][route.get(j + 1)] : matrix[route.get(i)][route.get(0)]);
        }
        else if (j + 1 == i)
        {
            cost = cost
                    - matrix[route.get(j)][route.get(i)]
                    - (j - 1 >= 0 ? matrix[route.get(j - 1)][route.get(j)] : matrix[route.get(route.size() - 1)][route.get(j)])
                    - (i + 1 < size ? matrix[route.get(i)][route.get(i + 1)] : matrix[route.get(i)][route.get(0)])
                    + (j - 1 >= 0 ? matrix[route.get(j - 1)][route.get(i)] : matrix[route.get(route.size() - 1)][route.get(i)])
                    + matrix[route.get(i)][route.get(j)]
                    + (i + 1 < size ? matrix[route.get(j)][route.get(i + 1)] : matrix[route.get(j)][route.get(0)]);
        }
        else
        {
            cost = cost
                    - (i - 1 >= 0 ? matrix[route.get(i - 1)][route.get(i)] : matrix[route.get(route.size() - 1)][route.get(i)])
                    + (i - 1 >= 0 ? matrix[route.get(i - 1)][route.get(j)] : matrix[route.get(route.size() - 1)][route.get(j)])
                    - (j - 1 >= 0 ? matrix[route.get(j - 1)][route.get(j)] : matrix[route.get(route.size() - 1)][route.get(j)])
                    + (j - 1 >= 0 ? matrix[route.get(j - 1)][route.get(i)] : matrix[route.get(route.size() - 1)][route.get(i)])

                    - (i + 1 < size ? matrix[route.get(i)][route.get(i + 1)] : matrix[route.get(i)][route.get(0)])
                    + (i + 1 < size ? matrix[route.get(j)][route.get(i + 1)] : matrix[route.get(j)][route.get(0)])
                    - (j + 1 < size ? matrix[route.get(j)][route.get(j + 1)] : matrix[route.get(j)][route.get(0)])
                    + (j + 1 < size ? matrix[route.get(i)][route.get(j + 1)] : matrix[route.get(i)][route.get(0)]);
        }

        int tmp = route.get(i);
        route.set(i, route.get(j));
        route.set(j, tmp);
    }

    public List<Integer> getRoute() {
        return route;
    }

    public int getCost() {
        return cost;
    }
}

