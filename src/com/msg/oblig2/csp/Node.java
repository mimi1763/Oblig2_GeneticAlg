package com.msg.oblig2.csp;

import java.awt.Color;
import java.util.Random;

import com.msg.oblig2.interfaces.Params;

public class Node {
	
	private Color colour;
	private int edgeSlots;
	
	public Node(int edgeCount) {
		this(null);
		this.edgeSlots = edgeCount;
		randomColour();
	}
	
	public Node() {
		this(1);
	}
	
	public Node(Color colour) {
		this.colour = colour;
	}

	public Color getColour() {
		return colour;
	}

	public void setColour(Color colour) {
		this.colour = colour;
	}
	
	public int getEdgeSlots() {
		return edgeSlots;
	}
	
	public void decEdgeSlots() {
		edgeSlots = Math.max(--edgeSlots, 0);
	}

	public void randomColour() {
		Random random = new Random(System.nanoTime());
		this.colour = Params.COLOURS[random.nextInt(Params.COLOURS.length)];
	}
	
	@Override
	public String toString() {
		return "Node";
	}
	
	public boolean equals(Node node) {
		return (this.colour.equals(node.colour));
	}
}
