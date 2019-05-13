package com.tree.bt;

public class BTree<Key extends Comparable<Key>, Value> {
	
	public static void main(String[] args) {
		BTree<Integer, String> bt = new BTree<>();
		for(int i=0; i<10; i++){
			bt.put(i, String.valueOf(i));
		}
		System.out.println(bt.toString());
		//bt.toString();
		System.out.println(bt.get(1));
		System.out.println(bt.get(-1));
	}
	
	//参数M 定义每个节点的链接数
	private static final int M = 4;
	
	private Node root;
	
	//树的高度  最底层为0 表示外部节点    根具有最大高度
	private int height;
	
	//键值对总数
	private int n;
	
	//节点数据结构定义
	private static final class Node{
		//值的数量
		private int m;
		private Entry[] children = new Entry[M];
		private Node(int k){
			m = k;
		}
	}
	
	//节点内部每个数据项定义
	private static class Entry{
		private Comparable key;
		private final Object val;
		//下一个节点
		private Node next;
		public Entry(Comparable key, Object val, Node next){
			this.key = key;
			this.val = val;
			this.next = next;
		}
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Entry [key=");
			builder.append(key);
			builder.append("]");
			return builder.toString();
		}
		
	}
	
	public BTree(){
		root = new Node(0);
	}
	
	public int size(){
		return n;
	}
	
	public boolean isEmpty(){
		return size() == 0;
	}
	
	public int height(){
		return height;
	}
	
	public Value get(Key key){
		return search(root, key, height);
	}

	private Value search(Node x, Key key, int ht) {
		Entry[] children = x.children;
		//外部节点  这里使用顺序查找
		//如果M很大  可以改成二分查找
		if(ht == 0){
			for(int j=0; j<x.m; j++){
				if(equal(key, x.children[j].key))
					return (Value)children[j].val;
			}
		}
		//内部节点  寻找下一个节点
		else{
			for(int j=0; j<x.m; j++){
				//最后一个节点  或者 插入值小于某个孩子节点值
				if(j+1==x.m || less(key, x.children[j+1].key))
					return search(x.children[j].next, key, ht-1);
			}
		}
		return null;
	}
	
	public void put(Key key, Value val){
		//插入后的节点  如果节点分裂，则返回分裂后产生的新节点
		Node u = insert(root, key, val, height);
		n++;
		//根节点没有分裂  直接返回
		if(u == null) return;
		//分裂根节点
		Node t = new Node(2); 
		//旧根节点第一个孩子   新分裂节点第一个孩子组成新节点作为根
		t.children[0] = new Entry(root.children[0].key, null, root);
		t.children[1] = new Entry(u.children[0].key, null, u);
		root = t;
		height++;
	}

	private Node insert(Node h, Key key, Value val, int ht) {
		int j;
		//新建待插入数据数据项
		Entry t = new Entry(key, val, null);
		// 外部节点  找到带插入的节点位置j
		if(ht == 0){
			for(j=0; j<h.m; j++){
				if(less(key, h.children[j].key))
					break;
			}
		}else{
			//内部节点  找到合适的分支节点
			for(j=0; j<h.m; j++){
				if(j+1==h.m || less(key, h.children[j+1].key)){
					Node u = insert(h.children[j++].next, key, val, ht-1);
					if(u == null) return null;
					t.key = u.children[0].key;
					t.next = u;
					break;
				}
			}
		}
		//System.out.println(j + t.toString());
		//j为带插入位置，将顺序数组j位置以后后移一位 将t插入
		for(int i=h.m; i>j; i++){
			h.children[i] = h.children[i-1];
		}
		System.out.println(j + t.toString());
		h.children[j] = t;
		h.m++;
		if(h.m < M) return null;
		//如果节点已满  则执行分类操作
		else return split(h);
	}

	private Node split(Node h) {
		//将已满节点h的后一般M/2节点分裂到新节点并返回
		Node t = new Node(M/2);
		h.m = M / 2;
		for(int j=0; j<M/2; j++)
			t.children[j] = h.children[M/2+j];
		return t;
	}
	
	public String toString() {
        return toString(root, height, "") + "\n";
    }

    private String toString(Node h, int ht, String indent) {
        StringBuilder s = new StringBuilder();
        Entry[] children = h.children;

        //外部节点
        if (ht == 0) {
            for (int j = 0; j < h.m; j++) {
                s.append(indent + children[j].key + " " + children[j].val + "\n");
            }
        }
        else {
            for (int j = 0; j < h.m; j++) {
                if (j > 0) s.append(indent + "(" + children[j].key + ")\n");
                s.append(toString(children[j].next, ht-1, indent + "     "));
            }
        }
        return s.toString();
    }
    
    private boolean equal(Comparable k1, Comparable k2){
    	return k1.compareTo(k2)==0;
    }
    
    private boolean less(Comparable k1, Comparable k2){
    	return k1.compareTo(k2)<0;
    }
	
	
	
}
