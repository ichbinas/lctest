package com.company;

import java.util.HashSet;
import java.util.Set;

public class Lc681 {
    public String nextClosestTime(String time) {
        Integer[] timeArray = new Integer[] {Integer.parseInt(time.substring(0,1)), Integer.parseInt(time.substring(1,2)),
                Integer.parseInt(time.substring(3,4)), Integer.parseInt(time.substring(4))
        };
        Integer timeInMin = (timeArray[0]*10+timeArray[1])*60 + timeArray[2]*10 + timeArray[3];

        Set<Integer> intAllowed = new HashSet<>();
        for (Integer i : timeArray) {
            intAllowed.add(i);
        }

        Integer minDist = Integer.MAX_VALUE;
        String minTime = "";
        //Find distinct numbers and try all permutations
        //Hour rule: hh<24
        for (Integer h1:intAllowed) {
            for (Integer h2:intAllowed) {
                if (h1*10+h2 <24) {
                    //Minute rule: mm <60
                    for (Integer m1:intAllowed) {
                        for (Integer m2:intAllowed) {
                            if (m1*10+m2 < 60) {
                                Integer curTimeInMin = (h1*10+h2)*60 + (m1*10+m2);
                                Integer curDist = (curTimeInMin > timeInMin) ? (curTimeInMin - timeInMin) :
                                        (curTimeInMin + 2400 - timeInMin);
                                if (curDist < minDist && curDist!=0) {
                                    minDist = curDist;
                                    minTime = h1.toString()+h2.toString()+":"+m1.toString()+m2.toString();
                                }
                            }
                        }
                    }
                }
            }
        }
        return minTime;
    }
}
