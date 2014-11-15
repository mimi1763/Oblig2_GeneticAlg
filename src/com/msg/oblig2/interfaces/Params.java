package com.msg.oblig2.interfaces;

import java.awt.Color;

public interface Params {
	
	// Graph class:
	
	/* Number of cells in a graph. */
	final static int GRAPH_SIZE = 5;
	/* Minimum number of cell edges. */
	final static int MIN_EDGES = GRAPH_SIZE-1;
	/* Maximum number of cell edges. */
	final static int MAX_EDGES = GRAPH_SIZE-1;
	
	// DrawGraph Class:
	
	/* Show nodes. */
	final static boolean SHOW_NODES = true;
	/* Show edges. */
	final static boolean SHOW_EDGES = true;
	/* Show number of neighbours. */
	final static boolean SHOW_NEIGHBOURS = true;
	/* Show number of similar neighbours. */
	final static boolean SHOW_SIMILAR = false;
	/* Radius fuzziness of cell drawing. */
	final static double RAD_FUZZINESS = 0.00;
	/* Cell size diameter. */
	final static int CELL_SIZE = 15;
	
	/* Time in seconds to wait before algorithm begins. */
	final static int WAIT_TIME = 2;
	/* Colours possible for a cell. */
	final static Color[] COLOURS = {Color.WHITE, Color.BLACK, Color.RED};
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
	static final float MUTATION_RATIO = 0.05f;
	/* Max percentage of POLYGON_COUNT mutations per child. */
	static final float MAX_MUTATIONS = 1 / (float)GRAPH_SIZE;
	/* Population size of chromosomes. */
	static final int POPULATION_SIZE = 20; // must be even number
}
