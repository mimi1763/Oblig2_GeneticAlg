package com.msg.oblig2.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
		graph.setNode(0, new Node());
		visited[0] = true;
		
		/* Phase I. Start from node 1, not 0. */	
		for (int n = 1; n < graph.getSize(); n++) {
			int edge;
			do
				edge = random.nextInt((getTrueCount(visited)+1));
			while(!visited[edge] || edge == n);
			System.out.println("I: edge " + edge);
			visited[edge] = true;	
			/* Create the new node n. */
			graph.setNode(n, new Node());
			/* Create edge from node n to node edge. */
			graph.addEdge(n, edge);
			System.out.println("I: adding edge (" + n + ", " + edge + ")");
		}
		
		int emptySlots = random.nextInt(graph.getMaxEdges() - graph.getEdgeSize());
		System.out.println("after I: emptyslots start at " + emptySlots);
		
		/* Phase II. Go through nodes until all edge slots are used. */
		while (emptySlots > 0) {
			for (int n = 0; n < graph.getSize(); n++) {
				if(emptySlots > 0) {
					System.out.println("II: node " + n +" emptyslots " + emptySlots);
					/* Shuffle the nodes in the list, giving random order. */
					shuffleList(shuffledNodes);
					/* Add a random edge to node n if it has available slots. */
					int n2 = -1;
					for (int e : shuffledNodes)
						if(e != n && !graph.hasEdge(n, e))
							n2 = e;
						else
							continue; // Skip to next in shuffledNodes.
					if(n2 > -1) {
						System.out.println("II: adding edge (" + n + ", " + n2 + ")");
						graph.addEdge(n, n2);
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
}
