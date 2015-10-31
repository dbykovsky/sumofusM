package com.support.android.designlibdemo.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class PrettyText {
    public void PrettyText() {
    }

    public String numberToAmounts(long number) {
        if (number < 1000) return "" + number;
        int exp = (int) (Math.log(number) / Math.log(1000));
        return String.format("%.1f %c",
                number / Math.pow(1000, exp),
                "kMGTPE".charAt(exp - 1));
    }

    public String addComma(long number) {
        if (number < 1000)
            return "" + number;
        else
            return NumberFormat.getInstance(Locale.US).format(number);
    }
}
