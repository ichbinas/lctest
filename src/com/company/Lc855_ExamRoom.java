package com.company;

import java.awt.*;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class Lc855_ExamRoom{
    int N;
    PriorityQueue<Point> pq; //Each point, empty slot left = x, empty slot right =y. Rank by dis
    TreeSet<Point> ts; //Rank by Point.x => used in leave() to find neigbor emptyslots quickly

    private int minDistToNeigbor(Point p) {
        if (p.x == 0 || p.y == N-1) {
            return p.y - p.x + 1;
        } else {
            return ((p.y-p.x)>>1) + 1;
        }
    }

    private int getMid(Point p) {
        if (p.x == 0) {
            return p.x;
        } else if (p.y == N-1) {
            return p.y;
        } else {
            return p.x + ((p.y-p.x)>>1);
        }
    }

    public Lc855_ExamRoom(int N) {
        this.N = N;

        pq = new PriorityQueue<>((p1, p2) -> {
            int dist1 = minDistToNeigbor(p1);
            int dist2 = minDistToNeigbor(p2);

            if (dist1 == dist2) {
                return p1.x - p2.x; //Ascending
            } else {
                return dist2 - dist1; //Descending
            }
        });

        ts = new TreeSet<>(Comparator.comparing(Point::getX));

        pq.add(new Point(0, N-1));
        ts.add(new Point(0 ,N-1));
    }

    public int seat() {
        Point cur = pq.poll();
        ts.remove(cur);

        int rtnSeat = getMid(cur);

        //Add two intervals, [x,rtnSeat-1], [rtnSeat+1, y]
        if (rtnSeat != cur.x) {
            Point left = new Point(cur.x, rtnSeat-1);
            pq.add(left);
            ts.add(left);
        }
        if (rtnSeat != cur.y) {
            Point right = new Point(rtnSeat+1, cur.y);
            pq.add(right);
            ts.add(right);
        }

        return rtnSeat;
    }

    public void leave(int p) {
        Point newInterval = new Point(p, p);
        Point left = ts.floor(newInterval);
        Point right = ts.ceiling(newInterval);

        if (left != null && left.y == p - 1) {
            pq.remove(left);
            ts.remove(left);
            newInterval.x = left.x;
        }
        if (right != null && right.x == p + 1) {
            pq.remove(right);
            ts.remove(right);
            newInterval.y = right.y;
        }

        pq.add(newInterval);
        ts.add(newInterval);
    }
}
