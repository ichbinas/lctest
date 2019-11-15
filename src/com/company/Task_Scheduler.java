package com.company;

import org.junit.Assert;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class Task_Scheduler {
    private int find1_IntOrdered(char[] tasks, int n) {
        // maintain a queue with max n + 1 elements
        // for task t, if q has t: poll all elements until first after t. then add '*' until size == n; add (t)
        // if q does not have t: if q.size() == n + 1, poll(); q.add(t);
        int cnt = 0;
        Queue<Character> q = new ArrayDeque<>();

        for (int i = 0; i < tasks.length; i++) {
            if (q.contains(tasks[i])) {
                // remove all elements before task[i]
                while (q.poll() != tasks[i]) {
                    cnt++;
                }
                cnt++;

                // add cooldown
                while (q.size() < n) {
                    q.add('*');
                }
            } else {
                if (q.size() == n + 1) {
                    cnt++;
                    q.poll();
                }
            }

            q.add(tasks[i]);
        }

        return cnt + q.size();
    }

    public void test_Find1_IntOrdered () {
        assert(1 == find1_IntOrdered(new char[]{'a'},3))  : "{'a'} expect 3";
        assert(5 == find1_IntOrdered(new char[]{'a','a'},3)) : "{'a','a'} expect 3";
        assert(5 == find1_IntOrdered(new char[]{'a','b','a'},3)) : "";
        assert(7 == find1_IntOrdered(new char[]{'a','b','b','a'},3)) : "";
        assert(7 == find1_IntOrdered(new char[]{'a','b','c','a','b','c'},3)) : "";
        assert(8 == find1_IntOrdered(new char[]{'a','b','c','d','a','b','c','d'},3)) : "";
    }

    public List<Character> findExecutions(char[] tasks, int n) {
        // maintain a queue of n+1 max element
        // new task t:
        // t in q : pop all elements until t not in queue. add poll() to return; add '*' (cooldown) until q.size() == n; add 't'
        // t not in q: if (q.size() == n + 1), add poll() to return; q.add(t);
        // return list
        List<Character> rtn = new ArrayList<>();
        if (tasks == null || tasks.length == 0) {
            return rtn;
        }

        Queue<Character> q = new ArrayDeque<>();
        for (int i = 0; i < tasks.length; i++) {
            if (q.contains(tasks[i])) {
                // poll all elements until element after task[i]
                while(q.peek() != tasks[i]) {
                    rtn.add(q.poll());
                }
                rtn.add(q.poll());

                // add cooldowns
                while (q.size() < n) {
                    q.add('*');
                }
            } else if (q.size() == n + 1) {
                rtn.add(q.poll());
            }

            q.add(tasks[i]);
        }

        rtn.addAll(q);
        return rtn;
    }

    public void testFindExecutions () {
        System.out.println(findExecutions(new char[]{'a','b','c','d','a','b','c','d'}, 3));


        Assert.assertEquals(new ArrayList<>(), findExecutions(new char[]{}, 1));
        Assert.assertEquals(Arrays.asList('a','*','*','*','a'), findExecutions(new char[]{'a','a'}, 3));
        Assert.assertEquals(Arrays.asList('a','b','*','*','a'), findExecutions(new char[]{'a','b','a'}, 3));
        Assert.assertEquals(Arrays.asList('a','b','*','*','*','b','a'), findExecutions(new char[]{'a','b','b','a'}, 3));
        Assert.assertEquals(Arrays.asList('a','b','c','*','*','*','c','b','a'), findExecutions(new char[]{'a','b','c','c','b','a'}, 3));
        Assert.assertEquals(Arrays.asList('a','b','c','d','a','b','c','d'), findExecutions(new char[]{'a','b','c','d','a','b','c','d'}, 3));
    }
}
