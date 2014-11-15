package com.msg.oblig2.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import com.msg.oblig2.csp.Graph;
import com.msg.oblig2.csp.Node;

public class RandomAlgorithm extends Algorithm<Graph> {
		
	public RandomAlgorithm() {
		// empty constructor
	}
	
	/**
	 * Random construction algorithm. Generates a Graph
	 * of maxIterations number of nodes with random
	 * number of edges between MIN_EDGES and MAX_EDGES 
	 * between each node.
	 * 
	 * @param Graph
	 * @return new Graph
	 */
	@Override
	public Graph process(Graph inputGraph) {
		Random random = new Random(System.nanoTime());
		Graph graph = new Graph(maxIterations);
		
		/* Create a list of all nodes in graph. */
		ArrayList<Integer> shuffledNodes = new ArrayList<Integer>();
		for (int n = 0; n < graph.getSize(); n++)
			shuffledNodes.add(n);

		boolean[] visited = new boolean[graph.getSize()];
		
		/* Create node 0 and set random available edge slots. */
		graph.setNode(0, new Node(getRandomEdgeCount()));
		visited[0] = true;
		/* Add this node's available edge slots to total emptySlots. */
		int emptySlots = graph.getNode(0).getEdgeSlots();
		
		/* Phase I. Start from node 1, not 0. */	
		for (int n = 1; n < graph.getSize(); n++) {
			int edge;
			do
				edge = random.nextInt((getTrueCount(visited)+1));
			while(!visited[edge] || edge == n);
			System.out.println("I: edge " + edge);
			visited[edge] = true;	
			/* Create the new node n. */
			graph.setNode(n, new Node(getRandomEdgeCount()));
			/* Create edge from node n to node edge. */
			graph.addEdge(n, edge);
			System.out.println("I: adding edge (" + n + ", " + edge + ")");
			/* Decrement available edge slots on node n. */
			graph.getNode(n).decEdgeSlots();
			/* Add this node's available edge slots to total emptySlots. */
			emptySlots += graph.getNode(n).getEdgeSlots();
		}
		System.out.println("start emptyslots " + emptySlots);
		/* Phase II. Go through nodes until all edge slots are used. */
		while (emptySlots > 0) {
			for (int n = 0; n < graph.getSize(); n++) {
				if(emptySlots > 0 && graph.getNode(n).getEdgeSlots() > 0) {
					System.out.println("node " + n +" emptyslots " + emptySlots);
					/* Shuffle the nodes in the list, giving random order. */
					shuffleList(shuffledNodes);
					/* Add a random edge to node n if it has available slots. */
					int edge = -1;
					for (int e : shuffledNodes)
						if(graph.getNode(e).getEdgeSlots() > 0 &&
								e != n &&
								!graph.getAdjacencyList(n).contains(e))
							edge = e;
						else
							continue; // Skip to next in shuffledNodes.
					if(edge > -1) {
						System.out.println("adding edge (" + n + ", " + edge + ")");
						graph.addEdge(n, edge);
						graph.getNode(n).decEdgeSlots();
						emptySlots--;
					}
				}
			}
		}
		return graph;
	}
	
	/**
	 * Random number of edges between MIN_EDGES and MAX_EDGES. 
	 * @param random
	 * @return edgeCount
	 */
	public int getRandomEdgeCount() {
		Random random = new Random(System.nanoTime());
		int edgeCount = MAX_EDGES;
		if(MIN_EDGES < MAX_EDGES)
			edgeCount = random.nextInt(MAX_EDGES - MIN_EDGES) + MIN_EDGES;
		return edgeCount;
	}
	
	public int getTrueCount(boolean[] array) {
		int count = 0;
		for (boolean b : array)
			if(b) count++;
		return count;
	}

	/* A Fisher–Yates shuffle for lists. */
	public static void shuffleList(List<Integer> list) {
		Random random = new Random(System.nanoTime());
		for (int i = list.size() - 1; i > 0; i--) {
			int index = random.nextInt(i + 1);
			// Simple swap
			int a = list.get(index);
			list.set(index, list.get(i));
			list.set(i, a);
		}
	}
	
	private int validateGraph(Graph graph) {
		DFSAlgorithm dfsAlg = new DFSAlgorithm(graph.getSize());
		dfsAlg.dfs(graph, 0);
		return dfsAlg.getCount();
	}
}
