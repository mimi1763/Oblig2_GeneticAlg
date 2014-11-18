package com.msg.oblig2.csp;

import java.util.Arrays;

import com.msg.oblig2.interfaces.Params;

public class Graph implements Comparable<Graph> {
	
	private Node[] nodes;
	private boolean[][] adjMatrix;
	private int numberOfEdges;
	private final int MAX_EDGES;
	private int fitness;
	
	public Graph(int size) {
		MAX_EDGES = (size * (size - 1)) / 2; // max edges (complete graph) = n(n-1)/2
		fitness = MAX_EDGES;
		nodes = new Node[size];
		adjMatrix = new boolean[size][size];
		numberOfEdges = 0;
	}
	
	/* Clone Constructor using deep copy for objects (non-primitives). */
	public Graph(Graph clone) {
		this.MAX_EDGES = (clone.nodes.length * (clone.nodes.length - 1)) / 2;
		this.nodes = new Node[clone.nodes.length];
		for (int n = 0; n < clone.nodes.length; n++)
			this.nodes[n] = new Node(clone.nodes[n].getColour());
		this.adjMatrix = new boolean[clone.adjMatrix.length][clone.adjMatrix.length];
		for (int i = 0; i < clone.adjMatrix.length; i++)
			for (int j = 0; j < clone.adjMatrix[i].length; j++)
				this.adjMatrix[i][j] = clone.adjMatrix[i][j];
		this.numberOfEdges = clone.numberOfEdges;
		this.fitness = clone.fitness;
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
	
	public void setEdge(int x, int y, boolean state) {
		adjMatrix[x][y] = state;
	}

	public int[] getEdge(int index) {
		int[][] edges = new int[getEdgeSize()][2];
		int e = 0;
		for (int y = 0; y < (nodes.length-1); y++)
			for (int x = y+1; x < (nodes.length); x++) {
				if(hasEdge(x, y)) {
					edges[e][0] = x;
					edges[e][1] = y;
					e++;
				}
			}
		return edges[index];
	}
	
	/**
	 * Returns true if both nodes on each end of edge are similar.
	 * Index indicates the edge number.
	 * @param index
	 * @return boolean
	 */
	public boolean isSimilar(int index) {
		int[] edge = getEdge(index);
		if(edge != null)
			return (nodes[edge[0]].equals(nodes[edge[1]]));
		return false;
	}
	
	public boolean isSimilar(int node1, int node2) {
		if(hasEdge(node1, node2))
			return (nodes[node1].equals(nodes[node2]));
		return false;
	}
	
	public boolean hasEdge(int node1, int node2) {
		return adjMatrix[node1][node2];
	}

	public boolean[][] getAdjMatrix() {
		return adjMatrix;
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
	 * @return edgeSize
	 */
	public int getEdgeSize() {
		return numberOfEdges;
	}
	
	public void setEdgeSize(int edgeSize) {
		this.numberOfEdges = edgeSize;		
	}

	/**
	 * Returns maximum number of edges, indicating a complete graph.
	 * @return MAX_EDGES
	 */
	public int getMaxEdges() {
		return MAX_EDGES;
	}
	
	/**
	 * Returns the total fitness of the graph based on
	 * the last run of calcFitness.
	 * @return fitness
	 */
	public int getFitness() {
		return this.fitness;
	}
	
	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

	/**
	 * Calculates total fitness of the graph.
	 */
	public void calcFitness() {
		int totalFitness = 0;
		for (int y = 0; y < (nodes.length-1); y++)
			for (int x = (y+1); x < nodes.length; x++)
				if(adjMatrix[y][x] && nodes[x].equals(nodes[y]))
					totalFitness++;
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
	
	/**
	 * Prints adjacency matrix. If similar = true, only edges are printed
	 * with 1 indicating similar node colours on each side, otherwise 0.
	 * @param similar
	 */
	public void printMatrix() {
		String line = "\n  ";
		/* Frame with node numbers. */
		for (int x = 0; x < nodes.length; x++)
			line += x + " ";
		System.out.println(line);
		line = " ";
		for (int x = 0; x < nodes.length; x++)
			line += "--";
		System.out.println(line);
		/* 1 indicates similar node colour on each side of edge, 
		 * otherwise 0.
		 */
		String sign;
		for (int y = 0; y < nodes.length; y++) {
			line = y + "|";
			for (int x = 0; x < nodes.length; x++) {
				sign = "- ";
				if(x != y && adjMatrix[y][x])
					if(isSimilar(x, y)) 
						sign = "1 ";
					else
						sign = "0 ";
				line += sign;
			}
			System.out.println(line);
		}
//		calcFitness();
		System.out.println("Final fitness: " + fitness);
	}
	
	/**
	 * Prints the graph nodes and their edges with colours, including
	 * whether the edges are in satisfactory state (both ends different colour).
	 */
	public void printEdges() {
		for (int y = 0; y < (nodes.length-1); y++) {
			System.out.println("Node " + y + " colour: " + nodes[y].getColour());
			for (int x = (y+1); x < nodes.length; x++) {
				if(adjMatrix[y][x]) {
					System.out.print("has edge to Node " + x + " colour: " + nodes[x].getColour());
					if(isSimilar(x, y))
						System.out.println(" = unsatisfied!");
					else
						System.out.println(" = satisfied.");
				}
			}			
		}
	}
	
	@Override
	public int compareTo(Graph g) {
        if(this.getFitness() > g.getFitness()) return 1;
        if(this.getFitness() < g.getFitness()) return -1;
        return 0;
	}
}
