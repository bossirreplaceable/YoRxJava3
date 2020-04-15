package com.yobo.yorxjava3.subscribeOn;

/**
 * Created by ZhangBoshi
 * on 2020-04-14
 */
public class LinkedQueue<T> implements Cloneable{

    private Node first;// 指向最早添加的结点的链接
    private Node last;// 指向最近添加的结点的链接
    private int N = 0;// 集合的数量
    // 结点

    class Node {
        T item;
        Node next;
    }

    /**
     * 将元素放入队尾
     */
    public void enqueue(T t) {
        Node oldLast = last;
        last = new Node();
        last.item = t;
        last.next = null;
        if (isEmpty()) {
            first = last;
        } else {
            oldLast.next = last;
        }
        N++;
    }

    /**
     * 将元素从队头删除
     */
    public T dequeue() {

        T item = first.item;
        first = first.next;
        N--;
        if (isEmpty())
            last = null;
        return item;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

}
