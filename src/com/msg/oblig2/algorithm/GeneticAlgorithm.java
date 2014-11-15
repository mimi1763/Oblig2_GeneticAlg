package com.msg.oblig2.algorithm;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import com.msg.oblig2.csp.Graph;
import com.msg.oblig2.tools.NanoTimer;

/* TODO:
 * 
 */

public class GeneticAlgorithm extends Algorithm<Graph[]> {
	
	private int previousBestFitness;
	private Graph bestChromosome;
	private ArrayList<Graph> population;
	private int popSize, generation, stagnation, bestFitness;
	private boolean hasChanged;
	
	public GeneticAlgorithm(Graph graph) {
		bestChromosome = new Graph(maxIterations);
		this.popSize = POPULATION_SIZE;
		population = new ArrayList<Graph>();
		previousBestFitness = Integer.MAX_VALUE;
	}

	/**
	 * Genetic algorithm. Optimizes a given population array of Graphs
	 * of size POPULATION_SIZE and returns the optimized array.
	 * 
	 * @param Graph[]
	 * @return new Graph[]
	 */
	@Override
	public Graph[] process(Graph[] inputPopulation) {
		/* Empty population list. */
		population.clear();
		/* Recalculate fitness of all PolygonImages in input population. */
		inputPopulation = recalculatePopulationFitness(inputPopulation);
		/* Create Graph list from population array parameter. */
		Collections.addAll(population, inputPopulation);		
		boolean[] usedPopulation = new boolean[popSize << 1];
		/* Set startFitness equal to fitness of first chromosome in population. */
		int startFitness = population.get(0).getFitness();
		bestFitness = startFitness;
		previousBestFitness = startFitness;
		Random random = new Random(System.nanoTime());
		NanoTimer nanoTimer = new NanoTimer();
		
		/* Initialize number of generations. */
		generation = 0;
		/* Initialize number of stagnations. */
		stagnation = 0;
		
		ParentChoice parentChoice;
		Graph[] parent = new Graph[2];
				
		/* Initialize current best chromosome. */
		bestChromosome = population.get(0);

		int minFitnessDiff = (int)Math.ceil(startFitness * MIN_FITNESS_DIFF_RATIO);
		
		System.out.println("\nGenetic algorithm CSP. Population: " + popSize + ". Node count: " +
				maxIterations + ". Minimum fitnessDiff: " + minFitnessDiff);
		
		/* 
		 * - -----------~~~=====<< Main algorithm loop. >>=====~~~----------- - 
		 */
		
		while (stagnation < NUMBER_OF_GENERATIONS && bestChromosome.getFitness() > 0) {

			random = new Random(System.nanoTime());
			
			nanoTimer.startTimer();
			
			/* Initialize usedPop array. */
			Arrays.fill(usedPopulation, false);
			
			/* Select mates by fitness. 
			 * Cross them and get two children using cross-over.
			 * Mutate children.
			 * Remove half of chromosomes by lowest fitness.
			 */
			int better = 0;		
			Graph[] children;

			/* Go through chromosomes via loop of half the size of popSize,
			 * since two parents are crossed each loop.
			 * Randomly choose one of three types of parent selection.
			 */			
			parentChoice = ParentChoice.values()[random.nextInt(ParentChoice.values().length)];
			for (byte i = 0; i < popSize; i += 2) {				
				switch (parentChoice) {
					case RND_TWO:			for (byte b = 0; b < 2; b++) {
												do {
													better = random.nextInt(popSize);
												} while (usedPopulation[better]);
												parent[b] = new Graph(population.get(better));
												usedPopulation[better] = true;
											}
											break;
					case RND_BEST_WORST:	for (byte b = 0; b < 2; b++) {
												do
													better = random.nextInt((popSize >> 1)) + b * (popSize >> 1);
												while (usedPopulation[better]);
												parent[b] = new Graph(population.get(better));
												usedPopulation[better] = true;
											}
											break;
					case TOP_TWO_BEST:		int sel = 0;
											boolean foundParent;
											for (byte b = 0; b < 2; b++) {
												foundParent = false;
												do {
													better = sel;
													if(!usedPopulation[sel])
														foundParent = true;
													else
														sel++;
												} while (!foundParent && sel < popSize);
												if(sel < popSize) {
													parent[b] = new Graph(population.get(better));
													usedPopulation[sel] = true;
												}
											}	
											break;
				}

				children = new Graph[2];
				
				/* Create the two children. */
				for (byte c = 0; c < 2; c++)	{			
					/* Create child as copy of its parent. */
					children[c] = new Graph(parent[c]);
				}
				
				/* Do two-point cross-over if ratio permits it. */
				if(random.nextDouble() < CROSSOVER_RATIO) {
					int pos1 = random.nextInt(maxIterations-1);
					int pos2 = Math.min((random.nextInt(Math.max(maxIterations>>1, 1)) + pos1), maxIterations);
//					System.out.println("crossing from " + pos1 + " to " + pos2 + " = " + (pos2 - pos1));
					for (int b = pos1; b < pos2; b++) {
						Color temp = children[0].getNode(b).getColour();
						children[0].getNode(b).setColour(children[1].getNode(b).getColour());
						children[1].getNode(b).setColour(temp);
					}
					children[0].recalculateFitness();
					children[1].recalculateFitness();
				}
				
				/* Mutate children if ratio permits it.
				 * Random number of mutations per child up to MAX_MUTATIONS percentage
				 * of GRAPH_SIZE. 
				 */
				if(random.nextDouble() < MUTATION_RATIO) {
					for (byte c = 0; c < 2; c++) {
						int nbr = (int)((random.nextDouble() * MAX_MUTATIONS) * maxIterations) + 1;
						for (int n = 0; n < nbr; n++) {
							int pos = random.nextInt(maxIterations);
							/* Change random node to random colour. */
							children[c].getNode(pos).randomColour();
						}
						children[c].recalculateFitness();
					}				
				}

				/* Copy the children to bottom half of population array. */
				for (byte c = 0; c < 2; c++)
					population.add(new Graph(children[c]));
			}
			
//			population = recalculatePopulationFitness(population);
			
			previousBestFitness = bestChromosome.getFitness();
			
			/* Sort newPopulation by fitness. */
			Collections.sort(population);
			
			/* Check if current fitness is the better one. */
			if(population.get(0).getFitness() < previousBestFitness)
				setCurrentBestGraph(population.get(0));

			/* Remove the worse half of population. */
			for (int p = population.size() - 1; p > popSize - 1; p--)
				population.remove(p);
			
			nanoTimer.stopTimer();
			
			int diff = previousBestFitness - bestChromosome.getFitness();
			if(diff > 0)
				System.out.println("Fitness improvement: " + diff);
			
			/* If current best fitness is not more than 0.1 percent better than previous best,
			 * increment iterations.
			 */
			generation++;
			if((previousBestFitness - bestChromosome.getFitness()) < minFitnessDiff)
				stagnation++;
			else
				stagnation = 0;
			
			/* If stagnating NUMBER_OF_STAGNATIONS times, inject new blood. */
			if(stagnation > 0 && stagnation % NUMBER_OF_STAGNATIONS == 0) {
				System.out.println("Stagnating: " + stagnation + 
						". Removing worst half, replacing by injecting new blood.");
				
				/* Remove the worse half of population. */
				population = getTopHalfPopulation(population);

				/* Replace removed worse half with new blood. */
				int addNumber = POPULATION_SIZE - population.size();
				Graph graph = new Graph(population.get(0));
				for (int blood = 0; blood < addNumber; blood++) {
					graph.shuffleColours();
					population.add(new Graph(graph));
				}
			}
			
			/* Print every PRINT_FREQUENCY generations. */
			if(PRINT_FREQUENCY > 0 && generation % PRINT_FREQUENCY == 0)
				nanoTimer.printElapsedTime(" Gen #: " + generation + ". Stagnating: " + stagnation +
						". Best fitness: " + bestChromosome.getFitness());
		}

		Graph[] array = new Graph[population.size()];
		return (Graph[])population.toArray(array);
	}

	/* 
	 * ------------------------<<<<< End main algorithm >>>>>------------------------- 
	 */
	
	public Graph getCurrentBestGraph() {
		return bestChromosome;
	}

	public void setCurrentBestGraph(Graph bestGraph) {
		bestChromosome = new Graph(bestGraph);
		hasChanged = true;
	}
	
	public Graph[] getPopulation() {
		Graph[] array = new Graph[population.size()];
		return (Graph[])population.toArray(array);
	}
	
	public void setPopulation(Graph[] population) {
		if(!this.population.isEmpty())
			this.population.clear();
		Collections.addAll(this.population, population);
	}
	
	public boolean hasChanged() {
		boolean bool = hasChanged;
		if(hasChanged)
			hasChanged = false;
		return bool;
	}
	
	public int getGeneration() {
		return generation;
	}
	
	public int getStagnation() {
		return stagnation;
	}
	
	/**
	 * Returns the top half of the given list of Graphs.
	 * The list returned thus has half the size of the parameter (input) list.
	 * 
	 * @param population
	 * @return top half of population list
	 */
	public ArrayList<Graph> getTopHalfPopulation(ArrayList<Graph> population) {
		int size = population.size();
		ArrayList<Graph> list = new ArrayList<Graph>();
		for (Graph graph : population)
			list.add(new Graph(graph));
		/* Remove the worse half of population. */
		for (int p = size - 1; p > (size >> 1) - 1; p--)
			list.remove(p);

		return list;
	}

	public static Graph[] recalculatePopulationFitness(Graph[] population) {
		Graph[] newPop = new Graph[population.length];
		System.arraycopy(population, 0, newPop, 0, population.length);
		for (int i = 0; i < population.length; i++)
			newPop[i].recalculateFitness();
		return newPop;
	}
	
	public static ArrayList<Graph> recalculatePopulationFitness(ArrayList<Graph> population) {
		ArrayList<Graph> newPop = new ArrayList<Graph>(population);
		for (Graph graph : newPop)
			graph.recalculateFitness();
		return newPop;
	}

}
