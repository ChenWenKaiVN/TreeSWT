package com.swt;

import java.util.ArrayList;
import java.util.List;

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

}
