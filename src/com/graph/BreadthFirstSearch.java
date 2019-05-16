package com.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import com.swt.GraphUtils;

public class BreadthFirstSearch {
	
	private boolean[] marked;
	private List<Integer> arr = new ArrayList<>();
	
	public BreadthFirstSearch(Graph g, int s){
		marked = new boolean[g.V()];
		bfs(g, s);
	}

	private void bfs(Graph g, int s) {
		Queue<Integer> q = new LinkedList<>();
		marked[s] = true;
		q.add(s);
		while(!q.isEmpty()){
			int v = q.poll();
			arr.add(v);
			for(int w : g.adj(v)){
				if(!marked[w]){
					marked[w] = true;
					q.add(w);
				}
			}
		}	
	}
	
	public List<Integer> getBFSVOrder(){
		return this.arr;
	}
	
	public static void main(String[] args) {
		Graph g = GraphUtils.getGraphPointG1().getG();
		System.out.println(g.toString());
		BreadthFirstSearch bfs = new BreadthFirstSearch(g, 0);
		System.out.println(bfs.arr);
	}

}
