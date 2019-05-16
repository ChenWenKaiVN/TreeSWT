package com.array;

public class SuffixArrayX {
	
	public static void main(String[] args) {
		String str = "it was the best time it was";
		//求字符串的最大重复子串
		SuffixArrayX sax = new SuffixArrayX(str);
		String lrs = "";
		for(int i=1; i<sax.n; i++){
			System.out.println(sax.lcp(i) + "  " + lrs.length());
			if(sax.lcp(i) > lrs.length()){	
				lrs = sax.select(i).substring(0, sax.lcp(i));
				System.out.println("lrs = " + lrs);
			}
			//System.out.println("lrs = " + lrs);
		}
		System.out.println("lrs = " + lrs);
		System.out.println(lrs);
	}
	
	private static final int CUTOFF = 5;
	
	//字符串字符存储
	private final char[] text;
	
	//index[j]=i  表示text.substring(j)是第i大子串
	private final int[] index;  
	
	//字符串长度
	private final int n;
	
	public SuffixArrayX(String text){
		n = text.length();
		text = text + '\0';
		this.text = text.toCharArray();
		this.index = new int[n];
		for(int i=0; i<n; i++){
			index[i] = i;
		}
		//System.out.println(this.text);
		//System.out.println(Arrays.toString(this.index));
		//三向快速排序
		sort(0, n-1, 0);
		//System.out.println(Arrays.toString(this.index));
		//for(int i=0; i<n; i++){
			//System.out.println(select(i));
		//}
	}

	//三向快速排序
	private void sort(int lo, int hi, int d) {
		//如果数组较小使用插入排序
		if(hi<=lo+CUTOFF){
			insertion(lo, hi, d);
			return;
		}
		int lt = lo;
		int gt = hi;
		char v = text[index[lo]+d];
		int i = lo + 1;
		while(i<gt){
			char t = text[index[i]+d];
			//交换后i指向lt值，此时可以向前移动一个位置
			if(t<v) exch(lt++, i++);
			//交换之后i位置值为gt值，其大小需要与t进行比较，因此i不需要移动
			else if(t>v) exch(i, gt--);
			else i++;
		}
		// a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi]. 
		sort(lo, lt-1, d);
		if(v>0) sort(lt, gt, d+1);
		sort(gt+1, hi, d);
	}

	// sort from a[lo] to a[hi], starting at the dth character
	private void insertion(int lo, int hi, int d) {
		for(int i=lo; i<=hi; i++){
			for(int j=i; j>lo&&less(index[j], index[j-1], d); j--){
				exch(j, j-1);
			}
		}	
	}

	private void exch(int j, int i) {
		int tmp = index[i];
		index[i] = index[j];
		index[j] = tmp;
	}

	//is text[i+d..n) < text[j+d..n) ?
	private boolean less(int i, int j, int d) {
		if(i==j) return false;
		i = i + d;
		j = j + d;
		while(i<n &&j<n){
			if(text[i]<text[j]) return true;
			if(text[i]>text[j]) return false;
			i++;
			j++;
		}
		return i > j;
	}
	
	public int length(){
		return n;
	}
	
	//第i大子字符串在原始字符串的起始位置
	public int index(int i){
		return index[i];
	}
	
	public int lcp(int i){
		return lcp(index[i], index[i-1]);
	}
	
	//返回text从i，j位置开始的最大公共前缀
	private int lcp(int i, int j){
		int length = 0;
		while(i<n && j<n){
			//System.out.println(text[i] + " " + text[j]);
			//System.out.println(i + " " + j + " " + select(i) + "                 " + select(j) + "----" + text[i] + " " + text[j]);
			if(text[i]!=text[j]) return length;
			i++;
			j++;
			length++;
		}
		return length;
	}
	
	public String select(int i){
		return new String(text, index[i], n-index[i]);
	}
	
	public int rank(String query) {
        int lo = 0, hi = n - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = compare(query, index[mid]);
            if      (cmp < 0) hi = mid - 1;
            else if (cmp > 0) lo = mid + 1;
            else return mid;
        }
        return lo;
    } 

    // is query < text[i..n) ?
    private int compare(String query, int i) {
        int m = query.length();
        int j = 0;
        while (i < n && j < m) {
            if (query.charAt(j) != text[i]) return query.charAt(j) - text[i];
            i++;
            j++;
        }
        if (i < n) return -1;
        if (j < m) return +1;
        return 0;
    }

}
