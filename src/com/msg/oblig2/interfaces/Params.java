package com.msg.oblig2.interfaces;

import java.awt.Color;

public interface Params {
	
	// Graph class:
	
	/* Number of nodes in a graph (n). */
	final static int GRAPH_SIZE = 8;
	/* Use complete graph = n(n-1)/2 edges. */
	final static boolean COMPLETE_GRAPH = false;
	
	// DrawGraph Class:
	
	/* Draw the graph, i.e. use graphics. */
	final static boolean DRAW_GRAPH = true;
	/* Show nodes. */
	final static boolean SHOW_NODES = true & DRAW_GRAPH;
	/* Show edges. */
	final static boolean SHOW_EDGES = true & DRAW_GRAPH;
	/* Cell size diameter in pixels. */
	final static int CELL_SIZE = 20;
	/* Show number of neighbours. */
	final static boolean SHOW_NEIGHBOURS = false & DRAW_GRAPH;
	/* Show number of similar neighbours. */
	final static boolean SHOW_SIMILAR = false & DRAW_GRAPH;
	/* Radius fuzziness of cell drawing. */
	final static double RAD_FUZZINESS = 1.00;
	
	/* Time in seconds to wait before algorithm begins. */
	final static int WAIT_TIME = 2;
	
	/* Colours. */
	final static Color BROWN_GREEN = new Color(171, 153, 29);
	final static Color AQUA_GREEN = new Color(95, 158, 92);
	final static Color OCHRE = new Color(204, 119, 34);
	final static Color FALU_RED = new Color(128, 24, 24);
	
	/* Colours possible for a cell. */
	final static Color[] COLOURS = {Color.WHITE, Color.BLACK, FALU_RED};
	
	/* Millisecond frequency for how often best image is 
	 * updated from algorithm thread. */ 
	static final int CHECK_FREQUENCY = 50;
	/* Frequency for how often print out of updates occur, 
	 * once every PRINT_FREQUENCY times. 0 = No print. */ 
	static final int PRINT_FREQUENCY = 0;
	/* Number of generations to iterate generic algorithm loop. */
	static final int NUMBER_OF_GENERATIONS = 200000;
	/* Number of stagnations before replacing worse half of population with new blood. */
	static final int NUMBER_OF_STAGNATIONS = 2000;
	/* Percentage of previous best fitness that current best has 
	 * to be better than, or stagnation increments. */
	final static float MIN_FITNESS_DIFF_RATIO = 1 / (float)GRAPH_SIZE;
	 
	// Main genetic algorithm class:
	
	/* enums for setting type of parent selection. */
	static enum ParentChoice {
		RND_TWO, RND_BEST_WORST, TOP_TWO_BEST;

		private final int value;
		
		private ParentChoice() {
			this.value = ordinal();
		}
	}
	/* Percent chance of entering cross-over stage. */
	static final float CROSSOVER_RATIO = 0.85f;
	/* Percent chance of entering mutation stage. */
	static final float MUTATION_RATIO = 0.1f;
	/* Max percentage of POLYGON_COUNT mutations per child. */
	static final float MAX_MUTATIONS = 1 / (float)GRAPH_SIZE;
	/* Population size of chromosomes. */
	static final int POPULATION_SIZE = 40; // must be even number
}
