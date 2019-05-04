package com.tree.ttft;

public class TwoThreeFourTree<Key extends Comparable<Key>, Value> {
	
	class Node{
		//2 2节点     使用kl  vl  left right
		//3 3节点     kl vl km vm  left m1  m2
		//4 4节点     kl vl km vm kr vr left m1 m2 right
		int type;
		Key kl;
		Value vl;
		Key km;
		Value vm;
		Key kr;
		Value vr;
		Node left,m1,m2,right;
		
		boolean is2Node() {return type==2?true:false;}
		
		boolean is3Node() {return type==3?true:false;}
		
		boolean is4Node() {return type==4?true:false;}
		
		public Node getTheCorretChild(Key key) {
			if(is2Node()){
				int cmp = key.compareTo(kl);
				if(cmp==0) return this;
				else if(cmp<0) return left;
				else return right;
			}
			if(is3Node()){
				int c1 = key.compareTo(kl);
				int c2 = key.compareTo(km);
				if(c1==0||c2==0) return this;
				else if(c1<0&&c2<0) return left;
				else if(c1>0&&c2>0) return m2;
				else return m1;
			}
			if(is4Node()){
				int c1 = key.compareTo(kl);
				int c2 = key.compareTo(km);
				int c3 = key.compareTo(kr);
				if(c1==0||c2==0||c3==0) return this;
				else if(c1<0&&c2<0&&c3<0) return left;
				else if(c1>0&&c2<0&&c3<0) return m1;
				else if(c1>0&&c2>0&&c3<0) return m2;
				else return right;
			}
			return null;
		}
		public void split() {
			
			
		}
		public void make3Node(Key key, Value val) {
			
			
		}
		public void make4Node(Key key, Value val) {
			
			
		}
	}
	
	private Node root;
	
	public Node get(Key key){
		if(root==null) return null;
		return get(root, key);
	}
	
	private Node get(Node x, Key key){
		if(x==null) return null;
		if(x.is2Node()){
			int cmp = key.compareTo(x.kl);
			if(cmp==0) return x;
			else if(cmp<0) return get(x.left, key);
			else return get(x.right, key);
		}
		if(x.is3Node()){
			int c1 = key.compareTo(x.kl);
			int c2 = key.compareTo(x.km);
			if(c1==0||c2==0) return x;
			else if(c1<0&&c2<0) return get(x.left, key);
			else if(c1>0&&c2>0) return get(x.m2, key);
			else return get(x.m1, key);
		}
		if(x.is4Node()){
			int c1 = key.compareTo(x.kl);
			int c2 = key.compareTo(x.km);
			int c3 = key.compareTo(x.kr);
			if(c1==0||c2==0||c3==0) return x;
			else if(c1<0&&c2<0&&c3<0) return get(x.left, key);
			else if(c1>0&&c2<0&&c3<0) return get(x.m1, key);
			else if(c1>0&&c2>0&&c3<0) return get(x.m2, key);
			else return get(x.right, key);
		}
		return null;
	}
		
	public void put(Key key, Value val){
		Node x = root;
		while(x.getTheCorretChild(key)!=null){
			x = x.getTheCorretChild(key);
			if(x.is4Node()) x.split();
		}
		if(x.is2Node()) x.make3Node(key, val);
		else if(x.is3Node()) x.make4Node(key, val);
	}

	

}
