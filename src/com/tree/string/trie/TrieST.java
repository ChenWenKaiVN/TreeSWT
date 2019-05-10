package com.tree.string.trie;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class TrieST<Value> {
	
	public static void main(String[] args) {
		TrieST<String> t = new TrieST<>();
		String[] ss = new String[]{"she","sh","shell","shells","by"};
		for(String s : ss){
			t.put(s, s);
		}
		//t.printTrieST();
//		System.out.println(t.get("she"));
//		System.out.println(t.get("she2"));
//		System.out.println("size = " + t.size());
//		Iterator<String> it = t.keys().iterator();
//		while(it.hasNext())
//			System.out.print(it.next()+",");
//		System.out.println();
//		
//		Iterator<String> it2 = t.keysWithPrefix("sh").iterator();
//		while(it2.hasNext())
//			System.out.print(it2.next()+",");
		
//		Iterator<String> it3 = t.keysThatMatch("sh.").iterator();
//		while(it3.hasNext())
//			System.out.print(it3.next()+",");
		
		System.out.println(t.longstPrefixOf("she"));
		
	}
	
	private static int R = 256;//字母表的基数
	
	private Node root;
	
	//java不支持范型数组  val为Object类型
	private static class Node{
		Object val;
		//每一个节点都持有一个Node[R]用来保存下一个字符所有可能情况基数R
		Node[] next = new Node[R];
	}
	
	public void delete(String key){
		root = delete(root, key, 0);
	}
	
	private Node delete(Node x, String key, int d) {
		if(x==null) return null;
		//找到待删除节点 将该节点置为null
		if(d==key.length()){
			x.val = null;
		}else{
			//如果没查找，则继续递归删除寻找下一个位置
			char c = key.charAt(d);
			x.next[c] = delete(x.next[c], key, d+1);
		}
		//如果x.val!=null或者x.next非空，则返回x
		if(x.val!=null) return x;
		for(char c=0; c<R; c++){
			if(x.next[c]!=null)
				return x;
		}
		//如果x.val==null && x.next==null则直接返回即可。
		return null;
	}

	//给定字符串的最长键前缀
	public String longstPrefixOf(String s){
		int length = search(root, s, 0, 0);
		return s.substring(0, length);
	}
	
	private int search(Node x, String s, int d, int length) {
		if(x == null) return length;
		if(x.val != null) length = d;
		if(d == s.length()) return length;
		char c = s.charAt(d);
		return search(x.next[c], s, d+1, length);
	}

	//通配符匹配  .匹配所有字符
	public Iterable<String> keysThatMatch(String pat){
		Queue<String> q = new LinkedList<>();
		collect(root, "", pat, q);
		return q;
	}
	
	private void collect(Node x, String pre, String pat, Queue<String> q) {
		//pre当前已经组装字符串长度
		//System.out.println(pre);
		int d = pre.length();
		if(x==null) return;
		//当前字符串长度与模式串相同，并且该节点val非空，将该字符串加入到队列中
		if(d==pat.length()&&x.val!=null) q.add(pre);
		if(d==pat.length()) return;
		//匹配下一个字符
		char next = pat.charAt(d);
		//从该节点开始探索所有情况，如果当前是指定字符，则进入到字典树指定分支；
		//如果当前是特殊字符，则依次匹配所有字符，尝试进入所有分支
		for(int c=0; c<R; c++){
			if(next=='.' || next==c){
				//System.out.println(pre+(char)c);
				collect(x.next[c], pre+(char)c, pat, q);
			}
		}
		
	}

	public Iterable<String> keys(){
		return keysWithPrefix("");
	} 
	
	private Iterable<String> keysWithPrefix(String pre) {
		Queue<String> q = new LinkedList<>();
		collect(get(root, pre, 0), pre, q);
		return q;
	}

	private void collect(Node x, String pre, Queue<String> q) {
		if(x==null) 
			return;
		if(x.val!=null) 
			q.add(pre);
		//以pre作为基准  添加一个字符  遍历所有可能情况
		for(int c=0; c<R; c++){
			collect(x.next[c], pre + (char)c, q);
		}
	}

	public int size(){
		return size(root);
	}
	
	private int size(Node x) {
		if(x==null) return 0;
		int cnt = 0;
		if(x.val!=null) cnt++;
		for(Node d : x.next){
			cnt += size(d);
		}
		return cnt; 
	}

	public Value get(String key){
		Node x = get(root, key, 0);
		if(x == null) return null;
		return (Value) x.val;
	}

	private Node get(Node x, String key, int d) {
		//查找结束于一条空链  该key不存在
		if(x==null) return null;
		//查找结束于某一个节点，判断该节点val值是否为空
		if(d==key.length()) return x;
		//查找未结束，继续查找第d+1个字符
		char c = key.charAt(d);	
		return get(x.next[c], key, d+1);
	}
	
	public void put(String key, Value val){
		root = put(root, key, val, 0);
	}

	private Node put(Node x, String key, Value val, int d) {
		//如果当前节点为空，则说明该节点字符不存在则新建节点
		if(x==null) 
			x = new Node();
		//判断查找路径深度是否等于键长度，如果相等则说明该节点即为目标节点，将值赋值
		if(d==key.length()){
			x.val = val;
			return x;
		}
		//继续往下寻找合适的插入位置
		char c = key.charAt(d);
		x.next[c] = put(x.next[c], key, val, d+1);
		return x;
	}
	
	public void printTrieST(){
		printTrieST(root);
	}

	private void printTrieST(Node x) {
		if(x==null) return;
		//System.out.print("val = " + x.val + "    next = ");
		for(int i=0; i<R; i++){
			Node d = x.next[i];
			if(d!=null){
				if(i<R-1){
					System.out.print((char)i);
					System.out.print(",");
				}else{
					System.out.print((char)i);				
				}
			}
		}
		System.out.println();
		for(Node d : x.next){
			printTrieST(d);		
		}
	}
	
	

}
