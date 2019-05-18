package com.graph;

import com.swt.GraphPoint;
import com.swt.GraphUtils;

//无向图  深度优先搜索  是否有环
public class Cycle {
	
	private boolean[] marked;
	
	private boolean hasCycle = false;
	
	public Cycle(Graph g){
		marked = new boolean[g.V()];
		//遍历每一个节点
		for(int s=0; s<g.V(); s++){
			//如果该节点没有被标记过，则进行深度优先搜索
			if(!marked[s]){
				dfs(g, s, s);
			}
		}
	}
	
	public boolean hasCycle(){
		return hasCycle;
	}

	private void dfs(Graph g, int s, int u) {
		marked[s] = true;
		for(int v : g.adj(s)){
			System.out.println(s + " " + u + " " + v + " " + marked[v]);
			//搜索节点s的所有临接节点v 如果v没有标记过，继续dfs
			if(!marked[v]){
				dfs(g, v, s);
			}
			//u保存访问v的上一个节点
			//如果已经标记过,如果此时v==u 说明v直接相连于u,即u-->v 此时并没有环
			else if(v != u){
				hasCycle = true;
				System.out.println("end");
				return;
			}
		}
	}
	
	public static void main(String[] args) {
//		int V = 13;
//		int E = 13;
//		int[] vr = new int[]{0,4,0,9,6,5,0,11,9,0,7,9,3};
//		int[] wr = new int[]{5,3,1,12,4,4,2,12,10,6,8,11,3};
//		Graph g = new Graph(V, E, vr, wr); 
		GraphPoint gp = GraphUtils.getGraphPointG2();
		Graph g = gp.getG();
		System.out.println(g.toString());
		Cycle c = new Cycle(g);
		System.out.println(c.hasCycle());
	}
}
