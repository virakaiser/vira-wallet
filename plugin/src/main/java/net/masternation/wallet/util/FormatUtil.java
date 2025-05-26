package net.masternation.wallet.util;

import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;

@UtilityClass
public class FormatUtil {

    public static String formatNumber(double number) {
        if (number == (long) number) {
            return String.format("%d", (long) number);
        } else {
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(number);
        }
    }
}
