package com.msg.oblig2.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.msg.oblig2.csp.Graph;
import com.msg.oblig2.csp.Node;

public class FileHandler {
	
	public static Graph loadGraph(int nodes, int edges) {
		
		Graph graph = null;
		// Wrap all in a try/catch block to trap I/O errors.
		try {
			InputStreamReader isReader = new InputStreamReader(
                        FileHandler.class.getClassLoader().getResourceAsStream("" + 
															nodes + "n_" + 
															edges + "e.graph"));
	
			// Create an ObjectInputStream to get objects from save file.
			BufferedReader load = new BufferedReader(isReader);
			
			// Now we do the loading.
			int size = Integer.parseInt(load.readLine()); // read graph size
			graph = new Graph(size);
			for (int n = 0; n < size; n++) // read colour int of each node
				graph.setNode(n, new Node(Integer.parseInt(load.readLine())));
			for (int i = 0; i < size; i++) // read boolean value of each matrix cell
				for (int j = 0; j < size; j++)
					graph.setEdge(i, j, load.readLine().equals("1") ? true : false);
			graph.setEdgeSize(Integer.parseInt(load.readLine()));
			graph.setFitness(Integer.parseInt(load.readLine()));
			
			// Close the file.
			load.close(); // This also closes loadFile.
		
		} catch(IOException e) {
			System.out.println("Unable to find stored specified graph.");
		}
		return graph;
	}

	public static void saveGraph(Graph graph) {
		
		try {  // Catch errors in I/O if necessary.
			PrintWriter save = new PrintWriter(new File(
					FileHandler.class.getClassLoader().getResource("").getPath() + 
								graph.getSize() + "n_" + 
								graph.getEdgeSize() + "e.graph"));
	
			// Now we do the saving.
			save.println(graph.getSize()); // start by saving the graph size
			for (int n = 0; n < graph.getNodes().length; n++)
				save.println(graph.getNodes()[n].getColour());
			for (int i = 0; i < graph.getAdjMatrix().length; i++)
				for (int j = 0; j < graph.getAdjMatrix()[i].length; j++)
					save.println(graph.getAdjMatrix()[i][j] ? 1 : 0); // save as integer 1 or 0
			save.println(graph.getEdgeSize());
			save.println(graph.getFitness());
	
			// Close the file.
			save.close(); // This also closes saveFile.
		} catch(IOException exc) {
			exc.printStackTrace(); // If there was an error, print the info.
		}
	}
}



