package com.company;

import java.util.HashMap;
import java.util.Map;

class LRUCache {
    class DNode {
        public int key;
        public int value;
        public DNode pre;
        public DNode next;

        public DNode(int key, int value) {
            this.key = key;
            this.value = value;
            pre = null;
            next = null;
        }

        public void insert(DNode newNode) {
            if(newNode == null) {
                return;
            }

            newNode.next = this.next;
            newNode.pre = this;
            newNode.next.pre = newNode;
            this.next = newNode;
        }

        public void remove(){
            if(pre != null) {
                pre.next = next;
            }
            if(next != null) {
                next.pre = pre;
            }
            this.next = null;
            this.pre = null;
        }
    }

    private int m_capacity;
    private DNode head,tail;
    private Map<Integer, DNode> m_cache;

    public LRUCache(int capacity) {
        this.m_capacity = capacity;
        this.m_cache = new HashMap<>();
        head = new DNode(0,0);
        tail = new DNode(0,0);

        head.pre = null;
        head.next = tail;
        tail.pre = head;
        tail.next = null;
    }

    public int get(int key) {
        DNode rtnNode = m_cache.get(key);

        if(rtnNode == null) {
            return Integer.MAX_VALUE;//Exception
        }
        //Remove the node from current position and move the head
        rtnNode.remove();
        head.insert(rtnNode);

        return rtnNode.value;
    }

    public void put(int key, int value) {
        DNode rtnNode = m_cache.get(key);

        //Update and move to head;
        if(rtnNode != null) {
            rtnNode.value = value;
            rtnNode.remove();
            head.insert(rtnNode);
        }
        else {
            rtnNode = new DNode (key, value);

            //Insert only
            if(m_cache.size()<m_capacity) {
                m_cache.put(key, rtnNode);
            }
            //Remove from tail and insert to head
            else {
                if(tail.pre != head){
                    m_cache.remove(tail.pre.key);
                    tail.pre.remove();
                }
            }
            head.insert(rtnNode);
        }
    }
}