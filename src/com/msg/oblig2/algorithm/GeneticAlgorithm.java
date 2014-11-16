package com.msg.oblig2.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import com.msg.oblig2.csp.Graph;
import com.msg.oblig2.interfaces.Params;
import com.msg.oblig2.tools.NanoTimer;


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
	 * @param Graph[]
	 * @return new Graph[]
	 */
	@Override
	public Graph[] process(Graph[] inputPopulation) {
		/* Empty population list. */
		population.clear();
		/* Recalculate fitness of all PolygonImages in input population. */
		inputPopulation = calcPopulationFitness(inputPopulation);
		/* Create Graph list from population array parameter. */
		Collections.addAll(population, inputPopulation);		
		boolean[] usedPopulation = new boolean[popSize << 1];
		/* Set startFitness equal to fitness of first chromosome in population. */
		int startFitness = population.get(0).getFitness();
		bestFitness = startFitness;
		previousBestFitness = startFitness;
		Random random = new Random(System.nanoTime());
		NanoTimer nanoTimer = new NanoTimer();
		NanoTimer totalTimer = new NanoTimer();
		
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
		totalTimer.startTimer();
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
					for (int b = pos1; b < pos2; b++) {
						int temp = children[0].getNode(b).getColour();
						children[0].getNode(b).setColour(children[1].getNode(b).getColour());
						children[1].getNode(b).setColour(temp);
					}
					children[0].calcFitness();
					children[1].calcFitness();
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
						children[c].calcFitness();
					}				
				}

				/* Copy the children to bottom half of population array. */
				for (byte c = 0; c < 2; c++)
					population.add(children[c]);
			}
			
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
				
				/* Remove worse half of population. */
				population = getTopHalfPopulation(population);

				/* Replace the worse half with new blood. */
				Graph graph = new Graph(0);
				int addNumber = POPULATION_SIZE - population.size();
				for (int blood = 0; blood < addNumber; blood++) {
					graph = new Graph(population.get(random.nextInt(population.size())));
					graph.shuffleColours();
					graph.calcFitness();
					population.add(graph);				
				}
			}
			
			/* Print every PRINT_FREQUENCY generations. */
			if(PRINT_FREQUENCY > 0 && generation % PRINT_FREQUENCY == 0)
				nanoTimer.printElapsedTime(" Gen #: " + generation + ". Stagnating: " + stagnation +
						". Best fitness: " + bestChromosome.getFitness());
		} // end main while-loop
		
		totalTimer.stopTimer();
		
		System.out.println("\nGenetic alg. done. bestChromosome fitness = " + 
				bestChromosome.getFitness());
		
		totalTimer.printElapsedTime();
		
//		bestChromosome.printMatrix();
//		bestChromosome.printEdges();
		
		/* Sort population by fitness one last time. */
		Collections.sort(population);
		/* Convert population list to array and return it. */
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
	
	public boolean hasChanged() {
		boolean bool = hasChanged;
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
	 * Calculates the total fitness of all graphs in inputPopulation.
	 * @param inputPopulation
	 * @return population
	 */
	private Graph[] calcPopulationFitness(Graph[] inputPopulation) {
		Graph[] newPop = new Graph[inputPopulation.length];
		System.arraycopy(inputPopulation, 0, newPop, 0, inputPopulation.length);
		for (Graph chrom : newPop)
			chrom.calcFitness();
		return newPop;
	}
	
	/**
	 * Returns the top half of the given list of Graphs.
	 * The list returned thus has half the size of the parameter (input) list.
	 * 
	 * @param population
	 * @return top half of population list
	 */
	public ArrayList<Graph> getTopHalfPopulation(ArrayList<Graph> population) {
		ArrayList<Graph> list = new ArrayList<Graph>();
		for (int i = 0; i < population.size() / 2; i++)
			list.add(new Graph(population.get(i)));
		return list;
	}
}
