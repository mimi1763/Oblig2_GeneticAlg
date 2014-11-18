package com.msg.oblig2.interfaces;

import java.awt.Color;

public interface Params {
	
	// Graph class:
	
	/* Number of nodes in a graph (n). */
	final static int GRAPH_SIZE = 250;
	/* Load graph instead of generating it. Graph must have been generated and saved before. */
	final static boolean LOAD_GRAPH = true;
	/* Saves the generated/loaded graph. */
	final static boolean SAVE_GRAPH = false;
	/* Maximum number of edges. This is just a formula and cannot be changed. */
	final static int MAX_EDGES = (GRAPH_SIZE * (GRAPH_SIZE - 1)) / 2;
	/* Use complete graph = n(n-1)/2 edges. */
	final static boolean COMPLETE_GRAPH = false;
	/* Use percentage of max edges (MAX_EDGES_RATIO). */
	final static boolean USE_EDGE_RATIO = true & !COMPLETE_GRAPH;
	/* Percentage of max edges (n(n-1)/2). Tweak this to set specific edge count. */
	final static float MAX_EDGES_RATIO = 0.1f;
	
	/* Time in seconds to wait before algorithm begins. */
	final static int WAIT_TIME = 0; 
	
	/* Number of cycles to run the same graph structure. */
	static final int CYCLES = 1;
	/* Millisecond frequency for how often best image is 
	 * updated from algorithm thread. */ 
	static final int CHECK_FREQUENCY = 50;
	/* Frequency for how often print out of updates occur, 
	 * once every PRINT_FREQUENCY times. 0 = No print. */ 
	static final int PRINT_FREQUENCY = 0;
	/* Number of generations to iterate generic algorithm loop. */
	static final int NUMBER_OF_GENERATIONS = 10000;
	/* Number of stagnations before replacing worse half of population with new blood. */
	static final int NUMBER_OF_STAGNATIONS = 2000;
	/* Percentage of previous best fitness that current best has 
	 * to be better than, or stagnation increments. */
	final static float MIN_FITNESS_DIFF_RATIO = 1 / (float)(MAX_EDGES);
	
	/* Colours. */
	final static Color BROWN_GREEN = new Color(171, 153, 29);
	final static Color AQUA_GREEN = new Color(95, 158, 92);
	final static Color OCHRE = new Color(204, 119, 34);
	final static Color FALU_RED = new Color(128, 24, 24);
	
	/* Colours possible for a cell. */
	final static Color[] COLOURS = {Color.WHITE, Color.BLACK, FALU_RED};
	 
	// Main genetic algorithm class:
	
	/* Percent chance of entering cross-over stage. */
	static final float CROSSOVER_RATIO = 0.85f;
	/* Percent chance of entering mutation stage. */
	static final float MUTATION_RATIO = 0.90f;
	/* Max percentage of POLYGON_COUNT mutations per child. */
	static final float MAX_MUTATIONS = 3 / (float)MAX_EDGES;
	/* Population size of chromosomes. */
	static final int POPULATION_SIZE = 10; // must be even number
	/* enums for setting type of parent selection. */
	static enum ParentChoice {
		RND_TWO, RND_BEST_WORST, TOP_TWO_BEST;

		private final int value;
		
		private ParentChoice() {
			this.value = ordinal();
		}
	}
	
	// DrawGraph Class:
	
	/* Draw the graph, i.e. use graphics. */
	final static boolean DRAW_GRAPH = false;
	/* Draw matrix. */
	final static boolean DRAW_MATRIX = false;
	/* Use asymmetrical graph drawing style. False = more orderly. */
	final static boolean GRAPH_DRAWING_RND = false;
	/* Show nodes. */
	final static boolean SHOW_NODES = true & DRAW_GRAPH;
	/* Show edges. */
	final static boolean SHOW_EDGES = false & DRAW_GRAPH;
	/* Cell size diameter in pixels. */
	final static int CELL_SIZE = 20;
	/* Show number of neighbours. */
	final static boolean SHOW_NEIGHBOURS = false & DRAW_GRAPH;
	/* Show number of similar neighbours. */
	final static boolean SHOW_SIMILAR = false & DRAW_GRAPH;
	/* Radius fuzziness of cell drawing. */
	final static double RAD_FUZZINESS = 0.00;
}
