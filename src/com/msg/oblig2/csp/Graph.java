package com.msg.oblig2.csp;

import java.util.Arrays;

import com.msg.oblig2.interfaces.Params;

public class Graph implements Comparable<Graph> {
	
	private Node[] nodes;
	private int fitness;
	private boolean[][] adjMatrix;
	private int numberOfEdges;
	private final int MAX_EDGES;
	
	public Graph(int size) {
		MAX_EDGES = (size * (size - 1)) / 2; // max edges (complete graph) = n(n-1)/2
		nodes = new Node[size];
		adjMatrix = new boolean[size][size];
		fitness = Integer.MAX_VALUE;
		numberOfEdges = 0;
	}
	
	/* Clone Constructor */
	public Graph(Graph clone) {
		this.MAX_EDGES = clone.MAX_EDGES;
		this.nodes = new Node[clone.nodes.length];
		System.arraycopy(clone.nodes, 0, this.nodes, 0, clone.nodes.length);
		adjMatrix = new boolean[clone.adjMatrix.length][clone.adjMatrix.length];
		for (int i = 0; i < clone.adjMatrix.length; i++)
			System.arraycopy(clone.adjMatrix[i], 0, this.adjMatrix[i], 0, clone.adjMatrix.length);
		this.fitness = clone.fitness;
		this.numberOfEdges = clone.numberOfEdges;
	}
	
	/**
	 * Retrieves node at given index.
	 * @param index
	 * @return node
	 */
	public Node getNode(int index) {
		return nodes[index];
	}
	
	/**
	 * Replaces node at given index with given node.
	 * 
	 * @param index
	 * @param node
	 */
	public void setNode(int index, Node node) {
		nodes[index] = node;
	}

	public Node[] getNodes() {
		return nodes;
	}

	public void setNodes(Node[] nodes) {
		System.arraycopy(nodes, 0, this.nodes, 0, nodes.length);
	}
	
	public boolean addEdge(int node1, int node2) {
		if(node1 != node2) {
			adjMatrix[node1][node2] = true;
			adjMatrix[node2][node1] = true;			
			numberOfEdges++;
			return true;
		}
		return false;
	}

	public int[] getEdge(int index) {
		int[] edge = null;
		int count = 0;
		top: for (int y = 0; y < (nodes.length-1); y++)
			for (int x = y+1; x < (nodes.length); x++) {
				if(count == index) {
					edge = new int[2];
					edge[0] = x;
					edge[1] = y;
					break top;
				}
				count++;
			}
		return edge;
	}
	
	public boolean hasEdge(int node1, int node2) {
		return adjMatrix[node1][node2];
	}

	public boolean[][] getAdjMatrix() {
		return adjMatrix;
	}

	public int getFitness() {
		return fitness;
	}
	
	/**
	 * Returns number of nodes in the graph.
	 * @return size
	 */
	public int getSize() {
		return nodes.length;
	}
	
	/**
	 * Returns number of edges in the graph.
	 * 
	 * @return edgeSize
	 */
	public int getEdgeSize() {
		return numberOfEdges;
	}

	/**
	 * Returns maximum number of edges, indicating a complete graph.
	 * @return MAX_EDGES
	 */
	public int getMaxEdges() {
		return MAX_EDGES;
	}

	/**
	 * Recalculate Graph fitness.
	 */	
	public void recalculateFitness() {
		int totalFitness = 0;
		for (int y = 0; y < (nodes.length-1); y++)
			for (int x = y+1; x < (nodes.length); x++)
				totalFitness += (adjMatrix[y][x]) ? 1 : 0;
		this.fitness = totalFitness;
	}	

	public void shuffleColours() {
		for (Node node : nodes)
			node.randomColour();
	}
	
	public static String getColourChar(Node node) {
		int index = Arrays.asList(Params.COLOURS).indexOf(node.getColour());
		switch (index) {
			case 0:	return "  cW";
			case 1:	return "  cB";
			case 2:	return "  cR";
		}
		return " ";
	}
	
	@Override
	public int compareTo(Graph g) {
        if(this.fitness > g.getFitness()) return 1;
        if(this.fitness < g.getFitness()) return -1;
        return 0;
	}
}
