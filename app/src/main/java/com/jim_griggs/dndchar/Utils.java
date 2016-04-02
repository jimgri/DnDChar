package com.jim_griggs.dndchar;

public class Utils {
    public static String toBonusString(int value){
        String result = Integer.toString(value);
        if (value >= 0){
            result = "+" + result;
        }
        return result;
    }


}
