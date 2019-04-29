package com.tree.bst;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

//二叉查找�?
public class BST<Key extends Comparable<Key>, Value> {
	
	public class Node{
		private Key key;
		private Value value;
		private Node left, right;
		private int N;		
		public Node(Key key, Value value, int N){
			this.key = key;
			this.value = value;
			this.N = N;
		}
		public Key getKey(){
			return this.key;
		}
		public Node getLeft(){
			return this.left;
		}
		public Node getRight(){
			return this.right;
		}
		@Override
		public String toString() {
			return "Node [key=" + key + "]";
		}
	}
	
	//带坐标的节点
	public class NodeCoordinate{
		Node node;
		int x;
		int y;
		public NodeCoordinate(Node n, int x, int y){
			this.node = n;
			this.x = x;
			this.y = y;
		}
		public Node getNode(){
			return this.node;
		}
		
		public int getX(){
			return this.x;
		}
		
		public int getY(){
			return this.y;
		}
	}
	
	private Node root;
	
	public int size(){
		return size(root);
	}
	
	private int size(Node x){
		if(x==null) return 0;
		return size(x.left) + size(x.right) + 1;
	}
	
	public Value get(Key key){
		return get(root, key);
	}
	
	private Value get(Node x, Key key){
		if(x == null) return null;
		int cmp = key.compareTo(x.key);
		if(cmp>0)
			return get(x.right, key);
		else if(cmp<0)
			return get(x.left, key);
		else
			return x.value;
	}
	
	public void put(Key key, Value val){
		root = put(root, key, val);
	}
	
	private Node put(Node x, Key key, Value val) {
		if(x == null)
			return new Node(key, val, 1);
		int cmp = key.compareTo(x.key);
		if(cmp>0)
			x.right = put(x.right, key, val);
		else if(cmp<0)
			x.left = put(x.left, key, val);
		else
			x.value = val;
		x.N = size(x.left) + size(x.right) + 1;
		return x;		
	}

	public Node max(){
		return max(root);
	}
	
	private Node max(Node x){
		if(x == null) 
			return null;
		if(x.right==null)
			return x;
		return max(x.right);
	}
	
	public Node min(){
		return min(root);
	}
	
	private Node min(Node x){
		if(x == null)
			return null;
		if(x.left == null)
			return x;
		return min(x.left);
	}
	
	//小于等于key的最大�??
	public Node floor(Key key){
		if(root == null) 
			return null;
		return floor(root, key);
	}
	
	private Node floor(Node x, Key key){
		if(x==null)
			return null;
		int cmp = key.compareTo(x.key);
		if(cmp==0)
			return x;
		if(cmp<0)
			return floor(x.left, key);
		Node r = floor(x.right, key);
		if(r!=null) return r;
		else return x;						
	}
	
	//大于等于key的最小�??
	public Node ceiling(Key key){
		if(root==null) return null;
		return ceiling(root, key);
	}
	
	private Node ceiling(Node x, Key key){
		if(x==null) return null;
		int cmp = key.compareTo(x.key);
		if(cmp==0) return x;
		if(cmp>0) return ceiling(x.right, key);
		Node l = ceiling(x.left, key);
		if(l!=null) return l;
		else return x;
	}
	
	//查找排名为k的节�?
	public Node select(int k){
		if(root==null) return null;
		return select(root, k);
	}
	
	private Node select(Node x, int k){
		if(x==null) return null;
		int leftnum = size(x.left);
		if(leftnum==k-1) return x;
		if(leftnum>k-1) return select(x.left, k);
		return select(x.right, k-1-leftnum);
	}
	
	//返回该key对应的排�?
	public int rank(Key key){
		if(root==null) return -1;
		return rank(root, key);
	}
	
	private int rank(Node x, Key key){
		if(x==null) return -1;
		int leftnum = size(x.left);
		int cmp = key.compareTo(x.key);
		if(cmp==0) return leftnum+1;
		if(cmp<0) return rank(x.left, key);
		return leftnum + 1 + rank(x.right, key);
	}
	
	public void delete(Key key){
		if(root==null) return;
		root = delete(root, key);
	}
	
	private Node delete(Node x, Key key){
		if(x==null) return null;
		int cmp = key.compareTo(x.key);
		if(cmp<0) x.left = delete(x.left, key);
		if(cmp>0) x.right = delete(x.right, key);
		if(x.left==null) return x.right;
		if(x.right==null) return x.left;
		Node t = x;
		x = min(t.right);
		x.right = deleteMin(t.right);
		x.left = t.left;
		x.N = size(x.left) + size(x.right) + 1;
		return x;
	}
	
	public void deleteMin(){
		root = deleteMin(root);
	}
	
	//�?直往左子树寻找，直到某个节点的左子树为空，将该节点的右子树指向指向该节点的左连接
	private Node deleteMin(Node x){
		if(x==null) return null;
		if(x.left==null) return x.right;
		x.left = deleteMin(x.left);
		x.N = size(x.left) + size(x.right) + 1;
		return x;
	}
	
	public void deleteMax(){
		root = deleteMax(root);
	}
	
	private Node deleteMax(Node x){
		if(x==null) return null;
		if(x.right==null) return x.left;
		x.right = deleteMax(x.right);
		x.N = size(x.left) + size(x.right) + 1;
		return x;
	}
	
	
	public Iterable<Key> keys(){
		return keys(min().key, max().key);
	}
	
	//返回给定范围内的key值集�?
	public Iterable<Key> keys(Key lo, Key hi){
		Queue<Key> q = new LinkedList<>();
		keys(root, q, lo, hi);
		return q;
	}
	
	private void keys(Node x, Queue<Key> q, Key lo, Key hi) {		
		if(x==null) return;
		int cmplo = lo.compareTo(x.key);
		int cmphi = hi.compareTo(x.key);
		if(cmplo<0) keys(x.right, q, lo, hi);
		if(cmplo<=0&&cmphi>=0) q.add(x.key);
		if(cmphi>0) keys(x.left, q, lo, hi);	
	}
	
	private String spaceFormat = " ";
	private String crossFormat = "-";
	//private String specialChar = "#";
	private String specialChar = "#";
	
	public void printBinaryTree(){ 
		List<List<Key>> ll = levelOrder();
		List<Key> l = null;
		int h = height();				
		for(int i=1; i<=h; i++){
			//节点�?  �?始地�?
			int lineStart = pow2(h-i+1)-2;
			//每个节点的偏移地�?
			int offset = pow2(h-i+2)-1;
			//该节点的左右节点的长度�??
			int brackets = pow2(h-i);
			//System.out.println("linenum = " + i + " start = " + lineStart + " offset = " + offset + " brackets = " + brackets);
			printFormat(lineStart, spaceFormat);
			l = ll.get(i-1);
			for(int j=0; j<l.size(); j++){
				//if(!l.get(j).equals("#")){
					System.out.print(l.get(j));
				//}	
				printFormat(offset, spaceFormat);		
			}
			//�?后一行不�?要绘�?
			if(i>=h)
				break;
			System.out.println();
			//横线部分行开始地�?
			int lineCrossStart = pow2(h-i)-2;
			printFormat(lineCrossStart, spaceFormat);
			for(int j=0; j<l.size(); j++){
				if(!l.get(j).equals(specialChar)){
					//每个元素下一行横线部分长�?
					printFormat(brackets*2+1, crossFormat);
					//横线部分 间隔  即下�?行的节点的间�?
					printFormat(pow2(h-i+2-1)-1, spaceFormat);
				}				
			}
			System.out.println();
		}		
	}
	
	public int pow2(int n){
		return (int) Math.pow(2, n);
	}

	public void printFormat(int num, String format){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<num; i++){
			sb.append(format);
		}
		System.out.print(sb.toString());
	}
	
	public int height(){
		return height(root);
	}
	
	public int height(Node x){
		if(x==null) return 0;
		if(x.left==null&&x.right==null) return 1;
		return 1+Math.max(height(x.left), height(x.right));
	}
	
	public List<List<Key>> levelOrder(){
		if(root==null) return null;
		List<List<Key>> ll = new ArrayList<>();
		Queue<Node> q = new LinkedList<>();
		q.add(root);
		int h = height();
		int depth = 1;
		while(q.size()>0){
			int s = q.size();
			//System.out.println("size = " + q.size());
			//System.out.println("q = " + q);
			List<Key> lk = new ArrayList<>();
			while(s>0){
				Node tmp = q.poll();
				lk.add(tmp.key);
				if(tmp.left!=null){
					q.add(tmp.left);
				}else{
					q.add(new Node((Key)specialChar, (Value)specialChar, 1));
				}
				if(tmp.right!=null){
					q.add(tmp.right);
				}else{
					q.add(new Node((Key)specialChar, (Value)specialChar, 1));
				}
				s--;
			}
			ll.add(lk);
			if(depth>h-1)
				break;
			depth++;			
		}
		for(List<Key> t : ll){
			System.out.println(t);
		}
		return ll;
	}
	
	public List<List<Node>> levelOrder2(){
		if(root==null) return null;
		List<List<Node>> ll = new ArrayList<>();
		Queue<Node> q = new LinkedList<>();
		q.add(root);
		int h = height();
		int depth = 1;
		while(q.size()>0){
			int s = q.size();
			List<Node> lk = new ArrayList<>();
			while(s>0){
				Node tmp = q.poll();
				lk.add(tmp);
				if(tmp.left!=null){
					q.add(tmp.left);
				}else{
					q.add(new Node((Key)specialChar, (Value)specialChar, 1));
				}
				if(tmp.right!=null){
					q.add(tmp.right);
				}else{
					q.add(new Node((Key)specialChar, (Value)specialChar, 1));
				}
				s--;
			}
			ll.add(lk);
			if(depth>h-1)
				break;
			depth++;			
		}
//		for(List<Node> t : ll){
//			for(Node n : t){
//				System.out.print(n.key + ",");
//			}
//			System.out.println();
//		}
		return ll;
	}
	
	public List<List<NodeCoordinate>> calculateNodeCoordinate(){
		int h = height();	
		//int ymax = 2*h-1;	
		List<List<Node>> ll = levelOrder2(); 
		List<List<NodeCoordinate>> llnc = new ArrayList<>();
		for(int i=1; i<=h; i++){
			List<NodeCoordinate> lnc = new ArrayList<>();
			int lineStart = pow2(h-i+1)-2;
			int offset = pow2(h-i+2);
			int t = lineStart;
			for(int j=0; j<ll.get(i-1).size(); j++){									
				NodeCoordinate nc = new NodeCoordinate(ll.get(i-1).get(j), t, i);
				lnc.add(nc);
				t = t + offset;
			}
			llnc.add(lnc);
		}
		return llnc;	
	}
	
	
	public void printBinaryTree2(){ 
		List<List<Node>> ll = levelOrder2();
		List<Node> l = null; 
		int h = height();				
		for(int i=1; i<=h; i++){
			//节点�?  �?始地�?
			int lineStart = pow2(h-i+1)-2;
			//每个节点的偏移地�?
			int offset = pow2(h-i+2)-1;
			//该节点到左子节点或右子节点的长度
			int brackets = pow2(h-i);
			//System.out.println("linenum = " + i + " start = " + lineStart + " offset = " + offset + " brackets = " + brackets);
			printFormat(lineStart, spaceFormat);
			l = ll.get(i-1);
			for(int j=0; j<l.size(); j++){
				if(!l.get(j).key.equals(specialChar)){
					System.out.print(l.get(j).key);
				}else{
					System.out.print(" ");
				}	
				printFormat(offset, spaceFormat);		
			}
			//�?后一行不�?要绘�?
			if(i>=h)
				break;
			System.out.println();
			//横线部分行开始地�?
			int lineCrossStart = pow2(h-i)-2;
			printFormat(lineCrossStart, spaceFormat);
			for(int j=0; j<l.size(); j++){
				//System.out.println(l.get(j).key);
				if(!l.get(j).key.equals(specialChar) && (l.get(j).left!=null || l.get(j).right!=null)){
					//每个元素下一行横线部分长�?
					printFormat(brackets*2+1, crossFormat);
					//横线部分 间隔  即下�?行的节点的间�?
					printFormat(pow2(h-i+2-1)-1, spaceFormat);
				}else{
					printFormat(brackets*2+1, spaceFormat);
					printFormat(pow2(h-i+2-1)-1, spaceFormat);
				}				
			}
			System.out.println();
		}
		System.out.println();
	}
	
	//利用SWT封装个二叉树工具包来进行学习转换
	public static void main(String[] args) {
		BST<Integer, String> bst = new BST<>();	
		int[] arr = new int[]{8,6,10,5,7,9,12,2,4};
		//int[] arr = new int[]{1,2,3,4,5};
		//int[] arr = new int[]{8,6,10,5,7,9,12};
		for(int i=0; i<arr.length; i++){
			bst.put(arr[i], String.valueOf(i));
		}
		bst.printBinaryTree2();
		bst.delete(8);
		bst.printBinaryTree2();
//		System.out.println("min = "+bst.min());
//		System.out.println("max = "+bst.max());
//		System.out.println("floor = "+bst.floor(10));
//		System.out.println("ceiling = "+bst.ceiling(5));
//		System.out.println("select = "+bst.select(4));
//		System.out.println("rank = "+bst.rank(11));
//		System.out.println("deleteMIn");
//		bst.deleteMin();
//		bst.printBinaryTree2();
//		System.out.println("min = "+bst.min());
//		System.out.println("deleteMax");
//		bst.deleteMax();
//		bst.printBinaryTree2();
//		System.out.println("max = "+bst.max());
		
//		bst.delete(8);
//		bst.printBinaryTree2();
//		System.out.println("get = "+bst.get(8));
//		System.out.println("keys = "+bst.keys(3,  7));		
	}
	

}
