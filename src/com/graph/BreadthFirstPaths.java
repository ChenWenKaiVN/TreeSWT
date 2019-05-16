package com.graph;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

//广度优先搜索  无向图  单点最短路径
public class BreadthFirstPaths {
	
	private boolean[] marked;
	private int[] edgeTo;
	private final int s;
	
	public BreadthFirstPaths(Graph g, int s){
		marked = new boolean[g.V()];
		edgeTo = new int[g.V()];
		this.s = s;
		bfs(g, s);
	}

	private void bfs(Graph g, int s) {
		Queue<Integer> q = new LinkedList<>();
		marked[s] = true;
		q.add(s);
		while(!q.isEmpty()){
			int v = q.poll();
			for(int w : g.adj(v)){
				if(!marked[w]){
					edgeTo[w] = v;
					marked[w] = true;
					q.add(w);
				}
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
		int V = 6;
		int E = 8;
		int[] vr = new int[]{0,2,2,1,0,3,3,0};
		int[] wr = new int[]{5,4,3,2,1,4,5,2};
		Graph g = new Graph(V, E, vr, wr);
		System.out.println(g.toString());
		BreadthFirstPaths bfp = new BreadthFirstPaths(g, 0);
		System.out.println();
		System.out.println(bfp.hasPathTo(4));
		System.out.println(bfp.pathTo(4));
		for(int i=0; i<bfp.edgeTo.length; i++){
			System.out.println(i + " " + bfp.edgeTo[i]);
		}
	}
	
	/*
	 * 图搜索算法思路
	 * 将起点加入数据结构中，重复一下步骤直到数据结构为空
	 * 	      取出数据结构中的数据，标记数据
	 *    将与该数据所有相邻未标记节点加入到数据结构中
	 * 深度优先数据结构使用栈  每次取出最近的节点
	 * 广度优先使用队列，每次取出最远的节点
	 * */

}
