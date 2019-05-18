package com.swt;

import java.util.List;

import com.digraph.Digraph;

public class DiGraphPoint {
	
	Digraph g;
	List<PointG> lp;
	int[] vr;
	int[] wr;
	
	public DiGraphPoint(Digraph g, List<PointG> lp, int[] vr, int[] wr){
		this.g= g;
		this.lp = lp;
		this.vr = vr;
		this.wr = wr;
	}

	public Digraph getG() {
		return g;
	}

	public void setG(Digraph g) {
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
