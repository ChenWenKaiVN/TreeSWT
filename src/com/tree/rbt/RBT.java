package com.tree.rbt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.tree.bst.BST;
import com.tree.bst.BST.Node;

/*
 * 红黑树由2-3平衡树演化而来
 * 二叉查找树
 * 节点的颜色由指向该节点的连接颜色确定，分为红连接以及黑连接   其中根为黑色的,空节点为黑色   所有红连接是左连接
 * 任何节点不能与两个红连接相连
 * 完美黑色平衡  任何空连接到根路径上的黑连接个数相等
 * 插入 删除  查找时间复杂度均为logN
 * */
public class RBT<Key extends Comparable<Key>, Value> {
	
	private static final boolean RED = true;
	private static final boolean BLACK = false;
	
	private Node root;
	
	private class Node{
		private Key key;
		private Value val;
		private Node left, right;
		private int N;
		private boolean color;
		
		public Node(Key key, Value val, int N, boolean color){
			this.key = key;
			this.val = val;
			this.N = N;
			this.color = color;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Node [key=");
			builder.append(key);
			builder.append(", val=");
			builder.append(val);
			builder.append(", left=");
			builder.append(left);
			builder.append(", right=");
			builder.append(right);
			builder.append(", N=");
			builder.append(N);
			builder.append(", color=");
			builder.append(color);
			builder.append("]");
			return builder.toString();
		}
	}
	
	public void put(Key key, Value val){
		root = put(root, key, val);
		root.color = BLACK;
	}
	
	private Node put(Node h, Key key, Value val) {
		if(h == null)
			return new Node(key, val, 1, RED);		
		int cmp = key.compareTo(h.key);
		if(cmp>0)
			h.right = put(h.right, key, val);
		else if(cmp<0)
			h.left = put(h.left, key, val);
		else
			h.val = val;
		if(!isRed(h.left)&&isRed(h.right))
			h = rotateLeft(h);
		if(isRed(h.left)&&isRed(h.left.left))
			h = rotateRight(h);
		if(isRed(h.left)&&isRed(h.right))
			flipColors(h);
		h.N = size(h.left) + size(h.right) + 1;
		return h;		
	}
	
	private boolean isRed(Node x){
		if(x == null) return false;
		return x.color;
	}
	
	private void flipColors(Node h){
		h.color = RED;
		h.left.color = BLACK;
		h.right.color = BLACK;
	}
	
	//左旋转h的右连接
	private Node rotateLeft(Node h){
		//System.out.println("rotateleft before " + h);
		//修改连接
		Node x = h.right;
		h.right = x.left;
		x.left = h;
		//修改颜色
		x.color = h.color;
		h.color = RED;
		//修改数量
		x.N = h.N;
		h.N = size(h.left)+size(h.right)+1;
		//System.out.println("rotateleft after " + x);
		return x;
	}
	
	//右旋转h的左连接
	private Node rotateRight(Node h){
		Node x = h.left;
		h.left = x.right;
		x.right = h;
		x.color = h.color;
		h.color = RED;
		x.N = h.N;
		h.N = size(h.left)+size(h.right)+1;
		return x;
	}
	
	public int size(){
		return size(root);
	}
	
	private int size(Node x){
		if(x==null) return 0;
		return size(x.left) + size(x.right) + 1;
	}
	
	public int height(){
		return height(root);
	}
	
	public int height(Node x){
		if(x==null) return 0;
		if(x.left==null&&x.right==null) return 1;
		return 1+Math.max(height(x.left), height(x.right));
	}
	
	private String spaceFormat = " ";
	private String crossFormat = "-";
	private String specialChar = "#";

	public List<List<Node>> levelOrder2() {
		if (root == null)
			return null;
		List<List<Node>> ll = new ArrayList<>();
		Queue<Node> q = new LinkedList<>();
		q.add(root);
		int h = height();
		int depth = 1;
		while (q.size() > 0) {
			int s = q.size();
			List<Node> lk = new ArrayList<>();
			while (s > 0) {
				Node tmp = q.poll();
				lk.add(tmp);
				if (tmp.left != null) {
					q.add(tmp.left);
				} else {
					q.add(new Node((Key) specialChar, (Value) specialChar, 1, BLACK));
				}
				if (tmp.right != null) {
					q.add(tmp.right);
				} else {
					q.add(new Node((Key) specialChar, (Value) specialChar, 1, BLACK));
				}
				s--;
			}
			ll.add(lk);
			if (depth > h - 1)
				break;
			depth++;
		}
		// for(List<Node> t : ll){
		// for(Node n : t){
		// System.out.print(n.key + ",");
		// }
		// System.out.println();
		// }
		return ll;
	}

	public int pow2(int n) {
		return (int) Math.pow(2, n);
	}

	public void printFormat(int num, String format) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < num; i++) {
			sb.append(format);
		}
		System.out.print(sb.toString());
	}

	public void printBinaryTree2() {
		List<List<Node>> ll = levelOrder2();
		List<Node> l = null;
		int h = height();
		for (int i = 1; i <= h; i++) {
			// 第i层节点之间的偏移量
			int lineStart = pow2(h - i + 1) - 2;
			// 第i层节点之间的间隔
			int offset = pow2(h - i + 2) - 1;
			// 该节点到左子节点或右子节点的长度
			int brackets = pow2(h - i);
			// System.out.println("linenum = " + i + " start = " + lineStart + "
			// offset = " + offset + " brackets = " + brackets);
			printFormat(lineStart, spaceFormat);
			l = ll.get(i - 1);
			for (int j = 0; j < l.size(); j++) {
				if (!l.get(j).key.equals(specialChar)) {
					System.out.print(l.get(j).key);
				} else {
					System.out.print(" ");
				}
				printFormat(offset, spaceFormat);
			}
			//
			if (i >= h)
				break;
			System.out.println();
			// 横线部分行开始地址
			int lineCrossStart = pow2(h - i) - 2;
			printFormat(lineCrossStart, spaceFormat);
			for (int j = 0; j < l.size(); j++) {
				// System.out.println(l.get(j).key);
				if (!l.get(j).key.equals(specialChar) && (l.get(j).left != null || l.get(j).right != null)) {
					// 每个元素下一行横线部分长度
					printFormat(brackets * 2 + 1, crossFormat);
					// 横线部分 间隔 即下一行节点之间的间隔距离
					printFormat(pow2(h - i + 2 - 1) - 1, spaceFormat);
				} else {
					printFormat(brackets * 2 + 1, spaceFormat);
					printFormat(pow2(h - i + 2 - 1) - 1, spaceFormat);
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		RBT<String, String> rbt = new RBT<>();
		BST<String, String> bst = new BST<>();
		String[] str = new String[]{"A","C","E","H","L"};
		for(String s : str){
			rbt.put(s, s);
			bst.put(s, s);
		}
		rbt.printBinaryTree2();
		bst.printBinaryTree2();
	}
	

}
