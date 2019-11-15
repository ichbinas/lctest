package com.company;

import com.sun.tools.javac.util.StringUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

public class Lc636 {
    private class Task {
        int id;
        String status;
        int timestamp;
        int totalExeTime;

        public Task(int id, String status, int timestamp, int totalExeTime) {
            this.id = id;
            this.status = status;
            this.timestamp = timestamp;
            this.totalExeTime = totalExeTime;
        }
    }

    //If I don't know how many tasks are there
    public int[] exclusiveTime(int n, List<String> logs) {
        Map<Integer, Integer> taskExcTime = new TreeMap<>();
        Deque<Task> taskStack = new LinkedList<>();

        for (String str : logs) {
            String[] taskComp = str.split(":");
            Task task = new Task(Integer.parseInt(taskComp[0]), taskComp[1], Integer.parseInt(taskComp[2]), 0);

            //if new line is a start, just push to stack
            if (task.status.equals("start")) {
                taskStack.push(task);
            }
            //if new line is a end, and id matches. a task is accomplished
            //1. remove task; 2. record the excTime, assuming any time consumed by subTask as been put into totalExetime of the task
            else if (taskStack.peek().id == task.id) {
                //paired "start" and "end" => delete from stack
                int timeInSubTask = task.timestamp - taskStack.peek().timestamp;
                taskExcTime.put(task.id, (taskExcTime.get(task.id) == null ? 0 : taskExcTime.get(task.id)) +
                        timeInSubTask +
                        taskStack.peek().totalExeTime);
                taskStack.pop();

                //update parent task's total time = CurrentTotal-(timeInSubTask)
                if (taskStack.size() > 0) {
                    Task parentTask = taskStack.pop();
                    parentTask.totalExeTime -= timeInSubTask;
                    taskStack.push(parentTask);
                }
            } else {
                //Something is wrong. Cannot find paired "start" for this <id, "end"> task
            }
        }

        return taskExcTime.values().stream().mapToInt(e -> e.intValue()).toArray();
    }

    public int[] exclusiveTimeFast(int n, List<String> logs) {
        int[] excTime = new int[n];
        int lastEnd = 0;
        ArrayDeque<Integer> taskStack = new ArrayDeque<>();

        for (String log : logs) {
            String[] taskComp = log.split(":");
            int taskId = Integer.parseInt(taskComp[0]);
            String taskStatus = taskComp[1];
            int taskTime = Integer.parseInt(taskComp[2]);

            //New task: 1. update parent exclusive Time; 2. update lastEnd with current Task timestamp;
            //3. push new task to stack.
            if (taskStatus.equals("start")) {
                if (!taskStack.isEmpty()) {
                    excTime[taskStack.peek()] += taskTime - lastEnd;
                }

                taskStack.push(taskId);
                lastEnd = taskTime;
            }
            //End a task: 1. update the excTime of the finished task; 2. update lastEnd; 3. pop task.
            else {
                excTime[taskId] += taskTime - lastEnd + 1;
                lastEnd = taskTime + 1;
                taskStack.pop();
            }
        }

        return excTime;
    }
}
