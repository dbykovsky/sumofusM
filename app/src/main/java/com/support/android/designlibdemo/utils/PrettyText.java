package com.support.android.designlibdemo.utils;

public class PrettyText {
    public void PrettyText() {
    }

    public String numberToAmounts(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f %c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp-1));
    }
}
