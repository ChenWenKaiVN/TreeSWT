package com.digraph;

import java.util.Iterator;

import com.bag.Bag;

//有向图数据结构表示
public class Digraph {
	
	private final int V;
	private int E;
	private Bag<Integer>[] adj;
	
	//创建一个只含有顶点的有向图
	public Digraph(int V){
		this.V = V;
		this.E = 0;
		adj = new Bag[V];
		for(int i=0; i<V; i++){
			adj[i] = new Bag<Integer>();
		}
	}
	
	//创建有向图  wr起点  vr终点
	public Digraph(int V, int E, int[] wr, int[] vr){
		this(V);
		for(int i=0; i<E; i++){
			addEdge(wr[i], vr[i]);
		}
	}

	//添加一条有向边  w-->v
	private void addEdge(int w, int v) {
		adj[w].add(v);
		E++;
	}
	
	public int V(){
		return this.V;
	}
	
	public int E(){
		return this.E;
	}
	
	//返回某个顶点指出的顶点连接
	public Iterable<Integer> adj(int v){
		return adj[v];
	}
	
	//有向图反转
	public Digraph reverse(){
		Digraph R = new Digraph(V);
		for(int v=0; v<E; v++){
			for(int w : adj[v]){
				R.addEdge(w, v);
			}	
		}
		return R;
	}
	
	public String toString(){
		String s = V + " vertices, " + E + " edges\n";
		for(int v =0; v<V; v++){
			s += v + ": ";
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
		int V = 13;
		int E = 22;
		int[] wr = {4,2,3,6,0,2,11,12,9,9,8,10,11,4,3,7,8,5,0,6,6,7};
		int[] vr = {2,3,2,0,1,0,12,9,10,11,9,12,4,3,5,8,7,4,5,4,9,6};
		Digraph d = new Digraph(V, E, wr, vr);
		System.out.println(d.toString());
	}

}
