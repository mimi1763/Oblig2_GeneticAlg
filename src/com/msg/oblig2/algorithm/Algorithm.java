package com.msg.oblig2.algorithm;

import com.msg.oblig2.interfaces.Params;

public abstract class Algorithm<T> implements Params {
	
	protected int maxIterations;
	
	/**
	 * Method for processing given object type T array,
	 * returning new object type T.
	 * 
	 * @param generic object T
	 * @return new generic object T
	 */
	public abstract T process(T object);
	
	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}
}
