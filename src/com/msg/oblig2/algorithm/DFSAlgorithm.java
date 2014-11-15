package com.msg.oblig2.algorithm;

import com.msg.oblig2.csp.Graph;

public class DFSAlgorithm {
	
	private boolean[] visited;
	private int count = 0;
	
	public DFSAlgorithm(int size) {
		visited = new boolean[size];
	}
	
	public void dfs(Graph graph, int node) {
		count++;
		visited[node] = true;
		for (int edgeId : graph.getAdjacencyList(node)) {
			int nb = graph.getEdges()[edgeId].getNode2();
			if(nb == node)
				nb = graph.getEdges()[edgeId].getNode1();
			if(!visited[nb])
				dfs(graph, nb);
		}
	}

	public int getCount() {
		return count;
	}
}
