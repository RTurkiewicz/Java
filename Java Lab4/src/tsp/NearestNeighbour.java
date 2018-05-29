package tsp;

import java.util.*;

import javax.swing.JTextArea;

public class NearestNeighbour {

    private int [][] costMatrix;
    private int dimension;
    private boolean [] visited;
    private JTextArea textArea;
    private List<Integer> bestPath;

    public NearestNeighbour() {
	}
    
    public void NearestNeighbourExec(int [][] costMatrix, int dimension, JTextArea textArea, boolean oneRun) {
    		
            this.costMatrix = costMatrix;
            this.dimension = dimension;
            this.visited = new boolean[dimension];
            this.textArea = textArea;
            
        if (oneRun) {
            List<Integer> bestPath = execute();
            textArea.append("Path: \n");
            textArea.append(bestPath.get(0) + "");
            for (int i = 1; i < bestPath.size(); i++)
            	textArea.append(" -> " + bestPath.get(i));
            textArea.append("\nKoszt: " + calculateCost(bestPath) + "\n\n");
    	}
    }
    
    public int justExecute(int [][] costMatrix, int dimension) {

        this.costMatrix = costMatrix;
        this.dimension = dimension;
        this.visited = new boolean[dimension];
        bestPath = execute();
        return calculateCost(bestPath);
    }
    
    public void justPrint() {
    	
        textArea.append("Path: \n");
        textArea.append(bestPath.get(0) + "");
        for (int i = 1; i < bestPath.size(); i++)
        	textArea.append(" -> " + bestPath.get(i));
        textArea.append("\nKoszt: " + calculateCost(bestPath) + "\n\n");
    }

    public List<Integer> execute() {

        Arrays.fill(visited, false);
        List<Integer> bestPath = new ArrayList<>();
        bestPath.add(0);
        visited[0] = true;
        int currentCity = 0;
        for(int i = 0; i < dimension; i++) {

            if(i == dimension - 1) {
                bestPath.add(0);
            } else {
                currentCity = findNearestNeighbour(currentCity);
                visited[currentCity] = true;
                bestPath.add(currentCity);
            }
        }
        return bestPath;
    }

    private int findNearestNeighbour(int index) {

        int minDistance = Integer.MAX_VALUE;
        int minIndex = 0;
        for(int i = 0; i < dimension; i++) {

            if(!visited[i] && i != index && costMatrix[index][i] < minDistance) {
                minDistance = costMatrix[index][i];
                minIndex = i;
            }
        }
        return minIndex;
    }
    
    private int calculateCost(List<Integer> route) {

        int cost = 0;
        for(int i = 0; i < dimension - 1; i++) {

            cost += costMatrix[route.get(i)][route.get(i + 1)];
        }

        cost += costMatrix[route.get(dimension - 1)][route.get(0)];
        return cost;
    }
}
