package com.company;

public class Lc683_kEmptySlots {
    //Array isOpenAfterDay[1..N], isOpenAfterDay[i]: the flower in slot i will be open after isOpenAfterDay[i]
    //Search day by day, in day i: isOpenAfter[index]<=i,isOpenAfter[index+1..index+k]>i, isOpenAfter[index+k+1]<=i
    public int kEmptySlots(int[] flowers, int k) {
        int N = flowers.length;
        int[] isOpenAfter = new int[N+1]; //Void slot[0]
        for (int i=0; i<N; i++) {
            isOpenAfter[flowers[i]] = i+1;
        }

        for (int day = 1; day <= N; day++) {
            int left = 1;
            int right = left + k +1;

            while(right <= N) {
                //Search for left=open, left+k+1=open
                while(right<=N && (isOpenAfter[left]>day || isOpenAfter[right]>day))  {
                    left++;
                    right++;
                }

                //There is no such case in "day"
                if (right > N ) {
                    break;
                }
                //Are all slots between left, right closed?
                else {
                    while(left<right && isOpenAfter[left]>day) {
                        left++;
                    }
                    if (left == right) {
                        return day;
                    } else {
                        left = right;
                        right = left + k +1;
                    }
                }
            }
        }

        return -1;
    }

    //If there is a such day i, $ as the new open, * as the old open, o as closed, then it must be either *ooo$ or $ooo*
    //So for each day i, just search f[i]-k-1, f[i+k+1]
    public int kEmptySlots_Fast(int[] flowers, int k) {
        int N = flowers.length;
        int[] isOpen = new int[N+1]; //Void slot[0], valid slots 1..N

        for (int day = 1; day <= N; day++) {
            int curIndex = flowers[day-1]; //slot index starts from 1
            isOpen[curIndex] = 1;

            int left = curIndex-k-1;
            if (left>=1 && isOpen[left] == 1 && isAllClose(left, curIndex, isOpen)) {
                return day;
            }

            int right = curIndex+k+1;
            if (right<=N && isOpen[right] == 1 && isAllClose(curIndex, right, isOpen)) {
                return day;
            }
        }

        return -1;
    }

    private boolean isAllClose(int left, int right, int[] isOpen) {
        if (left<0 || right >= isOpen.length) {
            return false;
        } else {
            int index = left+1;
            while (index<right && isOpen[index]==0) index++;

            return index==right;
        }
    }
}
