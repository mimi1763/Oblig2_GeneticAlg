package com.msg.oblig2.csp;

import java.util.ArrayList;
import java.util.Arrays;

import com.msg.oblig2.interfaces.Params;

public class Graph implements Comparable<Graph> {
	
	private Node[] nodes;
	private int fitness;
	private ArrayList<Integer>[] adjacencyList;
	private ArrayList<Edge> edges;
	private int numberOfEdges;
	
	@SuppressWarnings("unchecked")
	public Graph(int size) {
		adjacencyList = (ArrayList<Integer>[])new ArrayList[size];
		for (int i = 0; i < size; i++)
			adjacencyList[i] = new ArrayList<Integer>();
		edges = new ArrayList<Edge>();
		numberOfEdges = 0;
		nodes = new Node[size];
		fitness = Integer.MAX_VALUE;
	}
	
	/* Clone Constructor */
	@SuppressWarnings("unchecked")
	public Graph(Graph clone) {
		this.nodes = new Node[clone.nodes.length];
		System.arraycopy(clone.nodes, 0, this.nodes, 0, clone.nodes.length);
		adjacencyList = (ArrayList<Integer>[])new ArrayList[clone.adjacencyList.length];
		for (int i = 0; i < adjacencyList.length; i++) {
			adjacencyList[i] = new ArrayList<Integer>();
			for (int e : clone.adjacencyList[i])
				adjacencyList[i].add(e);
		}
		edges = new ArrayList<Edge>();
		for (Edge e : clone.edges)
			edges.add(e);
		this.fitness = clone.fitness;
		this.numberOfEdges = clone.numberOfEdges;
	}
	
	/**
	 * Retrieves node at given index.
	 * 
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
			edges.add(new Edge(node1, node2));
			adjacencyList[node1].add(node2);
			adjacencyList[node2].add(node1);
//			adjacencyList[node1].add(numberOfEdges + 1);
//			adjacencyList[node2].add(numberOfEdges + 1);			
			numberOfEdges++;
			return true;
		}
		return false;
	}
	
	public ArrayList<Edge> getEdges() {
		return edges;
	}
	
	public int[] getNodeEdges(int node) {
		int[] edgeArray = new int[adjacencyList[node].size()];
		for (int i = 0; i < edgeArray.length; i++)
			edgeArray[i] = adjacencyList[node].get(i);
		return edgeArray;
	}
	
//	public Edge[] getNodeEdges(int node) {
//		Edge[] edgeArray = new Edge[adjacencyList[node].size()];
//		for (int i = 0; i < edgeArray.length; i++)
//			edgeArray[i] = edges[adjacencyList[node].get(i)];
//		return edgeArray;
//	}
	
	public int getNodeEdge(int node, int index) {
		int edge = -1;
		if(adjacencyList[node].size() > index)
			edge = adjacencyList[node].get(index);
		return edge;
	}
	
//	public Edge getEdge(int node, int index) {
//		Edge edge = null;
//		if(adjacencyList[node].size() > index)
//			edge = edges[adjacencyList[node].get(index)];
//		return edge;
//	}

	public ArrayList<Integer> getAdjacencyList(int node) {
		return adjacencyList[node];
	}

	public int getFitness() {
		return fitness;
	}
	
	/**
	 * Returns number of nodes in the graph.
	 * 
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
	 * Recalculate Graph fitness.
	 */
	public void recalculateFitness() {
		int totalFitness = 0;
		for (ArrayList<Integer> edgeList : adjacencyList)
			if(edgeList != null)
				for (int e : edgeList)
				totalFitness += (e.isSimilar(this)) ? 1 : 0;
		this.fitness = totalFitness;
	}
	
//	public void recalculateFitness() {
//		int totalFitness = 0;
//		for (Edge e : edges)
//			if(e != null)
//				totalFitness += (e.isSimilar(this)) ? 1 : 0;
//		this.fitness = totalFitness;
//	}	

	public void shuffleColours() {
		for (Node node : nodes)
			node.randomColour();
	}
	
	/**
	 * Return number of neighbours with similar colour of given node.
	 *
	 * @param node
	 * @return count
	 */
	public int getSimilarCount(int node) {
		int count = 0;
		for (int edgeId : adjacencyList[node])
			count += (edges[edgeId].isSimilar(this)) ? 1 : 0;
		return count;
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
