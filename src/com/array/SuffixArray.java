package com.array;

import java.util.Arrays;

//后缀数组可以在常数时间内解决后缀排序以及最长重复子串问题
public class SuffixArray {
	
	public static void main(String[] args) {
		String str = "it was the best time it was";
		//求字符串的最大重复子串
		SuffixArray sa = new SuffixArray(str);
		String lrs = "";
		for(int i=1; i<sa.N; i++){
			if(sa.lcp(i) > lrs.length())
				lrs = sa.select(i).substring(0, sa.lcp(i));
		}
		System.out.println(lrs);
		//System.out.println(sa.forcelrs(str));
	}
	
	private final String[] suffixs;
	private final int N;
	
	public SuffixArray(String s){
		N = s.length();
		suffixs = new String[N];
		for(int i=0; i<N; i++){
			suffixs[i] = s.substring(i);
		}
		//System.out.println(Arrays.toString(suffixs));
		Arrays.sort(suffixs);
		//System.out.println(Arrays.toString(suffixs));
		for(String sf : suffixs){
			System.out.println(sf);
		}
	}

	//返回某个后缀字符串key在后缀数组中的索引值
	//由于后缀数组已经有序  可以使用二分查找
	public int rank(String key){
		int low = 0; 
		int high = N-1;
		while(low<=high){
			int mid = (low + high) / 2;
			int cmp = key.compareTo(suffixs[mid]);
			if(cmp==0) return mid;
			else if(cmp<0) high = mid-1;
			else low = mid + 1;
		}
		return -1;
	}
	
	public int length(){
		return N;
	}
	
	//后缀索引中i位置字符串
	public String select(int i){
		return suffixs[i];
	}
	
	//后缀索引中i位置字符串在原始字符串中的索引
	public int index(int i){
		return N - suffixs[i].length();
	}
	
	//一个字符串后缀数组索引位置i与前一个位置子字符串的最长重复子串
	public int lcp(int i){
		return lcp(suffixs[i], suffixs[i-1]);
	}
	
	//两个字符串最大公共前缀
	private static int lcp(String s, String t){
		int min = Math.min(s.length(), t.length());
		for(int i=0; i<min; i++){
			if(s.charAt(i)!=t.charAt(i))
				return i;
		}
		return min;
	}
	
	public String forcelrs(String s){
		String lrs = "";
		for(int i=0; i<s.length(); i++){
			for(int j=i+1; j<s.length(); j++){
				int l = lcp(s.substring(i), s.substring(j));
				if(l>lrs.length()){
					lrs = s.substring(0, l);
				}
			}
		}
		return lrs;
	}

}
