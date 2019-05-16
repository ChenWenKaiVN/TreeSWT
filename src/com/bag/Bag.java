package com.bag;

import java.util.Iterator;
import java.util.NoSuchElementException;

//背包数据结构  支持数据存储添加遍历操作
//不支持删除以及查找操作
//元素添加顺序不重要
public class Bag<Item> implements Iterable<Item> {
	
	private Node<Item> first;
	private int n;
	
	private static class Node<Item>{
		private Item item;
		private Node<Item> next;
	}
	
	public Bag(){
		first = null;
		n = 0;
	}
	
	public boolean isEmpty(){
		return first == null;
	}
	
	public int size(){
		return n;
	}
	
	public void add(Item item){
		Node<Item> oldfirst = first;
		first = new Node<>();
		first.item = item;
		first.next = oldfirst;
		n++;
	}

	@Override
	public Iterator<Item> iterator() {
		return new ListIterator<Item>(first);
	}
	
	private class ListIterator<Item> implements Iterator<Item> {
		
		private Node<Item> current;

		public ListIterator(Node<Item> first) {
			current = first;
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public Item next() {
			if(!hasNext()) throw new NoSuchElementException();
			Item item = current.item;
			current = current.next;
			return item;
		}
		
	}
	
	public static void main(String[] args) {
		Bag<Integer> bag = new Bag<>();
		for(int i=0; i<10; i++){
			bag.add(i);
		}
		Iterator<Integer> it = bag.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
	}

}
