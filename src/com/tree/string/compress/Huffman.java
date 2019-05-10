package com.tree.string.compress;

import java.util.Arrays;
import java.util.PriorityQueue;

public class Huffman {
	
	//字符串字母表基数
	private static int R = 256;
	
	private static Integer index = -1;
	
	//private static String trieStr = "";
	private static StringBuilder trieStr = new StringBuilder();
	
	public static void main(String[] args) throws Exception {
		String s = "itwasthebestoftimesitwastheworstoftimesLF";
		String r = compress(s);
		String e = expand(r);
		System.out.println("expand = " + e);
		System.out.println("code rate = " + r.length()/((double)s.length()*7));
		
		//String s = "ab";
		//String r = compress(s);
		
		//String e = expand(r);
		//System.out.println("expand = " + e);
		
//		System.out.println('a');
//		System.out.println((int)'a');
//		byte[] byteArray = String.valueOf('a').getBytes("UTF-8");
//		System.out.println(stringtoBinary("a"));
//		byteArray[0] = '1'; 
//		System.out.println(Arrays.toString(byteArray));
	}
	
	//霍夫曼树节点
	private static class Node implements Comparable<Node>{
		//叶子节点表示编码字符  非叶子节点为空
		private char ch;
		//字符出现的频率
		private int freq;
		private final Node left, right;
		Node(char ch, int freq, Node left, Node right) {
			this.ch = ch;
			this.freq = freq;
			this.left = left;
			this.right = right;
		}
		public boolean isLeaf(){
			return left==null && right==null;
		}
		//后面使用优先级队列来保存Node节点  注意Node节点排序规则  freq小的在前 
		@Override
		public int compareTo(Node o) {
			return this.freq - o.freq;
			//return o.freq - this.freq;
		}
		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Node [ch=");
			builder.append(ch);
			builder.append(", freq=");
			builder.append(freq);
			builder.append(", left=");
			builder.append(left);
			builder.append(", right=");
			builder.append(right);
			builder.append("]");
			return builder.toString();
		}	
	}
	
	//给定二进制序列   根据霍夫曼树译码
	public static String expand(String s){
		StringBuilder sb = new StringBuilder();
		System.out.println("trieStr = " + trieStr);
		Node root = readTrie(trieStr);
		System.out.println("reaftrie = " + root);
		for(int i=0; i<s.length();){
			Node x = root;
			while(!x.isLeaf()){
				//从字符串中读取每一个字符来模拟读取二进制bit
				if(s.charAt(i++)=='1'){
					x = x.right;
				}
				else{
					x = x.left;
				}
			}
			//System.out.println(x.ch);
			sb.append(x.ch);
			//i++;
		}
		return sb.toString();
	}
	
	//函数指针传递问题  int类型  函数参数传递  值拷贝一次 原来值不变
	//java函数参数传递问题
	//实参  形参   实参是对于形参的拷贝  原始类型相当于值拷贝   引用类型相当于引用拷贝
	//引用拷贝时由于实参引用以及形参引用指向同一个对象，因此形参值会改变实参值
	//将index值改成全局变量
	private static Node readTrie(StringBuilder s){
		index++;
		if(index>s.length()-1) return null;
		//System.out.println("index = " + index + " " + s.charAt(index));	
		if(s.charAt(index)=='1'){
			//System.out.println("node create = " + s.charAt(index+1));
			index++;
			return new Node(s.charAt(index), 1, null, null);
		}
		//假定此时index=i 需要进行两个递归调用  进入第一个递归调用  index=i+1   第一个调用返回  此时第二个传值仍为index=i+1
		//这里的效果应该为第一个调用index=i+1    第二个调用为index=i+2
		return new Node('\0', 0, readTrie(s), readTrie(s)); 
	}
	
	    //函数指针传递问题  int类型  函数参数传递  值拷贝一次 原来值不变
		// java函数参数传递问题
		//  实参  形参   实参是对于形参的拷贝  原始类型相当于值拷贝   引用类型相当于引用拷贝
		//引用拷贝时由于实参引用以及形参引用指向同一个对象，因此形参值会改变实参值
		private static Node readTrie(StringBuilder s, int index){
			if(index>s.length()-1) return null;
			System.out.println("index = " + index + " " + s.charAt(index));	
			if(s.charAt(index)=='1'){
				System.out.println("node create = " + s.charAt(index+1));
				return new Node(s.charAt(++index), 1, null, null);
			}
			//假定此时index=i 需要进行两个递归调用  进入第一个递归调用  index=i+1   第一个调用返回  此时第二个传值仍为index=i+1
			//这里的效果应该为第一个调用index=i+1    第二个调用为index=i+2
			return new Node('\0', 0, readTrie(s, ++index), readTrie(s, ++index)); 
		}
	
	//根据霍夫曼树  进行编码
	public static String compress(String s){
		
		char[] input = s.toCharArray();
		//要压缩的字符串字符个数
		int total = input.length;
		//统计字符串中每个字符出现的频率
		int[] freq = new int[R];
		for(int i=0; i<total; i++){
			freq[input[i]]++;
		}
		System.out.println("freq = " + Arrays.toString(freq));
		
		//构造霍夫曼树
		Node root = buildTree(freq);
		System.out.println("root = " + root);
		
		//构造编译表  每个字符对应的01序列
		String[] st = new String[R];
		buildCode(st, root, "");
		System.out.println("st = " + Arrays.toString(st));
		
		//保存解码用的单词查找树
		saveTrie(root, trieStr);
		System.out.println("trieStr = " + trieStr.toString());
		
		System.out.println("total = " + total);
		
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<total; i++){
			//System.out.println(input[i] + "  " + st[input[i]]);
			sb.append(st[input[i]]);
		}
		
		System.out.println("origin length = " + input.length*7);
		System.out.println("compress length = " + sb.length() + " result = " + sb.toString());
		return sb.toString();
	}
	
	//由于java  String 特性函数传递不会改变值    trieStr值可能不会改变
	private static void saveTrie(Node x, StringBuilder trieStr) {
		//中序遍历 如果是叶子结点打印1 打印节点字符值  如果是非叶子结点打印0
		//这里的'1'和'0'应为bit 二进制  这里为了显示方便使用字符'1'
		//'1'字符占用7个bits 而二进制1占用1bits
		if(x.isLeaf()){
			trieStr.append('1');
			trieStr.append(x.ch);
			return;
		}else{
			trieStr.append('0');
		}
		saveTrie(x.left, trieStr);
		saveTrie(x.right, trieStr);
	}
	
	private static void buildCode(String[] st, Node n, String s) {
		//System.out.println(n);
		if(n.isLeaf()){
			st[n.ch] = s;
			return;
		}
		//对于霍夫曼树 左边为0   右边为1
		//这里的'1'和'0'应为bit 二进制  这里为了显示方便使用字符'1'
		//'1'字符占用7个bits 而二进制1占用1bits
		buildCode(st, n.left, s+'0');
		buildCode(st, n.right, s+'1');		
	}
	
	private static Node buildTree(int[] freq) {
		PriorityQueue<Node> pq = new PriorityQueue<>();
		for(int i=0; i<freq.length; i++){
			if(freq[i]>0){
				//System.out.println((char)i);
				pq.add(new Node((char)i, freq[i], null, null));
			}
		}
		System.out.println("pq = " + pq.toString());
		while(pq.size()>1){
			Node l1 = pq.poll();
			Node l2 = pq.poll();
			//System.out.println(l1.ch + " " + l1.freq + " " + l2.ch + " " + l2.freq);
			pq.add(new Node('\0', l1.freq+l2.freq, l1, l2));
		}			
		return pq.poll();
	}
	
	//字符串转换为ASCII二进制编码  每个字符7bit
	public static String stringtoBinary(String str){ 
	    char[] strChar = str.toCharArray();
	    StringBuilder sb = new StringBuilder();
	    for(int i=0;i<strChar.length;i++){
	        sb.append(Integer.toBinaryString(strChar[i]));
	    }
	    System.out.println(sb.toString());
	    return sb.toString();
	}

}
