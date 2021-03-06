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
			int node;
			do
				node = random.nextInt(graph.getSize());
			while(visited[node]);
			visited[node] = true;
			int edge;
			do
				edge = random.nextInt(getTrueCount(visited));
			while(!visited[edge] || edge == node);
			visited[edge] = true;	
			/* Create the new node n. */
			graph.setNode(node, new Node());
			/* Create edge from node n to node edge. */
			graph.addEdge(node, edge);
		}
		
		int emptySlots = 0;
		if(!USE_EDGE_RATIO)
			emptySlots = (COMPLETE_GRAPH) ? graph.getMaxEdges() - graph.getEdgeSize() : 
				random.nextInt(graph.getMaxEdges() - graph.getEdgeSize());
		else
			emptySlots = (int)Math.max((graph.getMaxEdges() * MAX_EDGES_RATIO) - graph.getEdgeSize(), 0);
		
		/* Phase II. Go through nodes until all edge slots are used. */
		while (emptySlots > 0) {
			for (int n = 0; n < graph.getSize(); n++) {
				if(emptySlots > 0) {
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
						graph.addEdge(n, n2);
						emptySlots--;
					}
				}
			}
		}
		return graph;
	}
	
	public int getTrueCount(boolean[] array) {
		int count = 0;
		for (boolean b : array)
			if(b) count++;
		return count;
	}

	/* A Fisher�Yates shuffle for lists. */
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
