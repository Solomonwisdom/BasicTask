package com.whg.easy.minstack;

import java.util.Map;
import java.util.Stack;

/**
 * Solution class
 *
 * problemId 155
 * @author wanghaogang
 * @date 2018/6/29
 */

class MinStack {

    private Stack<Integer> realStack = null;
    private Stack<Integer> descendingSatck = null;
    private Stack<Integer> posStack = null;

    /**
     * initialize your data structure here.
     */
    public MinStack() {
        realStack = new Stack<>();
        descendingSatck = new Stack<>();
        posStack = new Stack<>();
    }

    public void push(int x) {
        realStack.push(x);
        if (descendingSatck.isEmpty() || descendingSatck.peek() > x) {
            descendingSatck.push(x);
            posStack.push(realStack.size());
        }
    }

    public void pop() {
        if (realStack.isEmpty()) {
            return;
        }
        if (realStack.peek().equals(descendingSatck.peek()) && realStack.size() == posStack.peek()) {
            descendingSatck.pop();
            posStack.pop();
        }
        realStack.pop();
    }

    public int top() {
        return realStack.peek();
    }

    public int getMin() {
        return descendingSatck.peek();
    }

    public static void main(String[] args) {
        MinStack minStack = new MinStack();
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        System.out.println(minStack.getMin());
        minStack.pop();
        System.out.println(minStack.top());
        System.out.println(minStack.getMin());
    }
}
