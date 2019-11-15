package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lc621_LeastInterval {
    public int leastInterval(char[] tasks, int n) {
        int[] cnt = new int[26];
        for(char c : tasks){
            cnt[c-'A']++;
        }
        Arrays.sort(cnt);
        //The max idle slot that can be occupied in each iteration.
        int max_interval = cnt[25]-1;

        int max_cooldown = max_interval*n;

        for(int i=24; i>0 && max_cooldown>0; i--) {
            //Only decrease when prefix of task rows does not match => when
            max_cooldown -= Math.min(cnt[i], max_interval);
        }

        return max_cooldown>0? tasks.length+max_cooldown:tasks.length;
    }

    //When tasks order cannot be changed.
    public int findTotalExeTime(char[] tasks, int n) {
        List<Character> list = new ArrayList<>();

        for(int i=0; i<tasks.length; i++) {
            appendTask(list, tasks[i], n);
        }
        return list.size();
    }

    private void appendTask(List<Character> list, char c, int n) {
        int maxIdle = n;

        //Look backward; If there is any task c, append idle '*' first
        int dist = 0; //dist to tail when iterator moves;
        int tail = list.size()-1;
        for (int i=maxIdle; i>0 && (tail - dist) >=0; i--) {
            if (Character.compare(c, list.get(tail - dist)) == 0) {
                for (int j=0; j<i; j++) {
                    list.add('*');
                }
                break;
            }
            dist++;
        }
        list.add(c);
    }
}
