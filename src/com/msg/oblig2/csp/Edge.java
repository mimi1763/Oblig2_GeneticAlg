package com.msg.oblig2.csp;

public class Edge {
	
	private int node1, node2;
	
	public Edge(int node1, int node2) {
		this.node1 = node1;
		this.node2 = node2;
	}

	public int getNode1() {
		return node1;
	}

	public void setNode1(int node1) {
		this.node1 = node1;
	}

	public int getNode2() {
		return node2;
	}

	public void setNode2(int node2) {
		this.node2 = node2;
	}
	
	public boolean isSimilar(Graph graph) {
		return (graph.getNode(node1).equals(graph.getNode(node2)));
	}
	
	public boolean contains(int node1, int node2) {
		return (node1 == this.node1 && node2 == this.node2) || 
				(node1 == this.node2 && node2 == this.node1);
	}
	
	@Override
	public String toString() {
		return ("Edge (" + node1 + ", " + node2 + ")");
	}
	
}
