package com.company;

public class ParkSeats {
    private char[] seat;
    public char[] getSeat() { return seat;}

    public ParkSeats(char[] seat) {
        this.seat = seat;
    }
    /**
     * Find a slot which has most space on both side: {10000100101} => {10100100101}
     * Claim the slot. X-occupied, O-empty
     */
    public boolean addOne() {
        //two recorders to search max{r-l}
        int globalLeft = -1;
        int globalRight = -1;

        int left =0;
        int right=0;

        while (left < seat.length && right < seat.length) {
            //search first 'O' for left
            while (left < seat.length && seat[left] == 'X') {
                left++;
            }

            //search first 'X' after left
            if (left < seat.length) {
                right = left + 1;
                while (right < seat.length && seat[right] == 'O') {
                    right++;
                }
            }

            //Record globalMax {l, r} if needed
            //Corner, left == seat.length => no empty space
            // right == seat.length => valid case
            if (left < seat.length && right <= seat.length) {
                if ((right - left) > (globalRight - globalLeft)) {
                    globalLeft = left;
                    globalRight = right;
                }
                left = right + 1;
            }
        }

        //no empty space
        if (globalLeft == -1 && globalRight == -1) {
            return false;
        }
        //globalLeft = 0 => no 'X' on its left, take globalLeft
        else if (globalLeft == 0) {
            seat[globalLeft] = 'X';
            return true;
        }
        //globalRight == length and globalLeft < globalRight => no 'X' on the right of globalRight => take lenght -1
        else if (globalLeft < globalRight && globalRight == seat.length) {
            seat[globalRight-1] = 'X';
            return true;
        }
        else {
            int index = globalLeft + ((globalRight - globalLeft)>>1);
            seat[index] = 'X';
            return true;
        }
    }

    public void testAdd() {
        seat = new char[] {'X'};
        addOne();
        System.out.println(seat);

        seat = new char[] {'O','O','O','O','O','O','O','O','X'};
        addOne();
        System.out.println(seat);

        seat = new char[] {'X','O','O','O','O','O','O','O','O'};
        addOne();
        System.out.println(seat);

        seat = new char[] {'X','X','X','X','X','X','X','X','X'};
        addOne();
        System.out.println(seat);

        seat = new char[] {'O','O','O','O','O','O','O','O','O'};
        addOne();
        System.out.println(seat);


        seat = new char[] {'O','X','O','O','X','O','O','O','X'};
        addOne();
        System.out.println(seat);

        for(char c:seat) {

        }
    }
}
