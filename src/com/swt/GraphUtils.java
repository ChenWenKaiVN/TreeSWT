package com.swt;

import java.util.ArrayList;
import java.util.List;

import com.digraph.Digraph;
import com.graph.Graph;

public class GraphUtils {
	
	public static GraphPoint getGraphPointG1(){
		int V = 13;
		int E = 14;
		int[] vr = new int[]{0,0,0,0,1,3,4,4,6,7,8,9,9,10};
		int[] wr = new int[]{1,2,5,6,3,5,5,6,7,8,10,10,11,12};
		Graph g = new Graph(V, E, vr, wr);
		List<PointG> lp = new ArrayList<>();
		for(int i=0; i<g.V(); i++){
			lp.add(new PointG(0,0));
			lp.add(new PointG(1,1));
			lp.add(new PointG(2,1));
			lp.add(new PointG(1,2));
			lp.add(new PointG(2,3));
			lp.add(new PointG(0,3));
			lp.add(new PointG(3,0));
			lp.add(new PointG(4,0));
			lp.add(new PointG(5,0));
			lp.add(new PointG(4,2));
			lp.add(new PointG(5,2));
			lp.add(new PointG(4,3));
			lp.add(new PointG(5,3));
		}	
		return new GraphPoint(g, lp, vr, wr);
	}
	
	public static GraphPoint getGraphPointG2(){
		int V = 6;
		int E = 8;
		int[] vr = new int[]{0,2,2,1,0,3,3,0};
		int[] wr = new int[]{5,4,3,2,1,4,5,2};
		Graph g = new Graph(V, E, vr, wr);
		List<PointG> lp = new ArrayList<>();
		for(int i=0; i<g.V(); i++){
			lp.add(new PointG(0,0));
			lp.add(new PointG(1,1));
			lp.add(new PointG(3,0));
			lp.add(new PointG(1,2));
			lp.add(new PointG(3,3));
			lp.add(new PointG(0,3));
		}	
		return new GraphPoint(g, lp, vr, wr);
	}
	
	public static GraphPoint getGraphPointG3(){
		int V = 13;
		int E = 13;
		int[] vr = new int[]{0,4,0,9,6,5,0,11,9,0,7,9,3};
		int[] wr = new int[]{5,3,1,12,4,4,2,12,10,6,8,11,3};
		Graph g = new Graph(V, E, vr, wr); 
		List<PointG> lp = new ArrayList<>();
		for(int i=0; i<g.V(); i++){
			lp.add(new PointG(0,0));
			lp.add(new PointG(1,1));
			lp.add(new PointG(2,1));
			lp.add(new PointG(1,2));
			lp.add(new PointG(2,3));
			lp.add(new PointG(0,3));
			lp.add(new PointG(3,0));
			lp.add(new PointG(4,0));
			lp.add(new PointG(5,0));
			lp.add(new PointG(4,2));
			lp.add(new PointG(5,2));
			lp.add(new PointG(4,3));
			lp.add(new PointG(5,3));
		}	
		return new GraphPoint(g, lp, vr, wr);
	}
	
	public static DiGraphPoint getDiGraphPointG1(){
		int V = 13;
		int E = 22;
		int[] wr = {2,3,2,0,1,0,12,9,10,11,9,12,4,3,5,8,7,4,5,4,9,6};
		int[] vr = {4,2,3,6,0,2,11,12,9,9,8,10,11,4,3,7,8,5,0,6,6,7};
		Digraph g = new Digraph(V, E, wr, vr);
		List<PointG> lp = new ArrayList<>();
		for(int i=0; i<g.V(); i++){
			lp.add(new PointG(0,0));
			lp.add(new PointG(1,1));
			lp.add(new PointG(2,1));
			lp.add(new PointG(1,2));
			lp.add(new PointG(2,3));
			lp.add(new PointG(0,3));
			lp.add(new PointG(3,0));
			lp.add(new PointG(4,0));
			lp.add(new PointG(5,0));
			lp.add(new PointG(4,2));
			lp.add(new PointG(5,2));
			lp.add(new PointG(4,3));
			lp.add(new PointG(5,3));
		}	
		return new DiGraphPoint(g, lp, vr, wr);
	}
	
	public static void main(String[] args) {
		System.out.println(Math.atan(10/10));
	}
	

}
