package com.graph;

import java.util.Arrays;
import java.util.Stack;

//无向图  深度优先搜索  单点路径
public class DepthFirstPaths {
	
	private boolean[] marked;
	private int[] edgeTo;
	private final int s;
	
	public DepthFirstPaths(Graph g, int s){
		this.marked = new boolean[g.V()];
		this.edgeTo = new int[g.V()];
		this.s = s;
		dfs(g, s);
	}
	
	private void dfs(Graph g, int v) {
		marked[v] = true;
		for(int w : g.adj(v)){
			if(!marked[w]){
				//向v下一个指向w
				//edgeTo[x]得到谁指向x
				edgeTo[w] = v;
				dfs(g, w);
			}
		}
	}

	//起点s到节点v是否存在单点路径
	public boolean hasPathTo(int v){
		return marked[v];
	}
	
	//返回s-->v路径
	public Iterable<Integer> pathTo(int v){
		if(!hasPathTo(v)) return null;
		//利用栈倒序存储
		Stack<Integer> s = new Stack<>();
		//由最终节点向起始节点寻找  edgeTo[x]表示谁指向x
		for(int x=v; x!=this.s; x = edgeTo[x]){
			s.push(x);
		}
		s.push(this.s);
		return s;
	}
	
	public static void main(String[] args) {
//		int V = 13;
//		int E = 13;
//		int[] vr = new int[]{0,4,0,9,6,5,0,11,9,0,7,9,5};
//		int[] wr = new int[]{5,3,1,12,4,4,2,12,10,6,8,11,3};
		int V = 6;
		int E = 8;
		int[] vr = new int[]{0,2,2,1,0,3,3,0};
		int[] wr = new int[]{5,4,3,2,1,4,5,2};
		Graph g = new Graph(V, E, vr, wr);
		System.out.println(g.toString());
		DepthFirstPaths dfp = new DepthFirstPaths(g, 0);
		for(int i=0; i<dfp.edgeTo.length; i++){
			System.out.println(i + " " + dfp.edgeTo[i]);
		}
		System.out.println(Arrays.toString(dfp.marked));
		System.out.println(dfp.hasPathTo(5));
		System.out.println(dfp.hasPathTo(3));
		System.out.println(dfp.pathTo(4));
	}

}
