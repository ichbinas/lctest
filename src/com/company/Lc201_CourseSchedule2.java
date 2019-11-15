package com.company;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Lc201_CourseSchedule2 {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        //Need a <course, <nextCourse>> map for graph traverse
        Map<Integer, Set<Integer>> graph = new HashMap<>();
        //Need a <course, preCount> to record prerequisites of "course".
        Map<Integer, Integer> coursePreCount = new HashMap<>();

        for (int i=0; i<prerequisites.length; i++) {
            int curCourse = prerequisites[i][1];
            int nextCourse = prerequisites[i][0];

            Set<Integer> nextCourseSet = graph.get(curCourse) == null ? new HashSet() : graph.get(curCourse);
            nextCourseSet.add(nextCourse);
            graph.put(curCourse, nextCourseSet);

            Integer preCount = coursePreCount.get(nextCourse) == null ? 1 : (coursePreCount.get(nextCourse)+1);
            coursePreCount.put(nextCourse, preCount);
        }

        //record all courses accomplished in order
        List<Integer> rtn = new ArrayList<>();

        //Now all courses not in coursePreCount can be started.
        Deque<Integer> queue = new ArrayDeque<>(); //queue keeps all courses that can be started
        for (int i=0; i<numCourses; i++) {
            if (!coursePreCount.containsKey(i)) {
                queue.offer(i);
            }
        }

        while(!queue.isEmpty()) {
            Integer cur = queue.poll();
            rtn.add(cur);

            Set<Integer> depCourses = graph.get(cur);
            if (depCourses == null) continue;

            //Since cur is done, it can be delete from the prerequisite sets of all dependent courses
            for (Integer dep : depCourses) {
                Integer preCount = coursePreCount.get(dep)-1;

                if (preCount == 0) {
                    queue.offer(dep);
                    coursePreCount.remove(dep);
                } else {
                    coursePreCount.put(dep, preCount);
                }
            }
        }
Character c = 'a';
        return rtn.stream().mapToInt(e->e.intValue()).toArray();
    }
}
