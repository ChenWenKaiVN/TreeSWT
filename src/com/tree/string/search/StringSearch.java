package com.tree.string.search;

import java.util.Arrays;

public class StringSearch {
	
	public static void main(String[] args) {
		
		String txt = "abcababcabababccdabsadasas";
		String pat = "ababcabababc";
		
		//String txt = "aaabaaabaaabaaabaaab";
		//String pat = "aaaab";
		//System.out.println(search1(txt, pat));
		//System.out.println(search2(txt, pat));
		System.out.println(kmp(txt, pat));
		System.out.println(boyerMoore(txt, pat));
		System.out.println(rabinKarp(txt, pat));
	}
	
	public static int rabinKarp(String txt, String pat){
		int M = pat.length();
		int N = txt.length();
		//模式串的散列值
		long patHash = 0;
		//hash表的大小 为一个大的long值
		long Q = 12347;
		//字母表基数
		int R = 256;
		//R^(M-1)%Q
		long RM = 1;
		//计算RM值
		for(int i=1; i<=M-1; i++){
			RM = (R*RM)%Q;
		}
		//计算模式串散列值
		patHash = hash(pat, M, R, Q);
		//计算文本串前M个字符散列值
		long txtHash = hash(txt, M, R, Q);
		//如果文本串以及模式串散列值相等，有极大概率说明两个子串相等，如果Q非常大，两个产生碰撞的概率极小
		if(patHash == txtHash && check(0, txt, pat)) return 0;
		//从M位置后每移动一位，计算一次新的文本串散列值，比较之
		for(int i=M; i<N; i++){
			txtHash = (txtHash + Q - RM*txt.charAt(i-M) % Q) % Q;
			txtHash = (txtHash * R + txt.charAt(i)) % Q;
			if(txtHash == patHash && check(i-M+1, txt, pat)){
				return i-M+1;
			}
		}
		return -1;
	}
	
	//从文本串i位置开始，检查后面连续字符串是否与模式串pat相等
	private static boolean check(int i, String txt, String pat){
		//不检查也行  当Q特别大，发生冲突概率很小
		//拉斯维加斯
//		for (int j = 0; j < pat.length(); j++) 
//            if (pat.charAt(j) != txt.charAt(i + j)) 
//                return false; 
		//蒙特卡洛
		return true;
	}
	
	//Horner 除数留余法计算散列值
	private static long hash(String key, int M, int R, long Q){
		long h = 0;
		for(int i=0; i<M; i++){
			h = (h*R+key.charAt(i)) % Q;
		}
		return h;
	}
	
	
	public static int boyerMoore(String txt, String pat){
		int[] right = getBMRight(pat);
		int N = txt.length();
		int M = pat.length();
		int skip = 0;
		//i文本串从0开始遍历  每次跳跃skip位置
		for(int i=0; i<N; i+=skip){
			skip = 0;
			//模式串从右向左
			for(int j=M-1; j>=0; j--){
				//从右向左  如果出现不匹配，则计算i应该的位置
				//如果文本中不匹配字符在模式串中right[txt.charAt(i+j)]==-1不存在，则skip=j+1
				//如果文本中不匹配字符出现在模式串中right[txt.charAt(i+j)]  则skip=j-right[txt.charAt(i+j)]
				//如果启发式不起作用即right[txt.charAt(i+j)]>j  此时如果调整相当于模式串左移j++ 回退到以前状态 此时 skip=1即可，判断文本串下一个字符
				if(txt.charAt(i+j) != pat.charAt(j)){
					skip = j - right[txt.charAt(i+j)];
					if(skip<1) skip = 1;
					break;
				}
			}
			//如果skip最终等于零,说明此时j==0 即此时已经匹配返回i即可
			if(skip == 0) return i;
		}
		return -1;
	}
	
	
	public static int[] getBMRight(String pat){
		int M = pat.length();
		int R = 256;//匹配字符串的字母表基数
		//right记录模式串中每个字符在模式串中出现的最右边位置 -1表示该字符没有出现
		int[] right = new int[R];
		for(int i=0; i<R; i++){
			right[i] = -1;
		}
		for(int i=0; i<M; i++){
			right[pat.charAt(i)] = i;
		}
		return right;
	}
	
	//暴力求解
	public static int search1(String txt, String pat){ 
		int N = txt.length();
		int M = pat.length();
		//文本串进行每一位进行循环
		for(int i=0; i<=N-M; i++){
			int j;
			for(j=0; j<M; j++){
				if(txt.charAt(i+j) != pat.charAt(j)) break;
			}
			if(j == M) 
				return i;
		}
		return -1;
	}
	
	//暴力求解
	public static int search2(String txt, String pat){
		int N= txt.length();
		int M = pat.length();
		int j = 0;
		int i = 0;
		//文本串以及模式都往后移动
		while(i<N && j<M){
			//如果匹配，则文本以及模式串都往后移动一个位置
			if(txt.charAt(i)==pat.charAt(j)){
				j++;
				i++;
			}else{
			//如果不匹配，则文本回退j个位置  模式串清零
			//回退j个位置回到本次不匹配的起始位置，还应该加一，使得文本串往后移动一次
				i = i-j+1;
				j = 0;
			}
		}
		if(j == M) return i-M;
		return -1;
	}
	
	public static int kmp(String txt, String pat){
		int N = txt.length();
		int M = pat.length();
		int[] next = getNext3(pat);
		int i = 0;
		int j = 0;
		while(i<N && j<M){
			if(j==-1 || txt.charAt(i)==pat.charAt(j)){
				i++;
				j++;
			}else{
				j = next[j];
			}
		}
		if(j == M) return i-M;
		return -1;
	}
	 
	
	//获取next数组
	public static int[] getNext3(String pat){
		int M = pat.length();
		int[] next = new int[M];
		next[0] = -1;
		//文本串--模式串从0位置开始
		int i = 0;
		//模式串从-1位置开始  j==-1 说明文本串以及模式串均需要后移
		int j = -1;
		while(i<M-1){	
			//System.out.println(i+" "+j+" "+Arrays.toString(next));
			//j==-1 表示当前文本串i位置不可能出现匹配，应该i++ j++,使得文本串后移一位，模式串指向初始位置0，开始匹配
			//S[i]==S[j] 说明出现最大相同前后缀 将前后缀长度更新到next[i]中
			if(j==-1 || pat.charAt(i)==pat.charAt(j)){					
				i++;
				j++;
				//判断下一个位置是否相同，如果相同，则直接next[i]=next[j]
				if(pat.charAt(i)!=pat.charAt(j))
					next[i] = j;
				else
					next[i] = next[j];
			}
			//如果出现不匹配，则j指针回退
			else{
				j = next[j];
			}
		}
		System.out.println(Arrays.toString(next));
		return next;
	}
		
	//获取next数组
	public static int[] getNext2(String pat){
		int M = pat.length();
		int[] next = new int[M];
		next[0] = -1;
		//文本串--模式串从0位置开始
		int i = 0;
		//模式串从-1位置开始  j==-1 说明文本串以及模式串均需要后移
		int j = -1;
		while(i<M-1){	
			//System.out.println(i+" "+j+" "+Arrays.toString(next));
			//j==-1 表示当前文本串i位置不可能出现匹配，应该i++ j++,使得文本串后移一位，模式串指向初始位置0，开始匹配
			//S[i]==S[j] 说明出现最大相同前后缀 将前后缀长度更新到next[i]中
			if(j==-1 || pat.charAt(i)==pat.charAt(j)){					
				i++;
				j++;
				next[i] = j;
			}
			//如果出现不匹配，则j指针回退
			else{
				j = next[j];
			}
		}
		System.out.println(Arrays.toString(next));
		return next;
	}
	
	public static int[] getNext1(String pat){
		int M = pat.length();
		int[] next = new int[M];
		next[0] = -1;
		next[1] = 0;
		int j = 2;
		//next[0] [1]由定义可以直接确定
		//对于j>1 next[j]使用暴力匹配求解，模式串与模式串右移一位开始匹配，直到发现最大相同前后缀
		while(j<M){
			System.out.println(Arrays.toString(next));
			for(int i=1; i<j; i++){
				if(pat.substring(i, j).equals(pat.substring(0, j-i))){
					next[j] = j - i;
					break;
				}
			}
			j++;
		}
		System.out.println(Arrays.toString(next));
		return next;
	}
	
	
	

}
