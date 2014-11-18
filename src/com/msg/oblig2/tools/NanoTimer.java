package com.msg.oblig2.tools;

public class NanoTimer {
	private long startNanoTime, elapsedNanoTime;
	
	public NanoTimer() {
		startTimer();
		stopTimer();
	}
	
	public void startTimer() {
		startNanoTime = System.nanoTime();
	}
	
	public void stopTimer() {
		elapsedNanoTime = System.nanoTime() - startNanoTime;
	}
	
	public long getElapsedTime() {
		return elapsedNanoTime;
	}
	
	public double getElapsedMins() {
		return (double)Math.round(((System.nanoTime() - startNanoTime) / 
				60000000000.0) * 1000) / 1000;
	}
	
	public void printElapsedTime() {
		System.out.println(this);
	}
	
	public void printElapsedTime(String addOn) {
		System.out.println(this + addOn);
	}
	
	@Override
	public String toString() {
		return String.valueOf((double)Math.round((elapsedNanoTime / 
				1000000000.0) * 1000) / 1000) + " seconds";
	}
}
