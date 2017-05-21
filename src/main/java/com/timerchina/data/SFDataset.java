package com.timerchina.data;

import java.util.Arrays;

import com.timerchina.model.DataPoint;
import com.timerchina.similarity.ch4.Distance;

public class SFDataset {
    
    private DataPoint[] data;
    private Distance distance;
    private double[][] adjacencyMatrix;
    
    public SFDataset(DataPoint[] data, Distance distance) {
        this.data = data;
        this.distance = distance;
        this.adjacencyMatrix = calculateAdjacencyMatrix();
    }
    
    public DataPoint[] getData() {
        return data;
    }
    
    // We might need to move Matrix related methods to separate class eventually.
    
    /**
     * Adjacency matrix for all data instances in the dataset.
     * Each element represents distance between corresponding elements.
     * 
     * @return
     */
    private double[][] calculateAdjacencyMatrix() {
        int n = data.length;
        double[][] adjMatrix = new double[n][n];
        
        DataPoint x = null;
        DataPoint y = null;
        
        for(int i = 0; i < n; i++) {
            x = data[i];
            for(int j = i + 1; j < n; j++) {
                y = data[j];
                adjMatrix[i][j] = distance.getDistance(
                        x.getNumericAttrValues(), y.getNumericAttrValues());
                adjMatrix[j][i] = adjMatrix[i][j];
            }
            adjMatrix[i][i] = 0.0;
        }
        
        return adjMatrix;
    }
    
    public double[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }
    
    public void printDistanceMatrix() {
        for(int i = 0, n = adjacencyMatrix.length; i < n; i++) {
            System.out.println(Arrays.toString(adjacencyMatrix[i]));
        }
    }
}
