package com.msg.oblig2.csp;

import java.util.Random;

import com.msg.oblig2.interfaces.Params;

public class Node {
	
	private int colour;
	
	public Node() {
		this(0);
		randomColour();
	}
	
	public Node(int colour) {
		this.colour = colour;
	}

	public int getColour() {
		return colour;
	}

	public void setColour(int colour) {
		this.colour = colour;
	}

	public void randomColour() {
		Random random = new Random(System.nanoTime());
		this.colour = random.nextInt(Params.COLOURS.length);
	}
	
	@Override
	public String toString() {
		return "Node";
	}
	
	public boolean equals(Node node) {
		return (this.colour == node.colour);
	}
}
