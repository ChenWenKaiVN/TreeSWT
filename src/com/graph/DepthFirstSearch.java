package com.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.swt.GraphUtils;

//深度优先搜索  遍历图  单点连通性
public class DepthFirstSearch {
	
	private boolean[] marked;
	private int count;
	
	//搜索顶点的顺序
	List<Integer> arr = new ArrayList<>();
	
	public DepthFirstSearch(Graph g, int s){
		marked = new boolean[g.V()];
		dfs(g, s);
	}

	private void dfs(Graph g, int s) {
		arr.add(s);
		marked[s] = true;
		count++;
		for(int v : g.adj(s)){
			if(!marked(v))
				dfs(g, v);
		}
	}
	
	public boolean marked(int w){
		return marked[w];
	}
	
	public int count(){
		return count;
	}
	
	public List<Integer> getDFSVOrder(){
		return this.arr;
	}
	
	public static void main(String[] args) {
//		int V = 6;
//		int E = 8;
//		int[] vr = new int[]{0,2,2,1,0,3,3,0};
//		int[] wr = new int[]{5,4,3,2,1,4,5,2};
//		Graph g = new Graph(V, E, vr, wr);
//		System.out.println(g.toString());
		Graph g = GraphUtils.getGraphPointG1().getG();
		System.out.println(g.toString());
		DepthFirstSearch dfs = new DepthFirstSearch(g, 0);
		System.out.println();
		System.out.println(dfs.count());
		System.out.println(dfs.arr);
	}

}
