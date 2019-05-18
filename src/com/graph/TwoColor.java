package com.graph;

//无向图  深度优先搜索  是否具有二分性
public class TwoColor {
	
	private boolean[] marked;
	
	private boolean[] color;
	
	private boolean isTwoColorable = true;
	
	public TwoColor(Graph g){
		marked = new boolean[g.V()];
	    color = new boolean[g.V()];
		//遍历每一个节点
		for(int s=0; s<g.V(); s++){
			//如果该节点没有被标记过，则进行深度优先搜索
			if(!marked[s]){
				dfs(g, s);
			}
		}
	}
	
	public boolean isBipartite(){
		return isTwoColorable;
	}

	private void dfs(Graph g, int s) {
		marked[s] = true;
		for(int v : g.adj(s)){
			//此时访问s的临接顶点v
			if(!marked[v]){
				//将v颜色设置为与s相反
				color[v] = !color[s];
				dfs(g, v);
			}
			//如果已经标记过，并且颜色相同，则说明图不具有二分性
			else if(color[v]==color[s]){
				isTwoColorable = false;
				return;
			}
		}
	}
	
	public static void main(String[] args) {
		
		int V = 13;
		int E = 14;
		int[] vr = new int[]{0,0,0,0,1,3,4,4,6,7,8,9,9,10};
		int[] wr = new int[]{1,2,5,6,3,5,5,6,7,8,10,10,11,12};
//		int V = 13;
//		int E = 13;
//		int[] vr = new int[]{0,4,0,9,6,5,0,11,9,0,7,9,3};
//		int[] wr = new int[]{5,3,1,12,4,4,2,12,10,6,8,11,3};
		
//		int V = 6;
//		int E = 8;
//		int[] vr = new int[]{0,2,2,1,0,3,3,0};
//		int[] wr = new int[]{5,4,3,2,1,4,5,2};
		Graph g = new Graph(V, E, vr, wr); 
		System.out.println(g.toString());
		TwoColor t = new TwoColor(g);
		System.out.println(t.isBipartite());
	}
	

}
