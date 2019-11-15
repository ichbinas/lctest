package com.company;

public class Lc148_MergeList {
    public ListNode sortList(ListNode head) {
            if(head == null || head.next == null) {
                return head;
            }

            //Divide and sort
            ListNode slow = head;
            ListNode fast = head;

            while(fast.next != null && fast.next.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }

            ListNode right = slow.next;
            slow.next = null;

            head = sortList(head);
            right = sortList(right);

            //Merge two sorted list
            return merge(head, right);
        }

        private ListNode merge(final ListNode n1, final ListNode n2) {
            ListNode head = new ListNode(0);
            ListNode pItr = head;

            ListNode p1 = n1;
            ListNode p2 = n2;

            while (p1 != null && p2 != null) {
                if (p1.val <= p2.val) {
                    pItr.next = p1;
                    p1 = p1.next;
                } else {
                    pItr.next = p2;
                    p2 = p2.next;
                }

                pItr = pItr.next;
            }

            ListNode rest = (p1 != null)? p1:p2;
            while (rest != null) {
                pItr.next = rest;
                pItr = pItr.next;
                rest = rest.next;
            }

            return head.next;
        }
}
