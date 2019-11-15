package com.company;

public class Lc482_LicenseFormat {
    public String licenseKeyFormatting(String S, int K) {
        char[] chars = S.toCharArray();
        StringBuilder sb = new StringBuilder();
        int index = 0;

        //First group of chars: either K chars or meet a dash
        for(;index<K && chars[index] != '-'; index++) {
            sb.append(Character.toUpperCase(chars[index]));
        }
        sb.append('-');

        int cntK = 0;
        while(index < chars.length) {
            char newChar = chars[index++];
            if (newChar == '-') {
                continue;
            }

            sb.append(newChar);
            if ((++cntK) == K && index != chars.length) {
                cntK = 0;
                sb.append('-');
            }
        }
        sb.reverse();
        return sb.toString();
    }
}
