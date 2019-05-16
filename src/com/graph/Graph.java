package com.graph;

import java.util.Iterator;

import com.bag.Bag;

//图数据结构表示
//利用SWT来表示一张图
//顶点  边  度数   连通图
public class Graph {
	
	//顶点数
	private final int V;
	//边数
	private int E;
	//邻接表
	private Bag<Integer>[] adj;
	
	//创建一个只含有顶点的图
	public Graph(int V){
		this.V = V;
		this.E = 0;
		this.adj = new Bag[V];
		for(int i=0; i<V; i++){
			adj[i] = new Bag<Integer>();
		}
	}
	
	public Graph(int V, int E, int[] vr, int[] wr){
		this(V);
		//this.E = E;
		for(int i=0; i<E; i++){
			addEdge(vr[i], wr[i]);
		}	
	}
	
	public int V(){
		return V;
	}
	
	public int E(){
		return E;
	}
	
	//添加一条边
	public void addEdge(int v, int w){
		adj[v].add(w);
		adj[w].add(v);
		E++;
	}
	
	//某一个节点的相邻节点
	public Iterable<Integer> adj(int v){
		return adj[v];
	}
	
	//某一个顶点度数
	public int degree(int v){
		return adj[v].size();
	}
	
	//所有顶点最大度数
	public int maxDegree(){
		int max = 0;
		for(int v=0; v<V; v++){
			if(adj[v].size()>max)
				max = adj[v].size();
		}
		return max;
	}
	
	public String toString(){
		String s = V + " vertices, " + E + " edges\n";
		for(int v =0; v<V; v++){
			s += v + ": ";
//			for(int w : this.adj(v)){
//				s += w + "--->";
//			}
			Iterator<Integer> it = this.adj(v).iterator();
			while(it.hasNext()){
				int w = it.next();
				if(it.hasNext())
					s += w + "--->";
				else
					s += w;
			}
			s += "\n";
		}
		return s;
	}
	
	public static void main(String[] args) {
//		int V = 13;
//		int E = 13;
//		int[] vr = new int[]{0,4,0,9,6,5,0,11,9,0,7,9,3};
//		int[] wr = new int[]{5,3,1,12,4,4,2,12,10,6,8,11,3};
//		Graph g = new Graph(V, E, vr, wr);
		
		int V = 13;
		int E = 14;
		int[] vr = new int[]{0,0,0,0,1,3,4,4,6,7,8,9,9,10};
		int[] wr = new int[]{1,2,5,6,3,5,5,6,7,8,10,10,11,12};
		Graph g = new Graph(V, E, vr, wr);
		System.out.println(g.toString());
		//System.out.println(g.degree(0));
		//System.out.println(g.maxDegree());
		DepthFirstSearch dfs = new DepthFirstSearch(g, 0);
	}
}
