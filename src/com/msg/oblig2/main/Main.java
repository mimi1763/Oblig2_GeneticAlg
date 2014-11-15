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
		
		/* Create a random graph. */	
		Graph[] population = new Graph[Params.POPULATION_SIZE];
		RandomAlgorithm randomAlg = new RandomAlgorithm();
		randomAlg.setMaxIterations(size);
		graph = randomAlg.process(null);
		
//		graph.printMatrix();
				
		/* Create population of random colour variants of the random graph. */
		for (int i = 0; i < population.length; i++) {
			population[i] = new Graph(graph);
			population[i].shuffleColours();
			population[i].recalculateFitness();
		}
		
		Collections.sort(Arrays.asList(population));
		
		Graph finalGraph = population[0];
		
		/* Create new thread for algorithm on complete graph. */
		GenAlgThread genAlgThread = new GenAlgThread(graph, population);
		Thread thread;
		thread = new Thread(genAlgThread);
				
		/* Display final constructed graph. */
		JFrame frame = new JFrame();
		DrawGraph drawGraph = new DrawGraph(finalGraph, 640, 640);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		frame.getContentPane().add(drawGraph);
		frame.setSize(drawGraph.getSize());
		drawGraph.repaint();
		frame.setVisible(true);
		
		/* Wait 2 seconds before starting algorithm. */
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

