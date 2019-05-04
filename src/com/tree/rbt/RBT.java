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
 * 红黑树性能分析
 * JDK中红黑树调整策略源码分析
 * */
public class RBT<Key extends Comparable<Key>, Value> {

	private static final boolean RED = true;
	private static final boolean BLACK = false;

	private Node root;

	private class Node {
		private Key key;
		private Value val;
		private Node left, right;
		private int N;
		private boolean color;
		int x;
		int y;

		public Node(Key key, Value val, int N, boolean color) {
			this.key = key;
			this.val = val;
			this.N = N;
			this.color = color;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
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

	public List<List<Node>> calculateNodeCoordinate2() {
		int h = height();
		List<List<Node>> ll = levelOrder2();
		for (int i = 1; i <= h; i++) {
			int lineStart = pow2(h - i + 1) - 2;
			int offset = pow2(h - i + 2);
			int t = lineStart;
			for (int j = 0; j < ll.get(i - 1).size(); j++) {
				Node n = ll.get(i - 1).get(j);
				n.setX(t);
				n.setY(i);
				t = t + offset;
			}
		}
		return ll;
	}

	public void delete(Key key) {
		root = delete(root, key);
		root.color = BLACK;
	}

	private Node delete(Node h, Key key) {
		int cmp = key.compareTo(h.key);
		//往左子树寻找待删除节点
		if (cmp < 0) {
			//如果该节点为2节点，将2节点转换为非2节点，从父节点借或者从兄弟节点中借
			if (!isRed(h.left) && !isRed(h.left.left))
				h = moveRedLeft(h);
			h.left = delete(h.left, key);
		} else {
			//在右子树中  如果出现2节点转换为非2节点
			if (isRed(h.left))
				h = rotateRight(h);
			//上述右旋转会改变h指针，因此需要重新判断
			if (key.compareTo(h.key) == 0 && h.right == null)
				return null;
			if (!isRed(h.right) && !isRed(h.right.left))
				h = moveRedRight(h);
			//如果找到待删除节点，则将待删除节点的右子树最小值替换为该节点，然后删除右子树的最小值
			//上述2节点转换操作会改变h指针  因此需要重新判断
			if (key.compareTo(h.key) == 0) {
				h.key = min(h.right).key;
				h.val = get(h.right, h.key);
				h.right = deleteMin(h.right);
			} else {
				h.right = delete(h.right, key);
			}
		}
		return fixUp(h);
	}

	public Node max() {
		return max(root);
	}

	private Node max(Node x) {
		if (x == null)
			return null;
		if (x.right == null)
			return x;
		return max(x.right);
	}

	public Node min() {
		return min(root);
	}

	private Node min(Node x) {
		if (x == null)
			return null;
		if (x.left == null)
			return x;
		return min(x.left);
	}

	private Value get(Node x, Key key) {
		if (x == null)
			return null;
		int cmp = key.compareTo(x.key);
		if (cmp > 0)
			return get(x.right, key);
		else if (cmp < 0)
			return get(x.left, key);
		else
			return x.val;
	}

	public void deleteMin() {
		root = deleteMin(root);
		root.color = BLACK;
	}

	private Node deleteMin(Node h) {
		if (h.left == null)
			return null;
		if (!isRed(h.left) && !isRed(h.left.left))
			h = moveRedLeft(h);
		h.left = deleteMin(h.left);
		return fixUp(h);
	}

	private Node moveRedLeft(Node h) {
		flipColorsD(h);
		if (isRed(h.right.left)) {
			h.right = rotateRight(h.right);
			h = rotateLeft(h);
			flipColors(h);
		}
		return h;
	}

	private void deleteMax() {
		root = deleteMax(root);
		root.color = BLACK;
	}

	private Node deleteMax(Node h) {
		// 父节点为非2节点 从父节点借一个值
		if (isRed(h.left))
			h = rotateRight(h);
		if (h.right == null)
			return null;
		// 从兄弟节点处理，借一个值或者直接与兄弟节点进行合并
		if (!isRed(h.right) && !isRed(h.right.left))
			h = moveRedRight(h);
		h.right = deleteMax(h.right);
		return fixUp(h);
	}

	private Node moveRedRight(Node h) {
		// 与兄弟节点合并
		flipColorsD(h);
		// 从兄弟节点借一个值
		if (isRed(h.left.left)) {
			h = rotateRight(h);
			flipColors(h);
		}
		return h;
	}

	private Node fixUp(Node h) {
		if (isRed(h.right))
			h = rotateLeft(h);
		if (isRed(h.left) && isRed(h.left.left))
			h = rotateRight(h);
		if (isRed(h.left) && isRed(h.right))
			flipColors(h);
		return h;
	}

	private boolean isEmpty() {
		return root == null ? true : false;
	}

	public void put(Key key, Value val) {
		root = put(root, key, val);
		root.color = BLACK;
	}

	// private Node insert(Node h, Key key, Value val){
	// if(h == null)
	// return new Node(key, val, 1, RED);
	// if(isRed(h.left)&&isRed(h.right))
	// flipColors(h);
	// int cmp = key.compareTo(h.key);
	// if(cmp==0)
	// h.val = val;
	// else if(cmp<0)
	// h.left = insert(h.left, key, val);
	// else
	// h.right = insert(h.right, key, val);
	// if(isRed(h.right))
	// rotateLeft(h);
	// if(isRed(h.left)&&isRed(h.left.left))
	// rotateRight(h);
	// h.N = size(h.left) + size(h.right) + 1;
	// return h;
	// }

	private Node put(Node h, Key key, Value val) {
		if (h == null)
			return new Node(key, val, 1, RED);
		int cmp = key.compareTo(h.key);
		if (cmp > 0)
			h.right = put(h.right, key, val);
		else if (cmp < 0)
			h.left = put(h.left, key, val);
		else
			h.val = val;
		if (!isRed(h.left) && isRed(h.right))
			h = rotateLeft(h);
		if (isRed(h.left) && isRed(h.left.left))
			h = rotateRight(h);
		if (isRed(h.left) && isRed(h.right))
			flipColors(h);
		h.N = size(h.left) + size(h.right) + 1;
		return h;
	}

	private boolean isRed(Node x) {
		if (x == null)
			return false;
		return x.color;
	}

	// 分解4节点
	private void flipColors(Node h) {
		h.color = RED;
		h.left.color = BLACK;
		h.right.color = BLACK;
	}

	// 生成4节点
	private void flipColorsD(Node h) {
		h.color = BLACK;
		h.left.color = RED;
		h.right.color = RED;
	}

	// 左旋转h的右连接
	private Node rotateLeft(Node h) {
		// System.out.println("rotateleft before " + h);
		// 修改连接
		Node x = h.right;
		h.right = x.left;
		x.left = h;
		// 修改颜色
		x.color = h.color;
		h.color = RED;
		// 修改数量
		x.N = h.N;
		h.N = size(h.left) + size(h.right) + 1;
		// System.out.println("rotateleft after " + x);
		return x;
	}

	// 右旋转h的左连接
	private Node rotateRight(Node h) {
		Node x = h.left;
		h.left = x.right;
		x.right = h;
		x.color = h.color;
		h.color = RED;
		x.N = h.N;
		h.N = size(h.left) + size(h.right) + 1;
		return x;
	}

	public int size() {
		return size(root);
	}

	private int size(Node x) {
		if (x == null)
			return 0;
		return size(x.left) + size(x.right) + 1;
	}

	public int height() {
		return height(root);
	}

	public int height(Node x) {
		if (x == null)
			return 0;
		if (x.left == null && x.right == null)
			return 1;
		return 1 + Math.max(height(x.left), height(x.right));
	}

	// 小于等于key的最大值
	public Node floor(Key key) {
		if (root == null)
			return null;
		return floor(root, key);
	}

	private Node floor(Node x, Key key) {
		if (x == null)
			return null;
		int cmp = key.compareTo(x.key);
		if (cmp == 0)
			return x;
		if (cmp < 0)
			return floor(x.left, key);
		Node r = floor(x.right, key);
		if (r != null)
			return r;
		else
			return x;
	}

	// 大于等于key的最小值
	public Node ceiling(Key key) {
		if (root == null)
			return null;
		return ceiling(root, key);
	}

	private Node ceiling(Node x, Key key) {
		if (x == null)
			return null;
		int cmp = key.compareTo(x.key);
		if (cmp == 0)
			return x;
		if (cmp > 0)
			return ceiling(x.right, key);
		Node l = ceiling(x.left, key);
		if (l != null)
			return l;
		else
			return x;
	}

	// 查找排名为k的节节点
	public Node select(int k) {
		if (root == null)
			return null;
		return select(root, k);
	}

	private Node select(Node x, int k) {
		if (x == null)
			return null;
		int leftnum = size(x.left);
		if (leftnum == k - 1)
			return x;
		if (leftnum > k - 1)
			return select(x.left, k);
		return select(x.right, k - 1 - leftnum);
	}

	// 返回该key对应的排名
	public int rank(Key key) {
		if (root == null)
			return -1;
		return rank(root, key);
	}

	private int rank(Node x, Key key) {
		if (x == null)
			return -1;
		int leftnum = size(x.left);
		int cmp = key.compareTo(x.key);
		if (cmp == 0)
			return leftnum + 1;
		if (cmp < 0)
			return rank(x.left, key);
		return leftnum + 1 + rank(x.right, key);
	}

	public Iterable<Key> keys() {
		return keys(min().key, max().key);
	}

	// 返回给定范围内的key值集合
	public Iterable<Key> keys(Key lo, Key hi) {
		Queue<Key> q = new LinkedList<>();
		keys(root, q, lo, hi);
		return q;
	}

	private void keys(Node x, Queue<Key> q, Key lo, Key hi) {
		if (x == null)
			return;
		int cmplo = lo.compareTo(x.key);
		int cmphi = hi.compareTo(x.key);
		if (cmplo < 0)
			keys(x.right, q, lo, hi);
		if (cmplo <= 0 && cmphi >= 0)
			q.add(x.key);
		if (cmphi > 0)
			keys(x.left, q, lo, hi);
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
		String[] str = new String[] { "A", "C", "E", "H", "L", "M" };
		for (String s : str) {
			rbt.put(s, s);
			bst.put(s, s);
		}
		rbt.printBinaryTree2();
		//bst.printBinaryTree2();
		// rbt.deleteMax();
		// rbt.printBinaryTree2();
		// rbt.deleteMin();
		// rbt.printBinaryTree2();
		rbt.delete("E");
		rbt.printBinaryTree2();
	}

}
