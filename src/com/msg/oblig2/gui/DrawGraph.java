package com.msg.oblig2.gui;

import java.awt.Color;
//import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Random;

import javax.swing.JPanel;

import com.msg.oblig2.csp.Edge;
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
	private final int PAD = 20;
    
	private int cellCount;
    private Cell[] cells;
    private int w, h;
    private Point centre;
    private Graph graph;
	private int generation = 0;
	private int stagnation = 0;
    
    public DrawGraph(Graph graph, int w, int h) {
    	this.w = w;
    	this.h = h;
    	centre = new Point(w >> 1, h >> 1);
    	centre.translate(-PAD, -PAD);
    	this.graph = graph;
    	cellCount = graph.getSize();
    	cells = generateCells();
    	setSize(w, h);
        setBackground(Color.DARK_GRAY);
        repaint();
    }
 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        /* Background gradient. */
//        double brightness = 0.3;
//    	Color color1 = new Color((int)(32*brightness), (int)(231*brightness), (int)(210*brightness));
//        Color color2 = new Color((int)(12*brightness), (int)(127*brightness), (int)(64*brightness));
//        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
//        g2.setPaint(gp);
        g2.setPaint(Color.DARK_GRAY);
        g2.fillRect(0, 0, w, h);

        /* Draw cells. */
        for(int i = 0; i < cellCount; i++) {
        	if(Params.SHOW_NODES) {
	            g2.setPaint(cells[i].colour);
	            g2.fill(new Ellipse2D.Double(cells[i].x, cells[i].y, 
	            		Params.CELL_SIZE, Params.CELL_SIZE));
        	}
            /* Draw edge count text. */
            if(Params.SHOW_NEIGHBOURS) {
	            g2.setPaint(Color.LIGHT_GRAY);
	            g2.drawString(String.valueOf(cells[i].edges.length), cells[i].x, cells[i].y);
            }
            /* Draw similar neighbour count text. */
            if(Params.SHOW_SIMILAR) {
	            g2.setPaint(Color.RED);
	            g2.drawString(String.valueOf(graph.getSimilarCount(cells[i].cellId)), 
	            		cells[i].x + Params.CELL_SIZE, cells[i].y + Params.CELL_SIZE);
            }           
            /* Draw fitness, generations and stagnations text. */
            g2.setPaint(Color.LIGHT_GRAY);
            g2.drawString("Best fitness: " + graph.getFitness() + 
            		"            Generation #: " + generation + 
            		"            Stagnations: " + stagnation + "  of  " + 
            		Params.NUMBER_OF_GENERATIONS, 10, 10);
        }
        
        /* Draw edges. */
        if(Params.SHOW_EDGES) {
	        g2.setPaint(Color.GRAY);
	        for(int i = 0; i < cellCount; i++) {
		        for (int e = 0; e < cells[i].edges.length; e++) {
		        	Point start = new Point(cells[cells[i].edges[e].getNode1()].x, 
		        			cells[cells[i].edges[e].getNode1()].y);
		        	Point end = new Point(cells[cells[i].edges[e].getNode2()].x, 
		        			cells[cells[i].edges[e].getNode2()].y);
		        	/* Start and end edge lines from and to the centre of cells. */
		        	start.translate(Params.CELL_SIZE >> 1, Params.CELL_SIZE >> 1);
		        	end.translate(Params.CELL_SIZE >> 1, Params.CELL_SIZE >> 1);
		        	g2.draw(new Line2D.Double(start, end));
	//	        	drawArrowHead(g2, end, start, Color.GRAY);
		        }
	        }
        }
    }    

//	private void drawArrowHead(Graphics2D g2, Point tip, Point tail, Color color) {  
//	    g2.setPaint(color);
//	    double dy = tip.y - tail.y;  
//	    double dx = tip.x - tail.x;  
//	    double theta = Math.atan2(dy, dx);  
//	    double x, y, rho = theta + PHI;  
//	    for(int j = 0; j < 2; j++) {  
//	        x = tip.x - Params.CELL_SIZE * Math.cos(rho);  
//	        y = tip.y - Params.CELL_SIZE * Math.sin(rho);  
//	        g2.draw(new Line2D.Double(tip.x, tip.y, x, y));  
//	        rho = theta - PHI;  
//	    }  
//	}
    
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
//        cells[0] = new Cell(w, h, 0);
//        cells[0].x = centre.x;
//        cells[0].y = centre.y;
        /* Coordinates. */
        double theta = 0.0;
    	for (int i = 0; i < cellCount; i++) {
    		cells[i] = new Cell(w, h, i);
    		theta = cells[i].generateCoords(theta);
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
    	public Edge[] edges;
    	public int x, y, w, h, cellId;
    	public Color colour;
    	
    	public Cell(int w, int h, int cellId) {
    		this.w = w;
    		this.h = h;
    		this.cellId = cellId;
    		edges = graph.getNodeEdges(cellId);
    	}
    	
    	public void generateColour() {
            colour = graph.getNode(cellId).getColour();
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
