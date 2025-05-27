package net.masternation.wallet.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class FormatUtil {

    @Getter
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public static String formatNumber(double number) {
        if (number == (long) number) {
            return String.format("%d", (long) number);
        } else {
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(number);
        }
    }

}
