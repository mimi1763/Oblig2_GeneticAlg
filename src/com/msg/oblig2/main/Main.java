package com.msg.oblig2.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JFrame;

import com.msg.oblig2.algorithm.GeneticAlgorithm;
import com.msg.oblig2.algorithm.RandomAlgorithm;
import com.msg.oblig2.csp.Graph;
import com.msg.oblig2.gui.DrawGraph;
import com.msg.oblig2.interfaces.Params;

public class Main {
		
	public Main() {
		
		Graph graph;
		
		/* Number of nodes in graph. */
		int size = Params.GRAPH_SIZE;
			
		/* Initialize new population. */
		Graph[] population = new Graph[Params.POPULATION_SIZE];
		
		/* Create a random graph. */
		if(Params.LOAD_GRAPH) {
			int edgeSize = (int)(Params.MAX_EDGES * Params.MAX_EDGES_RATIO);
			graph = new Graph(FileHandler.loadGraph(size, edgeSize));
		} else {
			RandomAlgorithm randomAlg = new RandomAlgorithm();
			randomAlg.setMaxIterations(size);
			graph = randomAlg.process(null);
		}
		
		if(Params.SAVE_GRAPH)
			FileHandler.saveGraph(graph);
		
		int cycles = Params.CYCLES;
		int totalInitial = 0;
		int totalFitness = 0;
		for (int cycle = 0; cycle < cycles; cycle++) {
				
			/* Create population of random colour variants of the random graph. */
			for (int i = 0; i < population.length; i++) {
				population[i] = new Graph(graph);
				population[i].shuffleColours();
				population[i].calcFitness();
			}
			
			Collections.sort(Arrays.asList(population));
			
			Graph finalGraph = population[0];
			
			totalInitial += finalGraph.getFitness();
			
			/* Create new thread for algorithm on complete graph. */
			GenAlgThread genAlgThread = new GenAlgThread(graph, population);
			Thread thread;
			thread = new Thread(genAlgThread);
					
			/* Display final constructed graph. */
			JFrame frame = new JFrame();
			int height = 80;
			if(Params.DRAW_GRAPH)
				height = 640;
			else if(Params.DRAW_MATRIX)
				height = 100 + Params.GRAPH_SIZE * 24;
			DrawGraph drawGraph = new DrawGraph(finalGraph, 640, height);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setAlwaysOnTop(true);
			frame.getContentPane().add(drawGraph);
			frame.setSize(drawGraph.getSize());
			drawGraph.repaint();
			frame.setVisible(true);
			
			/* Wait WAIT_TIME seconds before starting algorithm. */
			for (int i = 0; i < Params.WAIT_TIME; i++)
				try {
					frame.setTitle("Starting algorithm in " + (Params.WAIT_TIME-i) + " seconds...");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			frame.setTitle("Genetic algorithm - CSP.      Current best graph:");
			
			/* Start the genetic algorithm thread. */
			thread.start();
			
			/* Loop until algorithm thread is done. */
			while (thread.getState() != Thread.State.TERMINATED) {
				try {
					Thread.sleep(Params.CHECK_FREQUENCY);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(genAlgThread.hasChanged()) {
					finalGraph = genAlgThread.getGraph();
					frame.getContentPane().remove(drawGraph);
					drawGraph.setGraph(finalGraph);
					drawGraph.reloadColours();
				}
				drawGraph.setGeneration(genAlgThread.getGeneration());
				drawGraph.setStagnation(genAlgThread.getStagnation());
				frame.getContentPane().add(drawGraph);
				drawGraph.repaint();
			}
			totalFitness += finalGraph.getFitness();
			frame.dispose();
		}
		
		System.out.println("--------------------------------------------");
		int averageInitial = (int)(totalInitial / (double)cycles);
		System.out.println("Average initial fitness: " + averageInitial);
		int average = (int)(totalFitness / (double)cycles);
		System.out.println("Average fitness through " + cycles + " cycles : " + average);
		System.out.println("Average to initial ratio: " + (average / (double)averageInitial));
	}

	public static void main(String[] args) {
		new Main();
	}
	
	/**
	 * Converts an object of given type to an array.
	 * 
	 * @param object
	 * @return array of the object
	 */
	public static <T> T[] convertToArray(T object) {
		ArrayList<T> list = new ArrayList<T>();
		list.add(object);
		@SuppressWarnings("unchecked")
		T[] array = (T[]) java.lang.reflect.Array.newInstance(object.getClass(), 1);
		return list.toArray(array);
	}
	
	private class GenAlgThread implements Runnable {
		
		private Graph graph;
		Graph[] population = null;
		private GeneticAlgorithm geneticAlg;
		
		/**
		 * 
		 * @param graph
		 * @param population
		 */
		public GenAlgThread(Graph graph, Graph[] population) {
			this.graph = graph;
			this.population = population;
			geneticAlg = new GeneticAlgorithm(this.graph);
			geneticAlg.setMaxIterations(Params.GRAPH_SIZE);
		}

		@Override
		public void run() {
			population = geneticAlg.process(population);
		}
		
		public boolean hasChanged() {
			return geneticAlg.hasChanged();
		}
		
		public Graph getGraph() {
			return geneticAlg.getCurrentBestGraph();
		}
		
		public int getGeneration() {
			return geneticAlg.getGeneration();
		}
		
		public int getStagnation() {
			return geneticAlg.getStagnation();
		}
	}
}

