package com.graph;

import java.util.Arrays;

//无向图  深度优先搜索  所有连通分量
public class CC {
	
	private boolean[] marked;
	//将每个节点所在连通分量的编号记录下来
	private int[] id;
	//连通分量总数
	private int count;
	
	public CC(Graph g){
		marked = new boolean[g.V()];
		id = new int[g.V()];
		for(int s=0; s<g.V(); s++){
			if(!marked[s]){
				dfs(g, s);
				count++;
			}
		}
	}
	
	private void dfs(Graph g, int s) {
		marked[s] = true;
		id[s] = count;
		for(int v : g.adj(s)){
			if(!marked[v]){
				dfs(g, v);
			}
		}
	}

	public boolean connected(int v, int w){
		return id[v] == id[w];
	}
	
	public int count(){
		return count;
	}
	
	public int id(int v){
		return id[v];
	}
	
	
	public static void main(String[] args) {
		int V = 13;
		int E = 13;
		int[] vr = new int[]{0,4,0,9,6,5,0,11,9,0,7,9,3};
		int[] wr = new int[]{5,3,1,12,4,4,2,12,10,6,8,11,3};
		Graph g = new Graph(V, E, vr, wr); 
		System.out.println(g.toString());
		CC cc = new CC(g);
		System.out.println(Arrays.toString(cc.id));
	}

}
