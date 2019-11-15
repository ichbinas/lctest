package com.company;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class SnakeGame {
    private int width, height;
    private int[][] food;
    private int curFood;
    private Set<Integer> snakeSet;
    private Deque<Integer> snakeQueue; //first->last => tail>head
    private int score;

    /** Initialize your data structure here.
     @param width - screen width
     @param height - screen height
     @param food - A list of food positions
     E.g food = [[1,1], [1,0]] means the first food is positioned at [1,1], the second is at [1,0]. */
    public SnakeGame(int width, int height, int[][] food) {
        this.width = width;
        this.height = height;
        this.food = food;
        curFood = 0;
        snakeSet = new HashSet();
        snakeQueue = new LinkedList();

        snakeSet.add(0);
        snakeQueue.offerLast(0);
        score=0;
    }

    /** Moves the snake.
     @param direction - 'U' = Up, 'L' = Left, 'R' = Right, 'D' = Down
     @return The game's score after the move. Return -1 if game over.
     Game over when snake crosses the screen boundary or bites its body. */
    public int move(String direction) {
        //Sanity Check
        if( score ==-1 && (!direction.equals("U") && !direction.equals("D") && !direction.equals("L") && !direction.equals("R"))){
            return -1;
        }

        //where is current head
        int hRow = snakeQueue.peekLast()/width;
        int hCol = snakeQueue.peekLast()%width;

        //after this move, where is the head
        switch(direction){
            case "U": hRow--;
                break;
            case "D": hRow++;
                break;
            case "L": hCol--;
                break;
            case "R": hCol++;
                break;
        }

        int hashNewHeadPos = hRow*width+hCol;
        int hashTailPos = snakeQueue.peekFirst();
        //if the new head out of boundary or hit body, then fail
        if(hRow>=height || hRow<0 || hCol>=width || hCol<0 ||
                (snakeSet.contains(hashNewHeadPos) && hashNewHeadPos != hashTailPos)){
            return -1;
        }

        //if hit food
        if(curFood < food.length && food[curFood][0]==hRow && food[curFood][1]==hCol) {
            curFood++;
            score++;
        }
        else {
            snakeSet.remove(hashTailPos);
            snakeQueue.removeFirst();
        }
        snakeSet.add(hashNewHeadPos);
        snakeQueue.offerLast(hashNewHeadPos);

        return score;
    }
}
