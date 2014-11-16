package com.msg.oblig2.gui;

import java.awt.Color;
import java.awt.Font;
//import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Random;

import javax.swing.JPanel;

import com.msg.oblig2.csp.Graph;
import com.msg.oblig2.interfaces.Params;
 
@SuppressWarnings("serial")
public class DrawGraph extends JPanel {
	
	final static double DOUBLE_PI = Math.PI * 2.0;
	final static double HALF_PI = Math.PI / 2.0;
	final static double QUARTER_PI = Math.PI / 4.0;
	final static double OCTANT_PI = Math.PI / 8.0;
	final static double UNCI_PI = Math.PI / 12.0;
	/* Edge arrow-line degree from main line. */
	final static double PHI = UNCI_PI;
	private final int PAD = 40;
	private final int logN;
	
	private int cellCount;
    private Cell[] cells;
    private int w, h;
    private Point centre;
    private Graph graph;
	private int generation = 0, stagnation = 0;
    
    public DrawGraph(Graph graph, int w, int h) {
    	this.w = w;
    	this.h = h;
    	centre = new Point(w >> 1, h >> 1);
    	centre.translate(-PAD, -PAD);
    	this.graph = graph;
    	logN = (int)(Math.log10(graph.getSize()) / Math.log10(2));
    	cellCount = graph.getSize();
    	cells = generateCells();
    	setSize(w, h);
        setBackground(Color.DARK_GRAY);
        repaint();
    }
 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setPaint(Color.DARK_GRAY);
        g2.fillRect(0, 0, w, h);
        
        if(Params.DRAW_MATRIX) {
	        /* Matrix frame. */
	        g2.setPaint(Color.WHITE);
	        g2.drawLine(PAD, PAD, PAD + (graph.getSize() * (12 << 1)), PAD); // top
	        g2.drawLine(PAD, PAD, PAD, PAD + (graph.getSize() * (12 << 1))); // left
	        g2.drawLine(PAD + (graph.getSize() * (12 << 1)), PAD, 
	        		PAD + (graph.getSize() * (12 << 1)), PAD + (graph.getSize() * (12 << 1))); // right
	        g2.drawLine(PAD, PAD + (graph.getSize() * (12 << 1)), 
	        		PAD + (graph.getSize() * (12 << 1)), PAD + (graph.getSize() * (12 << 1))); // bottom
        }

        /* Draw cells. */
        int node;
        for(int i = 0; i < cellCount; i++) {
        	if(Params.DRAW_MATRIX) {
	        	g2.setPaint(Color.WHITE);
	        	g2.drawString(String.valueOf(i), (PAD + 12 + i * (12 << 1)), PAD); // hori nodes
	        	g2.drawString(String.valueOf(i), PAD - 12, PAD + 12 + i * (12 << 1)); // vert nodes
	        	
	        	for(int x = 0; x < cellCount; x++) {
	        		node = 0;
	        		if(graph.isSimilar(i, x))
	        			node = 1;
	        		if(graph.hasEdge(i, x))
	        			g2.drawString(String.valueOf(node), (PAD + 12 + x * (12 << 1)), PAD + 12 + i * (12 << 1));
	        	}
        	}       	
        	
        	if(Params.SHOW_NODES) {
	            g2.setPaint(Params.COLOURS[cells[i].colour]);
	            g2.fill(new Ellipse2D.Double(cells[i].x, cells[i].y, 
	            		Params.CELL_SIZE, Params.CELL_SIZE));
	            
	            g2.setPaint(Color.LIGHT_GRAY);
	            g2.setFont(new Font("SansSerif", Font.ITALIC, 12));
	            g2.drawString(String.valueOf("n#"+i), cells[i].x, cells[i].y);
        	}
          
            /* Draw fitness, generations and stagnations text. */
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2.setPaint(Color.LIGHT_GRAY);
            g2.drawString("Best fitness: " + graph.getFitness() + 
            		"          Generation #: " + generation + 
            		"          Stagnations: " + stagnation + "  of  " + 
            		Params.NUMBER_OF_GENERATIONS +
            		"          Total # of edges: " + graph.getEdgeSize(), 10, 10);
        }
        
        /* Draw edges. */
        if(Params.SHOW_EDGES) {
	        for (int e = 0; e < graph.getEdgeSize(); e++) {
		        g2.setPaint(Params.OCHRE);
	        	Point start = new Point(cells[graph.getEdge(e)[0]].x, cells[graph.getEdge(e)[0]].y);
	        	Point end = new Point(cells[graph.getEdge(e)[1]].x, cells[graph.getEdge(e)[1]].y);
	        	/* Start and end edge lines from and to the centre of cells. */
	        	start.translate(Params.CELL_SIZE >> 1, Params.CELL_SIZE >> 1);
	        	end.translate(Params.CELL_SIZE >> 1, Params.CELL_SIZE >> 1);
	        	if(graph.isSimilar(e))
	        		g2.setPaint(Params.FALU_RED);
	        	g2.draw(new Line2D.Double(start, end));
	        }
        }
        
 
    }    
    
	public void setGraph(Graph graph) {
		this.graph = graph;
	}
	
	public void setGeneration(int generation) {
		this.generation = generation;
	}
	
	public void setStagnation(int stagnation) {
		this.stagnation = stagnation;
	}
    
    public Cell[] generateCells() {
        Cell[] cells = new Cell[cellCount];
        
        /* Coordinates. */
        double theta = 0.0;
    	for (int i = 0; i < cellCount; i++) {
    		cells[i] = new Cell(w, h, i);
    		if(Params.GRAPH_DRAWING_RND)
    			theta = cells[i].generateCoords(theta);
    		else
    			cells[i].createCoords();
    	}

    	/* Colours. */
    	for (int i = 0; i < cellCount; i++)
    		cells[i].generateColour();
    	
    	return cells;
    }
    
    public void reloadColours() {
       	for (int i = 0; i < cellCount; i++)
    		cells[i].generateColour();
    }
    
    class Cell {   	
    	public int x, y, w, h, cellId;
    	public int colour;
    	
    	public Cell(int w, int h, int cellId) {
    		this.w = w;
    		this.h = h;
    		this.cellId = cellId;
    	}
    	
    	public void generateColour() {
            colour = graph.getNode(cellId).getColour();
    	}
    	
    	public void createCoords() {
    		double radius = ((centre.x - PAD) * (((cellId) / (double)(logN * logN + logN))));
    		double theta = (DOUBLE_PI / logN) * ((cellId+1) % logN) + (HALF_PI * ((cellId+1) / (double)logN));
    		x = centre.x + (int)(radius * Math.cos(theta));
    		y = centre.y - (int)(radius * Math.sin(theta));
    	}
    	
    	public double generateCoords(double prevTheta) {
    		Random random = new Random(System.nanoTime());
    		double radius = (centre.x - PAD) - (random.nextFloat() * (centre.x * Params.RAD_FUZZINESS));
    		double theta = (prevTheta + random.nextFloat() * QUARTER_PI) + OCTANT_PI;
    		double factor = (theta < DOUBLE_PI ? 1.0 : Math.ceil(theta / DOUBLE_PI));
    		if(factor != 1.0)
    			factor = 1.0 - (0.1 * factor) + 0.1;
    		x = centre.x + (int)(radius * factor * Math.cos(theta));
    		y = centre.y - (int)(radius * factor * Math.sin(theta));
    		return theta;
    	}
    }
}
