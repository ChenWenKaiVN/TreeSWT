package com.tree.string.sort;

import java.util.Arrays;

public class StringSort {
	
	private static int M = 0;//小数组切换阈值
	
	private static String[] aux;
	
	private static int R = 256; //基数
	
	public static void main(String[] args) {
		
//		char[] a = new char[]{'a','b','a','c','f','b'};
//		keyIndexCount(a);
//		System.out.println(Arrays.toString(a));
//		
//		
//		String[] ss = new String[]{"abc","bcd","abg","xdr","fdg"};
//		lsd(ss);
//		System.out.println(Arrays.toString(ss));
		
		String[] ss1 = new String[]{"abcd","bcdf","abgc","xdr","fdgfgh"};
		msd(ss1);
		System.out.println(Arrays.toString(ss1));
		
		
		quick3String(ss1);
		System.out.println(Arrays.toString(ss1));
		
		
	}
	
	//三向字符串快速排序
	public static void quick3String(String[] ss){
		quick3Sort(ss, 0, ss.length-1, 0);
	}
	
	private static void quick3Sort(String[] ss, int lo, int hi, int d) {
		if(hi<=lo) return;
		int lt = lo;
		int gt = hi;
		int i = lo+1;
		//以ss[lo]第d字符作为基准，将ss[lo,hi]进行交换调整，使得对于d位字符有ss[lo,lt-1]<v  ss[lt,gt]=v  ss[gt+1t,hi]>v
		int v = charAt(ss[lo], d);
		//System.out.println(lo + " " + hi);
		while(i<=gt){
			int t = charAt(ss[i], d);
			if(t<v) exch(ss, lt++, i++);
			else if(t>v) exch(ss, i, gt--);
			else i++;
		}
		//对于小于v部分递归排序
		quick3Sort(ss, lo, lt-1, d);
		//对于等于v部分 由于第d位已经相等，从d+1位开始排序
		if(v>=0)
			quick3Sort(ss, lt, gt, d+1);
		//对于大于v部分递归排序
		quick3Sort(ss, gt+1, hi, d);
	}

	//高位优先  通用型排序
	//大量小数组
	//等值键   递归深度与键长度相同  效率低
	//额外空间
	public static void msd(String[] ss){
		int N = ss.length;
		aux = new String[N];
		sort(ss, 0, N-1, 0);
	}
	//对于字符串数组从索引lo-hi利用字符串的第d位字符键值索引来排序
	private static void sort(String[] ss, int lo, int hi, int d) {
		//如果是小数组则执行插入排序
		if(hi<=lo+M){
			insertSort(ss, lo, hi, d);
			return;
		}
		//利用第d位字符键索引排序
		//由于每次递归调用都需要新建new int[R+2]，因此当切割成大量小数组时，耗费空间太大
		//当字符串数组完全相同或者前缀相同太多时，该算法效率下降
		int[] count = new int[R+2];
		for(int i=lo; i<=hi; i++){
			//System.out.println(charAt(ss[i], d));
			count[charAt(ss[i], d)+2]++;
		}
		for(int r=1; r<R+2; r++){
			count[r] = count[r-1] + count[r];
		}
		for(int i=lo; i<=hi; i++){
			aux[count[charAt(ss[i], d)+1]++] = ss[i];
		}
		for(int i=lo; i<=hi; i++){
			ss[i] = aux[i-lo];
		}
		//对于第d+1位，根据分成的子数组进行递归排序
		//这里会切分产生大量空数组
		for(int i=0; i<R; i++){
			sort(ss, lo+count[i], lo+count[i+1]-1, d+1);
		}
	}

	//对于字符串数组从第d位开始插入排序
	private static void insertSort(String[] ss, int lo, int hi, int d) {
		for(int i=lo; i<=hi; i++){
			for(int j=i; j>lo&&less(ss[j], ss[j-1], d); j--){
				exch(ss, j, j-1);
			}
		}		
	}

	private static void exch(String[] ss, int j, int i) {
		String s = ss[i];
		ss[i] = ss[j];
		ss[j] = s;		
	}

	private static boolean less(String s1, String s2, int d) {
		return s1.substring(d).compareTo(s2.substring(d)) < 0;
	}

	//获取字符串s中第d位的索引，如果不存在则返回-1，然后将该值+1得到>=0值作为该字符在count数组中的索引
	private static int charAt(String s, int d){
		if(d<s.length())
			return s.charAt(d);
		return -1;
	}
	
	//低位优先排序方式  从右往左开始依次利用键索引计数法来进行排序
	//适用于所有字符串长度相等
	public static void lsd(String[] ss){
		int W = ss[0].length();
		int N = ss.length;	
		String[] aux = new String[N];
		System.out.println("origin = " + Arrays.toString(ss));
		for(int i=W-1; i>=0; i--){
			int[] count = new int[R+1];		
			for(int j=0; j<N; j++){
				count[ss[j].charAt(i)+1]++;
			}
			for(int r=1; r<R+1; r++){
				count[r] = count[r-1] + count[r];
			}
			for(int j=0; j<N; j++){
				aux[count[ss[j].charAt(i)]++] = ss[j];
			} 
			for(int j=0; j<N; j++){
				ss[j] = aux[j];
			}
			System.out.println("i = " + i + " " + Arrays.toString(ss));
		}
	}
	
	//键索引计数法
	//排序小整数键
	//假定待排序的字符串键值为0-R-1中的任意整数，每个字符串对应一个整数，现在按照字符串的键值来排序
	//类似于计数排序
	public static void keyIndexCount(char[] a){
		int[] count = new int[R+1];
		int N = a.length;
		char[] aux = new char[N];
		//count[0]=0  计算每个索引处字符串的个数
		for(int i=0; i<N; i++){
			count[a[i]+1]++;
		}
		//计算索引为r的字符串在全部数据中起始位置
		for(int r=1; r<R+1; r++){
			count[r] = count[r-1] + count[r];
		}
		//将字符串防止到aux合适位置中
		for(int i=0; i<N; i++){
			aux[count[a[i]]++] = a[i];
		} 
		//将aux回写到a中 完成排序
		for(int i=0; i<N; i++){
			a[i] = aux[i];
		}
	}

}
