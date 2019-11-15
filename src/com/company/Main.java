package com.company;

import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) {
        testLinkedList();
        testTinyUrl();
        testIsland();
        testAnagramsMapping();

        testPattern();

        testExclusiveTime();

        testLc556();

        testLc621();

        testLc399();

        testLc683();

        testLc729();

        testLc910();

        testLc904();

        testLc148();

        TreeSet<Integer> ts = new TreeSet<>();
        Set<Character> set = new HashSet<>(); set.add('a');
        for (Character c : set) {

        }
        Map<String, String> map = new HashMap<>();


        map.merge("A", "Suffix", (name, suffix)->name.concat(suffix));
        map.merge("A", "Suffix", (name, suffix)->name.concat(suffix));

        int left=0, right = 0;
        System.out.println(left+":"+right);

        Queue<Integer> q = new ArrayDeque<>();

        Deque<Pair<String, Integer>> deque = new ArrayDeque<>(); deque.peek();
        deque.add(new Pair("a", 1));
        Pair<String, Integer> pair = new Pair<>("b",2);

        Arrays.sort(new int[]{});
        Stack<Integer> stack = new Stack<Integer>();
        LinkedList<Integer> l1 = new LinkedList<>();
        Map<String, List<Integer>> mapNullValue = new HashMap<>();
        map.put("1", null);

        List<Integer> iList = Arrays.asList(1,2,3,4,5);
        iList.stream()
                .map(e -> {System.out.println(e); return e;})
                .filter(e -> e>3)
                .findAny();
        testParkSeats();

        testLC421();

        testLC471();

        testLc215();

        testLc497();

        testLc855();

        testLc849();

        testLc159();

        testLc216();

        //testLc901();

        testLc768();

        testLc1024();

        testLc255();

        Task_Scheduler taskScheduler = new Task_Scheduler();
        taskScheduler.test_Find1_IntOrdered();
        taskScheduler.testFindExecutions();
    }

    private static void testPattern () {
        Pattern pat = Pattern.compile("http://.[^/]*/+.[^/]*/");
        String loc = "http://s3.amazonaws.com/aiv-prod-ss-pr/3683/e894/53fe/4034-8698-758f062ffec4/855ae60a-57e0-436b-9545-cf9a22fb4836.ism";
        Matcher matcher = pat.matcher(loc);
        String newTail = "New Tail";
        if(matcher.find()) {
            String newLoc = loc.substring(0, matcher.end())+newTail;
            System.out.println(newLoc);
        }
// Pattern p = Pattern.compile("cat");
//      Matcher m = p.matcher("one cat two cats in the yard");
//      StringBuffer sb = new StringBuffer();
//      while (m.find()) {
//          m.appendReplacement(sb, "dog");
//      }
//      m.appendTail(sb);
//      System.out.println(sb.toString());
        TreeMap<String, String> treeMap = new TreeMap<>((a,b) -> (a.compareTo(b)));
        int k = (int) Math.floor(Math.sqrt(1D));
        Map<String, String> hashMap = new HashMap<>();
        int[] testArr = {1,2,3,4};
        Arrays.stream(testArr).forEach(e -> new Integer(e));
        List<Integer> list = new ArrayList<>();
        list.sort((a,b) -> (a - b));
    }

    private static void testLinkedList() {
        LinkedList<Integer> list1 = new LinkedList<>();
        list1.addAll(Arrays.asList(1,2,3));
        LinkedList<Integer> list2 = new LinkedList<>();
        list2.addAll(Arrays.asList(3,4,5));
        list1.addAll(list1.size(), list2);
        list1.offerLast(1); Integer[] firstInd = new Integer[] {-1, -1};
        System.out.print(list1);

        ResourceType rt = null;
        CompareEnum(rt);
        testLRUCache();
        String str = " ";

        Character c = str.toCharArray()[0];
        int[] intArray = new int[256];
        int i = intArray[c];

        testSnake();
        test3Sum();
        testSort();

        Integer n=0;
        change(n);
        System.out.println(n);
    }

    private static void change(Integer n) {
        n = -1;
    }
    private static void CompareEnum(ResourceType rt){
        if(ResourceType.Csai.equals(rt)) {
            System.out.println("w!");
            System.out.println("w!");
        }
    }

    enum ResourceType {Csai, Ssai, otherAI;}

    private static void testLRUCache (){
        LRUCache lruCache= new LRUCache(2);
        List<Pair<Integer, Integer>> input = new ArrayList<>();
        input.add(new Pair<>(1,1));
        input.add(new Pair<>(2,2));
        input.add(new Pair<>(3,3));
        input.add(new Pair<>(4,4));

        int tmp=-1;
        lruCache.put(input.get(0).getKey(), input.get(0).getValue());
        lruCache.put(input.get(1).getKey(), input.get(1).getValue());
        tmp = lruCache.get(1);
        lruCache.put(input.get(2).getKey(), input.get(2).getValue());
        tmp = lruCache.get(2);
    }

    private static void testSnake(){
        int[][] food = {{0,1},{0,2},{1,2},{2,2},{2,1},{2,0},{1,0}};
        List<String> steps = Arrays.asList("R","R","D","D","L","L","U","U","R","R","D","D","L","L","U","R","U","L","D");
        SnakeGame sg = new SnakeGame(3, 3, food);
        int score = 0;
        for(String str:steps) {
            score = sg.move(str);
        }

    }

    private static void testExclusiveTime() {
        String input = "0:start:0|1:start:2|1:end:5|0:end:6";
        Lc636 solution = new Lc636();
        solution.exclusiveTimeFast(2, Arrays.asList(input.split("\\|")));
    }

    private static void test3Sum() {
        int[] sum = {1,2,-1,0,-1,4};
        ThreeSum.get3Sum(sum);
    }

    private static void testSort() {
        int[] input={-1,2,0};
        QuickSort qs = new QuickSort();
        int rtn = qs.findKthLargest(input, 2);
        rtn = qs.findKthLargest(input, 1);
        rtn = qs.findKthLargest(input, 3);

        rtn = qs.findKthLargest(new int[]{99,99},1);
        rtn = qs.findKthLargest(new int[]{99,99},2);
    }

    private static void testTinyUrl() {
        String url = "http://www.leetcode.com/faq/?id=10";
        TinyUrl tinyUrl = new TinyUrl();
        String shortUrl = tinyUrl.encode(url);
        System.out.println(tinyUrl.decode(shortUrl));

        url = "http://www.leetcode.com/faq/?id=11";
        shortUrl = tinyUrl.encode(url);
        System.out.println(tinyUrl.decode(shortUrl));
    }

    private static void testIsland() {
        NumOfIslands ni = new NumOfIslands();
        ni.numIslands2(3,3,new int[][]{{0,0},{0,1},{1,2},{2,1}});
    }

    private static void  testAnagramsMapping() {
        int[] A = new int[] {12,28,46,32,50};
        int[] B = new int[] {50,12,32,46,28};
        AnagramMappings am = new AnagramMappings();
        int[] output = am.anagramMappings(A, B);
        System.out.println(output);
    }

    private static void testLc556() {
        Lc556 sol = new Lc556();
        sol.nextGreaterElement(12222333);
        Arrays.stream("12:23".split(":")).map(e->Integer.parseInt(e)).collect(Collectors.toList());
    }

    private static void testLc621() {
        Lc621_LeastInterval sol = new Lc621_LeastInterval();
        char[] tasks = {'a','a','a','a'};

        int rtn = sol.findTotalExeTime(tasks, 3);
        assert(rtn == 13);

        char[] tasks2 = {'a','b','c','a','c','a','c','a'};
        rtn = sol.findTotalExeTime(tasks2, 3);
        //a,b, c, *, a, *, c, *, a, *, c, *, a
        assert(rtn ==13);
        System.out.println(rtn);
    }

    private static void testLc399() {
        Lc399_EvaluateDivision sol = new Lc399_EvaluateDivision();
        String[][] eq = { {"a","b"},{"b","c"}};
        double[] values = {2.0, 3.0};
        String[][] queries ={{"a","c"},{"b","c"},{"a","e"},{"a","a"},{"x","x"}};
        sol.calcEquation(eq, values, queries);
        int myNum = 10;
        myNum = (myNum-1)>>1;
        System.out.println(myNum);
    }

    private static void testLc683() {
        Lc683_kEmptySlots sol = new Lc683_kEmptySlots();
        sol.kEmptySlots_Fast(new int[]{1,3,2}, 1);
    }

    private static void testLc729() {
        Lc729_MyCalendarI sol = new Lc729_MyCalendarI();
        sol.book_binaray(10,20);
        sol.book_binaray(15,25);
        sol.book_binaray(30,40);
    }

    private static void testLc910() {
        Lc910_SmallestRangeII sol = new Lc910_SmallestRangeII();
        sol.smallestRangeII(new int[]{7,8,8,5,2},4);
    }

    private static void testLc904() {
        LC904_FruitsIntoBaskets sol = new LC904_FruitsIntoBaskets();
        sol.totalFruitWithNBaskets(new int[]{3,3,3,1,2,1,1,2,3,3,4}, 2);
    }

    private static void testParkSeats() {
        ParkSeats parkSeats = new ParkSeats(new char[]{});
        parkSeats.testAdd();
    }

    private static void testLC421() {
        LC421 sol = new LC421();
        sol.findMaximumXOR(new int[] {3, 10, 5, 25, 2, 8});
        //sol.findMaximumXOR(new int[] {3,2});
    }

    private static void testLC471() {
        Lc471_EncodeString sol = new Lc471_EncodeString();
        sol.encode("aabcabca");
    }

    private static void testLc215()
    {
        Lc215_KthLargest sol = new Lc215_KthLargest();
        sol.findKthLargest(new int[] {3,2,1,5,6,4}, 2);
        Point p = new Point();

        Random r = new Random(10000);
        r.nextInt(10000);
    }

    private static void testLc497() {
        Lc497_RandomPointsInRects sol = new Lc497_RandomPointsInRects(new int[][]{
                {82918473, -57180867, 82918476, -57180863},
                {83793579, 18088559, 83793580, 18088560},
                {66574245, 26243152, 66574246, 26243153},
                {72983930, 11921716, 72983934, 11921720}});
        int cnt = 20;
        while ((cnt--) >=0) {
            int[] result = sol.pick();
        }

        Lc497_RandomPointsInRectsTree sol2 = new Lc497_RandomPointsInRectsTree(new int[][]{
                {-2, -2, -1, -1},
                {1, 0, 3, 0}});
        cnt = 20;
        while ((cnt--) >=0) {
            int[] result = sol2.pick();
        }

    }

    private static void testLc855() {
        Lc855_ExamRoom sol = new Lc855_ExamRoom(4);
        int rtn=-1;

        System.out.println(sol.seat());
        System.out.println(sol.seat());
        System.out.println(sol.seat());
        System.out.println(sol.seat());
        sol.leave(1);
        sol.leave(3);
        System.out.println(sol.seat());

    }

    private static void testLc849() {
        Lc849_MaxDistToClosestPerson sol = new Lc849_MaxDistToClosestPerson();
        sol.maxDistToClosest(new int[] {1,0,0,0});
    }

    private static void testLc159() {
        Lc159_LongestSubWith2DistinctChar sol = new Lc159_LongestSubWith2DistinctChar();
        sol.lengthOfLongestSubstringTwoDistinct("bacc");
    }

    private static void testLc216() {
        Lc216_CombinationSum3 sol = new Lc216_CombinationSum3();
        sol.combinationSum3(3, 7);
    }

    private static void testLc768() {
        Lc768_MaxChunkII sol = new Lc768_MaxChunkII();
        sol.maxChunksToSorted(new int[] {5,4,3,2,1});
    }

    private static void testLc901() {
        Lc901_1 sol = new Lc901_1();
        System.out.println(sol.next(100));
        System.out.println(sol.next(80));
        System.out.println(sol.next(60));
        System.out.println(sol.next(70));
        System.out.println(sol.next(60));
        System.out.println(sol.next(75));
        System.out.println(sol.next(85));

    }

    private static void commonContainers() {
        Deque<Integer> deque = new ArrayDeque<>();

        //As stack: stack top on the left [10, 9, 8, 7, ..., 1]. Push always adds element at the HEAD
        deque.push(1);
        deque.pop();
        deque.peekFirst();

        //As queue: queue head on the left [1, 2, 3, 4, 5]. Add always adds element at the TAIL
        deque.add(6); deque.addLast(6);
        deque.pop(); deque.pollFirst();
        deque.remove(); deque.removeFirst();
        deque.peek();

        StringBuilder sb = new StringBuilder();
        Deque<Character> dequeChar = new ArrayDeque<>();
        dequeChar.stream().map(e -> sb.append(e));

        //Append a list to another
        List<Integer> list = new ArrayList<>();
        list.addAll(new ArrayList<>());
    }

    private static void testLc148() {
        int[] val = {4,2,1,3};
        ListNode head = new ListNode(0);
        ListNode itr = head;
        for(int i:val) {
            itr.next = new ListNode(i);
            itr = itr.next;
        }

        Lc148_MergeList sol = new Lc148_MergeList();
        sol.sortList(head.next);
    }

    private static void testLc1024() {
        int[][] clips = {{0,1},{6,8},{0,2},{5,6},{0,4},{0,3},{6,7},{1,3},{4,7},{1,4},{2,5},{2,6},{3,4},{4,5},{5,7},{6,9}};

        Lc1024 sol = new Lc1024();

        System.out.print("LC1024:" + sol.videoStitching(clips, 9));
    }

    private static void testLc255() {
        int[] input = {5,2,1,3,6};

        Lc255_verifyPreorder sol = new Lc255_verifyPreorder();

        System.out.print("LC255" + sol.verifyPreorder(input));
    }
}