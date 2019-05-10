package com.tree.string.trie;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class TST<Value> {
	
	public static void main(String[] args) {
		TST<String> t = new TST<>();
		String[] ss = new String[]{"she","sh","shell","shells","by","shel","uvw","shemv","s","shem"};
		//String[] ss = new String[]{"she"};
		for(String s : ss){
			t.put(s, s);
		}
		//t.printTST();
		//System.out.println(t.root.toString());
		//System.out.println(t.get("sh"));
		//System.out.println(t.size());
		
		Iterator<String> it = t.keys().iterator();
		while(it.hasNext())
			System.out.print(it.next()+",");
		System.out.println();
		
		Iterator<String> it2 = t.keysWithPrefix("s").iterator();
		while(it2.hasNext())
			System.out.print(it2.next()+",");
		System.out.println();
		
		Iterator<String> it3 = t.keysThatMatch("sh.ll").iterator();
		while(it3.hasNext())
			System.out.print(it3.next()+",");
		System.out.println();
		
		System.out.println(t.longestPrefixOf("shemabc"));
		System.out.println(t.longestPrefixOf("shellsort"));
		
	}
	
	private Node root;
	
	private class Node{
		char c;
		Node left, mid, right;
		Value val;
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Node [c=");
			builder.append(c);
			builder.append(", left=");
			builder.append(left);
			builder.append(", mid=");
			builder.append(mid);
			builder.append(", right=");
			builder.append(right);
			builder.append(", val=");
			builder.append(val);
			builder.append("]");
			return builder.toString();
		}		
	}
	
	//给定字符串的最长键前缀
	public String longestPrefixOf(String query) {     
        if (query.length() == 0) return null;
        int length = 0;
        Node x = root;
        int i = 0;
        //沿着三向字典树进行匹配以及记录匹配字符长度
        while (x != null && i < query.length()) {
            char c = query.charAt(i);
            if      (c < x.c) x = x.left;
            else if (c > x.c) x = x.right;
            //当c==x.c时，此时出现匹配，则判断该节点是否有值，继续沿mid方向匹配
            else {
                i++;
                if (x.val != null) length = i;
                x = x.mid;
            }
        }
        return query.substring(0, length);
    }
	
	//通配符匹配  .匹配所有字符
	public Iterable<String> keysThatMatch(String pat){
		Queue<String> q = new LinkedList<>();
		collect(root, "", 0, pat, q);
		return q;
	}
	
	private void collect(Node x, String pre, int i, String pattern, Queue<String> queue) {
        if (x == null) return;
        char c = pattern.charAt(i);
        if (c == '.' || c < x.c) collect(x.left, pre, i, pattern, queue);
        if (c == '.' || c == x.c) {
            if (i == pattern.length() - 1 && x.val != null) queue.add(pre + x.c);
            if (i < pattern.length() - 1) {
            	//类似于回溯算法，沿着mid向下匹配时，此时pre需要更新为pre+x.c
            	pre = pre + x.c;
                collect(x.mid, pre, i+1, pattern, queue);
                //当中间部分递归完成，需要回溯，进行右分支的匹配pre去掉x.c
                pre = pre.substring(0, pre.length()-1);
            }
        }
        if (c == '.' || c > x.c) collect(x.right, pre, i, pattern, queue);
    }

	public Iterable<String> keys(){
		Queue<String> queue = new LinkedList<String>();
        collect(root, "", queue);
        return queue;
	} 
	
	private Iterable<String> keysWithPrefix(String pre) {
		Queue<String> q = new LinkedList<>();
		Node n = get(root, pre, 0);
		//System.out.println(n);
		if(n == null) return null;
		if(n.val != null) q.add(pre);
		//System.out.println(q);
		collect(n.mid, pre, q);
		return q;
	}

	private void collect(Node x, String pre, Queue<String> q) {
		//System.out.println(pre);
		if(x == null) return;
		//在左分支内部匹配
		collect(x.left, pre, q);
		//判断当前节点
		if(x.val != null) q.add(pre+x.c);
		//回溯探索mid分支
		pre = pre + x.c;
		collect(x.mid, pre, q);
		pre = pre.substring(0, pre.length()-1);
		//在右分支中寻找
	    collect(x.right, pre, q);
	}

	
	public int size(){
		return size(root);
	}
	
	private int size(Node x) {
		if(x == null) return 0;
		int cnt = 0;
		if(x.val != null) cnt++;
		return cnt + size(x.left) + size(x.mid) + size(x.right); 
	}
	
	public Value get(String key){
		Node x = get(root, key, 0);
		if(x==null) return null;
		return x.val;
	}

	private Node get(Node x, String key, int d) {
		if(x==null) return null;
		char c = key.charAt(d);
		//与当前节点值不匹配，则进入到左分支与右分支，此时当前字符没有匹配，即下一步继续匹配该字符
		//左分支
		if(c<x.c)
			return get(x.left, key, d);
		//右分支
		else if(c>x.c)
			return get(x.right, key, d);
		//当前节点还没有完全匹配key d记录已经匹配完成的字符长度
		else if(d<key.length()-1)
			return get(x.mid, key, d+1);
		else
		//当前节点完全匹配，返回当前节点  c==x.c && d==key.length-1
			return x;
	}
	
	public void put(String key, Value val){
		root = put(root, key, val, 0);
	}

	private Node put(Node x, String key, Value val, int d) {
		char c = key.charAt(d);
		if(x==null){
			x = new Node();
			x.c = c;
		}
		if(c<x.c){
			x.left = put(x.left, key, val, d);
		}else if(c>x.c){
			x.right = put(x.right, key, val, d);
		}else if(d<key.length()-1){
			x.mid = put(x.mid, key, val, d+1);
		}else{
			x.val = val;
		}
		return x;
	}
	
	public void printTST(){
		Queue<Node> q = new LinkedList<>();
		q.add(root);
		while(q.size()>0){
			int size = q.size();
			while(size>0){
				Node n = q.poll();	
				System.out.print(n.c);
				if(n.left!=null)
					q.add(n.left);
				if(n.mid!=null)
					q.add(n.mid);
				if(n.right!=null)
					q.add(n.right);				
				size--;
			}
			System.out.println();
		}
	}

}
