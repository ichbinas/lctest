package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class MeetingScheduler {
    public int findMeetingTime (List<int[][]> schedule, int meetingLen) {
        // Assuming schedule for each person sorted by schedule.get(i)[0]
        int maxAttendee = 0;
        int timeToStart = -1;

        List<TreeMap<Integer, Integer>> scheduleList = new ArrayList<>();
        for (int i = 0; i < schedule.size(); i++) {
            TreeMap<Integer, Integer> map = new TreeMap<>();
            for (int meeting = 0; meeting < schedule.get(i).length; meeting++) {
                map.put(schedule.get(i)[meeting][0], schedule.get(i)[meeting][1]);
            }
            scheduleList.add(map);
        }

        for (int i = 0; i < 1444; i++) {
            // if we start meeting at i, how many people can join
            int attendeeCnt = findAttendeeCnt(scheduleList, meetingLen, i);
            if (attendeeCnt > maxAttendee) {
                timeToStart = i;
                maxAttendee = attendeeCnt;
            }
        }

        return timeToStart;
    }

    // O(M*N)
    private int findAttendeeCnt(List<TreeMap<Integer, Integer>> schedule, int meetingLen, int start) {
        int cnt = 0;
        int rightBound = start + meetingLen;

        for (int i = 0; i < schedule.size(); i++) {
            TreeMap<Integer, Integer> iSchedule = schedule.get(i); // meeting schedule of the ith people

            // When did the closest meeting start?
            int iStart = iSchedule.floorKey(start);

            // any schedule conflict
            boolean hasConflict = false;
            for (int j = iStart; j < iSchedule.size() && j < rightBound; j++) {
                if (iSchedule.containsKey(j) &&
                        ((j <= start && iSchedule.get(j) > start) ||
                         (j > start && iSchedule.get(j) <= rightBound))) {
                    hasConflict = true;
                    break;
                }
            }

            if (!hasConflict) {
                cnt++;
            }
        }

        return cnt;
    }
}