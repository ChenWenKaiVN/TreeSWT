package com.swt;

import java.util.List;

import com.graph.Graph;

public class GraphPoint {
	
	Graph g;
	List<PointG> lp;
	int[] vr;
	int[] wr;
	
	public GraphPoint(Graph g, List<PointG> lp, int[] vr, int[] wr){
		this.g= g;
		this.lp = lp;
		this.vr = vr;
		this.wr = wr;
	}

	public Graph getG() {
		return g;
	}

	public void setG(Graph g) {
		this.g = g;
	}

	public List<PointG> getLp() {
		return lp;
	}

	public void setLp(List<PointG> lp) {
		this.lp = lp;
	}

	public int[] getVr() {
		return vr;
	}

	public void setVr(int[] vr) {
		this.vr = vr;
	}

	public int[] getWr() {
		return wr;
	}

	public void setWr(int[] wr) {
		this.wr = wr;
	}

}
