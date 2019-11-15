package com.company;

public class Lc471_EncodeString {
    public String encode(String s) {
        //create a dp[i][j] => encoded string for subString(i, j+1), with j-i+1 characters
        //for all subString lengths (1:S.length), search dp[i][*] for i=0..(i+length-1)<n
        int sLen = s.length();
        String[][] dp = new String[sLen][sLen];

        //Try all lengths. Since dp[i][j] will use dp for subStrings, we should figure out
        //all dp with shorter subString first.
        for (int curLen = 1; curLen <= sLen; curLen++) {
            //Process subString starting from position 0
            for (int start = 0; start + curLen - 1 < sLen;  start++) {
                int end = start + curLen - 1; //index of last charater for current substring.
                String subString = s.substring(start, end+1);
                dp[start][end] = subString;

                //There is no way to make "aaaa" shorter
                if (subString.length() <= 4) {
                    continue;
                }

                //for this subString, which dp[start][k]+dp[k+1][end] makes dp[start][end] shortest
                for (int splitIndex = start; splitIndex < end; splitIndex++) {
                    String left = dp[start][splitIndex];
                    String right = dp[splitIndex+1][end];

                    if (left.length() + right.length() < dp[start][end].length()) {
                        dp[start][end] = left+right;
                    }
                }

                //for this subString, can it be described as n[subString] pattern?
                //e.g. "abcab" processed, now "abcabc", we want to write it as 2[abc]
                //e.g. "ababc"
                //How? If a string is not contructed by repeated pattern, (s+s).indexOf(s,1)=s.length
                String repeatPatternHelp = subString+subString;
                int secondStrStart = repeatPatternHelp.indexOf(subString, 1);
                if (secondStrStart != subString.length()) {
                    //repeatTimes[dp[start][secondStrStart-1]].
                    //Not repeatTimes[subString.subString(0, secondStrStart)]!.
                    //The subString may have been encoded already!
                    String repeatStr = subString.length()/(secondStrStart) + "["
                            + dp[start][start + secondStrStart-1]
                            + "]";
                    if (repeatStr.length() < dp[start][end].length()) {
                        dp[start][end] = repeatStr;
                    }
                }
            }
        }

        return dp[0][sLen-1];
    }
}
